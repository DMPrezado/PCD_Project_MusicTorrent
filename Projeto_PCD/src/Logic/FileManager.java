package Logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

// Ponto 2
public class FileManager {
    private static final String WORKING_FOLDER_PATH = "pasta de trabalho";
    private List<String> sharedFiles;

    public FileManager() {
        sharedFiles = new ArrayList<>();
        loadSharedFiles();
    }


    // Método para carregar os ficheiros da pasta de trabalho
    private void loadSharedFiles() {
        File folder = new File(WORKING_FOLDER_PATH);

        // Verificar se a pasta existe e é um diretório
        if (folder.exists()) {      // && folder.isDirectory()
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        sharedFiles.add(file.getName());
                    }
                }
            }
        } else {
            System.out.println("A pasta de trabalho não foi encontrada.");
        }

        // Exibir os ficheiros carregados (apenas para verificar)
        System.out.println("Ficheiros partilhados:");
        for (String fileName : sharedFiles) {
            System.out.println(fileName);
        }
    }

    // Método para obter a lista de ficheiros partilhados
    public List<String> getSharedFiles() {
        return sharedFiles;
    }
}
