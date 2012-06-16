package com.wordsbattle.server.tests;

import static org.junit.Assert.assertEquals;

import org.apache.log4j.Logger;

import com.wordsbattle.common.domain.Letter;
import com.wordsbattle.common.domain.LetterPool;
import com.wordsbattle.common.domain.Player;
import com.wordsbattle.net.IWBClientDelegate;
import com.wordsbattle.net.WBClient;

/** Extend this class and override methods to provide custom behavior of fake player. <b>Don't forget to call super methods!</b>*/
public class WBResponder implements IWBClientDelegate{
    protected Logger LOGGER;
    protected LetterPool pool;
    protected String name;
    protected WBClient client;

    public WBResponder(String name) {
        this.name = name;
    }
    
    public void pickLetterWithId(int id) {
        for (Letter letter : pool) {
            if (letter == null) {
                continue;
            }
            if (letter.getId() == id) {
                client.pickLetter(letter);
            }
        }
        System.out.println("No such letter in pool!");
    }

    public WBClient getClient() {
        return client;
    }
    
    public void disconnect() {
        client.disconnect();
    }
    
    @Override
    public void UserNameAlreadyExists() {LOGGER.debug(name);}

    @Override
    public void UserNameSuccessfullyRegistered(String registeredName) {
        LOGGER.debug(name);
        assertEquals(name, registeredName);
    }

    @Override
    public void UserTriesToReregister() {LOGGER.debug(name);}

    @Override
    public boolean opponentRequestsGame(String opponentName) {
        LOGGER.debug(name);
        return false;
    }

    @Override
    public void opponentAcceptedGameRequest(String opponentName) {LOGGER.debug(name);}

    @Override
    public void opponentDeniedGameRequest(String opponentName) {LOGGER.debug(name);}

    @Override
    public void gameStarted() {LOGGER.debug(name);}

    @Override
    public void gamePaused() {LOGGER.debug(name);}

    @Override
    public void gameResumed() {LOGGER.debug(name);}

    @Override
    public void gameEnded() {LOGGER.debug(name);}

    @Override
    public void update(LetterPool aPool, Player aPlayer, Player anOpponent) {
        pool = aPool;
        LOGGER.debug(name + " " + aPool + " Player: " + aPlayer + " Opponent: " + anOpponent);
    }    
}