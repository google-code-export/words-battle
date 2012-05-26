package server.core;

import java.net.Socket;

import org.apache.log4j.Logger;

public class ClientHandler implements Runnable {
	private final static Logger LOGGER = Logger.getLogger(ClientHandler.class);
	private WBConnection connection = null;
	private Socket clientSocket = null;
	
	public ClientHandler(Socket aClientSocket, String name) {
		this.clientSocket = aClientSocket;
		this.connection = new WBConnection(this.clientSocket);
	}
	
	@Override
	public void run() { 
		while(true) {
			String str = this.connection.read();
			if (str != null) {
				LOGGER.info("Read: " + str);
				// to be sure that server can send and client can receive:
				LOGGER.info("Writes: Received your message");
				this.connection.println("Received your message");
			} else { 
				LOGGER.info("Client Disconnected");
				break; // TODO(danichbloom): don't forget to do everything that needed before finishing thread.
			}
		}
	}
}