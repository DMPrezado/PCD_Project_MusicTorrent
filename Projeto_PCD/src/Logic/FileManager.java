package Logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Ponto 2
public class FileManager {
    private static final String WORKING_FOLDER_PATH = "Projeto_PCD/files";
    private File[] files;

    public FileManager() {
        loadNodeFiles();
        System.out.println(toString());
    }

    // Método para carregar os ficheiros da pasta de trabalho
    private void loadNodeFiles() {
        File folder = new File(WORKING_FOLDER_PATH);

        // Verificar se a pasta existe e é um diretório
        if (folder.exists()) {

            files = folder.listFiles();
            toString();

        } else {
            System.out.println("A pasta de trabalho não foi encontrada.");
        }
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

    public void updateNodeFiles(){
        loadNodeFiles();
    }

    public File[] getFiles() {
        return files;
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
}
