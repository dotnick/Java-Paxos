package com.nick.Paxos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.nick.Paxos.Network.Node;
import com.nick.Paxos.Network.SerializationUtil;


public class Paxos extends SerializationUtil {
	
	private static int procn;
	public static Node node;
	
	public static void main(String[] args) {
			
		if(args.length > 0){
			try{
				procn = Integer.parseInt(args[0]);

				System.out.println("Starting Paxos process with process number: " + procn);
				node = new Node();
				node.start();
				
				String inputLine = null;
				InputStreamReader ISR = new InputStreamReader(System.in); 
				BufferedReader BR = new BufferedReader(ISR);
				
				while(true){ 
					System.out.print("> ");
					try {
						inputLine = BR.readLine();
					} catch (IOException e) {
						e.printStackTrace(); 
					}
					String[] input = inputLine.split("\\s+");
					
					if(input[0].equals("quit")) { 
						cleanExit();
					} else if(input[0].equals("print")) { 
						
						System.out.println(node.data.data.get(input[1]));
						
					} else {
						System.out.println("Unknown command.");
					}
				}
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
	
	public static void cleanExit() {
		try{
    	node.heartbeatListener.running = false;
    	node.heartbeatSender.running = false;
    	node.leaderListener.running = false;
    	node.leaderProc.running = false;
		} catch(NullPointerException npe){}
		System.out.println("Bye..");
		node.running = false;
    	System.exit(0);
    }
		
}


