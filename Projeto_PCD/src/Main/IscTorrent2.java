package Main;

import Logic.Node;

public class IscTorrent2 {

    public static void main(String[] args) {
        String folderPath = "Projeto_PCD/dl2/";
        new Node("localhost", 8082,folderPath);
    }
}
