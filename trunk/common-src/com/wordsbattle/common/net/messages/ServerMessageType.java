package com.wordsbattle.common.net.messages;

/** Message types used to communicate with client
 * @see ServerMessage
 *  */
public enum ServerMessageType {
	/** Response to REGISTER_PLAYER_NAME: your name was registered successfully. */
	NAME_REGISTERED,
	
	/**
	 * Response to REGISTER_PLAYER_NAME:
	 * your name was not registered because it 
	 * was already in use by some other user.
	 * You will have to REGISTER_PLAYER_NAME again.
	 */
	NAME_CONFLICT,
	
	/** User tries to register again, while he 
	 * is already registered during this connection */
	MULTIPLE_NAMES_FOR_ONE_USER_CONFLICT,
	
	/** Server asks another player if he wants to play with player who sent request */
	GAME_REQUEST,
	
	/** Response to REQUEST_GAME: the other player has accepted your request. */
	GAME_REQUEST_ACCEPTED,
	
	/** Response to REQUEST_GAME: the other player has denied your request. */
	GAME_REQUEST_DENIED,
	// TODO(danichbloom): NO_USER_WITH_REQUESTED_NAME need to be implemented in server, not just added here
	/** *Let the games begin!* */
	GAME_STARTED,
	
	/** One of the players paused the game, provided it had not been paused. */
	GAME_PAUSED,
	
	/** One of the players resumed the game, provided it had not been paused. */
	GAME_RESUMED,
	
	/** GAME OVER. Sorry, guys, but the princess is in another castle. */
	GAME_ENDED,
	
	/** Contains current state of the word pool and both players' words. */
	UPDATE
}
