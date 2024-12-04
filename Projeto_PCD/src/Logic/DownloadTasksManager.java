package Logic;

import java.util.ArrayList;
import java.util.List;

public class DownloadTasksManager {
    private List<FileBlockRequestMessage> blockRequests = new ArrayList<>();
    private static final int BLOCK_SIZE = 10240; // 10 KB
    private Node node;


    public DownloadTasksManager(Node node){
        this.node=node;
    }

    public void createFileBlockRequestsMessages(FileInfo fileInfo) {
        int offset = 0;
        long fileSize = fileInfo.getLength();
        int fileHash = fileInfo.getHash();

        while (offset < fileSize) {
            int length = (int) Math.min(BLOCK_SIZE, fileSize - offset);
            FileBlockRequestMessage request = new FileBlockRequestMessage(fileHash, offset, length);
            blockRequests.add(request);
            offset += length;
        }
    }

    public List<FileBlockRequestMessage> getBlockRequests() {
        return blockRequests;
    }

    public void searchFiles(FileSearch fileSearch) {
        List<Connection> connections = new ArrayList<>(node.getConnectionHandler().getConnections().values());
        for (Connection connection : connections) {
            connection.send(fileSearch);
        }
    }

    public void getSerchFilesResults(FileInfo[] results){

    }
}

