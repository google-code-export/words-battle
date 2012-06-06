package com.wordsbattle.net;

import com.wordsbattle.common.domain.LetterPool;
import com.wordsbattle.common.domain.Player;

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
    void UserNameSuccessfullyRegistered();
    
    /** User tries to register again, while he 
     * is already registered during this connection */
    void UserTriesToReregister();
    
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
    /** After call of this method, game info presented and players disconnect from eachother. Lobby appear again.
     * user will need to send game request again.
     *  */
    void gameEnded();
    /** When this method is called client should represnt changes, according to new pool and Players data. */
    void update(LetterPool pool, Player player, Player opponent);
    //TODO(danichbloom): add all other methods for updating screen
}
