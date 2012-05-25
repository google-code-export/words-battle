package common.domain;

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
