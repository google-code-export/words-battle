package com.wordsbattle.server.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class MultiThreadedServer implements Runnable {
	private final static Logger LOGGER = Logger.getLogger(MultiThreadedServer.class);	
	private int serverPort;
	private ServerSocket serverSocket = null;
	private boolean isStopped = false;

	private Vector<WBPlayer> randomPlayersPool;
	private HashMap<String, WBPlayer> players;
	private Vector<WBGame> games;
	
	public MultiThreadedServer(int port) {
		this.serverPort = port;
		this.randomPlayersPool = new Vector<WBPlayer>();
		this.players = new HashMap<String, WBPlayer>();
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
			LOGGER.log(Level.INFO, "Accepted player with address: "+clientSocket.getInetAddress().getHostAddress());
			new Thread(new ClientHandler(clientSocket, this)).start();			
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
	
	public void playerDisconnected(WBPlayer player) {
		this.players.remove(player.getName());
	}
	
	public boolean registerPlayer(WBPlayer newPlayer) {
		if (!playerWithNameIsRegistered(newPlayer.getName())) {
			this.players.put(newPlayer.getName(), newPlayer);
			return true;
		} else return false;
	}
	
	public boolean playerWithNameIsRegistered(String name) {
		return this.players.containsKey(name);
	}
	
	public void playerRequestsGameWithOpponent(String playerName, String opponentName) {
		if (!playerWithNameIsRegistered(opponentName)) {
			// TODO(danichbloom): implement for new ServerMessageType: NO_USER_WITH_REQUESTED_NAME
			return;
		}
		WBPlayer opponent = this.players.get(opponentName);
		opponent.opponentRequestsGame(playerName);
	}
	
	public void opponentReactedOnGameRequestFromPlayer(String playerName, String opponentName, boolean reaction) {
		//TODO(danichbloom): if accepts than creat game
		WBPlayer player = this.players.get(playerName);
		player.opponentReactedOnMyGameRequest(opponentName, reaction);
	}
	
	public static void main(String[] args) {
		MultiThreadedServer server = new MultiThreadedServer(6789);
		new Thread(server).start();    	
	}
}