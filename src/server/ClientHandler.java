package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
		// TODO implement run()
	}

	public boolean send(String line) {
		// TODO implement send()
		
		return true;
	}

	public void shutdown() {
		// TODO implement shutdown()
	}

	public String getUsername() {
		return username;
	}
}