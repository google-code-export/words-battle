package com.wordsbattle.common;

public class GameSettings {
	private int poolSize;
	
	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	// TODO(danichbloom): temporary constructor. 
	// Remove when player will be able to send settings
	public GameSettings() {
		this.poolSize = 15;
	}
}
