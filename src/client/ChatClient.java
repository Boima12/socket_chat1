package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.File;
import java.util.Base64;
import java.nio.file.Files;
import java.net.Socket;

import client.ui.MainUI;
import common.Message;
import common.Utils;

public class ChatClient {
    private final String host;
    private final int port;
    private final String username;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    private Thread receiverThread;

    // direct reference to MainUI
    private final MainUI ui;

    public ChatClient(String host, int port, String username, MainUI ui) throws IOException {
        this.host = host;
        this.port = port;
        this.username = username;
        this.ui = ui;

        connect();
    }

    private void connect() throws IOException {
    	// TODO implement connect()
    }

    private void receiveLoop() {
    	// TODO implement receiveLoop()
    }

    public void sendText(String text) {
    	// TODO implement sendText()
    }
    
    public void sendImage(File file) {
    	// TODO implement sendImage()
    }

    public void close() {
    	// TODO implement close()
    }

    public boolean isConnected() {
    	// TODO implement isConnected()
    	
    	return true;
    }
}
