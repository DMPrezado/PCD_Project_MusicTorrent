package Logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Logic.Search.FileSearchManager;
import Logic.Utils.FileInfo;

// Ponto 2
public class FileManager {
    private String folderPath;
    private File[] files;

    public FileManager(String folderPath) {
        this.folderPath = folderPath;
        loadNodeFiles();
        System.out.println(toString());
    }

    // Método para carregar os ficheiros da pasta de trabalho
    private void loadNodeFiles() {
        File folder = new File(folderPath);
        
        // Verificar se a pasta existe e é um diretório
        if (folder.exists()) {
            files = folder.listFiles();
        } else {
            System.out.println("A pasta de trabalho não foi encontrada.");
        }
    }

    public void addFileToNodeFolder(File newFile){
        File[] newFiles = new File[files.length+1];
        for(int i = 0; i<= files.length-1; i++)
            newFiles[i] = files[i];
        newFiles[files.length] = newFile;

        files = newFiles;
    }

    public List<String> getFilesNames() {
        List<String> filesNames = new ArrayList<String>();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    filesNames.add(file.getName());
                }
            }
        }

        return filesNames;
    }

    public File[] getFiles() {
        loadNodeFiles();
        return files;
    }

    public List<File> getFilesList(){
        List<File> filesList = new ArrayList<>();
        for (File file : files)
            filesList.add(file);
        return filesList;
    }
    
    public List<FileInfo> getFileSearchResults(FileSearchManager search) {
        String searchStr = search.getSearchString();
        List<FileInfo> matchingFiles = new ArrayList<FileInfo>();
    
        for (File file : files) {
            if (file.getName().toLowerCase().contains(searchStr.toLowerCase())) {
                matchingFiles.add(new FileInfo(file.getName(), file.length()));
            }
        }

        // Exibir os resultados
        System.out.println("Resultados da pesquisa por: " + searchStr);
        for (FileInfo fileInfo : matchingFiles) {
            System.out.println("\tNome: " + fileInfo.getName());
            System.out.println("\tTamanho: " + fileInfo.getLength() + " bytes");
            System.out.println();
        }

        return matchingFiles;
    }

    @Override
    public String toString() {
        String str = "";
        // Exibir os ficheiros carregados (apenas para verificar)
        str += "Ficheiros carregados:\n";
        for (String fileName : getFilesNames()) {
            str+= "\t"+fileName+"\n";
        }
        return str;
    }

    public List<FileInfo> searchFiles(String searchStr) {
        List<FileInfo> matchingFiles = new ArrayList<>();

        if (files == null || files.length == 0 || searchStr == null || searchStr.trim().isEmpty()) {
            return matchingFiles; // Retorna vazio se não houver arquivos ou string de busca inválida
        }

        String normalizedSearchStr = searchStr.trim().toLowerCase();

        File[] ficheiros = getFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().contains(normalizedSearchStr)) {
                matchingFiles.add(new FileInfo(file.getName(), file.length()));
            }
        }

        return matchingFiles;
    }

    
}
