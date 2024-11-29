package Logic;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;


public class NodeConnectionHandler {
    private Node node;
    private ServerSocket serverSocket;
    private Map<Integer, Connection> connections;

    public NodeConnectionHandler(Node node) {
        this.node = node;
        connections = new HashMap<>();
    }

    // Método para iniciar o servidor e aceitar conexões
    public void startServer() {
        try {
            serverSocket = new ServerSocket(node.getPort());
            System.out.println("ServerSocket iniciado no porto: " + node.getPort());

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


    // Método para se ligar a outro nó
    public void connectToNode(String destAddress, int destPort) {
        try {
            if(connections.containsKey(destPort))
                throw new IllegalArgumentException("Conexão com "+destPort+" já existente!");

            if(destPort==node.getPort())
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
}
