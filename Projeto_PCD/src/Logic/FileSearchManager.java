package Logic;

import java.util.Collection;
import java.util.HashMap;

public class FileSearchManager{
    private String  searchString;
    private int     nConnections;
    private int     nResults;
    private Collection<Connection> connections;
    private HashMap<Integer,SearchResult> searchResultHashMap = new HashMap<>();
    private HashMap<Integer,FileInfo> portFileInfoHashMap;
    private static HashMap<FileInfo,Integer> fileInfoCountHashMap;

    public void sendSearchRequest(String str){
        setSearchString(str);
        for (Connection connection : Node.getConnectionHandler().getConnections().values()) {
            connection.send(new SearchRequest(searchString));
        }
    }

    public synchronized void receiveSearchResults(int port, SearchResult searchResult){
        nResults++;
        searchResultHashMap.put(port, searchResult);

        if(nConnections==nResults){
            tratarDadosSearch();
            imprimir();
        }
    }

    public void tratarDadosSearch(){

        //separar files por porto a guardar num HASHMAP
        portFileInfoHashMap = new HashMap<>();
        for (int porto : searchResultHashMap.keySet()) {
            for (FileInfo fileInfo : searchResultHashMap.get(porto).getResultList()) {
                portFileInfoHashMap.put(porto,fileInfo);
            }
        }


        //contar fornecedores por ficheiro!
        fileInfoCountHashMap = new HashMap<>();
        for (FileInfo fileInfo : portFileInfoHashMap.values()) {
            fileInfoCountHashMap .merge(fileInfo, 1, Integer::sum);
        }

    }


    //Getters
    public String getSearchString(){return searchString;}
    public static HashMap<FileInfo,Integer> getFileInfoCountHashMap(){return fileInfoCountHashMap;}


    //Setters
    public void setSearchString(String str){
        this.searchString=str;
    }



    public void imprimir(){
        for (FileInfo file : fileInfoCountHashMap.keySet()) {
            System.out.println(file.getName()+ " - " + fileInfoCountHashMap.get(file).toString());
        }
    }


}