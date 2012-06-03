package com.wordsbattle.server.core;

public class WBGame {
	private WBPlayer player1;
	private WBPlayer player2;
	
	WBGame(WBPlayer pl1, WBPlayer pl2) {
		this.player1 = pl1;
		this.player1.setGame(this);
		this.player2 = pl2;
		this.player2.setGame(this);
	}
}
