package Logic.Utils;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        FileInfo fileInfo = (FileInfo) obj;
        return name.equals(fileInfo.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
