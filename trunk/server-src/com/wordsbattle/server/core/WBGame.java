package com.wordsbattle.server.core;


import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.wordsbattle.common.GameSettings;
import com.wordsbattle.common.domain.Letter;
import com.wordsbattle.common.domain.LetterPool;
import com.wordsbattle.common.domain.Player;
import com.wordsbattle.common.net.messages.ServerMessage;
import com.wordsbattle.common.net.messages.ServerMessageType;
import com.wordsbattle.util.LetterGenerator;

public class WBGame {
    private final static Logger LOGGER = Logger.getLogger(WBGame.class);    
    //private WBPlayer players[];
    private ArrayList<WBPlayer> players;
    private GameSettings settings;
    private LetterPool pool;
    private MultiThreadedServer server;
    
    public ArrayList<WBPlayer> getPlayers() {
        return players;
    }
    
    WBGame(WBPlayer player1, WBPlayer player2, GameSettings settings, MultiThreadedServer server) {
        player1.setGame(this);
        player2.setGame(this);
        this.players = new ArrayList<WBPlayer>(2);
        this.players.add(player1);
        this.players.add(player2);
        this.settings = settings;
        this.server = server;
        
        this.pool  = generateLetterPool();
        update();
        startGame();
    }
    
    public void startGame() {
        sendMessageToBoth(new ServerMessage(ServerMessageType.GAME_STARTED));
    }
    
    public Letter playerPickLetter(WBPlayer player, Letter letter) {
        Letter poolLetter;
        if ( null != (poolLetter = pool.pickLetter(letter)) ) {
            return poolLetter;
        } else {
            return null;
        }
    }
    
    private LetterPool generateLetterPool() {
        LetterPool newPool = new LetterPool();
        int poolSize = this.settings.getPoolSize();
        for (int i = 0; i < poolSize; i++) {
            newPool.add(new Letter(LetterGenerator.generateLetter()));
        }
        return newPool;
    }
    
    public void update() {
        for (WBPlayer player: players) {
            Player msgPlayer = player.getPlayerForMessage();
            Player msgOpponent = getOpponentForPlayer(player).getPlayerForMessage();
            // TODO(danichbloom): is it fair to send messages like this? 
            // Does always players[0] receives message before players[1]?   
            ServerMessage message = new ServerMessage(ServerMessageType.UPDATE, this.pool, msgPlayer, msgOpponent);
            player.sendMessageToThisPlayer(message);
        }
    }
    
    public void playerLostConnection(WBPlayer player) {
        WBPlayer opponent = getOpponentForPlayer(player);
        if (opponent != null) {
            opponent.opponentLostConnection(player);
        }
    }
    
    public void playerLeavesTheGame(WBPlayer player) {
        players.remove(player);
        if (players.size() == 0) endGame();
    }
    
    private void endGame() {
        server.gameEnded(this);
        server = null;
        players = null;
        pool = null;
        settings = null;
        LOGGER.info("End of game");        
    }
    
    private void sendMessageToBoth(ServerMessage message) {
        for (WBPlayer player: players) {
            player.sendMessageToThisPlayer(message);
        }
    }
    
    private WBPlayer getOpponentForPlayer(WBPlayer player) {
        if (players.size() != 2) {
            LOGGER.warn("Less than to players in game!");
            return null;
        }
        int playerIndex = players.indexOf(player);
        if (playerIndex == -1) {
            LOGGER.warn("Player from another game!!!");
            return null;
        }
        int index = 1 - playerIndex;
        return players.get(index);
    }
}
