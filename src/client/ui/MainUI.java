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
            if (isChatting) {
                JOptionPane.showMessageDialog(this,
                        "You must disconnect before changing username!",
                        "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            askUsername("Change Username");
        });
        menuHome.add(menuChangeUser);

        JMenuItem menuExit = new JMenuItem("Exit");
        menuExit.addActionListener(e -> {
            disconnect();
            System.exit(0);
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
        tfAddress.addActionListener(e -> connect());
        leftPanel.add(tfAddress);

        btnConnect = new JButton("Connect");
        btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnConnect.setBounds(86, 326, 255, 51);
        btnConnect.addActionListener(e -> connect());
        leftPanel.add(btnConnect);

        btnDisconnect = new JButton("Disconnect");
        btnDisconnect.setEnabled(false);
        btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 16));
        btnDisconnect.setBounds(86, 399, 255, 51);
        btnDisconnect.addActionListener(e -> disconnect());
        leftPanel.add(btnDisconnect);

        JButton btnHelp = new JButton("?");
        btnHelp.setFont(new Font("Franklin Gothic Demi", Font.PLAIN, 20));
        btnHelp.setBackground(new Color(217, 217, 217));
        btnHelp.setBounds(351, 261, 48, 39);
        btnHelp.addActionListener(e -> HelpDialog.show(this));
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
        taInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !e.isShiftDown()) {
                    e.consume();
                    sendMessage();
                }
            }
        });
        JScrollPane spInput = new JScrollPane(taInput);
        spInput.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spInput.setBounds(0, 639, 508, 54);
        rightPanel.add(spInput);

        btnSend = new JButton("");
        btnSend.setEnabled(false);
        btnSend.setBounds(606, 639, 95, 54);
        btnSend.setIcon(new ImageIcon(new ImageIcon(MainUI.class.getResource("/Images/send.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btnSend.addActionListener(e -> sendMessage());
        rightPanel.add(btnSend);
        
        btnImageSend = new JButton("");
        btnImageSend.setEnabled(false);
        btnImageSend.setBounds(510, 639, 95, 54);
        btnImageSend.setIcon(new ImageIcon(new ImageIcon(MainUI.class.getResource("/Images/attach.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        btnImageSend.addActionListener(e -> sendImage());
        rightPanel.add(btnImageSend);

        // === Ask username at startup ===
        askUsername("Login");
    }

    private void askUsername(String action) {
        username = LoginDialog.askForUsername(this);
        setTitle("Socket Chat (" + username + ")");
    }

    private void connect() {
        chatModeOn();
        try {
            client = new ChatClient(tfAddress.getText(), Constants.PORT, username, this);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Couldn't connect to the server.\nCheck your connection.",
                    "Connection failed", JOptionPane.ERROR_MESSAGE);
            chatModeOff();
        }
    }

    private void sendMessage() {
        String msg = taInput.getText().trim();
        if (msg.isEmpty()) return;

        client.sendText(msg);
        taInput.setText("");
    }
    
    private void sendImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select an image to send");
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            client.sendImage(file);
        }
    }


    private void disconnect() {
        chatModeOff();
        if (client != null) {
            client.close();
            client = null;
        }
    }

    private void chatModeOn() {
        isChatting = true;
        btnConnect.setEnabled(false);
        tfAddress.setEnabled(false);
        btnDisconnect.setEnabled(true);
        taInput.setEnabled(true);
        btnSend.setEnabled(true);
        btnImageSend.setEnabled(true);
    }

    private void chatModeOff() {
        isChatting = false;
        btnConnect.setEnabled(true);
        tfAddress.setEnabled(true);
        btnDisconnect.setEnabled(false);
        taInput.setEnabled(false);
        btnSend.setEnabled(false);
        btnImageSend.setEnabled(false);
    }

    
    public void onMessage(Message msg) {
        SwingUtilities.invokeLater(() -> {
            switch (msg.type) {
                case TEXT -> addTextMessage(msg.sender, msg.content);
                case IMAGE -> addImageMessage(msg.sender, msg.content);
                case SYSTEM -> addTextMessage(msg.sender, msg.content);
            }
        });
    }
    
    // Helper to add message type
    private void addTextMessage(String sender, String message) {
        JPanel msgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
        	private static final long serialVersionUID = 1L;	// dòng này cho thêm vô cho đỡ bị warning chứ cũng không làm gì
        	
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize(); 
            }
        };
        msgPanel.setBackground(Color.WHITE);
        msgPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // padding inside

        JLabel lbl = new JLabel("<html><b>" + sender + ":</b> " + message + "</html>");
        msgPanel.add(lbl);
        
        msgPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // add to chatPanel
        chatPanel.add(msgPanel);
        chatPanel.revalidate();
        chatPanel.repaint();

        // auto scroll
        JScrollBar vertical = chatScroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private void addImageMessage(String sender, String base64Data) {
        JPanel msgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
        	private static final long serialVersionUID = 1L;	// dòng này cho thêm vô cho đỡ bị warning chứ cũng không làm gì
        	
            @Override
            public Dimension getMaximumSize() {
                return getPreferredSize(); // no stretching
            }
        };
        msgPanel.setBackground(Color.WHITE);
        msgPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        msgPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Sender label
        JLabel lbl = new JLabel("<html><b>" + sender + ":</b></html>");
        msgPanel.add(lbl);

        try {
            byte[] data = Base64.getDecoder().decode(base64Data);
            ImageIcon icon = new ImageIcon(data);

            // Scale to 300x300 max
            Image scaled = icon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            JLabel imgLbl = new JLabel(new ImageIcon(scaled));

            msgPanel.add(imgLbl);
        } catch (Exception e) {
            msgPanel.add(new JLabel("[Image load failed]"));
        }

        // add to chatPanel
        chatPanel.add(msgPanel);
        chatPanel.revalidate();
        chatPanel.repaint();

        // auto scroll
        JScrollBar vertical = chatScroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    public void onDisconnected() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                    "Disconnected from server.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            chatModeOff();
        });
    }
}
