package Logic.Download;

import java.io.Serializable;

import Logic.Utils.FileInfo;

public class ChunkResult implements Serializable, Comparable<ChunkResult>{
    private byte[] chunkData;
    private int offset;
    private FileInfo fileInfo;
    private int portFornecedor;

    public ChunkResult(byte[] chunkData, int offset, FileInfo fileInfo, int port) {
        this.chunkData = chunkData;
        this.offset = offset;
        this.fileInfo=fileInfo;
        this.portFornecedor=port;
    }

    public byte[] getChunkData() {
        return chunkData;
    }

    public int getOffset() {
        return offset;
    }

    public FileInfo getFileInfo() {
        return fileInfo;
    }

    public int getPortFornecedor() {
        return portFornecedor;
    }

    public int getSize(){
        return chunkData.length;
    }

    @Override
    public int compareTo(ChunkResult other) {
        return Integer.compare(this.offset, other.offset);
    }

    @Override
    public String toString() {
        return String.format("ChunkResult[offset=%d, size=%d, file=%s]", 
                              offset, chunkData.length, fileInfo.getName());
    }
}
