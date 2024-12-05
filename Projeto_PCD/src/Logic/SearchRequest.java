package Logic;

import java.io.Serializable;

public class SearchRequest implements Serializable{
    private String str;

    public SearchRequest(String str){
        this.str=str;
    }

    public String getStr() {
        return str;
    }
}
