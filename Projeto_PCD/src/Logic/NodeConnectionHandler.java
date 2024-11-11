package Logic;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NodeConnectionHandler {
    private int port;
    private ServerSocket serverSocket;
    private ExecutorService threadPool = Executors.newFixedThreadPool(5); // Limita o número de threads a 5

    public NodeConnectionHandler(int port) {
        this.port = port;
    }

    // Método para iniciar o servidor e aceitar conexões
    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor iniciado no porto: " + port);

            // Loop para aceitar conexões de forma assíncrona
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Conexão estabelecida com: " + clientSocket.getInetAddress().getHostAddress());
                threadPool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.out.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    // Método para se ligar a outro nó
    public void connectToNode(String address, int port) {
        try {
            Socket socket = new Socket(address, port);
            System.out.println("Ligado ao nó: " + address + " no porto: " + port);

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

    // Classe interna para tratar as conexões de clientes
    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                // Ler mensagens do cliente
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String message = in.readLine();
                System.out.println("Mensagem recebida: " + message);

                // Responder ao cliente
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println("Mensagem recebida com sucesso!");

                // Fechar a conexão
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Erro ao tratar a conexão do cliente: " + e.getMessage());
            }
        }
    }
}
