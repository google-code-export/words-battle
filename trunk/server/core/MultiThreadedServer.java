package server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Logger;

public class MultiThreadedServer implements Runnable {
	private final static Logger LOGGER = Logger.getLogger(MultiThreadedServer.class);	
	private int serverPort;
	private ServerSocket serverSocket = null;
	private boolean isStopped = false;

	public MultiThreadedServer(int port) {
		this.serverPort = port;
	}

	public void run() {
		openServerSocket();
		while(!isStopped()) {
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
			} catch (IOException e) {
				if(isStopped()) {
					LOGGER.info("Server Stopped.");
					return;
				}
				throw new RuntimeException("Error accepting client connection", e);
			}
			new Thread(new ClientHandler(clientSocket, "Multithreaded Server")).start();
		}
		LOGGER.info("Server Stopped.\n");
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop() {
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port " + this.serverPort, e);
		}
	}

	public static void main(String[] args) {
		MultiThreadedServer server = new MultiThreadedServer(6789);
		new Thread(server).start();    	
	}
}