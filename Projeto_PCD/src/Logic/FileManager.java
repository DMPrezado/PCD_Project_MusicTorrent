package Logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Ponto 2
public class FileManager {
    private static final String WORKING_FOLDER_PATH = "Projeto_PCD/files";
    private List<String> nodeFiles;

    public FileManager() {
        nodeFiles = new ArrayList<>();
        loadNodeFiles();
    }


    // Método para carregar os ficheiros da pasta de trabalho
    private void loadNodeFiles() {
        File folder = new File(WORKING_FOLDER_PATH);

        // Verificar se a pasta existe e é um diretório
        if (folder.exists()) {

            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        nodeFiles.add(file.getName());
                    }
                }
            }

            // Exibir os ficheiros carregados (apenas para verificar)
            System.out.println("Ficheiros carregados:");
            for (String fileName : nodeFiles) {
                System.out.println("\t"+fileName);
            }

        } else {
            System.out.println("A pasta de trabalho não foi encontrada.");
        }
    }

    

    public void updateNodeFiles(){
        loadNodeFiles();
    }

    // Método para obter a lista de ficheiros partilhados
    public List<String> getNodeFiles() {
        return nodeFiles;
    }
}
