package Main;

import GUI.IscTorrentGUI;
import Logic.Node;

public class IscTorrent1 {
    
    public static void main(String[] args) {
        Node node = new Node("localhost", 8081);
        IscTorrentGUI gui = new IscTorrentGUI(node);
    }
}
