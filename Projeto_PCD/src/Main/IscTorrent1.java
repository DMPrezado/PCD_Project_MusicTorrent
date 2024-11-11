package Main;

import GUI.IscTorrentGUI;
import Logic.FileManager;
import Logic.Node;

public class IscTorrent1 {
    
    public static void main(String[] args) {
        // Inicializar no
        Node node1 = new Node("localhost", 8081);
        node1.start();

        // Inicializar o FileManager
        FileManager fileManager = new FileManager();

        IscTorrentGUI gui = new IscTorrentGUI(node1.getAddress(), node1.getPort());
        gui.showGUI();
    }
}
