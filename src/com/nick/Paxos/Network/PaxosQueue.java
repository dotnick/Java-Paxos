package com.nick.Paxos.Network;

import java.util.Vector;

import com.nick.Paxos.Command;

public class PaxosQueue {
	
	private Vector<Command> commandQueue;
	
	public PaxosQueue() {
		this.commandQueue = new Vector<Command>();
	}
	
	public boolean isEmpty() {
		return commandQueue.isEmpty();
	}
	
	public void enqueue(Command cmd) {
		commandQueue.add(cmd);
	}
	
	public Command dequeue() {
		Command cmd = commandQueue.get(0);
		commandQueue.remove(0);
		return cmd;
	}
	
	public Command peek() {
		return commandQueue.get(0);
	}
	
	public void clear() {
		commandQueue.removeAllElements();
	}
	
}
