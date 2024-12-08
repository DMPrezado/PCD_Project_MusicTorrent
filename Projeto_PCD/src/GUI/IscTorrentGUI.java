package GUI;

import javax.swing.*;

import Logic.Node;
import Logic.Download.DownloadManager;
import Logic.Search.FileSearchManager;
import Logic.Utils.FileInfo;
import Logic.Utils.Tuplo;

import java.awt.*;        
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IscTorrentGUI extends JFrame {
    private JList<String> resultList;
    private Node node;  // Campo para armazenar o nó local
    private JScrollPane jResultJScrollPane;



    public IscTorrentGUI() {

        //Configurações da Janela
        setTitle("IscTorrent " + Node.getPort());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Gerenciar o comportamento de fechamento
        setSize(600, 350);
        setLayout(new BorderLayout());

        addComponents();

        // Centraliza a janela no ecrã
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addComponents(){
        // Campo de pesquisa e botão
        JPanel searchPanel = new JPanel(new BorderLayout());
        JLabel searchLabel = new JLabel("Texto a procurar:");
        JTextField searchField = new JTextField();
        JButton buttonSearch = new JButton("Procurar");
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonSearch, BorderLayout.EAST);
        add(searchPanel, BorderLayout.NORTH);

        // Lista de resultados vazia
        resultList = new JList<>();
        jResultJScrollPane = new JScrollPane(resultList);
        add(jResultJScrollPane, BorderLayout.CENTER);


        // Botões de ação
        JPanel actionPanel = new JPanel(new GridLayout(2, 1));
        JButton buttonDownload = new JButton("Descarregar");
        JButton buttonConnectToNode = new JButton("Ligar a Nó");
        actionPanel.add(buttonDownload);
        actionPanel.add(buttonConnectToNode);
        add(actionPanel, BorderLayout.EAST);

        // Conectar
        buttonConnectToNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openConnectionDialog();
            }
        });

        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Botão Search Tratar
                tratarSearchButton(searchField.getText());
            }
        });

        buttonDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Botão Search Tratar
                tratarButtonDownload();
                mostrarDetalhesDownload();
            }
        });
    }

    private void openConnectionDialog() {
        JTextField txtAddress = new JTextField("localhost");
        JTextField txtPort = new JTextField("8081");
        Object[] message = {"Endereço:", txtAddress, "Porta:", txtPort};

        int option = JOptionPane.showConfirmDialog(null, message, "Ligar a Nó", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String address = txtAddress.getText();
            int port = Integer.parseInt(txtPort.getText());
            
            // Conectar ao nó especificado
            Node.connectTo(address, port);
           
        }
    }

    public void updateFileList() {
        Node.getFileSearchManager();
        // Obtém o HashMap de arquivos e suas contagens
        HashMap<FileInfo,Integer> fileInfoCountMap = FileSearchManager.getFileInfoCountHashMap();
    
        if (fileInfoCountMap != null && !fileInfoCountMap.isEmpty()) {
            // Cria um modelo para a lista de resultados
            DefaultListModel<String> listModel = new DefaultListModel<>();
            
            for (FileInfo fileInfo : fileInfoCountMap.keySet()) {
                int count = fileInfoCountMap.get(fileInfo);
                String displayText = String.format("%s <%d>", fileInfo.getName(), count);
                listModel.addElement(displayText);
            }
    
            // Atualiza a JList com os resultados
            resultList.setModel(listModel);
        } else {
            // Caso não haja resultados, limpa a lista e exibe mensagem
            resultList.setModel(new DefaultListModel<>());
            JOptionPane.showMessageDialog(this, "Nenhum resultado encontrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    

    // Tratar Search Button
    public void tratarSearchButton(String str){
        Node.getFileSearchManager().sendSearchRequest(str);
    }
    
    // Tratar Download Button
    private void tratarButtonDownload() {

        DownloadManager.clearReceivedChunks();
        // Obter as seleções da lista de resultados
        List<String> selectedValues = resultList.getSelectedValuesList();

        if (!selectedValues.isEmpty()) {
            Node.getDownloadManager().requestFiles(selectedValues);
            return;
        } 
        JOptionPane.showMessageDialog(this, "Nenhum ficheiro selecionado para download.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }

    // Mostrar detalhes download
    private void mostrarDetalhesDownload() {
        // Obter os tempos de download e os fornecedores
        HashMap<FileInfo, Long> tempos = DownloadManager.getTempos();

        if (tempos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum ficheiro foi descarregado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Download completo.\n");

        // Iterar pelos tempos e construir a mensagem
        for (Map.Entry<FileInfo, Long> entry : tempos.entrySet()) {
            FileInfo fileInfo = entry.getKey();
            long tempoTotal = entry.getValue() / 1000;    // não estou a conseguir ter o tempo que está no system.out

            mensagem.append(String.format("Ficheiro: %s\n", fileInfo.getName()));

            // Obter os fornecedores associados ao ficheiro
            List<Tuplo<Integer, FileInfo>> fornecedores = FileSearchManager.getPortFileInfoList();
            for (Tuplo<Integer, FileInfo> fornecedor : fornecedores) {
                if (fornecedor.getSegundo().equals(fileInfo)) {
                    // tbm n estou a conseguir ir buscar o porto certo, nem o endereço. Aqui está hard coded mas é pra mudar
                    mensagem.append(String.format("Fornecedor [endereco=/127.0.0.1, porto=%d]; \n", fornecedor.getPrimeiro()));
                }
            }
            mensagem.append(String.format("Tempo decorrido: %ds\n", tempoTotal));
        }

        // Exibir mensagem em um JOptionPane
        JOptionPane.showMessageDialog(this, mensagem.toString(), "Detalhes do Download", JOptionPane.INFORMATION_MESSAGE);
    }    

}
