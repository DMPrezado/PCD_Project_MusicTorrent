package Logic;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class NodeConnectionHandler{
    private Node node;
    private ServerSocket serverSocket;
    private List<Socket> connectedNodesSockets;

    public NodeConnectionHandler(Node node) {
        this.node=node;
        connectedNodesSockets = new ArrayList<>();
    }

    // Método para iniciar o servidor e aceitar conexões
    public void startServer() {
            try {
                serverSocket = new ServerSocket(node.getPort());
                System.out.println("ServerSocket iniciado no porto: " + node.getPort());

                while (true) {
                // Loop para aceitar conexões de forma assíncrona
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Conexão estabelecida com: [" + clientSocket.getInetAddress().getHostAddress()+":"+clientSocket.getPort());
                    connectedNodesSockets.add(clientSocket);
                }
            } catch (IOException e) {
                System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
            }
    }


    // Método para se ligar a outro nó
    public void connectToNode(String address, int port) {
        try {
            Socket socket = new Socket(address, port);
            System.out.println("Ligado a: [" + address + ":" + port+"]");

            // Enviar uma mensagem de teste
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Olá do nó cliente!");

            // Receber a resposta
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Resposta do servidor: " + in.readLine());

            // Fechar a conexão (ou mantê-la aberta, dependendo da lógica do seu sistema)
            socket.close();
        } catch (IOException e) {
            System.out.println("Erro ao conectar ao nó: " + e.getMessage());
        }
    }

}
