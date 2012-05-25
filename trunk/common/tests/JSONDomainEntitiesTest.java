package common.tests;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import common.domain.Letter;
import common.domain.Word;

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
		
		LOGGER.debug("toJson(a) yields: " + aJson);
		LOGGER.debug("toJson(word) yields: " + wordJson);
		
		Letter newA = gson.fromJson(aJson, Letter.class);
		LOGGER.debug("Letter test: newA.toString() yields: " + newA.toString());
		Word newWord = gson.fromJson(wordJson, Word.class);
		LOGGER.debug("Word test: newWord.toString() yields: " + newWord.toString());
	}
}
