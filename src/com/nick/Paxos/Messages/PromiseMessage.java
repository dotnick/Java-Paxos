package com.nick.Paxos.Messages;


public class PromiseMessage extends PaxosMessage {

	private static final long serialVersionUID = 1L;

	public PromiseMessage(int seqn) {
		setSeqNo(seqn);
	}

}
