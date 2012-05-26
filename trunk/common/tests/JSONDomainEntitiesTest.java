package common.tests;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import common.domain.Letter;
import common.domain.LetterPool;
import common.domain.Word;
import common.messages.ServerMessage;
import common.messages.ServerMessageType;

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
		
		LOGGER.debug("Testing domain objects Letter and Word...");
		
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
		pool.add(b);
		ServerMessage msg = new ServerMessage(ServerMessageType.UPDATE, pool, word, word);
		LOGGER.debug("msg.toString() yields: " + msg.toString());
		
		String msgJson = gson.toJson(msg);
		LOGGER.debug("toJson(msg) yields: " + msgJson);
		
		ServerMessage newMsg = gson.fromJson(msgJson, ServerMessage.class);
		LOGGER.debug("fromJsom(toJson(msg)) yields: " + newMsg.toString());
	}
}

enum MessageType {
	TYPE1, TYPE2
}