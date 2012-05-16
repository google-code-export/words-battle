package server.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WBConnection {
	private Socket clientSocket = null;
	private PrintWriter out;
	private BufferedReader in;
	
	public WBConnection(Socket aSocket) {
		try {
			clientSocket = aSocket; 
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void println(String message) {
		out.println(message);
	}
	
	public boolean ready() {
		boolean ready = false;
		try {
			ready = this.in.ready();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return ready;
	}

	public String read() {
		String string = null;
		try {
			string = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return string;
	}

	public void closeConnection() {
		try {
			this.clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}