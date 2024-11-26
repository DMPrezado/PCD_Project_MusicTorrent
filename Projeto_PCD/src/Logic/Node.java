package Logic;

public class Node {
    private String address;
    private int port;
    private NodeConnectionHandler connectionHandler;
    private FileManager fileManager;

    public Node(String address, int port) {
        this.address = address;
        this.port = port;
        this.fileManager = new FileManager(); 

        System.out.println("Nó iniciado:\t [" + address + ":" + port+"]");
        waitForConnection(); 
    }

    private void waitForConnection(){
        connectionHandler = new NodeConnectionHandler(port);
        new Thread(() -> connectionHandler.startServer()).start();
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

    public FileManager getFileManager() {
        return fileManager;
    }
}
