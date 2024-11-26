package Main;

import GUI.IscTorrentGUI;
import Logic.Node;

public class IscTorrent3 {

    public static void main(String[] args) {
        String folderPath = "Projeto_PCD/FilesNode3/";
        Node node = new Node("localhost", 8083,folderPath);
        new IscTorrentGUI(node);
    }
}
