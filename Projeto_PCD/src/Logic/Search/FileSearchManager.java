package Logic.Search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Logic.Connection;
import Logic.Node;
import Logic.Utils.FileInfo;
import Logic.Utils.Tuplo;

public class FileSearchManager{
    private String  searchString;
    private int     nConnections;
    private int     nResults;
    private static HashMap<Integer,SearchResult> searchResultHashMap = new HashMap<>();
    private static List<Tuplo<Integer, FileInfo>> portFileInfoList;
    private static HashMap<FileInfo,Integer> fileInfoCountHashMap = new HashMap<>();;

    public void sendSearchRequest(String str){
        setSearchString(str);
        for (Connection connection : Node.getConnectionHandler().getConnections().values()) {
            connection.send(new SearchRequest(searchString));
        }
        nResults=0;
        nConnections = Node.getConnectionHandler().getConnections().size();

    }

    public synchronized void receiveSearchResults(int port, SearchResult searchResult){
        nResults++;
        if(searchResult!=null)
            searchResultHashMap.put(port, searchResult);

        if(nConnections==nResults){
            tratarDadosSearch();
            Node.getGui().updateFileList();
        }
    }

    public void tratarDadosSearch() {
        portFileInfoList = new ArrayList<>();
        fileInfoCountHashMap.clear(); // Limpar o mapa antes de começar

        for (int porto : searchResultHashMap.keySet()) {
            for (FileInfo fileInfo : searchResultHashMap.get(porto).getResultList()) {
                portFileInfoList.add(new Tuplo<>(porto, fileInfo));

                // Contar fornecedores por ficheiro diretamente aqui
                fileInfoCountHashMap.put(
                    fileInfo,
                    fileInfoCountHashMap.getOrDefault(fileInfo, 0) + 1
                );
            }
        }
    }



    //Getters
    public String getSearchString(){return searchString;}
    public static HashMap<FileInfo,Integer> getFileInfoCountHashMap(){return fileInfoCountHashMap;}
    public static List<Tuplo<Integer, FileInfo>> getPortFileInfoList(){return portFileInfoList;}


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