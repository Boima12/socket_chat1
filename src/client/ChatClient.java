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
        socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);

        // send username as first line
        writer.println(username);

        // start receiver
        receiverThread = new Thread(this::receiveLoop, "chat-client-receiver");
        receiverThread.start();
    }

    private void receiveLoop() {
        try {
            String line;
            while (socket != null && !socket.isClosed() && (line = reader.readLine()) != null) {
                Message msg = Message.fromJson(line);
                ui.onMessage(msg);
            }
        } catch (IOException e) {
            // connection closed/failed
        } finally {
            close();
            ui.onDisconnected();
        }
    }

    public void sendText(String text) {
        if (writer != null) {
            Message msg = new Message(Message.Type.TEXT, username, text);
            writer.println(msg.toJson());
        }
    }
    
    public void sendImage(File file) {
        if (writer != null && file != null) {
            try {
                byte[] data = Files.readAllBytes(file.toPath());
                String base64 = Base64.getEncoder().encodeToString(data);

                Message msg = new Message(Message.Type.IMAGE, username, base64);
                writer.println(msg.toJson());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        try {
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (IOException ignored) {
        }
        Utils.safeClose(reader);
        Utils.safeClose(writer);
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
}
