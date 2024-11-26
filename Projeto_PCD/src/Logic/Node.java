package Logic;

public class Node {
    private String address;
    private int port;
    private NodeConnectionHandler connectionHandler;
    private FileManager fileManager;

    public Node(String address, int port, String folderPath) {
        this.address = address;
        this.port = port;
        this.fileManager = new FileManager(folderPath); 
        this.connectionHandler= new NodeConnectionHandler(this);

        System.out.println("Nó iniciado:\t [" + address + ":" + port+"]");
        waitForConnection(); 
    }

    private void waitForConnection(){
        new Thread(() -> connectionHandler.startServer()).start();
    }

    // Método para conectar a outro nó
    public void connectTo(String remoteAddress, int remotePort) {
        new Thread(() -> connectionHandler.connectToNode(remoteAddress, remotePort)).start();
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
