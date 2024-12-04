package GUI;

import javax.swing.*;

import Logic.Connection;
import Logic.FileInfo;
import Logic.FileSearch;
import Logic.Node;

import java.awt.*;
import java.util.List;          
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IscTorrentGUI extends JFrame {
    private JList<String> resultList;
    private Node node;  // Campo para armazenar o nó local
    private JScrollPane jResultJScrollPane;



    public IscTorrentGUI(Node node) {
        this.node = node;

        //Configurações da Janela
        setTitle("IscTorrent " + node.getPort());
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
                Node.getNode().getDownloadTasksManager().searchFiles(new FileSearch(searchField.getText().toLowerCase()));
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
            node.connectTo(address, port);
           
        }
    }

    public void updateFileList() {
        jResultJScrollPane.removeAll();
        List<FileInfo> files = FileSearch.getResults();
    
        if (files == null || files.isEmpty()) {
            System.out.println("Nenhum arquivo encontrado.");
            return;
        }
    
        DefaultListModel<String> listModel = new DefaultListModel<>();
    
        for (FileInfo file : files) {
            System.out.println("Arquivo encontrado: " + file.getName());
            listModel.addElement(file.getName());
        }
    
        resultList.setModel(listModel);
        jResultJScrollPane.add(resultList);
    }

}
