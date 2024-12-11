package Main;

import Logic.Node;

public class IscTorrent4 {

    public static void main(String[] args) {
        String folderPath = "Projeto_PCD/dl4/";
        new Node("localhost", 8084,folderPath);
    }
    
}
