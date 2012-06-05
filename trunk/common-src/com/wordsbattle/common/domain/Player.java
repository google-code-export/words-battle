package com.wordsbattle.common.domain;

/** This class is used to send messages from server to client */
public class Player {
	private int score;
	private Word word;
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Word getWord() {
		return word;
	}
	public void setWord(Word word) {
		this.word = word;
	}

	public Player(int aScore, Word aWord) {
		this.score = aScore;
		this.word = aWord;
	}
	
	@Override
	public String toString() {
		return "player: " + 
				(word == null ? "" : ", word=" + word) + 
				(", score=" + score);
	}
}
