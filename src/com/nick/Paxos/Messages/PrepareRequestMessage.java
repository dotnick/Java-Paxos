package com.nick.Paxos.Messages;


public class PrepareRequestMessage extends PaxosMessage {

	private static final long serialVersionUID = 4291835295987067981L;
	
	@SuppressWarnings("unused")
	private final String message = "prepare";

	public PrepareRequestMessage(int seqn){
		this.setSeqNo(seqn);
	}


}
