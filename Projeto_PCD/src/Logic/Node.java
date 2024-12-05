package Logic;

import GUI.IscTorrentGUI;

public class Node {
    private static String address;
    private static int port;
    private static NodeConnectionHandler connectionHandler;
    private static FileManager fileManager;
    private static IscTorrentGUI gui;
    private static DownloadTasksManager downloadTasksManager;
    private static FileSearchManager fileSearchManager;

    public Node(String address, int port, String folderPath) {
        Node.address = address;
        Node.port = port;
        Node.fileManager = new FileManager(folderPath); 
        Node.connectionHandler= new NodeConnectionHandler(this);
        Node.downloadTasksManager= new DownloadTasksManager(this);
        Node.fileSearchManager= new FileSearchManager();

        System.out.println("Nó iniciado:\t [" + address + ":" + port+"]");
        connectionHandler.startServer();

        startGUI();
    }

    // Método para conectar a outro nó
    public static void connectTo(String remoteAddress, int remotePort) {
        connectionHandler.connectToNode(remoteAddress, remotePort);
    }

    //Iniciar a GUI
    public static void startGUI(){
        Node.gui = new IscTorrentGUI();
    }

    // Getters
    public static String                   getAddress() {return address;}
    public static int                      getPort() {return port;}
    public static FileManager              getFileManager() {return fileManager;}
    public static NodeConnectionHandler    getConnectionHandler() {return connectionHandler;}
    public static IscTorrentGUI            getGui() {return gui;}
    public static DownloadTasksManager     getDownloadTasksManager() {return downloadTasksManager;}
    public static FileSearchManager        getFileSearchManager(){return fileSearchManager;}
}
