package com.wordsbattle.common.domain;

public class Letter {
	/** Must only be accessed from within getNewId() */
	private static int lastId = 0;
	
	private char letter;
	private int id;
	
	public Letter(char value) {
		this.letter = value;
		this.id = getNewId();
	}
	
	public char getValue() {
		return letter;
	}
	
	public int getId() {
		return id;
	}
	
	public int getWeight() {
		// Vegeta, what does the scouter say about his power level?
		return LetterWeight.getWeight(letter);
		// What, 9000?!
	}
	
	/**
	 * Generates new id: should be called in constructor.
	 */
	protected static int getNewId() {
		return lastId++;
	}
	
	public String toString() {
		return "letter='" + letter + "', id=" + id;
	}
}
