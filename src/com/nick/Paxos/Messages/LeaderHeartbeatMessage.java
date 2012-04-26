package com.nick.Paxos.Messages;

public class LeaderHeartbeatMessage extends HeartbeatMessage {
	
	private static final long serialVersionUID = 1L;
	private int latestRound;
	
	public LeaderHeartbeatMessage(int latestRound) {
		this.latestRound = latestRound;
	}
	
	public int getLatestRound() {
		return this.latestRound;
	}

}
