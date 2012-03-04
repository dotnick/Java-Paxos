package com.nick.Paxos;
import java.util.HashMap;
import java.util.Vector;

import com.nick.Paxos.Messages.AcceptNotificationMessage;
import com.nick.Paxos.Messages.AcceptRequestMessage;
import com.nick.Paxos.Messages.PrepareRequestMessage;


public class Data {
	
	/*
	 * The Paxos log is stored using an int Vector to store sequence 
	 * numbers and a <int,Command> HashMap to create
	 * <Sequence number, command> key,value pairs.
	 * This will be used to replay the log in the case of a 
	 * failure and (possibly) help new Paxos nodes catch up. 
	 */
	public  HashMap<String,String> data;
	public  HashMap<Integer,Command> commandsProcessed; //<Sequence Number, Command>
	public  Vector<Integer> SeqNumbersProcessed;// Used for Map indexing
	
	public static AcceptRequestMessage accepted; // Wait for Accept notification before committing
	private static Integer minSeqn;
	
	private final static int OK = 1;
	private final static int OLD_ROUND = -1;

	
	public Data() {
		
		this.data = new HashMap<String,String>();
		this.commandsProcessed = new  HashMap<Integer,Command>(); 
		this.SeqNumbersProcessed = new Vector<Integer>();
	}
	
	
	public void process(Object message) {
		int result = 0;
		if (message instanceof PrepareRequestMessage) {
			PrepareRequestMessage PRM = (PrepareRequestMessage) message;
			minSeqn = PRM.getSeqNo();
			result = processPrepareRequest(PRM);
			if (result == OK) {
				// Send promise
			} 
	
		} else if (message instanceof AcceptRequestMessage) {
			AcceptRequestMessage arm = (AcceptRequestMessage) message;
			result = processAcceptRequest(arm);
			if (result == OK) {
				// Notify the leader that we accepted the message
			} 

		} else if (message instanceof AcceptNotificationMessage) {
			AcceptNotificationMessage ANM = (AcceptNotificationMessage) message;
			result = commit(ANM);
			if (result == OK) {
				// Notify the leader/learners
			} 
		}
		
		if (result == OLD_ROUND) {
			// Send OldRound
		}
	}
	
	public int processAcceptRequest(AcceptRequestMessage ARM) {
		if(ARM.getSeqNo() > this.getMinSeqn()) {
			accepted = ARM;
			return 1;
		} else {
			return -1;
		}
	}
	
	public int processPrepareRequest(PrepareRequestMessage PRM) {
		if(PRM.getSeqNo() > this.getMinSeqn()) {
			return 1;
		} else {
			return -1;
		}
	}
	
	/* 
	 * Will be used by the leader in the future 
	 * to reply to read requests without
	 * going through a Paxos round.
	 */
	@SuppressWarnings("unused")
	private static  boolean isReadOperation(Command cmd) {
		return cmd.getOperation().equalsIgnoreCase("READ");
	}
	
	private int write(int seqn, Command cmd) {
		commandsProcessed.put(seqn, cmd);
		data.put(cmd.getVariable(), cmd.getValue());
		return 1;
	}
	
	public int getLargestSeqNumber() {
		if (SeqNumbersProcessed.size() > 0 ) {
			return SeqNumbersProcessed.lastElement();
		} else {
			return 0;
		}
		
	}
	
	public int commit(AcceptNotificationMessage ANM) {
		if(isCommandAccepted(ANM.getCommand())) {
			SeqNumbersProcessed.add(ANM.getSeqNo());
			accepted = null;
			return write(ANM.getSeqNo(), ANM.getCommand());
		} else { // Should never happen
			return -1;
		}
	}
	
	public int getMinSeqn() {
		if(minSeqn != null ) {
			return minSeqn;
		} else {
			return 0;
		}
	}
	
	// Command.equals returned false even when commands were the same
	public boolean isCommandAccepted(Command cmd) {
		if(accepted != null) {
		return (accepted.getCommand().getOperation().equals(cmd.getOperation())
		 	 && accepted.getCommand().getVariable().equals(cmd.getVariable())
			 && accepted.getCommand().getValue().equals(cmd.getValue()));
	} else {
		return false;
		}
	}
	
}
