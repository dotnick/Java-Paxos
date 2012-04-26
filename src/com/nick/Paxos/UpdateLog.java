package com.nick.Paxos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

public class UpdateLog implements Serializable {

	private static final long serialVersionUID = 1L;
	private Vector<Integer> seqNumbers;
	private HashMap<Integer,Command> commands;
	
	public UpdateLog(Vector<Integer> seqNumbers, HashMap<Integer,Command> commands){
		this.seqNumbers = seqNumbers;
		this.commands = commands;
	}
	
	public Vector<Integer> getSeqNumbers(){
		return this.seqNumbers;
	}
	
	public HashMap<Integer,Command> getCommands(){
		return this.commands;
	}
}
