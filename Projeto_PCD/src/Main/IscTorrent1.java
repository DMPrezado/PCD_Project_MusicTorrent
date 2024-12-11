package Main;

import Logic.Node;

public class IscTorrent1 {
    
    public static void main(String[] args) {
        String folderPath = "Projeto_PCD/dl1/";
        new Node("localhost", 8081,folderPath);
    }
    
}
    