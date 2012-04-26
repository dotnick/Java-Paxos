package com.nick.Paxos;
import java.util.HashMap;
import java.util.Vector;

import com.nick.Paxos.Messages.AcceptNotificationMessage;
import com.nick.Paxos.Messages.AcceptReplyMessage;
import com.nick.Paxos.Messages.AcceptRequestMessage;
import com.nick.Paxos.Messages.AcceptedNotificationMessage;
import com.nick.Paxos.Messages.OldRoundMessage;
import com.nick.Paxos.Messages.PrepareRequestMessage;
import com.nick.Paxos.Messages.PromiseMessage;
import com.nick.Paxos.Network.*;

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
	public  Vector<Integer> SeqNumbersProcessed; // Used for Map indexing
	
	//TODO: Fix list of nodes for quorum
	
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
			System.out.println("Received new prepare message.");
			PrepareRequestMessage PRM = (PrepareRequestMessage) message;
			result = processPrepareRequest(PRM);
			if (result == OK) {
				System.out.println("Request has a valid sequence number. Sending promise...");
				minSeqn = PRM.getSeqNo();
				// Send Promise
				Node.respondToLeader(new PromiseMessage(PRM.getSeqNo()));
			} 
	
		} else if (message instanceof AcceptRequestMessage) {
			System.out.println("Received new accept request.");
			AcceptRequestMessage arm = (AcceptRequestMessage) message;
			result = processAcceptRequest(arm);
			if (result == OK) {
				System.out.println("Accepted the command. Sending accept reply..");
				// Notify the leader that we accept the request
				Node.respondToLeader(new AcceptReplyMessage(arm.getSeqNo()));
			} 

		} else if (message instanceof AcceptNotificationMessage) {
			
			AcceptNotificationMessage ANM = (AcceptNotificationMessage) message;
			result = commit(ANM); 
			if (result == OK) {
				// Notify that we commited the proposal 
				System.out.println("Received an accepted notification. Committing the command.");
				Node.sendMessageToGroup(new AcceptedNotificationMessage(ANM.getSeqNo(), ANM.getCommand()));
			} 
		}
		
		if (result == OLD_ROUND) {
			Node.respondToLeader(new OldRoundMessage());
		}
	}
	
	public int processAcceptRequest(AcceptRequestMessage ARM) {
		if(ARM.getSeqNo() >= this.getMinSeqn()) {
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
	
	public int addFromSync(int seqNumber, Command cmd){
		int result = write(seqNumber,cmd);
		if(result == OK) {
			this.minSeqn = seqNumber;
		}
		return result;
	}
	
}
