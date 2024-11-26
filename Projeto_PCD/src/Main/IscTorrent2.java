package Main;

import GUI.IscTorrentGUI;
import Logic.Node;

public class IscTorrent2 {

    public static void main(String[] args) {
        String folderPath = "Projeto_PCD/FilesNode2/";
        Node node = new Node("localhost", 8082,folderPath);
        new IscTorrentGUI(node);
    }
}
