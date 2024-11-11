package GUI;

import javax.swing.*;

import Logic.FileManager;
import Logic.Node;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IscTorrentGUI extends JFrame {
    private JList<String> resultList;
    private FileManager fileManager;
    private Node localNode;  // Campo para armazenar o nó local
    private String nodeAddress;  // Endereço do nó local
    private int nodePort;        // Porto do nó local

    public IscTorrentGUI(String nodeAddress, int nodePort) {
        this.nodeAddress = nodeAddress;  // Recebe o endereço do nó
        this.nodePort = nodePort;        // Recebe a porta do nó

        setTitle("IscTorrent");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setLayout(new BorderLayout());

        // Campo de pesquisa e botão
        JPanel searchPanel = new JPanel(new BorderLayout());

        JLabel searchLabel = new JLabel("Texto a procurar:");
        JTextField searchField = new JTextField();
        JButton buttonSearch = new JButton("Procurar");
        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(buttonSearch, BorderLayout.EAST);
        add(searchPanel, BorderLayout.NORTH);

        // Lista de resultados
        resultList = new JList<>();
        add(new JScrollPane(resultList), BorderLayout.CENTER);

        // Botões de ação
        JPanel actionPanel = new JPanel(new GridLayout(2, 1));

        JButton buttonDownload = new JButton("Descarregar");
        JButton buttonConnectToNode = new JButton("Ligar a Nó");
        actionPanel.add(buttonDownload);
        actionPanel.add(buttonConnectToNode);
        add(actionPanel, BorderLayout.EAST);

        // Centraliza a janela no ecrã
        setLocationRelativeTo(null);

        // Nó que inicializa a GUI
        getLocalNode();

        // Listeners
        buttonConnectToNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openConnectionDialog();
            }
        });
    }

    private void getLocalNode() {
        // Nó local com o endereço e porto recebidos
        localNode = new Node(nodeAddress, nodePort);
        System.out.println("Nó local: " + nodeAddress + " : " + nodePort);
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
    private void updateResultList() {
        // Atualizar a lista de resultados com os ficheiros partilhados
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String fileName : fileManager.getSharedFiles()) {
            listModel.addElement(fileName);
        }
        resultList.setModel(listModel);
    }

    public void showGUI() {
        setVisible(true);
    }
}