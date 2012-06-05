package com.wordsbattle.net;

import com.wordsbattle.common.domain.LetterPool;

/** This interface should be implemented by some controlling class.
 * In that class you would do all necessary actions in response to game events.
 * e.g.  opponentRequestsGame is called. You promt user "do you want to play with this opponent?"
 * and return true or false etc.
 * */
public interface IWBClientDelegate {
	/** This method is called when server returned message about username conflict.
	 * use sendRegitsteringRequestForPlayerName method for reregistering the username.
	 * @see com.wordsbattle.WBClient#sendRegitsteringRequestForPlayerName(String)
	 *  */
	void UserNameAlreadyExists();
	
	/** User tries to register again, while he 
	 * is already registered during this connection */
	void UserTriesToReregister();
	void UserNameSuccessfullyRegistered();
	
	/** Called when someone wants to play with the user 
	 * @param opponentName name of that someone
	 * @return true if user accepts, false when user denies
	 * */
	boolean opponentRequestsGame(String opponentName);
	
	/** This method will be called when opponent will react on game request from user */
	void opponentAcceptedGameRequest(String opponentName);
	/** This method will be called when opponent will react on game request from user */
	void opponentDeniedGameRequest(String opponentName);
	
	void gameStarted();
	void gamePaused();
	void gameResumed();
	void gameEnded();
	void newPool(LetterPool pool);
	//TODO(danichbloom): add all other methods for updating screen
}
