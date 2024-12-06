package GUI;

import javax.swing.*;

import Logic.Node;
import Logic.Search.FileSearchManager;
import Logic.Utils.FileInfo;

import java.awt.*;        
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;

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
    

    //Tratar Search Button
    public void tratarSearchButton(String str){
        Node.getFileSearchManager().sendSearchRequest(str);
    }

}
