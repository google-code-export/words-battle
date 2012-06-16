package com.wordsbattle.server.tests;

import static com.wordsbattle.server.tests.ServerClientTestsLauncher.port;
import static com.wordsbattle.server.tests.ServerClientTestsLauncher.serverAdress;
import static com.wordsbattle.server.tests.ServerClientTestsLauncher.sleep;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.wordsbattle.net.WBClient;

public class AndriodVsFakePlayerTest implements IServerClientTest {
    private final static Logger LOGGER = Logger.getLogger(AndriodVsFakePlayerTest.class);
    private final String opponentName = "hunternif-opp";
    private WBClient opponentClient; 
    private OpponentResponder opponentResponder; 

    public AndriodVsFakePlayerTest() {   
    }
    
    @Override
    public void tearUp() {
        opponentResponder = new OpponentResponder(opponentName);
        opponentClient = opponentResponder.getClient();
        if (!opponentClient.ConnectToServer())  {
            LOGGER.error(opponentName + " couldn't connect to server");
            fail("opponent couldn't connect");
        }                
    }
    
    /** after registration you can manually pick letters from pull by entering letter id in console */
    @Override
    public void run() {
        long shortSleepTime = 50;
        
        opponentClient.sendRegitsteringRequestForPlayerName(opponentName); // register successfully
        sleep(shortSleepTime);
        
        // Letting tester to pick letters manually
        String str = "";
        while (!str.equals("q")){
            System.out.print("[q for quit] To pick a letter enter it's ID:");
            BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
            try {
                str = inFromUser.readLine();
                int letterId = Integer.parseInt(str);
                opponentResponder.pickLetterWithId(letterId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                
            }
        }
    }
    
    @Override
    public void tearDown() {
        opponentClient.disconnect();
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

