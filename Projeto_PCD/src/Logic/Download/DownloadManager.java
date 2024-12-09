package Logic.Download;

import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Logic.Connection;
import Logic.FileManager;
import Logic.Node;
import Logic.Search.FileSearchManager;
import Logic.Utils.FileInfo;
import Logic.Utils.Tuplo;

public class DownloadManager {
    private static final int THREAD_POOL_SIZE = 5;
    private static HashMap<FileInfo, List<ChunkResult> > receivedChunks = new HashMap<>();
    private Lock lock =new ReentrantLock ();
    private static HashMap<FileInfo,Long> tempos;

    public static void clearReceivedChunks(){
        receivedChunks = new HashMap<>();
        tempos = new HashMap<>();
    }

    public static List<ChunkResult> getReceivedFileInfoChunks(FileInfo fileInfo) {
        return receivedChunks.get(fileInfo);
    }

    public static void setTotalTime(FileInfo fileInfo, long time){
        tempos.put(fileInfo, time);
    }

    public static HashMap<FileInfo, Long> getTempos() {
        return tempos;
    }

    public void requestFiles(List<String> selectedFiles){
        long inicio = System.currentTimeMillis();

        List<Tuplo<FileInfo, List<Integer>>> selectedFilesPorts = getSelectedFileInfoPort(selectedFiles);
        
        
        // Criar Thread Pool
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

        // Para cada arquivo, gerar ChunkRequests e enviá-los aos ports
        for (Tuplo<FileInfo, List<Integer>> tuplo : selectedFilesPorts) {
            FileInfo fileInfo = tuplo.getPrimeiro();
            List<Integer> ports = tuplo.getSegundo();

        
            tempos.put(fileInfo, inicio);

            // Gerar os ChunkRequests para o arquivo
            List<ChunkRequest> chunkRequests = creatChunkRequests(fileInfo);

            // Distribuir chunks entre os ports
            Map<Integer, List<ChunkRequest>> portToChunksMap = distributeChunksToPorts(chunkRequests, ports);

            // Enviar os chunks atribuídos a cada port
            for (Map.Entry<Integer, List<ChunkRequest>> entry : portToChunksMap.entrySet()) {
                int port = entry.getKey();
                List<ChunkRequest> assignedChunks = entry.getValue();

                // Submeter envio de chunks para um porto
                executor.submit(() -> sendChunksToPort(assignedChunks, port));
            }
        }

        // Finalizar Thread Pool
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow(); // Forçar finalização se demorar mais que 60 segundos
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        
    }




    //Receber Chunks
    public synchronized static void receberChunkResult(ChunkResult chunkResult) {
        if (chunkResult == null) {
            return;
        }

        // Adiciona o chunk ao mapa de chunks recebidos
        FileInfo fileInfo = chunkResult.getFileInfo();
        receivedChunks.putIfAbsent(fileInfo, new ArrayList<>());
        receivedChunks.get(fileInfo).add(chunkResult);

        // Verifica se o arquivo foi completamente recebido
        if (fileIsComplete(fileInfo)) {
            System.out.printf("AVISO: O ficheiro '%s' foi completamente recebido!%n", fileInfo.getName());
            try {
                FileManager.juntarChunks(receivedChunks);
            } catch (IOException e) {
            } 
        }
    }



    private static boolean fileIsComplete(FileInfo fileInfo){
        List<ChunkResult> chunks = receivedChunks.get(fileInfo);

        if (chunks == null || chunks.isEmpty()) {
            return false;
        }

        // Soma o tamanho de todos os chunks recebidos
        int receivedSize = 0;
        for (ChunkResult chunkResult : chunks) {
            if (chunkResult != null) {
                receivedSize += chunkResult.getSize();
            }
        }

        // Retorna true se o tamanho recebido for igual ao tamanho total do arquivo
        return receivedSize == fileInfo.getLength();
    }






    //Enviar Chunks
    private List<Tuplo<FileInfo, List<Integer>>> getSelectedFileInfoPort(List<String> selectedFiles) {
        // Limpar os nomes dos ficheiros (remover " <Integer>" no final)
        List<String> cleanedSelectedFiles = new ArrayList<>();
        for (String fileName : selectedFiles) {
            // Remover a parte entre parênteses, incluindo espaço antes do "<"
            if (fileName.contains(" <")) {
                fileName = fileName.substring(0, fileName.lastIndexOf(" <"));
            }
            cleanedSelectedFiles.add(fileName);
        }

        // Obter a lista completa de tuplos
        List<Tuplo<Integer, FileInfo>> tuplos = FileSearchManager.getPortFileInfoList();
        List<Tuplo<FileInfo, List<Integer>>> selectedTuplos = new ArrayList<>();

        // Filtrar e agrupar portas por FileInfo
        HashMap<FileInfo, List<Integer>> fileInfoPortsMap = new HashMap<>();
        for (Tuplo<Integer, FileInfo> tuplo : tuplos) {
            FileInfo fileInfo = tuplo.getSegundo();
            int port = tuplo.getPrimeiro();

            if (cleanedSelectedFiles.contains(fileInfo.getName())) {
                fileInfoPortsMap.putIfAbsent(fileInfo, new ArrayList<>());
                fileInfoPortsMap.get(fileInfo).add(port);
            }
        }

        // Converter o mapa em uma lista de tuplos
        for (FileInfo fileInfo : fileInfoPortsMap.keySet()) {
            selectedTuplos.add(new Tuplo<>(fileInfo, fileInfoPortsMap.get(fileInfo)));
        }

        return selectedTuplos;
    }

    private List<ChunkRequest> creatChunkRequests(FileInfo fileInfo){
        List<ChunkRequest> chunkResquests = new ArrayList<>();
        for (int offset = 0; offset < fileInfo.getLength(); offset+=ChunkRequest.CHUNK_MAX_SIZE) {
            chunkResquests.add(new ChunkRequest(fileInfo,offset));
        }
        return chunkResquests;
    }

    private Map<Integer, List<ChunkRequest>> distributeChunksToPorts(List<ChunkRequest> chunkRequests, List<Integer> ports) {
        Map<Integer, List<ChunkRequest>> portToChunksMap = new HashMap<>();

        // Inicializar a lista de chunks para cada port
        for (int port : ports) {
            portToChunksMap.put(port, new ArrayList<>());
        }

        // Atribuir chunks aos ports de forma balanceada
        for (int i = 0; i < chunkRequests.size(); i++) {
            int port = ports.get(i % ports.size()); // Balancear por índice circular
            portToChunksMap.get(port).add(chunkRequests.get(i));
        }

        return portToChunksMap;
    }

    private void sendChunksToPort(List<ChunkRequest> chunks, int port) {
        try {
            Connection connection = Node.getConnectionHandler().getConnection(port);
            if (connection != null) {
                for (ChunkRequest chunk : chunks) {
                    lock.lock();
                    try{
                        connection.send(chunk);
                    } finally{
                        lock.unlock();
                    }
                }
                System.out.println("foram enviados "+chunks.size());
            } else {
                System.err.printf("Conexão para o porto %d não encontrada.%n\n", port);
            }
        } catch (Exception e) {
            System.err.printf("Erro ao enviar ChunkRequests para o porto %d: %s%n\n", port, e.getMessage());
        }
    }

    
}
