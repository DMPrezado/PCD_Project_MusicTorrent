package GUI;

import javax.swing.*;

import Logic.FileManager;
import Logic.Node;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class IscTorrentGUI extends JFrame {
    private JList<File> resultList;
    private FileManager fileManager;
    private Node localNode;  // Campo para armazenar o nó local
    private String nodeAddress;  // Endereço do nó local
    private int nodePort;        // Porto do nó local
    private DefaultListModel<File> filesListModel = new DefaultListModel<>();



    public IscTorrentGUI(Node node) {
        this.localNode = node;
        this.nodeAddress = node.getAddress();  // Recebe o endereço do nó
        this.nodePort = node.getPort();        // Recebe a porta do nó

        //Configurações da Janela
        setTitle("IscTorrent"+ nodePort);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setLayout(new BorderLayout());

        addComponents();

        // Centraliza a janela no ecrã
        setLocationRelativeTo(null);

        updateFileList();

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
        JList<File> resultList = new JList<>(filesListModel);
        // Criar um renderizador de células para exibir apenas o nome do arquivo
        resultList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
            // Exibe o nome do arquivo usando value.getName()
            return new JLabel(value.getName());
        });
        add(new JScrollPane(resultList), BorderLayout.CENTER);


        // Botões de ação
        JPanel actionPanel = new JPanel(new GridLayout(2, 1));
        JButton buttonDownload = new JButton("Descarregar");
        JButton buttonConnectToNode = new JButton("Ligar a Nó");
        actionPanel.add(buttonDownload);
        actionPanel.add(buttonConnectToNode);
        add(actionPanel, BorderLayout.EAST);

        // Listeners
        buttonConnectToNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openConnectionDialog();
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
            
            // Conetar a outros nós
            // Verificar se o nó local foi inicializado e conectado
            if (localNode != null) {
                // Conectar ao nó especificado
                localNode.connectTo(address, port);
                System.out.println("Conectado a " + address + " no porto " + port);
            } else {
                System.out.println("Erro: Nó local não inicializado.");
            }
        }
    }

    // Ponto 2
    public void updateFileList() {
        filesListModel.clear();

        File[] files = localNode.getFileManager().getFiles();
        for (File file : files) {
            filesListModel.addElement(file); // Add each file or directory to the model
        }

        //Para atualizao o JList??????
        //resultList.setModel(filesListModel);

    }

}
