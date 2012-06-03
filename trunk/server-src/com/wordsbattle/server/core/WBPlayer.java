package com.wordsbattle.server.core;

import com.wordsbattle.common.net.messages.ServerMessage;
import com.wordsbattle.common.net.messages.ServerMessageType;

public class WBPlayer {
	private ClientHandler playerHandler;
	private WBGame game;
	private String name = null;
	
	WBPlayer(ClientHandler aPlyaerHandler) {
		this.playerHandler = aPlyaerHandler;	
	}
	
    public void setGame(WBGame aGame) {
    	this.game = aGame; 
	}
    
    public void sendGameBoard() {
    	// TODO(danichbloom):implement, sends current game board state.
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void opponentRequestsGame(String opponentName) {
		sendMessage(new ServerMessage(ServerMessageType.GAME_REQUEST, opponentName));
	}
	
	public void opponentReactedOnMyGameRequest(String opponentName, boolean reaction) {
		if (reaction) {			
			sendMessage(new ServerMessage(ServerMessageType.GAME_REQUEST_ACCEPTED, opponentName));
		} else {
			sendMessage(new ServerMessage(ServerMessageType.GAME_REQUEST_DENIED, opponentName));
		}
	}
	
	private void sendMessage(ServerMessage msg) {
		this.playerHandler.sendMessage(msg);
	}
}