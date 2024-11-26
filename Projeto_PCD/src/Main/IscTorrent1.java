package Main;

import GUI.IscTorrentGUI;
import Logic.Node;

public class IscTorrent1 {
    
    public static void main(String[] args) {
        String folderPath = "Projeto_PCD/FilesNode1/";
        Node node = new Node("localhost", 8081,folderPath);
        new IscTorrentGUI(node);
    }
    
}
