package common.messages;

public enum ServerMessageType {
	/** Response to REGISTER_PLAYER_NAME: your name was registered successfully. */
	NAME_REGISTERED,
	
	/**
	 * Response to REGISTER_PLAYER_NAME:
	 * your name was not registered for some reason.
	 * You will have to REGISTER_PLAYER_NAME again.
	 */
	NAME_CONFLICT,
	
	/** Response to REQUEST_GAME: the other player has accepted your request. */
	GAME_REQUEST_ACCEPTED,
	
	/** Response to REQUEST_GAME: the other player has denied your request. */
	GAME_REQUEST_DENIED,
	
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
