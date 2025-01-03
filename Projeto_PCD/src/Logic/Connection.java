package Logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Logic.Download.ChunkRequest;
import Logic.Download.ChunkResult;
import Logic.Download.DownloadManager;
import Logic.Search.SearchRequest;
import Logic.Search.SearchResult;

public class Connection{
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public Connection(Socket socket) {
        this.socket = socket;
        try {
            // Inicializar ObjectOutputStream antes do ObjectInputStream
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); // Garantir envio imediato do cabeçalho do stream
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Streams inicializados com sucesso: " + socket);

            // Iniciar thread para escutar mensagens recebidas
            new Thread(this::listen).start();

        } catch (IOException e) {
            System.err.println("Falha ao inicializar Connection para o socket: " + socket);
            e.printStackTrace();
        }
    }

    public void send(Object object) {
        if (socket.isClosed())
            return;
        try {
            System.out.println("Object to " + socket.getPort() + ": " + object.getClass());
            out.writeObject(object);
            out.flush();
        } catch (IOException e) {
            System.err.println("Falha ao enviar -"+object.getClass()+"!");
        }
    }

    private void listen() {
        try {
            while (true) {
                // Ler objeto do stream de entrada
                Object object = in.readObject();

                // Verificar e tratar o objeto
                if (object instanceof String) {
                    if((String)object=="Z"){
                        close();
                        continue;
                    }

                    System.out.println("Mensagem recebida (String): " + object);
                    continue; // Passa para a próxima iteração
                }

                if (object instanceof SearchRequest) {
                    tratarSearchRequest((SearchRequest) object);
                    continue;
                }

                if (object instanceof SearchResult) {
                    tratarSearchResult((SearchResult) object);
                    continue;
                }

                if (object instanceof ChunkRequest) {
                    tratarChunkRequest((ChunkRequest)object);
                    continue;
                }

                if (object instanceof ChunkResult) {
                    tratarChunkResult((ChunkResult)object);
                    continue;
                }


                // Tratamento de objetos desconhecidos
                System.out.println("Objeto desconhecido recebido: " + object.getClass());
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro durante a escuta de mensagens no socket: " + socket);
            e.printStackTrace();
            close(); // Fechar conexão em caso de erro
        }
    }

    private void tratarSearchRequest(SearchRequest searchRequest){
        send(new SearchResult(Node.getFileManager().searchFiles(searchRequest.getStr())));
    }

    private void tratarSearchResult(SearchResult object){
        Node.getFileSearchManager().receiveSearchResults(getSocket().getPort(),object);
    }

    private void tratarChunkRequest(ChunkRequest object){
        send(Node.getFileManager().createChunkResult(object));
    }

    private void tratarChunkResult(ChunkResult chunkResult){
        Node.getFileManager().loadNodeFiles();
        DownloadManager.receberChunkResult(chunkResult);
    }








    // Método para fechar o socket e streams
    public void close() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            Node.getConnectionHandler().getConnections().remove(socket.getPort());
            System.out.println("Conexão fechada: " + socket);
        } catch (IOException e) {
            System.err.println("Erro ao fechar conexão: " + socket);
        }
    }






    //Getters
    public Socket getSocket() {return socket;}
    public ObjectOutputStream getOut() {return out;}
    public ObjectInputStream getIn() {return in;}
  

}
