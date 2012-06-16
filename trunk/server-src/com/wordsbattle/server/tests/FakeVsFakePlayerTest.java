package com.wordsbattle.server.tests;

//import static com.wordsbattle.server.tests.ServerClientTestsLauncher.sleep;
//import static com.wordsbattle.server.tests.ServerClientTestsLauncher.serverAdress;
//import static com.wordsbattle.server.tests.ServerClientTestsLauncher.port;
import static com.wordsbattle.server.tests.ServerClientTestsLauncher.port;
import static com.wordsbattle.server.tests.ServerClientTestsLauncher.serverAdress;
import static com.wordsbattle.server.tests.ServerClientTestsLauncher.sleep;
import static org.junit.Assert.fail;

import org.apache.log4j.Logger;

import com.wordsbattle.net.WBClient;

public class FakeVsFakePlayerTest implements IServerClientTest {
    private final static Logger LOGGER = Logger.getLogger(FakeVsFakePlayerTest.class);    
    private final String playerName = "acbelter-player"; 
    private final String opponentName = "hunternif-opp";
    private WBClient playerClient;
    private PlayerResponder playerResponder;
    private WBClient opponentClient; 
    private OpponentResponder opponentResponder; 
    
    public FakeVsFakePlayerTest() {   
    }

    @Override
    public void tearUp() {
        playerResponder = new PlayerResponder(playerName);
        playerClient = playerResponder.getClient();
        if (!playerClient.ConnectToServer()) {
            LOGGER.error(playerName + " couldn't connect to server");
            fail("player couldn't connect");
        }
        
        opponentResponder = new OpponentResponder(opponentName);
        opponentClient = opponentResponder.getClient();
        if (!opponentClient.ConnectToServer())  {
            LOGGER.error(opponentName + " couldn't connect to server");
            fail("opponent couldn't connect");
        }                
    }
    
    @Override
    public void run() {
        long shortSleepTime = 50;
        playerClient.sendRegitsteringRequestForPlayerName(playerName); // register successfully
        sleep(shortSleepTime);
        playerClient.sendRegitsteringRequestForPlayerName(opponentName); // Re-register fault
        sleep(shortSleepTime);
        
        opponentClient.sendRegitsteringRequestForPlayerName(playerName); // Name_conflict
        sleep(shortSleepTime);
        opponentClient.sendRegitsteringRequestForPlayerName(opponentName); // register successfully
        
        sleep(1000);
        
        playerClient.sendGameRequest(opponentName);

        sleep(1000);
    }
    
    @Override
    public void tearDown() {
        playerClient.disconnect();
        opponentClient.disconnect();
        
    }
    
    // ============ PLAYER ============
    class PlayerResponder extends WBResponder {

        public PlayerResponder(String name) {
            super(name);
            this.LOGGER = Logger.getLogger(PlayerResponder.class);
            client = new WBClient(serverAdress, port, this);
        }
        
        @Override
        public boolean opponentRequestsGame(String opponentName) {
            boolean accept = false;
            LOGGER.debug(name + " " + accept);
            return accept;
        }

        @Override
        public void gameStarted() {
            super.gameStarted();
            client.pickLetter(pool.get(0));
        }    
    }
    
    // ============ OPPONENT ============
    class OpponentResponder extends WBResponder {
        public OpponentResponder(String name) {
            super(name);
            this.LOGGER = Logger.getLogger(OpponentResponder.class);
            client = new WBClient(serverAdress, port, this);
        }


        @Override
        public boolean opponentRequestsGame(String opponentName) {
            boolean accept = true;
            LOGGER.debug(name + " " + accept);
            return accept;
        }
    }
}

