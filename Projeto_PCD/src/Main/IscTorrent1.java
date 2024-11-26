package Main;

import GUI.IscTorrentGUI;
import Logic.Node;

public class IscTorrent1 {
    
    public static void main(String[] args) {


        // Inicializar no
        Node node1 = new Node("localhost", 8081);


        IscTorrentGUI gui = new IscTorrentGUI(node1);
    }
}
