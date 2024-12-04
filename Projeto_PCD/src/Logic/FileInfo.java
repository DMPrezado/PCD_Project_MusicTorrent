package Logic;

import java.io.Serializable;

public class FileInfo implements Serializable{
    private String  name;
    private int     hash;
    private long    length;

    public FileInfo(String name, int hash, long length){
        this.hash=hash;
        this.name=name;
        this.length=length;
    }

    public int getHash() {return hash;}
    public long getLength() {return length;}
    public String getName() {return name;}

}
