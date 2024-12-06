package Logic;

import GUI.IscTorrentGUI;
import Logic.Download.DownloadManager;
import Logic.Search.FileSearchManager;

public class Node {
    private static String address;
    private static int port;
    private static NodeConnectionHandler connectionHandler;
    private static FileManager fileManager;
    private static IscTorrentGUI gui;
    private static DownloadManager downloadManager;
    private static FileSearchManager fileSearchManager;

    public Node(String address, int port, String folderPath) {
        Node.address = address;
        Node.port = port;
        //lida com a pasta e com os ficheiros
        Node.fileManager = new FileManager(folderPath); 
        //para as conecções 
        Node.connectionHandler= new NodeConnectionHandler();
        //orienta os downloads
        Node.downloadManager= new DownloadManager();
        //orienta as pesquisas
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
    public static DownloadManager          getDownloadManager() {return downloadManager;}
    public static FileSearchManager        getFileSearchManager(){return fileSearchManager;}
}
