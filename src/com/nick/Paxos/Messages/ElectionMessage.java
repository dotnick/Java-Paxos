package com.nick.Paxos.Messages;

import java.io.Serializable;

public class ElectionMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private int procn;
	
	public ElectionMessage(int procn) {
		this.procn = procn;
	}
	
	public int getProcn() {
		return this.procn;
	}
}
