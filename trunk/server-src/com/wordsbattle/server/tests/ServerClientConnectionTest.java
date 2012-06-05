package com.wordsbattle.server.tests;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.wordsbattle.common.domain.Letter;
import com.wordsbattle.common.domain.LetterPool;
import com.wordsbattle.common.domain.Player;
import com.wordsbattle.net.IWBClientDelegate;
import com.wordsbattle.net.WBClient;
import com.wordsbattle.server.core.MultiThreadedServer;

class WBResponder implements IWBClientDelegate{
	protected Logger LOGGER;
	protected LetterPool pool;
	protected String name;
	protected WBClient client;
	
	public WBResponder(String name) {
		this.name = name;
	}
	
	public void setClient(WBClient client) {
		this.client = client;
	}
	
	@Override
	public void UserNameAlreadyExists() {LOGGER.debug(name);}

	@Override
	public void UserNameSuccessfullyRegistered() {LOGGER.debug(name);}
	
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

class PlayerResponder extends WBResponder {
	
	public PlayerResponder(String name) {
		super(name);
		this.LOGGER = Logger.getLogger(PlayerResponder.class);
	}

	@Override
	public boolean opponentRequestsGame(String opponentName) {
		boolean accept = false;
		LOGGER.debug(name + accept);
		return accept;
	}

	@Override
	public void gameStarted() {
		LOGGER.debug(name);
		client.pickLetter(pool.get(0));
	}	
}

class OpponentResponder extends WBResponder {
	public OpponentResponder(String name) {
		super(name);
		this.LOGGER = Logger.getLogger(OpponentResponder.class);
	}
	
	@Override
	public void gameStarted() {
		LOGGER.debug(name);
		client.pickLetter(pool.get(0));
	}	
	

	@Override
	public boolean opponentRequestsGame(String name) {
		super.opponentRequestsGame(name);
		boolean accept = true;
		LOGGER.debug(accept);
		return accept;
	}  //true: accepts game request
}

public class ServerClientConnectionTest {
	private String playerName = "playerName"; 
	private String opponentName = "opponentName";
	
	private final static Logger LOGGER = Logger.getLogger(ServerClientConnectionTest.class);
	
	public ServerClientConnectionTest() {
		MultiThreadedServer server = new MultiThreadedServer(6789);
		new Thread(server).start();
		
		PlayerResponder playerResponder = new PlayerResponder(playerName);
		WBClient playerClient = new WBClient("localhost", 6789, playerResponder);
		playerResponder.setClient(playerClient);
		
		if (playerClient.ConnectToServer()) {
			playerClient.sendRegitsteringRequestForPlayerName(playerName); // register successfully
			playerClient.sendRegitsteringRequestForPlayerName(opponentName); // Re-register fault
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		} else {
			LOGGER.error(playerName + " couldn't connect to server");
		}		
		
		OpponentResponder opponentResponder = new OpponentResponder(opponentName);
		WBClient opponentClient = new WBClient("localhost", 6789, opponentResponder);
		opponentResponder.setClient(opponentClient);
		if (opponentClient.ConnectToServer()) {
			opponentClient.sendRegitsteringRequestForPlayerName(playerName); // Name_conflict
			opponentClient.sendRegitsteringRequestForPlayerName(opponentName); // register successfully
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		} else {
			LOGGER.error(opponentName + " couldn't connect to server");
			return;  // FIXME(danichbloom): how do i do it right way?
		}		
		playerClient.sendGameRequest(opponentName);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}			
		playerClient.stop();
		opponentClient.stop();
		server.stop();
	}
	
	public static void main(String[] args) {
		Logger.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout("%r [%t] %p %c %x {%M} - %m%n")));
		new ServerClientConnectionTest();
	}
}