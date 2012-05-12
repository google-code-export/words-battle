package server.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.core.WBConnection;


public class TCPClient implements Runnable{
	private final static Logger LOGGER = Logger.getLogger(TCPClient.class.getName());
	private String address;
	private int clientPort;
	private WBConnection connection;
	private TCPClient blinker;
	
	public TCPClient(String address, int port) {
		this.address = address;
		this.clientPort = port;
		this.connection = null;
		this.blinker = this;
		new Thread(this).start();
	}

	@Override
	public void run() {
		Socket connectionSocket = null;
		try {
			connectionSocket = new Socket(this.address, this.clientPort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		final WBConnection con = new WBConnection(connectionSocket);
		this.connection = con;
		while(this.blinker == this) {
			if (con.ready()) {
				String str = con.read();
				LOGGER.log(Level.INFO, "Read: " + str + "\n");
			}
		}
	}
	
	public void stop() {
		this.connection.closeConnection();
		this.blinker = null;
	}
	
	public boolean isConnected() {
		return this.connection != null ? true : false;
	}

	public void sendMessage(String message) {
		LOGGER.log(Level.INFO, "Writes: " + message + "\n");
		this.connection.println(message);
	}
	
	public static void main(String[] args) throws Exception {
		TCPClient client = new TCPClient("localhost", 6789);
		new Thread(client).start();
		while(true) {
			System.out.print("[Client] Enter message: ");
			BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
			String sentence = "none";
			try {
				sentence = inFromUser.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			client.sendMessage(sentence);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}	
	}
}

