package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import common.Constants;

public class ChatServer {
	private final ServerSocket serverSocket;
	private final ExecutorService clientPool = Executors.newCachedThreadPool();
	// thread-safe list for handlers
	public static final CopyOnWriteArrayList<ClientHandler> handlers = new CopyOnWriteArrayList<>();
	private volatile boolean running = true;

	public ChatServer(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void start() {
		System.out.println("Server started on port " + serverSocket.getLocalPort());
		try {
			while (running && !serverSocket.isClosed()) {
				Socket client = serverSocket.accept();
				ClientHandler handler = new ClientHandler(client);
				handlers.add(handler);
				clientPool.submit(handler);
			}
		} catch (IOException e) {
			if (running)
				e.printStackTrace();
		} finally {
			stop();
		}
	}

	public void stop() {
		running = false;
		try {
			for (ClientHandler h : handlers) {
				h.shutdown();
			}

			clientPool.shutdownNow();

			if (!serverSocket.isClosed()) {
				serverSocket.close();
			}

			System.out.println("Server stopped.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** Broadcast a line to every connected client (non-blocking for loop). */
    public static void broadcast(String msg) {
        for (ClientHandler h : handlers) {
            if (!h.send(msg)) {
                h.shutdown();
            }
        }
    }

	public static void main(String[] args) throws Exception {
		int port = Constants.PORT;
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			ChatServer server = new ChatServer(serverSocket);
			server.start();
		}
	}
}