package com.wordsbattle.net;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wordsbattle.common.domain.Letter;
import com.wordsbattle.common.net.WBConnection;
import com.wordsbattle.common.net.messages.ClientMessage;
import com.wordsbattle.common.net.messages.ClientMessageType;
import com.wordsbattle.common.net.messages.ServerMessage;
import com.wordsbattle.common.net.messages.ServerMessageType;
/** be careful with the name. If user wants to change 
 * name you need to stop currently 
 * running WBClient and create a new one. */
public class WBClient implements Runnable{
    private String serverAddress = null;
    private int port = 0;
    private WBConnection connection = null;
    private WBClient blinker = null;
    //private String name = null;
    //private boolean nameIsRegistered = false;
    //private boolean ServerAnsweredRegRquest = true;
    private IWBClientDelegate delegate = null;
        
    public WBClient(String aServerAddress, int aPort, IWBClientDelegate aDelegate) {
        this.serverAddress = aServerAddress;
        this.port = aPort;
        this.blinker = this;
        this.delegate = aDelegate;
    }

    @Override
    public void run() {
        while(this.blinker == this) {
            if (this.connection.ready()) {
                String str = this.connection.read();
                Log.i("blabla", "received: " + str);
                Gson gson = new Gson();
                try {
                    ServerMessage message = gson.fromJson(str, ServerMessage.class); 
                    HandleMessage(message);
                } catch (JsonSyntaxException e) {
                    // TODO Auto-generated catch block
                    Log.i("debug", "Message is badly formed:" + str);
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void stop() {
        this.connection.closeConnection();
        this.blinker = null;
    }
    
    /** Tries to connect to server
     *  @return true in case of success and false otherwise. 
     *  */
    public boolean ConnectToServer() {
        Socket connectionSocket = null;
        try {
            connectionSocket = new Socket(this.serverAddress, this.port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (connectionSocket != null) {
            final WBConnection con = new WBConnection(connectionSocket);
            this.connection = con;
            new Thread(this).start();
            return true;
        } else return false; 
    }
        
    public void sendMessage(ClientMessage message) {
        Gson gson = new Gson();
        Log.i("blabla", "sent: " + message);
        this.connection.println(gson.toJson(message));
    }    
    /** Send to server registration request for userName. */
    public void sendRegitsteringRequestForPlayerName(String userName) {
        sendMessage(new ClientMessage(ClientMessageType.REGISTER_PLAYER_NAME, userName));
    }
    
    public void sendGameRequest(String opponentName) {
        sendMessage(new ClientMessage(ClientMessageType.REQUEST_GAME, opponentName));
    }
    
    /** Call this method when user taps at letter */
    public void pickLetter(Letter letter) {
        sendMessage(new ClientMessage(ClientMessageType.PICK_LETTER, letter));
    }
    
    private void HandleMessage(ServerMessage msg) {
        switch (msg.getType()) {
        case NAME_CONFLICT:
            Log.i("blabla", "Name conflic!");
            this.delegate.UserNameAlreadyExists();
            break;
        case NAME_REGISTERED:            
            Log.i("blabla", "Name " + msg.getPlayerName() + " registered!");
            //this.nameIsRegistered = true;
            delegate.UserNameSuccessfullyRegistered();
            break;
        case MULTIPLE_NAMES_FOR_ONE_USER_CONFLICT:
            Log.i("blabla", "User tries to reregister with name [" + msg.getPlayerName() + "], he is already registered this connection!");
            delegate.UserTriesToReregister();
            break;
        case GAME_REQUEST:{
            String opponentName = msg.getPlayerName();
            Log.i("blabla", "User recieves game request from user " + opponentName);
            if (delegate.opponentRequestsGame(opponentName)) {
                sendMessage(new ClientMessage(ClientMessageType.ACCEPT_GAME_REQUEST, opponentName));
            } else {
                sendMessage(new ClientMessage(ClientMessageType.DENY_GAME_REQUEST, opponentName));
            }
            break;
        }
        case GAME_REQUEST_ACCEPTED: {
            String opponentName = msg.getPlayerName();
            Log.i("blabla", "Opponent "+ opponentName + " accepted game request ");
            delegate.opponentAcceptedGameRequest(opponentName);
            break;
        }
        case GAME_REQUEST_DENIED: {
            String opponentName = msg.getPlayerName();
            Log.i("blabla", "Opponent "+ opponentName + " declined game request ");            
            delegate.opponentDeniedGameRequest(opponentName);
            break;
        }
        case UPDATE: {
            Log.i("blabla", "recieved update: " + msg.toString());            
            delegate.update(msg.getLetterPool(), msg.getPlayer(), msg.getOpponent());
            break;
        }
        case GAME_STARTED: {
            Log.i("blabla", "Game started");            
            delegate.gameStarted();
            break;
        }
        default:
            Log.i("blabla", "Unrecognized message type" + msg.getType());
            break;
        }
    }
}