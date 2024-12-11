package Main;

import Logic.Node;

public class IscTorrent3 {

    public static void main(String[] args) {
        String folderPath = "Projeto_PCD/dl3/";
        new Node("localhost", 8083,folderPath);
    }
}
