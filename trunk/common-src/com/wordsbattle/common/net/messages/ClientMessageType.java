package com.wordsbattle.common.net.messages;

public enum ClientMessageType {
	/**
	 * Your register yourself on the server.
	 * Your first message when connecting to the server.
	 * */
	REGISTER_PLAYER_NAME,
	
	/** You request a game with a player known by name. */
	REQUEST_GAME,
	
	/** Accept game request from user with name */
	ACCEPT_GAME_REQUEST,
	
	/** Deny game request from user with name */
	DENY_GAME_REQUEST,
	///** Request a game with a random player */
	//REQUEST_RANDOM_GAME,
	
	/** You add a letter to your word. */
	PICK_LETTER,
	
	/** You update letters in your word, e.g. if you had replaced or removed some. */
	UPDATE_WORD,
	
	/**
	 * You submit your word to the server, when you think it is completed.
	 * The server has to check it up in the dictionary, then do an update.
	 */
	SUBMIT_WORD,
	
	/** Pause the game. Pretty much self-descriptive. */
	PAUSE_GAME,
	
	/** Resume the game. Pretty much self-descriptive, lol. */
	RESUME_GAME,
	
	// Hurray for the witty comments in code!
}
