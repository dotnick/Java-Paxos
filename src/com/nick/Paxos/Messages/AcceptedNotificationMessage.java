package com.nick.Paxos.Messages;

import com.nick.Paxos.Command;

public class AcceptedNotificationMessage extends PaxosMessage {

	private static final long serialVersionUID = 1L;
	
	private Command cmd;
	
	public AcceptedNotificationMessage(int seqn, Command cmd) {
		setSeqNo(seqn);
		this.cmd = cmd;
	}
	
	public Command getCommand() {
		return this.cmd;
	}
}
