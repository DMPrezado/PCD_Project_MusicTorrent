package Logic;

public class FileBlockRequestMessage {

    private String fileHash; // Hash do ficheiro (SHA-256)
    private int offset;     // Índice do byte onde começar
    private int length;      // Tamanho do bloco

    public FileBlockRequestMessage(String fileHash, int offset, int length) {
        this.fileHash = fileHash;
        this.offset = offset;
        this.length = length;
    }

    // Getters e setters
    public String getFileHash() { return fileHash; }
    public long getOffset() { return offset; }
    public int getLength() { return length; }
}
