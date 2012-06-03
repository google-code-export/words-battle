package com.wordsbattle.common.net.messages;

import com.wordsbattle.common.domain.LetterPool;
import com.wordsbattle.common.domain.Word;

public class ServerMessage {
	private ServerMessageType type;
	
	// Message payload ------------------------------------
	private String playerName;
	private LetterPool letterPool;
	private Word word1;
	private Word word2;
	// ----------------------------------------------------
	
	public String getPlayerName() {
		return playerName;
	}
	public LetterPool getLetterPool() {
		return letterPool;
	}
	public Word getWord1() {
		return word1;
	}
	public Word getWord2() {
		return word2;
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
	
	/** Constructor for type: UPDATE */
	public ServerMessage (ServerMessageType type, LetterPool pool, Word word1, Word word2) {
		this(type);
		this.letterPool = pool;
		this.word1 = word1;
		this.word2 = word2;
	}
	
	public String toString() {
		return "ClientMessage: type=" + type +
				(playerName == null ? "" : ", playerName=" + playerName) +
				(letterPool == null ? "" : ", letterPool=" + letterPool.toString()) +
				(word1 == null ? "" : ", word1=" + word1.toString()) + 
				(word2 == null ? "" : ", word2=" + word2.toString());
	}
}
