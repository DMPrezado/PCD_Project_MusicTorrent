package Main;

import GUI.IscTorrentGUI;
import Logic.FileManager;
import Logic.Node;

public class IscTorrent2 {

    public static void main(String[] args) {
        // Inicializar no
        Node node2 = new Node("localhost", 8082);
        node2.start();

        // Inicializar o FileManager
        FileManager fileManager = new FileManager();

        IscTorrentGUI gui = new IscTorrentGUI(node2.getAddress(), node2.getPort());
        gui.showGUI();
    }
}
