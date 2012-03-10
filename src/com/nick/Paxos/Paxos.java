package com.nick.Paxos;

import com.nick.Paxos.Network.*;


public class Paxos extends SerializationUtil {
	
	private static int procn;
	private static Node node;
	
	public static void main(String[] args) {
			
		if(args.length > 0){
			try{
				procn = Integer.parseInt(args[0]);

				System.out.println("Starting Paxos process with process number: " + procn);
				node = new Node();
				node.start();
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
		
}


