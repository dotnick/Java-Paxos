package com.nick.Paxos.Messages;

public class AcceptReplyMessage extends PaxosMessage {

	private static final long serialVersionUID = 1L;
	
	public AcceptReplyMessage(int seqn) {
		setSeqNo(seqn);
	}
}
