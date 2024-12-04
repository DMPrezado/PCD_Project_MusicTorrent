package Logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileSearch implements Serializable{
    private String str;
    private static List<FileInfo> results = new ArrayList<>();

    public FileSearch(String str){
        this.str=str;
    }

    public String getStr() {return str;}

    public synchronized static void  addResults(List<FileInfo> resultFileInfos){
        for (FileInfo fileInfo : resultFileInfos) {
            results.add(fileInfo);
        }
        Node.getNode().getGui().updateFileList();
    }

    public static List<FileInfo> getResults() {
        return results;
    }
}
