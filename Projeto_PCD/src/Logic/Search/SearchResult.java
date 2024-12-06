package Logic.Search;

import java.io.Serializable;
import java.util.List;

import Logic.Utils.FileInfo;

public class SearchResult implements Serializable{
    private int port;
    private List<FileInfo> resultList;

    public SearchResult(List<FileInfo> resultList){
        this.resultList=resultList;
    }

    public List<FileInfo> getResultList() {return resultList;}
    public int getPort() {return port;}

    public void setPort(int port) {
        this.port = port;
    }
}
