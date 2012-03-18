package com.nick.Paxos.Messages;

public class NewLeaderMessage extends ElectionMessage {

	private static final long serialVersionUID = 1L;

	public NewLeaderMessage(int procn) {
		super(procn);
	}

}
