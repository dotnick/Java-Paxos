package com.nick.Paxos.Messages;

import com.nick.Paxos.Command;


public class AcceptRequestMessage extends PaxosMessage {
	
	private static final long serialVersionUID = 5141719313977492338L;
	private Command cmd;
	
	public AcceptRequestMessage(int seqn, Command cmd) {
		this.setSeqNo(seqn);
		this.cmd = cmd;
	}
	
	public Command getCommand() {
		return this.cmd;
	}
	
}
