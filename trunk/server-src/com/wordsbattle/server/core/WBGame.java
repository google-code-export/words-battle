package com.wordsbattle.server.core;


import com.wordsbattle.common.GameSettings;
import com.wordsbattle.common.domain.Letter;
import com.wordsbattle.common.domain.LetterPool;
import com.wordsbattle.common.domain.Player;
import com.wordsbattle.common.net.messages.ServerMessage;
import com.wordsbattle.common.net.messages.ServerMessageType;
import com.wordsbattle.util.LetterGenerator;



public class WBGame {
    private WBPlayer players[];
    private GameSettings settings;
    private LetterPool pool;
    
    WBGame(WBPlayer player1, WBPlayer player2, GameSettings settings) {
        player1.setGame(this);
        player2.setGame(this);
        this.players = new WBPlayer[2];
        this.players[0] = player1;
        this.players[1] = player2;
        this.settings = settings;
        
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
    
    private void sendMessageToBoth(ServerMessage message) {
        for (WBPlayer player: players) {
            player.sendMessageToThisPlayer(message);
        }
    }
    
    private WBPlayer getOpponentForPlayer(WBPlayer player) {
        if (this.players[0] == player) {
            return this.players[1];
        } else
            return this.players[0];
    }
}
