package com.wordsbattle.common.domain;

import java.util.ArrayList;

/** Note: Don't use {@link ArrayList#remove(Object)} because letters are not compared by objects!, to remove letter from pool use {@link #pickLetter(Letter)}, 
 * because we need to holes in place of pickedLetter. Otherwise gap disappear 
 * */
public class LetterPool extends LetterArray {    
    public LetterPool() {
        // TODO Auto-generated constructor stub
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
