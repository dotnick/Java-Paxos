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
	
	public static void main(String [] args) throws IOException{
		DatagramSocket ds = new DatagramSocket();
		Command cmd = new Command("write", "x" , "10");
		for(int i=0;i<Integer.parseInt(args[0]);i++){
			AcceptRequestMessage pm = new AcceptRequestMessage(i,cmd);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput oo = new ObjectOutputStream(bos);
			oo.writeObject(pm);
			oo.close();
			byte[] buff = bos.toByteArray();
			DatagramPacket dp = new DatagramPacket(buff, buff.length , InetAddress.getByName("239.0.0.1"), 1234);
		
			ds.send(dp);
		}
		ds.close();
	}

}
