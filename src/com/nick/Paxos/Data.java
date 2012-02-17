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
	
	private static HashMap<Integer,Command> data = new  HashMap<Integer,Command>(); //<Sequence Number, Command>
	private static Vector<Integer> SeqNumbersProcessed = new Vector<Integer>(); // Used for Map indexing
	
	public static AcceptRequestMessage accepted; // Wait for Accept notification before committing
	private static Integer minSeqn;
	
	private final static int OK = 1;
	private final static int OLD_ROUND = -1;
	
	
	public static void process(Object message) {
		int result = 0;
		if (message instanceof PrepareRequestMessage) {
			PrepareRequestMessage PRM = (PrepareRequestMessage) message;
			minSeqn = PRM.getSeqNo();
			result = Data.processPrepareRequest(PRM);
			if (result == OK) {
				// Send promise
			} 
	
		} else if (message instanceof AcceptRequestMessage) {
			AcceptRequestMessage arm = (AcceptRequestMessage) message;
			result = Data.processAcceptRequest(arm);
			if (result == OK) {
				// Notify the leader that we accepted the message
			} 

		} else if (message instanceof AcceptNotificationMessage) {
			AcceptNotificationMessage ANM = (AcceptNotificationMessage) message;
			result = Data.commit(ANM);
			if (result == OK) {
				// Notify the leader/learners
			} 
		}
		
		if (result == OLD_ROUND) {
			// Send OldRound
		}
	}
	
	public static int processAcceptRequest(AcceptRequestMessage ARM) {
		if(ARM.getSeqNo() > getMinSeqn()) {
			accepted = ARM;
			return 1;
		} else {
			return -1;
		}
	}
	
	public static int processPrepareRequest(PrepareRequestMessage PRM) {
		if(PRM.getSeqNo() > getMinSeqn()) {
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
		return cmd.getOperation().equals("READ");
	}
	
	private static int write(int seqn, Command cmd) {
		data.put(seqn, cmd);
		return 1;
	}
	
	public static int getLargestSeqNumber() {
		if (SeqNumbersProcessed.size() > 0 ) {
			return SeqNumbersProcessed.lastElement();
		} else {
			return 0;
		}
		
	}
	
	public static int commit(AcceptNotificationMessage ANM) {
		if(isCommandAccepted(ANM.getCommand())) {
			SeqNumbersProcessed.add(ANM.getSeqNo());
			accepted = null;
			return write(ANM.getSeqNo(), ANM.getCommand());
		} else { // Should never happen
			return -1;
		}
		
	}
	
	public static int getMinSeqn() {
		if(minSeqn != null ) {
			return minSeqn;
		} else {
			return 0;
		}
	}
	
	// Command.equals returned false even when commands were the same
	public static boolean isCommandAccepted(Command cmd) {
		if(accepted != null) {
		return (accepted.getCommand().getOperation().equals(cmd.getOperation())
		 	 && accepted.getCommand().getVariable().equals(cmd.getVariable())
			 && accepted.getCommand().getValue().equals(cmd.getValue()));
	} else {
		return false;
		}
	}

}
