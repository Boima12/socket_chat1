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
	public static final CopyOnWriteArrayList<ClientHandler> handlers = new CopyOnWriteArrayList<>();
	private volatile boolean running = true;

	public ChatServer(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	public void start() {
		// TODO implement start()
	}

	public void stop() {
		// TODO implement stop()
	}

	public static void broadcast(String line) {
		// TODO implement broadcast()
	}

	public static void main(String[] args) throws Exception {
		// TODO implement main()
	}
}