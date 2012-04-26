package com.nick.Paxos.Messages;

import java.io.Serializable;

public class CatchUpRequestMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	int latestRound;
	
	public CatchUpRequestMessage(int latestRound) {
		this.latestRound = latestRound;
	}
	
	public int getLatestRound() {
		return this.latestRound;
	}

}
