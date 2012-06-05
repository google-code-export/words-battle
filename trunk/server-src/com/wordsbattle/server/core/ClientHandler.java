package com.wordsbattle.server.core;

import java.net.Socket;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wordsbattle.common.domain.Letter;
import com.wordsbattle.common.net.WBConnection;
import com.wordsbattle.common.net.messages.ClientMessage;
import com.wordsbattle.common.net.messages.ClientMessageType;
import com.wordsbattle.common.net.messages.ServerMessage;
import com.wordsbattle.common.net.messages.ServerMessageType;


public class ClientHandler implements Runnable {
	private final static Logger LOGGER = Logger.getLogger(ClientHandler.class);
	private WBConnection connection = null;
	private MultiThreadedServer server = null;
	private WBPlayer player = null;
	// For case when same player wants to reregister with diffrent name
	private boolean userIsRegistered = false;
	
	/** Client handler constructor 
	 * @param aClientSocket Server should pass the socket that 
	 * he obtained after acception of new connection
	 * @param aServer the main server instance
	 * */
	public ClientHandler(Socket aClientSocket, MultiThreadedServer aServer) {
		this.connection = new WBConnection(aClientSocket);
		this.server = aServer;
		this.player = new WBPlayer(this);
	}
	
	/** main listening loop */
	@Override
	public void run() { 
		while(true) {
			String str = this.connection.read();
			if (str != null) {
				LOGGER.debug("received: " + str);
				Gson gson = new Gson();
				ClientMessage message;
				try {
					message = gson.fromJson(str, ClientMessage.class);
					if (message != null) {
						HandleMessage(message);
					} else LOGGER.error("Message is badly formed:" + str);
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					LOGGER.error("Message is badly formed:" + str);
					e.printStackTrace();
				}
//				 to be sure that server can send and client can receive:
//				LOGGER.info("Writes: Received your message");
//				this.connection.println("Received your message");
			} else { 
				LOGGER.info("Client Disconnected");
				this.server.playerDisconnected(this.player);
				this.userIsRegistered = false;
				break; // TODO(danichbloom): don't forget to do everything that needed before finishing thread.
			}
		}
	}
	
	/** Sends message to user-client that is connected to this ClientHandler */
	public void sendMessage(ServerMessage message) {
		Gson gson = new Gson();
		LOGGER.debug("sent: " + gson.toJson(message));
		this.connection.println(gson.toJson(message));
	}	
	
	/** recognize message type and calls appropriate methods on server or on player */
	private void HandleMessage(ClientMessage msg) {
		String nameOfClient = "{" + player.getName() + "}: ";
		if (!this.userIsRegistered && msg.getType()!= ClientMessageType.REGISTER_PLAYER_NAME) {
			LOGGER.warn(nameOfClient + "User tries to do something without registration!");
			return;
		}
		switch (msg.getType()) {
		case REGISTER_PLAYER_NAME: {
			String PlayerName = msg.getPlayerName();
			LOGGER.info(nameOfClient + "User tries to register name: " + PlayerName);
			if (this.userIsRegistered) {
				sendMessage(new ServerMessage(ServerMessageType.MULTIPLE_NAMES_FOR_ONE_USER_CONFLICT, PlayerName));
			} else {
				this.player.setName(PlayerName);
				if (this.server.registerPlayer(this.player)) {
					sendMessage(new ServerMessage(ServerMessageType.NAME_REGISTERED, PlayerName));
					this.userIsRegistered = true;
				} else {
					sendMessage(new ServerMessage(ServerMessageType.NAME_CONFLICT, PlayerName));
				}
			}
			break;
		}
		case REQUEST_GAME: {
			String opponentName = msg.getPlayerName();
			LOGGER.info(nameOfClient + "User requests game with opponent: " + opponentName);
			this.server.playerRequestsGameWithOpponent(this.player.getName(), opponentName);
			break;
		}	
		case DENY_GAME_REQUEST: {
			String playerName = msg.getPlayerName();
			String opponentName = this.player.getName();
			LOGGER.info(nameOfClient + "Player " + playerName + "denied game request from opp " + opponentName);
			this.server.opponentReactedOnGameRequestFromPlayer(playerName, opponentName, false);
			break;
		}
		case ACCEPT_GAME_REQUEST: {
			String playerName = msg.getPlayerName();
			String opponentName = this.player.getName();
			LOGGER.info(nameOfClient + "Player " + playerName + " accepted game request from opp " + opponentName);
			this.server.opponentReactedOnGameRequestFromPlayer(playerName, opponentName, true);			
			break;
		}
		case PICK_LETTER: {
			Letter letter = msg.getLetter();
			if (player.pickLetter(letter)) {
				LOGGER.info(nameOfClient + "Player successfully picked a letter " + letter);
			} else {
				LOGGER.info(nameOfClient + "Player failed to pick a letter " + letter);
			}
			break;
		}
		default:
			LOGGER.warn("Unrecognized message type" + msg.getType());
			break;
		}
	}
}