package Main;

import GUI.IscTorrentGUI;
import Logic.Node;

public class IscTorrent2 {

    public static void main(String[] args) {
        Node node = new Node("localhost", 8082);
        IscTorrentGUI gui = new IscTorrentGUI(node);
    }
}
