package server.core;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
	private final static Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
	private WBConnection connection = null;
	private Socket clientSocket = null;
	
	public ClientHandler(Socket aClientSocket, String name) {
		this.clientSocket = aClientSocket;
		this.connection = new WBConnection(this.clientSocket);
	}
	@Override
	public void run() { 
		while (true){
			String str = this.connection.read();
			if (str != null) {
				LOGGER.log(Level.INFO, "Read: "+str + "\n");
				// to be sure that server can send and client can receive:
				LOGGER.log(Level.INFO, "Writes: Received your message\n");
				this.connection.println("Received your message");
			} else { 
				LOGGER.log(Level.INFO, "Client Disconnected\n");
				break; // TODO(danichbloom): don't forget to do everything that needed before finishing thread.
			}
		}
	}
}