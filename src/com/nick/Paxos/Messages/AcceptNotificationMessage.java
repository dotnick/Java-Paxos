package com.nick.Paxos.Messages;

import com.nick.Paxos.Command;


public class AcceptNotificationMessage extends PaxosMessage {

	
	private static final long serialVersionUID = 3418462538001206424L;
	private Command cmd;

	public AcceptNotificationMessage(int seqn, Command cmd) {
		this.setSeqNo(seqn);
		this.cmd = cmd;
	}
	
	public Command getCommand() { 
		return this.cmd;
	}
}
