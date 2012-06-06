package com.wordsbattle.common.domain;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/** Note: Don't use {@link ArrayList#remove(Object)} because letters are not compared by objects!, to remove letter from pool use {@link #pickLetter(Letter)}, 
 * because we need to holes in place of pickedLetter. Otherwise gap disappear 
 * */
public class LetterPool extends ArrayList<Letter> {
	private final static Logger LOGGER = Logger.getLogger(LetterPool.class);	
	
	/**This method search the letter in pool by comparing letters' Ids and pop it.
	 * @param letter a letter to find in pool and pop it.
	 * @return  
	 * If it finds letter,
	 *  it puts null instead of it and returns found letter.
	 * if not
	 *  returns null.
	 *  */
	public Letter pickLetter(Letter letter) {
		for (int i = 0; i < this.size(); i++) {
			Letter poolLetter = this.get(i);
			if (poolLetter == null) continue;
			if (poolLetter.getId() == letter.getId()) {
				if (poolLetter.getValue() != poolLetter.getValue()) {
					LOGGER.error(letter + " has same ID as " + poolLetter + "but different value");
				}
				this.set(i, null);
				return poolLetter;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		return ("pool: " + super.toString());
	}
	
	/**
	 * This constant must be set because we are extending a Serializable class.
	 * It is generated correctly, just don't change it.
	 */
	private static final long serialVersionUID = 9198498918349306070L;

}
