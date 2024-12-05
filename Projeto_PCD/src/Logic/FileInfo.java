package Logic;

import java.io.Serializable;

public class FileInfo implements Serializable{
    private String  name;
    private long    length;

    public FileInfo(String name, long length){
        this.name=name;
        this.length=length;
    }

    public long getLength() {return length;}
    public String getName() {return name;}

}
