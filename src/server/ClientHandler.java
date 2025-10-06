package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import common.Message;
import common.Utils;

public class ClientHandler implements Runnable {
	private final Socket socket;
	private volatile boolean active = true;
	private String username = "Unknown";

	private BufferedReader reader;
	private PrintWriter writer;

	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			Message msg;

			// First line from client should be the username
			String nameLine = reader.readLine();
			if (nameLine == null || nameLine.trim().isEmpty()) {
				username = "Guest-" + socket.getPort();
			} else {
				username = nameLine.trim();
			}

			// connect notification
			System.out.println(username + " connected from " + socket.getRemoteSocketAddress());
			msg = new Message(Message.Type.SYSTEM, "Server", username + " has joined the chat.");
			ChatServer.broadcast(msg.toJson());

			String line;
			while (active && (line = reader.readLine()) != null) {
				if (line.equalsIgnoreCase("/quit") || !socket.isConnected()) {
					break;
				}
//			    msg = Message.fromJson(line);
			    ChatServer.broadcast(line);
			}

		} catch (IOException e) {
			// connection dropped
			// e.printStackTrace();
		} finally {
			shutdown();
		}
	}

	/** Send a line to this client. Returns true on success. */
	public boolean send(String line) {
		try {
			if (writer != null) {
				writer.println(line);
				return true;
			}
		} catch (Exception ignored) {
		}
		return false;
	}

	public void shutdown() {
		if (!active)
			return;
		active = false;
		try {
			ChatServer.handlers.remove(this);
			System.out.println(username + " disconnected.");
			
			Message msg = new Message(Message.Type.SYSTEM, "Server", username + " has left the chat.");
			ChatServer.broadcast(msg.toJson());
			
			Utils.safeClose(reader);
			Utils.safeClose(writer);
			if (socket != null && !socket.isClosed())
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getUsername() {
		return username;
	}
}