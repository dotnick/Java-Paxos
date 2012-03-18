package com.nick.Paxos.Network.Leader;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import com.nick.Paxos.Command;
import com.nick.Paxos.Paxos;
import com.nick.Paxos.Network.SerializationUtil;

public class LeaderListener extends Thread { 
	
	private DatagramSocket listenSocket;
	private DatagramPacket packet;
	private byte[] buf = new byte[512];
	public boolean running = false;
	
	public LeaderListener(int port) {
		try {
			listenSocket = new DatagramSocket(port);
			running = true;
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(running) {
			packet = new DatagramPacket(buf,buf.length);
			try {
				listenSocket.receive(packet);
				Object obj = SerializationUtil.deSerialize(buf);
				if(obj instanceof Command) {
					Paxos.node.leaderProc.enqueueCommand((Command) obj);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}