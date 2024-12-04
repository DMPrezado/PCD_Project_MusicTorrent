package Logic;

import GUI.IscTorrentGUI;

public class Node {
    private static Node node;
    private String address;
    private int port;
    private NodeConnectionHandler connectionHandler;
    private FileManager fileManager;
    private IscTorrentGUI gui;
    private DownloadTasksManager downloadTasksManager;

    public Node(String address, int port, String folderPath) {
        this.address = address;
        this.port = port;
        this.fileManager = new FileManager(folderPath); 
        this.connectionHandler= new NodeConnectionHandler(this);
        this.downloadTasksManager= new DownloadTasksManager(this);
        node = this;

        System.out.println("Nó iniciado:\t [" + address + ":" + port+"]");
        connectionHandler.startServer();

        startGUI();
    }

    // Método para conectar a outro nó
    public void connectTo(String remoteAddress, int remotePort) {
        connectionHandler.connectToNode(remoteAddress, remotePort);
    }

    //Iniciar a GUI
    public void startGUI(){
        gui = new IscTorrentGUI(this);
    }

    // Getters
    public String                   getAddress() {return address;}
    public int                      getPort() {return port;}
    public FileManager              getFileManager() {return fileManager;}
    public NodeConnectionHandler    getConnectionHandler() {return connectionHandler;}
    public IscTorrentGUI            getGui() {return gui;}
    public DownloadTasksManager     getDownloadTasksManager() {return downloadTasksManager; }

    public static Node getNode() {return node;}
}
