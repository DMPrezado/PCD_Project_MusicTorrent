package Main;

import GUI.IscTorrentGUI;
import Logic.Node;

public class IscTorrent3 {

    public static void main(String[] args) {
        Node node = new Node("localhost", 8083);
        IscTorrentGUI gui = new IscTorrentGUI(node);
    }
}
