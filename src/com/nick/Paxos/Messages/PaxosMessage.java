package com.nick.Paxos.Messages;


import java.io.Serializable;

import com.nick.Paxos.Paxos;

public abstract class PaxosMessage implements Serializable {
	

	private static final long serialVersionUID = 4667835754232191478L;
	
	private int seqn;
	private final int procn = Paxos.getProcn();

	
	public int getSeqNo() {
		return seqn;
	}
	
	public int getProcNo() {
		return procn;
	}
	
	protected void setSeqNo(int seqn) {
	  this.seqn = seqn;
	}
	
	
}
