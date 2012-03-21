package com.nick.Paxos;

import java.io.Serializable;
import java.net.InetAddress;

public class Command implements Serializable {
	
	/*
	 * Since this is a demonstration on *how* Paxos reaches 
	 * consensus, we don't focus too much on the actual commands. 
	 * For now, we send and receive commands in the form of:
	 * <operation> (write,read), <variable>, <value>
	 * Examples: "write x 10" or "read y"
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -143924259460910234L;
	private String operation;
	private String variable;
	private String value;
	private InetAddress from;
	
	public Command(String operation, String variable, String value, InetAddress from) {
		this.operation = operation;
		this.variable = variable;
		this.value = value;
		this.from = from;
	}
	
	public InetAddress getFromAddress() {
		return this.from;
	}
	
	public String getOperation() {
		return this.operation;
	}
	
	public String getVariable() {
		return this.variable;
	}
	
	public String getValue() {
		return this.value;
	}
}
