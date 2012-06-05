package com.wordsbattle.common.net.messages;

import com.wordsbattle.common.domain.LetterPool;
import com.wordsbattle.common.domain.Player;

public class ServerMessage {
	private ServerMessageType type;
	
	// Message payload ------------------------------------
	private String playerName;
	private LetterPool letterPool;
	private Player player;
	private Player opponent;
	// ----------------------------------------------------
	
	public String getPlayerName() {
		return playerName;
	}
	public LetterPool getLetterPool() {
		return letterPool;
	}
	public Player getPlayer() {
		return player;
	}
	public Player getOpponent() {
		return opponent;
	}
	
	public ServerMessageType getType() {
		return type;
	}
	
	/** Constructor for types:
	 * GAME_STARTED, GAME_ENDED,
	 * GAME_PAUSED, GAME_RESUMED
	 */
	public ServerMessage (ServerMessageType type) {
		this.type = type;
	}
	
	/** Constructor for types:
	 * NAME_REGISTERED, NAME_CONFLICT, MULTIPLE_NAMES_FOR_ONE_USER_CONFLICT
	 * GAME_REQUEST, GAME_REQUEST_ACCEPTED, GAME_REQUEST_DENIED
	 */
	public ServerMessage (ServerMessageType type, String playerName) {
		this(type);
		this.playerName = playerName;
	}
	
	/** Constructor for type: UPDATE 
	 * @param pool new letter pool
	 * @param player new data for user itself. <b>player</b> - the one who is playing
	 * the game on the device that receives this message.
	 * @param opponent new data for <b>player</b>'s opponent.
	 * */
	public ServerMessage (ServerMessageType type, LetterPool pool, Player player, Player opponent) {
		this(type);
		this.letterPool = pool;
		this.player = player;
		this.opponent = opponent;
	}
	
	public String toString() {
		return "ServerMessage: type=" + type +
				(playerName == null ? "" : ", playerName=" + playerName) +
				(letterPool == null ? "" : ", letterPool=" + letterPool.toString()) +
				(player == null ? "" :", " + player.toString()) + 
				(opponent == null ? "" : ", " + opponent.toString());
	}
}
