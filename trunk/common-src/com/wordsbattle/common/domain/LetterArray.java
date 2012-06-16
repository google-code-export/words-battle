package com.wordsbattle.common.domain;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/** ArrayList that is designed to work with letters. The empty place is null. */
public class LetterArray extends ArrayList<Letter> {
    /**
     * This constant must be set because we are extending a Serializable class.
     * It is generated correctly, just don't change it.
     */ 
    private static final long serialVersionUID = 1538883423897363413L;
    private final static Logger LOGGER = Logger.getLogger(LetterArray.class);    
    /**This method search the letter in array by comparing letters' Ids and pop it.
     * @param letter a letter to find in array and pop it.
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
}

