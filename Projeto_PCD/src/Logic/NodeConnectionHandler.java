package Logic;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class NodeConnectionHandler {
    private Node node;
    private ServerSocket serverSocket;
    private Map<Integer, Socket> connections;

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
                        connections.put(clientSocket.getPort(), clientSocket);
                        System.out.println("Conexão estabelecida com: [" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "]");

                        // Thread para lidar com cada cliente individualmente
                        new Thread(() -> handleClientConnection(clientSocket)).start();

                    } catch (IOException e) {
                        System.err.println("Erro ao aceitar conexão: " + e.getMessage());
                    }
                }
            }).start();

        } catch (IOException e) {
            System.err.println("Problema ao iniciar o ServerSocket no porto: " + node.getPort());
            e.printStackTrace();
        }
    }

    // Método para tratar cada cliente conectado
    private void handleClientConnection(Socket clientSocket) {
        try (
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {

            // Receber objeto de teste
            Object question = in.readObject();
            System.out.println("Question from " + clientSocket.getPort() + ": " + question);

            // Enviar resposta
            Object answer = "Mensagem de Teste Recebida";
            System.out.println("Answer to " + clientSocket.getPort() + ": " + answer);
            out.writeObject(answer);
            out.flush();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Problemas com os INs e OUTs dos sockets: [" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "]");
            e.printStackTrace();
        }
    }

    // Método para se ligar a outro nó
    public void connectToNode(String destAddress, int destPort) {
        try {
            Socket socket = new Socket(destAddress, destPort);
            connections.put(destPort, socket);
            System.out.println("Conexão estabelecida com: [" + socket.getInetAddress().getHostAddress() + ":" + destPort + "]");

            // Criar fluxos de entrada e saída
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            // Enviar uma mensagem de teste
            Object question = "Mensagem de Teste";
            System.out.println("Question to " + destPort + ": " + question);
            out.writeObject(question);
            out.flush();

            // Receber resposta
            Object answer = in.readObject();
            System.out.println("Answer from " + destPort + ": " + answer);

            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao conectar-se ao nó [" + destAddress + ":" + destPort + "]");
            e.printStackTrace();
        }
    }








    //Getters Para testes (Possivel que seja para eliminar)
    public Map<Integer, Socket> getConnections() {return connections;}
}
