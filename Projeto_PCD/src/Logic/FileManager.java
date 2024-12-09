package Logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import Logic.Download.ChunkRequest;
import Logic.Download.ChunkResult;
import Logic.Download.DownloadManager;
import Logic.Utils.FileInfo;

// Ponto 2
public class FileManager {
    private static String folderPath;
        private File[] files;
    
        public FileManager(String folderPath) {
            FileManager.folderPath = folderPath;
            loadNodeFiles();
            System.out.println(toString());
        }
    
        // Método para carregar os ficheiros da pasta de trabalho
        private void loadNodeFiles() {
            File folder = new File(folderPath);
            
            // Verificar se a pasta existe e é um diretório
            if (folder.exists()) {
                files = folder.listFiles();
            } else {
                System.out.println("A pasta de trabalho não foi encontrada.");
            }
        }
    
        public void addFileToNodeFolder(File newFile){
            File[] newFiles = new File[files.length+1];
            for(int i = 0; i<= files.length-1; i++)
                newFiles[i] = files[i];
            newFiles[files.length] = newFile;
    
            files = newFiles;
        }
    
        public List<String> getFilesNames() {
            List<String> filesNames = new ArrayList<String>();
    
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        filesNames.add(file.getName());
                    }
                }
            }
    
            return filesNames;
        }
    
        public File[] getFiles() {
            loadNodeFiles();
            return files;
        }
    
    
        public List<File> getFilesList(){
            List<File> filesList = new ArrayList<>();
            for (File file : files)
                filesList.add(file);
            return filesList;
        }
    
        @Override
        public String toString() {
            String str = "";
            // Exibir os ficheiros carregados (apenas para verificar)
            str += "Ficheiros carregados:\n";
            for (String fileName : getFilesNames()) {
                str+= "\t"+fileName+"\n";
            }
            return str;
        }
    
        public List<FileInfo> searchFiles(String searchStr) {
            List<FileInfo> matchingFiles = new ArrayList<>();
    
            if (files == null || files.length == 0 || searchStr == null || searchStr.trim().isEmpty()) {
                return matchingFiles; // Retorna vazio se não houver arquivos ou string de busca inválida
            }
    
            String normalizedSearchStr = searchStr.trim().toLowerCase();
    
            File[] ficheiros = getFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().contains(normalizedSearchStr)) {
                    matchingFiles.add(new FileInfo(file.getName(), file.length()));
                }
            }
    
            return matchingFiles;
        }
    
        public File getFile(FileInfo fileInfo){
            for (File file : files) {
                if (file.getName().equals(fileInfo.getName())) {
                    return file;
                }
            }
            return null;
        }
        
    
    
        //Metodos para o Download
        public ChunkResult createChunkResult(ChunkRequest chunkRequest){
            try {
                return getChunk(getFile(chunkRequest.getFileInfo()), chunkRequest.getOffset(),chunkRequest);
            } catch (FileNotFoundException e) {
                return null;
            } catch (IOException e) {
                System.err.println("Problema ao criar Chunk");
            }
            return null;
        }
        //
        public static ChunkResult getChunk(File file, int offset, ChunkRequest chunkRequest) throws FileNotFoundException, IOException  {
            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                raf.seek(offset); // Mover para o offset especificado
    
                // Lê o chunk
                byte[] buffer = new byte[ChunkRequest.CHUNK_MAX_SIZE];
                int bytesRead = raf.read(buffer);
    
                // Se não leu nada, lança uma exceção
                if (bytesRead == -1) {
                    throw new IOException("Offset fora do alcance do ficheiro.");
                }
    
                // Ajustar o tamanho do buffer para o tamanho real dos dados lidos
                byte[] actualChunk = (bytesRead < ChunkRequest.CHUNK_MAX_SIZE) ? java.util.Arrays.copyOf(buffer, bytesRead) : buffer;
    
                // Retornar o resultado
                return new ChunkResult(actualChunk,offset,chunkRequest.getFileInfo(),Node.getPort());
            }
        }
    
    
    //Juntar Chunks
    public static void juntarChunks(HashMap<FileInfo, List<ChunkResult>> fileChunks) throws FileNotFoundException, IOException {
        for (FileInfo fileInfo : fileChunks.keySet()) {
            String filePath = folderPath + fileInfo.getName();
            List<ChunkResult> chunks = fileChunks.get(fileInfo);
            Collections.sort(chunks);

            try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
                for (ChunkResult chunk : chunks) {
                    // Escreve os dados do chunk no arquivo
                    outputStream.write(chunk.getChunkData());
                }
            }


            //para Calculo dos tempos
            long inicio = DownloadManager.getTempos().get(fileInfo);
            long fim = System.currentTimeMillis();
            DownloadManager.setTotalTime(fileInfo, Math.abs(inicio - fim));
            System.out.printf("Arquivo foi reconstruído com sucesso em %d ms: %s%n",DownloadManager.getTempos().get(fileInfo), filePath);

            Node.getGui().mostrarDetalhesDownload();
        }
    }
}
