package Main;

import GUI.IscTorrentGUI;
import Logic.FileManager;
import Logic.Node;

public class IscTorrent3 {

    public static void main(String[] args) {
        // Inicializar o no
        Node node3 = new Node("localhost", 8083);
        node3.start();

        // Inicializar o FileManager
        FileManager fileManager = new FileManager();

        IscTorrentGUI gui = new IscTorrentGUI(node3.getAddress(), node3.getPort());
        gui.showGUI();
    }
}
