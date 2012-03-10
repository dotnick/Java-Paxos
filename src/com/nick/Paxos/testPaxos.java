package com.nick.Paxos;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import com.nick.Paxos.Messages.*;
public class testPaxos {
  	
		static DatagramSocket ds;
		static Command cmd;
		static int port = 1234;
		static int seqn;

	public static void main(String [] args) throws IOException {
		
		cmd = new Command(args[0], args[1], args[2]);
		seqn = Integer.parseInt(args[3]);
		ds = new DatagramSocket();
		//sendAcceptRequest();
		//sendAcceptNotification();
		sendHeartBeat();
		ds.close();
	}

		public static void sendAcceptRequest() throws IOException {
			AcceptRequestMessage pm = new AcceptRequestMessage(seqn,cmd);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput oo = new ObjectOutputStream(bos);
			oo.writeObject(pm);
			oo.close();
			byte[] buff = bos.toByteArray();
			DatagramPacket dp = new DatagramPacket(buff, buff.length , InetAddress.getByName("239.0.0.1"), port);
			ds.send(dp);
			
				
		}

		public static void sendHeartBeat() throws IOException {
			LeaderHeartbeatMessage hbm = new LeaderHeartbeatMessage();	
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput oo = new ObjectOutputStream(bos);
			oo.writeObject(hbm);
			oo.close();
			byte[] buff = bos.toByteArray();
			DatagramPacket dp = new DatagramPacket(buff, buff.length , InetAddress.getByName("239.0.0.1"), port);
			ds.send(dp);
		}

		public static void sendAcceptNotification() throws IOException{
			AcceptNotificationMessage anm = new AcceptNotificationMessage(seqn,cmd);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput oo = new ObjectOutputStream(bos);
			oo.writeObject(anm);
			oo.close();
			byte[] buff = bos.toByteArray();
			DatagramPacket dp = new DatagramPacket(buff, buff.length , InetAddress.getByName("239.0.0.1"), port);
			ds.send(dp);
				
		}		
		

	
}
