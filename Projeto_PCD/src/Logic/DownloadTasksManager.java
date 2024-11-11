package Logic;

import java.util.ArrayList;
import java.util.List;

public class DownloadTasksManager {
    private List<FileBlockRequestMessage> blockRequests = new ArrayList<>();
    private static final int BLOCK_SIZE = 10240; // 10 KB

    public void createBlockRequests(String fileHash, long fileSize) {
        int offset = 0;

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
}

