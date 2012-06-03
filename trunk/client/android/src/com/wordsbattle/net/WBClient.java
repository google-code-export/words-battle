package com.wordsbattle.net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wordsbattle.common.net.WBConnection;
import com.wordsbattle.common.net.messages.ClientMessage;
import com.wordsbattle.common.net.messages.ClientMessageType;
import com.wordsbattle.common.net.messages.ServerMessage;
/** be careful with the name. If user wants to change 
 * name you need to stop currently 
 * running WBClient and create a new one. */
public class WBClient implements Runnable{
	private final static Logger LOGGER = Logger.getLogger(WBClient.class);
	private String serverAddress = null;
	private int port = 0;
	private WBConnection connection = null;
	private WBClient blinker = null;
	//private String name = null;
	//private boolean nameIsRegistered = false;
	//private boolean ServerAnsweredRegRquest = true;
	private IWBClientDelegate delegate = null;
		
	public WBClient(String aServerAddress, int aPort, IWBClientDelegate aDelegate) {
		this.serverAddress = aServerAddress;
		this.port = aPort;
		this.blinker = this;
		this.delegate = aDelegate;
	}

	@Override
	public void run() {
		while(this.blinker == this) {
			if (this.connection.ready()) {
				String str = this.connection.read();
				LOGGER.debug("received: " + str);
				Gson gson = new Gson();
				try {
					ServerMessage message = gson.fromJson(str, ServerMessage.class); 
					HandleMessage(message);
				} catch (JsonSyntaxException e) {
					// TODO Auto-generated catch block
					LOGGER.error("Message is badly formed:" + str);
					e.printStackTrace();
				}
			}
		}
	}
	
	public void stop() {
		this.connection.closeConnection();
		this.blinker = null;
	}
	
	/** Tries to connect to server
	 *  @return true in case of success and false otherwise. 
	 *  */
	public boolean ConnectToServer() {
		Socket connectionSocket = null;
		try {
			connectionSocket = new Socket(this.serverAddress, this.port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (connectionSocket != null) {
			final WBConnection con = new WBConnection(connectionSocket);
			this.connection = con;
			new Thread(this).start();
			return true;
		} else return false; 
	}
		
	public void sendMessage(ClientMessage message) {
		Gson gson = new Gson();
		LOGGER.debug("sent: " + gson.toJson(message));
		this.connection.println(gson.toJson(message));
	}	
	/** Send to server registration request for userName. */
	public void sendRegitsteringRequestForPlayerName(String userName) {
		sendMessage(new ClientMessage(ClientMessageType.REGISTER_PLAYER_NAME, userName));
	}
	
	public void sendGameRequest(String opponentName) {
		sendMessage(new ClientMessage(ClientMessageType.REQUEST_GAME, opponentName));
	}
	
	private void HandleMessage(ServerMessage msg) {
		switch (msg.getType()) {
		case NAME_CONFLICT:
			LOGGER.info("Name conflic!");
			this.delegate.UserNameAlreadyExists();
			break;
		case NAME_REGISTERED:			
			LOGGER.info("Name registered!");
			//this.nameIsRegistered = true;
			this.delegate.UserNameSuccessfullyRegistered();
			break;
		case MULTIPLE_NAMES_FOR_ONE_USER_CONFLICT:
			LOGGER.info("User tries to reregister, he is already registered this connection!");
			this.delegate.UserTriesToReregister();
			break;
		case GAME_REQUEST:{
			String opponentName = msg.getPlayerName();
			LOGGER.info("User recieves game request from user " + opponentName);
			if (this.delegate.opponentRequestsGame(opponentName)) {
				sendMessage(new ClientMessage(ClientMessageType.ACCEPT_GAME_REQUEST, opponentName));
			} else {
				sendMessage(new ClientMessage(ClientMessageType.DENY_GAME_REQUEST, opponentName));
			}
			break;
		}
		case GAME_REQUEST_ACCEPTED: {
			String opponentName = msg.getPlayerName();
			LOGGER.info("Opponent "+ opponentName + " accepted game request ");
			this.delegate.opponentAcceptedGameRequest(opponentName);
			break;
		}
		case GAME_REQUEST_DENIED: {
			String opponentName = msg.getPlayerName();
			LOGGER.info("Opponent "+ opponentName + " declined game request ");			
			this.delegate.opponentDeniedGameRequest(opponentName);
			break;
		}
		case UPDATE: {
			LOGGER.info("Recieved update");			
			this.delegate.newPool(msg.getLetterPool());
			break;
		}
		default:
			LOGGER.warn("Unrecognized message type" + msg.getType());
			break;
		}
	}
}