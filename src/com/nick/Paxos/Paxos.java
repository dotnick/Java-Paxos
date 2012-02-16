package com.nick.Paxos;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import com.nick.Paxos.Messages.PaxosMessage;


public class Paxos extends SerializationUtil {
	
	private static int procn;
	
	public static void main(String[] args) {
			
		if(args.length > 0){
			try{
				procn = Integer.parseInt(args[0]);
				System.out.println("Starting Paxos process with process number: " + procn);
				running();
			} catch(NumberFormatException e) {
				System.err.println("Error: Process number must be an integer.");
				System.exit(0);
			}
		} else {
			System.err.println("Usage: java Paxos [process number]");
		}
		
	}
	
	public static int getProcn() {
		return procn;
	}

	private static void running() {
		MulticastSocket msocket = null;
		try {
			 msocket = new MulticastSocket(1234);
			 msocket.joinGroup(InetAddress.getByName("239.0.0.1"));
		} catch (IOException e) {
			System.err.println("Could not create multicast socket.");
			System.exit(1);
		}
		while(true) {
			byte[] buf = new byte[512];
			DatagramPacket inputPacket = new DatagramPacket(buf,buf.length);
			
			try {
					msocket.receive(inputPacket);
				} catch (IOException e) {
				System.err.println("Could not receive packet.");		
			}
			
			Object obj = null;
			try {
				obj = deSerialize(inputPacket.getData());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(obj instanceof PaxosMessage) {
				Data.process(obj);
			}
		
		}
	}
			
}


