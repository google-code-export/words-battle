package com.wordsbattle.server.tests;

import static org.junit.Assume.assumeTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.wordsbattle.server.core.MultiThreadedServer;


/** This modes are fore switching between differnet testing cases. 
 * OpponentResponder always accepts game request. 
 * Depending on the mode, player is presented by fake class playerResponder
 * or real client (emulator or device)*/
enum testingMode {
    /** Two fake players:[PlayerResponder and OpponentResponder] from this file connect to server and emulate game*/
    FakeVsFake, 

    /** One fake player:[OpponentResponder] help testing real client (emulator or device). */
    AndriodVsFake}

/** Depending on your needs set the <b>mode</b> field 
 * @see testingMode*/
public class ServerClientTestsLauncher {
    private final static testingMode mode = testingMode.AndriodVsFake;
    static public String serverAdress = "localhost";
    static public int port = 6789;
    private MultiThreadedServer server;
    
    
    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().removeAllAppenders();
        Logger.getRootLogger().addAppender(new ConsoleAppender(new PatternLayout("%r [%t] %p %c %x {%M} - %m%n")));
        
        server = new MultiThreadedServer(6789);
        new Thread(server).start();
    }
    
    @After
    public void tearDown() throws Exception {
        sleep(1000);
        server.stop();
    }
    
    @Test
    public void FakeVsFakePlayer() {
        assumeTrue(testingMode.FakeVsFake == mode);
        if (testingMode.FakeVsFake != mode) return;
        FakeVsFakePlayerTest test = new FakeVsFakePlayerTest();
        
        test.tearUp();
        test.run();        
        test.tearDown();
    }
    
    /** this test doesn't stop until you hit enter in the end. 
     * Note that you have to quit picking-letter loop with 'q' before.
     * @See {@link AndriodVsFakePlayerTest#run()}*/    
    @Test
    public void RealAndriodVsFakePlayer() {
        assumeTrue(testingMode.AndriodVsFake == mode);
        if (testingMode.AndriodVsFake != mode) return;
        
        AndriodVsFakePlayerTest test = new AndriodVsFakePlayerTest();

        test.tearUp();
        test.run();
        System.out.println("To finish test press enter in console");
        BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
        try {
            inFromUser.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        test.tearDown();
    }
    
    static public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }                
    }

}


