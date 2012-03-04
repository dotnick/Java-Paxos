package com.nick.Paxos.Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Vector;

import com.nick.Paxos.Data;
import com.nick.Paxos.Messages.HeartbeatMessage;
import com.nick.Paxos.Messages.PaxosMessage;
import com.nick.Paxos.Network.SerializationUtil;


public class Node extends Thread {
	
	
	// Multicast group and port
	private MulticastSocket msocket;
	private static final String GROUP = "239.0.0.1";
	private static final int PORT = 1234;
	
	// Heartbeat processing
	private HeartbeatSender heartbeatSender;
	private HeartbeatListener heartbeatListener;
	
	// List of online nodes and timeout timers
	public HashMap<InetAddress, Integer> nodeTimers;
	public Vector<InetAddress> nodes;
	
	public InetAddress leader;
	private boolean running;
	private Data data = new Data();
	
	private int DEFAULT_TIMER = 6;
	
	public Node() {	
		
		try {
			 this.msocket = new MulticastSocket(PORT);
			 this.msocket.joinGroup(InetAddress.getByName(GROUP));
		} catch (IOException e) {
			System.err.println("Error: Could not create multicast socket.");
			System.exit(1);
		}
		
		this.nodes = new Vector<InetAddress>();
		this.nodeTimers = new HashMap<InetAddress, Integer>();
		this.heartbeatSender = new HeartbeatSender();
		this.heartbeatSender.start();	
		this.heartbeatListener = new HeartbeatListener();
		this.heartbeatListener.start();
		this.running = true;
	}
	
	public void run() {
		
		DatagramPacket packet;
		Object obj = null;
		
		while(running) {
			packet = receivePacket();

			try {
				obj = SerializationUtil.deSerialize(packet.getData());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			if(obj!= null){
				if(obj instanceof PaxosMessage) {
					data.process(obj);			
				} else if (obj instanceof HeartbeatMessage) {	
					resetNodeTimer(packet.getAddress());
				}
			} else {
				System.out.println("Error: Could not receive packet.");
			}		
		}
	}
	
	private void resetNodeTimer(InetAddress node) {
		
		if(!nodes.contains(node)) {
			nodes.add(node);
		}
			nodeTimers.put(node, DEFAULT_TIMER);
	}
	
	public int sendPacket(Object obj) {
		
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
	
	public DatagramPacket receivePacket() { 
		
		byte[] buf = new byte[1024];
		DatagramPacket inputPacket = new DatagramPacket(buf,buf.length);
		try {
				msocket.receive(inputPacket);
			} catch (IOException e) {
				System.err.println("Could not receive packet.");		
			}	
		return inputPacket;
	}
	
	public static void respontToLeader(PaxosMessage message) throws SocketException {
		
		//byte[] buff = SerializationUtil.serialize(message);
		//DatagramPacket response = new DatagramPacket(buff, buff.length, leader , port)
		//DatagramSocket dsToLeader = new DatagramSocket();
		//ds.send(response);
				
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
					int currentTime = nodeTimers.get(node);
					System.out.println("Current time: " + currentTime);
					nodeTimers.put(node, --currentTime);
					
					if(nodeTimers.get(node) < 1) {
						if(node.equals(leader)) {
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
		
		public HeartbeatSender() {
			this.running = true;
		}
		public void run() {
			while(running) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				sendPacket(new HeartbeatMessage());
			}
		}
	}
}