package com.wordsbattle;

import com.wordsbattle.common.domain.LetterPool;
import com.wordsbattle.common.domain.Player;
import com.wordsbattle.net.IWBClientDelegate;

public class GameController implements IWBClientDelegate {
    private WordsBattleActivity gameActivity;
    
    public GameController(WordsBattleActivity activity) {
        gameActivity = activity;
    }

    @Override
    public void UserNameAlreadyExists() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void UserNameSuccessfullyRegistered() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void UserTriesToReregister() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean opponentRequestsGame(String opponentName) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void opponentAcceptedGameRequest(String opponentName) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void opponentDeniedGameRequest(String opponentName) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void gameStarted() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void gamePaused() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void gameResumed() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void gameEnded() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(LetterPool pool, Player player, Player opponent) {
        // TODO Auto-generated method stub
        
    }

}
