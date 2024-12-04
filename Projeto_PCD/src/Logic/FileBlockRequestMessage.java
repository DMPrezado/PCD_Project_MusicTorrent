package Logic;

public class FileBlockRequestMessage {

    private int fileHash; // Hash do ficheiro (SHA-256)
    private int offset;     // Índice do byte onde começar
    private long length;      // Tamanho do bloco

    public FileBlockRequestMessage(int fileHash, int offset, long length) {
        this.fileHash = fileHash;
        this.offset = offset;
        this.length = length;
    }

    // Getters e setters
    public int getFileHash() { return fileHash; }
    public int getOffset() { return offset; }
    public long getLength() { return length; }
    
}
