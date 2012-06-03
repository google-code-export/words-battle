package com.wordsbattle.common.net.messages;

import com.wordsbattle.common.domain.Letter;
import com.wordsbattle.common.domain.Word;

public class ClientMessage {
	private ClientMessageType type;
	
	// Message payload ------------------------------------
	private String playerName;
	private Letter letter; //instead of letterId
	private Word word;
	// ----------------------------------------------------
	
	public String getPlayerName() {
		return playerName;
	}
	public Letter getLetter() {
		return letter;
	}
	public Word getWord() {
		return word;
	}
	
	public ClientMessageType getType() {
		return type;
	}
	
	/** Constructor for types: PAUSE_GAME, RESUME_GAME */
	public ClientMessage (ClientMessageType type) {
		this.type = type;
	}
	
	/** Constructor for types: REGISTER_PLAYER_NAME, REQUEST_GAME,
	 *  ACCEPT_GAME_REQUEST, DENY_GAME_REQUEST */
	public ClientMessage (ClientMessageType type, String name) {
		this(type);
		this.playerName = name;
	}
	
	/** Constructor for type: PICK_LETTER */
	public ClientMessage (ClientMessageType type, Letter letter) {
		this(type);
		this.letter = letter;
	}
	
	/** Constructor for types: UPDATE_WORD, SUBMIT_WORD */
	public ClientMessage (ClientMessageType type, Word word) {
		this(type);
		this.word = word;
	}
	
	public String toString() {
		return "ClientMessage: type=" + type +
				(playerName == null ? "" : ", playerName=" + playerName) +
				(letter == null ? "" : ", letter=" + letter.toString()) +
				(word == null ? "" : ", word=" + word.toString());
	}
}
