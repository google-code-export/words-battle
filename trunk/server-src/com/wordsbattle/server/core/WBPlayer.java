package com.wordsbattle.server.core;

import org.apache.log4j.Logger;
import org.junit.Assert;

import com.wordsbattle.common.domain.Letter;
import com.wordsbattle.common.domain.Player;
import com.wordsbattle.common.domain.Word;
import com.wordsbattle.common.net.messages.ServerMessage;
import com.wordsbattle.common.net.messages.ServerMessageType;

/** This class represents player on server side. During all communication
 * process environment call logic methods on it */
public class WBPlayer {
    private final static Logger LOGGER = Logger.getLogger(WBPlayer.class);
    private ClientHandler playerHandler;
    private WBGame game;
    private String name;
    private int score;
    private Word word;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void setGame(WBGame aGame) {
        Assert.assertNull(game);
        this.game = aGame;
    }
    
    /** @return player that would be sent by message */
    public Player getPlayerForMessage() {
        return new Player(score, word);
    }
    
    WBPlayer(ClientHandler aPlyaerHandler) {
        this.playerHandler = aPlyaerHandler;
        this.score = 0;
        this.word = new Word();
    }
    
    public void opponentRequestsGame(String opponentName) {
        sendMessageToThisPlayer(new ServerMessage(ServerMessageType.GAME_REQUEST, opponentName));
    }
    
    public void opponentReactedOnMyGameRequest(String opponentName, boolean accepted) {
        if (accepted) {            
            sendMessageToThisPlayer(new ServerMessage(ServerMessageType.GAME_REQUEST_ACCEPTED, opponentName));
        } else {
            sendMessageToThisPlayer(new ServerMessage(ServerMessageType.GAME_REQUEST_DENIED, opponentName));
        }
    }
    
    public void thisPlayerLostConnection() {
        LOGGER.info("Player " + name + " lost connection");
        if (game != null) {
            game.playerLostConnection(this);
            LeaveTheGame();
        }
    }    
    
    public void opponentLostConnection(WBPlayer opponent) {
        // TODO(danichbloom): send message to realplayer, that opponent lost connection
    }

    public void LeaveTheGame() {
        game.playerLeavesTheGame(this);
        resetData();
    }
    
    public boolean pickLetter(Letter letter) {
        Letter poolLetter;
        if (null != (poolLetter = game.playerPickLetter(this, letter))) {
            word.add(poolLetter);
            game.update();
            return true;
        } else {
            return false;
        }
    }
    
    public void sendMessageToThisPlayer(ServerMessage msg) {
        playerHandler.sendMessage(msg);
    }
        
    private void resetData() {
        LOGGER.info("Reset called for player with name: " + name);        
        score = 0;
        word = new Word();
        game = null;
    }
}