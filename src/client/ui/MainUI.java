package client.ui;

import client.ChatClient;
import common.Constants;
import common.Message;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

public class MainUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField tfAddress;
    private JButton btnConnect;
    private JButton btnDisconnect;
    private JPanel chatPanel;
    private JScrollPane chatScroll;
    private JTextArea taInput;
    private JButton btnSend;

    private ChatClient client;
    private String username;
    private boolean isChatting = false;
    private JButton btnImageSend;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            } catch (Exception ignored) {}

            try {
                MainUI frame = new MainUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MainUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1170, 770);
        setTitle("Socket_chat1");
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // === Menu ===
        JMenuBar menuBar = new JMenuBar();
        JMenu menuHome = new JMenu("Home");
        menuBar.add(menuHome);

        JMenuItem menuChangeUser = new JMenuItem("Change Username");
        menuChangeUser.addActionListener(e -> {            
            // TODO check isChatting before changing the name, then implement askUsername
        });
        menuHome.add(menuChangeUser);

        JMenuItem menuExit = new JMenuItem("Exit");
        menuExit.addActionListener(e -> {
        	// TODO disconnect() then exit the program
        });
        menuHome.add(menuExit);
        setJMenuBar(menuBar);

        // === Left panel ===
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(219, 219, 219));
        leftPanel.setBounds(10, 10, 436, 693);
        leftPanel.setLayout(null);
        contentPane.add(leftPanel);

        JLabel lblAddress = new JLabel("IPv4 Address:");
        lblAddress.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblAddress.setBounds(87, 236, 136, 25);
        leftPanel.add(lblAddress);

        tfAddress = new JTextField(Constants.DEFAULT_HOST);
        tfAddress.setFont(new Font("Tahoma", Font.PLAIN, 14));
        tfAddress.setBackground(new Color(229, 229, 229));
        tfAddress.setBounds(86, 261, 255, 39);
        tfAddress.setColumns(10);
        // TODO make tfAddress run connect() when interact
        leftPanel.add(tfAddress);

        btnConnect = new JButton("Connect");
        btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnConnect.setBounds(86, 326, 255, 51);
        // TODO make btnConnect run connect() when interact
        leftPanel.add(btnConnect);

        btnDisconnect = new JButton("Disconnect");
        btnDisconnect.setEnabled(false);
        btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnDisconnect.setBounds(86, 399, 255, 51);
        // TODO make btnDisconnect run disconnect() when interact
        leftPanel.add(btnDisconnect);

        JButton btnHelp = new JButton("?");
        btnHelp.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
        btnHelp.setBackground(new Color(217, 217, 217));
        btnHelp.setBounds(351, 261, 48, 39);
        // TODO make btnHelp show HelpDialog when interact
        leftPanel.add(btnHelp);

        // === Right panel ===
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(new Color(243, 243, 243));
        rightPanel.setBounds(445, 10, 701, 693);
        rightPanel.setLayout(null);
        contentPane.add(rightPanel);
        
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        chatPanel.setBackground(new Color(226, 226, 226));
        chatScroll = new JScrollPane(chatPanel);
        chatScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        chatScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        chatScroll.setBounds(0, 0, 701, 640);
        rightPanel.add(chatScroll);

        taInput = new JTextArea();
        taInput.setEnabled(false);
        taInput.setWrapStyleWord(true);
        taInput.setLineWrap(true);
        taInput.setFont(new Font("Tahoma", Font.PLAIN, 12));
        taInput.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        taInput.setBackground(new Color(239, 239, 239));
        // TODO make taInput sendMessage() when press Enter key
        JScrollPane spInput = new JScrollPane(taInput);
        spInput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spInput.setBounds(0, 639, 508, 54);
        rightPanel.add(spInput);

        btnSend = new JButton("");
        btnSend.setEnabled(false);
        btnSend.setBounds(606, 639, 95, 54);
        btnSend.setIcon(new ImageIcon(new ImageIcon(MainUI.class.getResource("/Images/send.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        // TODO make btnSend run sendMessage() when interact
        rightPanel.add(btnSend);
        
        btnImageSend = new JButton("");
        btnImageSend.setEnabled(false);
        btnImageSend.setBounds(510, 639, 95, 54);
        btnImageSend.setIcon(new ImageIcon(new ImageIcon(MainUI.class.getResource("/Images/attach.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        // TODO make btnImageSend run sendImage() when interact
        rightPanel.add(btnImageSend);

        // === Ask username at startup ===
        askUsername("Login");
    }

    private void askUsername(String action) {
    	// TODO implement askUsername()
    }

    private void connect() {
    	// TODO implement connect()
    }

    private void sendMessage() {
    	// TODO implement sendMessage()
    }
    
    private void sendImage() {
    	// TODO implement sendImage()
    }

    private void disconnect() {
    	// TODO implement disconnect()
    }

    private void chatModeOn() {
    	// TODO implement chatModeOn()
    }

    private void chatModeOff() {
    	// TODO implement chatModeOff()
    }

    public void onMessage(Message msg) {
    	// TODO implement onMessage()
    }
    
    
    private void addTextMessage(String sender, String message) {
    	// TODO implement addTextMessage()
    }

    private void addImageMessage(String sender, String base64Data) {
    	// TODO implement addImageMessage()
    }

    public void onDisconnected() {
    	// TODO implement onDisconnected()
    }
}
