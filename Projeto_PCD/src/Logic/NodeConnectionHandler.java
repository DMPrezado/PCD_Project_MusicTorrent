package Logic;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class NodeConnectionHandler{
    private Node node;
    private ServerSocket serverSocket;
    private Map<Integer,Connection> connections;

    public NodeConnectionHandler(Node node) {
        this.node=node;
        connections = new HashMap<>();

    }


    // Método para iniciar o servidor e aceitar conexões
    public void startServer() {
        try {
            serverSocket = new ServerSocket(node.getPort());
            System.out.println("ServerSocket iniciado no porto: " + node.getPort());

            while (true) {
                new Thread(() -> {
                    // Loop para aceitar conexões de forma assíncrona
                    Socket clientSocket;
                    try {
                        clientSocket = serverSocket.accept();
                        // Aceita uma conexão
                        Connection connection = receiveConnection(clientSocket); // Recebe a conexão

                    
                
                        // Aqui você pode manipular a conexão de forma assíncrona
                        connections.put(connection.getNodePortB(), connection);
                        System.out.println("Conexão estabelecida com: [" + connection.getInetAddress().getHostAddress() + ":" + connection.getNodePortA() + "]");
                    } catch (IOException | ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } 
                }).start(); // Inicia a thread para tratar a conexão
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }


    private Connection receiveConnection(Socket clientSocket) throws ClassNotFoundException, IOException {
        ObjectInputStream inConnection = new ObjectInputStream(clientSocket.getInputStream());
        Connection connection = null;
        while (true) {
            Object object = inConnection.readObject();
            if (object instanceof Connection) {
                connection = (Connection) object;
                break;
            }
        }
        return connection;
    }



    // Método para se ligar a outro nó
    public void connectToNode(String destAddress, int destPort) {
        Connection connection = new Connection(node.getPort(), destPort);
        connections.put(destPort, connection);
        System.out.println("Conexão estabelecida com: [" + connection.getInetAddress().getHostAddress()+":"+connection.getPort()+"]");

        // Enviar uma mensagem de teste
        connection.out(new String("Mensagem de teste!"));
    }

}

