package com.nick.Paxos.Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Vector;

import com.nick.Paxos.Data;
import com.nick.Paxos.Messages.HeartbeatMessage;
import com.nick.Paxos.Messages.LeaderHeartbeatMessage;
import com.nick.Paxos.Messages.PaxosMessage;
import com.nick.Paxos.Network.SerializationUtil;



public class Node extends Thread {
	
	
	// Multicast group and port
	private MulticastSocket msocket;
	private static final String GROUP = "239.0.0.1";
	private static final int PORT = 1234;
	
	// Used by the leader to receive responses
	@SuppressWarnings("unused")
	private DatagramSocket leaderSocket;
	private final static int LEADER_PORT = 1235;
	
	// Heartbeat processing
	private HeartbeatSender heartbeatSender;
	private HeartbeatListener heartbeatListener;
	
	// List of online nodes and timeout timers
	public HashMap<InetAddress, Integer> nodeTimers;
	public Vector<InetAddress> nodes;
	
	private boolean running;
	private Data data = new Data();
	
	private int DEFAULT_TIMER = 6;
	
	private int LEADER_HB = 1;
	private int NORMAL_HB = 0;
	
	boolean isLeader = false;
	private static InetAddress leaderAddress = null;
	private InetAddress localAddress;
	
	public Node() {	
		
		try {
			 this.msocket = new MulticastSocket(PORT);
			 this.msocket.joinGroup(InetAddress.getByName(GROUP));
			 this.localAddress = InetAddress.getLocalHost();
		} catch (IOException e) {
			System.err.println("Error: Could not create multicast socket.");
			cleanExit();
		}
		
		this.nodes = new Vector<InetAddress>();
		this.nodeTimers = new HashMap<InetAddress, Integer>();
		this.running = true;
	}
	
	public void run() {
		
		DatagramPacket packet = null;
		Object obj = null;
		
		checkForLeader();
		
		this.heartbeatListener = new HeartbeatListener();
		this.heartbeatListener.start();
		
		while(running) {
			
			if(isLeader) {
				
			} else {
				packet = receiveMessage();
				obj = SerializationUtil.deSerialize(packet.getData());
		
				if(obj!= null){
					if(obj instanceof PaxosMessage) {
						data.process(obj);			
					} else if (obj instanceof HeartbeatMessage) {	
						if(!packet.getAddress().equals(localAddress) ) {
							resetNodeTimer(packet.getAddress());
						}			
					}
				} else {
					System.out.println("Error: Could not receive packet.");
				}		
			}
		}
	}
	
	private void checkForLeader() {
		int secondsRemaining = 5; // Listen for 5 seconds for a leader
		DatagramPacket pkt ;
		Object _obj;
		byte[] _buff = new byte[512];
		while(!isLeader && leaderAddress == null && secondsRemaining > 0) {
			pkt = new DatagramPacket(_buff, _buff.length);
			_obj = null;
			try {
				msocket.setSoTimeout(1000);
			} catch (SocketException e1) {
				e1.printStackTrace();
			}
			pkt = receiveMessage();
			
			if(pkt != null) {
				_obj = SerializationUtil.deSerialize(pkt.getData());	 
					
				if(_obj != null && _obj instanceof LeaderHeartbeatMessage) {
					leaderAddress = pkt.getAddress();
				}
			}	
			secondsRemaining--;
		}
		
		if(!isLeader && leaderAddress == null) { // Become Leader
					
			becomeLeader();
			
		} else {
			System.out.println("Leader already exists..");
			this.heartbeatSender = new HeartbeatSender(NORMAL_HB);
			this.heartbeatSender.start();	
		}
		
		try {
			msocket.setSoTimeout(0);
		} catch (SocketException e1) {
			System.err.println("Error setting socket timeout.");
			cleanExit();
		}
	}

	private void becomeLeader() {
		System.out.println("Becoming leader..");
		this.isLeader = true;
		this.heartbeatSender = new HeartbeatSender(LEADER_HB);
		this.heartbeatSender.start();
		
		try {
			this.leaderSocket = new DatagramSocket(LEADER_PORT);
		} catch (SocketException e) {
			System.err.println("Error while creating leader socket.");
			cleanExit();
		}
	}

	private void resetNodeTimer(InetAddress node) {
		
		if(!nodes.contains(node)) {
			nodes.add(node);
		}
		nodeTimers.put(node, DEFAULT_TIMER);
	}
	
	public static int sendMessage(Object obj) {
		
		byte[] buff;
		try {
			buff = SerializationUtil.serialize(obj);
			DatagramPacket sendingPacket = new DatagramPacket(buff,buff.length, InetAddress.getByName(GROUP),PORT);
			DatagramSocket ds = new DatagramSocket();
			ds.send(sendingPacket);
			return 0;
		} catch (IOException e) {
			return 1;
		}	
	}
	
	public DatagramPacket receiveMessage() { 
		
		byte[] buf = new byte[1024];
		DatagramPacket inputPacket = new DatagramPacket(buf,buf.length);
		try {
				msocket.receive(inputPacket);
			} catch (SocketTimeoutException ste) {
				return null;
			} catch (IOException e) {
				System.err.println("Could not receive packet.");		
			}	
		return inputPacket;
	}
	
	public static void respondToLeader(PaxosMessage message) {
		
		byte[] buff = SerializationUtil.serialize(message);
		DatagramPacket response = new DatagramPacket(buff, buff.length, leaderAddress , LEADER_PORT);
		DatagramSocket socketToLeader = null;
		
		try {
			socketToLeader = new DatagramSocket();
		} catch (SocketException e1) {
			System.err.println("Error while creating socket to leader.");
			return;
		}
		try {
			socketToLeader.send(response);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	
	private class HeartbeatListener extends Thread {
		
		boolean running;
		public HeartbeatListener() {
			this.running = true;
		}
		
		@Override
		public void run() {
			while(running) {
				for(InetAddress node : nodes) {
					// For debugging
					int currentTime = nodeTimers.get(node);
					System.out.println("Current time: " + currentTime);
					nodeTimers.put(node, --currentTime);
					
					if(nodeTimers.get(node) < 1) {
						if(node.equals(leaderAddress)) {
							// New leader election
						}
						System.out.println("Node " + node.toString() + " timed-out.");
						nodeTimers.remove(node);
						nodes.remove(node);
						// Notify the rest of the nodes?
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
    private class HeartbeatSender extends Thread {
		
    	private boolean running;
		private int type;
		
		public HeartbeatSender(int type) {
			this.running = true;
			this.type = type;
		}
		
		public void run() {
			while(running) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(type == LEADER_HB) {
					sendMessage(new LeaderHeartbeatMessage());
				} else {
					sendMessage(new HeartbeatMessage());
				}
			}
		}
	}
    
    public void cleanExit() {
    	this.heartbeatListener.interrupt();
    	this.heartbeatSender.interrupt();
    	this.interrupt();
    	System.exit(0);
    }
    
}
