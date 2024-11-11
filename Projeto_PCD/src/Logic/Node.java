package Logic;

public class Node {
    private String address;
    private int port;
    private NodeConnectionHandler connectionHandler;

    public Node(String address, int port) {
        this.address = address;
        this.port = port;
        this.connectionHandler = new NodeConnectionHandler(port);
    }

    // Método para iniciar o servidor do nó
    public void start() {
        new Thread(() -> connectionHandler.startServer()).start();
        System.out.println("Nó iniciado no endereço " + address + " e porto " + port);
    }

    // Método para conectar a outro nó
    public void connectTo(String remoteAddress, int remotePort) {
        connectionHandler.connectToNode(remoteAddress, remotePort);
    }

    // Getters
    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public Node getNode() {
        return this;
    }
}
