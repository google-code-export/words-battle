package com.wordsbattle.common.tests;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.wordsbattle.common.domain.Letter;
import com.wordsbattle.common.domain.LetterPool;
import com.wordsbattle.common.domain.Player;
import com.wordsbattle.common.domain.Word;
import com.wordsbattle.common.net.messages.ServerMessage;
import com.wordsbattle.common.net.messages.ServerMessageType;

public class JSONDomainEntitiesTest {
	private final static Logger LOGGER = Logger.getLogger(JSONDomainEntitiesTest.class);
	
	public static void main(String[] args) {
		// Log4j config
		BasicConfigurator.configure();

		//TODO: make proper JUnit tests
		Letter a = new Letter('a');
		Letter b = new Letter('b');
		Word word = new Word();
		word.add(a);
		word.add(b);
		
		Gson gson = new Gson();
		String aJson = gson.toJson(a);
		String wordJson = gson.toJson(word);
		
		LOGGER.debug("Testing domain objects Letter, Word and Player...");
		
		LOGGER.debug("toJson(a) yields: " + aJson);
		LOGGER.debug("toJson(word) yields: " + wordJson);
		
		Letter newA = gson.fromJson(aJson, Letter.class);
		LOGGER.debug("Letter test: newA.toString() yields: " + newA.toString());
		Word newWord = gson.fromJson(wordJson, Word.class);
		LOGGER.debug("Word test: newWord.toString() yields: " + newWord.toString());
		
		LOGGER.debug("Testing enum: " + gson.toJson(MessageType.TYPE1));
		
		LOGGER.debug("Testing ServerMessage...");
		LetterPool pool = new LetterPool();
		pool.add(a);
		pool.add(null);
		pool.add(b);
		Player player = new Player(0, word);
		
		ServerMessage msg = new ServerMessage(ServerMessageType.UPDATE, pool, player, player);
		LOGGER.debug("msg.toString() yields\t\t" + msg.toString());
		
		String msgJson = gson.toJson(msg);
		LOGGER.debug("toJson(msg) yields: " + msgJson);
		
		ServerMessage newMsg = gson.fromJson(msgJson, ServerMessage.class);
		LOGGER.debug("fromJsom(toJson(msg)) yields:\t" + newMsg.toString());
		
		if (!msg.toString().equals(newMsg.toString())) {
            LOGGER.error("json broke message!");
        }
		
		try {
			Letter let = gson.fromJson("klasjdflkjsdlfj", Letter.class);			
			LOGGER.info(let.toString());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("cont exception");
		}
	}
}

enum MessageType {
	TYPE1, TYPE2
}