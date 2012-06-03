package com.wordsbattle.server.tests;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.wordsbattle.common.domain.LetterPool;
import com.wordsbattle.net.IWBClientDelegate;
import com.wordsbattle.net.WBClient;
import com.wordsbattle.server.core.MultiThreadedServer;

public class ServerClientConnectionTest {
	private final static Logger LOGGER = Logger.getLogger(ServerClientConnectionTest.class);
	
	public ServerClientConnectionTest() {
		BasicConfigurator.configure();
		
		String firstName = "first_client"; 
		String secondName = "second_client"; 
		
		MultiThreadedServer server = new MultiThreadedServer(6789);
		new Thread(server).start();
		WBClient firstClient = new WBClient("localhost", 6789,new IWBClientDelegate() {

			@Override
			public void UserNameAlreadyExists() {}

			@Override
			public void UserTriesToReregister() {}

			@Override
			public void UserNameSuccessfullyRegistered() {}

			@Override
			public boolean opponentRequestsGame(String opponentName) {return false;}

			@Override
			public void opponentAcceptedGameRequest(String opponentName) {}

			@Override
			public void opponentDeniedGameRequest(String opponentName) {}

			@Override
			public void gameStarted() {}

			@Override
			public void gamePaused() {}

			@Override
			public void gameResumed() {}

			@Override
			public void gameEnded() {}

			@Override
			public void newPool(LetterPool pool) {}
			
			});
		if (firstClient.ConnectToServer()) {
			firstClient.sendRegitsteringRequestForPlayerName(firstName); // register successfully
			firstClient.sendRegitsteringRequestForPlayerName(secondName); // Reregister fault
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		} else {
			LOGGER.error("Client1 couldn't connect to server");
		}		
		
		
		WBClient secondClient = new WBClient("localhost", 6789, new IWBClientDelegate() {
			
			@Override
			public boolean opponentRequestsGame(String opponentName) {return true;}  //true: accepts game request
			
			@Override
			public void UserTriesToReregister() {}
			
			@Override
			public void UserNameSuccessfullyRegistered() {}
			
			@Override
			public void UserNameAlreadyExists() {}

			@Override
			public void opponentAcceptedGameRequest(String opponentName) {}

			@Override
			public void opponentDeniedGameRequest(String opponentName) {}

			@Override
			public void gameStarted() {}

			@Override
			public void gamePaused() {}

			@Override
			public void gameResumed() {}

			@Override
			public void gameEnded() {}

			@Override
			public void newPool(LetterPool pool) {}
		});
		if (secondClient.ConnectToServer()) {
			secondClient.sendRegitsteringRequestForPlayerName(firstName); // Name_conflict
			secondClient.sendRegitsteringRequestForPlayerName(secondName); // register successfully
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		} else {
			LOGGER.error("Client2 couldn't connect to server");
		}		
		firstClient.sendGameRequest(secondName);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}			
		firstClient.stop();
		secondClient.stop();
		server.stop();
	}
	
	public static void main(String[] args) {
		new ServerClientConnectionTest();
	}
}