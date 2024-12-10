package Logic;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;


public class NodeConnectionHandler {
    private ServerSocket serverSocket;
    private static Map<Integer, Connection> connections= new HashMap<>();

    
    // Método para iniciar o servidor e aceitar conexões
    public void startServer() {
        try {
            serverSocket = new ServerSocket(Node.getPort());
            System.out.println("ServerSocket iniciado no porto: " + Node.getPort());

            // Thread para lidar com conexões de entrada
            new Thread(() -> {
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        clientSocket.setKeepAlive(true);
                        
                        Connection connection = new Connection(clientSocket);
                        connections.put(clientSocket.getPort(), connection);

                        System.out.println("Conexão estabelecida com: [" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "]");
                    } catch (IOException e) {
                        System.err.println("Erro ao aceitar conexão: " + e.getMessage());
                    }
                }
            }).start();

        } catch (IOException e) {
            System.err.println("Problema ao iniciar o ServerSocket: "+ e.getMessage());
        }
    }

    public void closeConnections(){
        //Enviar caracter para finalização e eliminação da conexão das Listas dos outros nós
        for(Connection con : connections.values())
            con.send('Z');
    }

    // Método para se ligar a outro nó
    public void connectToNode(String destAddress, int destPort) {
        try {
            if(connections.containsKey(destPort))
                throw new IllegalArgumentException("Conexão com "+destPort+" já existente!");

            if(destPort==Node.getPort())
                throw new IllegalArgumentException("Impossivel connectar com o próprio nó!");

            Socket socket = new Socket(destAddress, destPort);
            socket.setKeepAlive(true);
            connections.put(destPort, new Connection(socket));
            System.out.println("Conexão estabelecida com: [" + destAddress + ":" + destPort + "]");
        } catch (IOException e) {
            System.err.println("Erro ao conectar-se ao nó [" + destAddress + ":" + destPort + "]");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    //Getters Para testes (Possivel que seja para eliminar)
    public Map<Integer, Connection> getConnections() {return connections;}

    public static void closeConnection(int key){
        connections.remove(key);
    }

    public Connection getConnection(int port) {
        return connections.get(port);
    }
}
