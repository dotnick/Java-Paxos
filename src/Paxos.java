import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Paxos extends SerializationUtil {
	
	private static int procn;
	
	public static void main(String[] args){
			
		if(args.length > 0){
			try{
				procn = Integer.parseInt(args[0]);
				System.out.println("Starting Paxos process with process number: " + procn);
				running();
			}catch(NumberFormatException e){
				System.err.println("Error: Process number must be an integer.");
				System.exit(0);
			}
		}
		else{
			System.err.println("Usage: java Paxos [process number]");
		}
		
	}
	
	public static int getProcn(){
		return procn;
	}

	private static void running(){
		MulticastSocket m = null;
		try {
			 m = new MulticastSocket(1234);
			 m.joinGroup(InetAddress.getByName("239.0.0.1"));
		} catch (IOException e) {
			System.err.println("Could not create multicast socket.");
			System.exit(1);
		}
		while(true){
			byte[] buf = new byte[256];
			DatagramPacket inputPacket = new DatagramPacket(buf,buf.length);
			
			try {
				m.receive(inputPacket);
				Object obj = deSerialize(inputPacket.getData());
					
				if(obj instanceof PrepareMessage){
					PrepareMessage pm = (PrepareMessage) obj;
					System.out.println("Received prepare message from Paxos instance " + pm.getProcNo() 
							+ " with sequence number " + pm.getSeqNo());
				}
				else if(obj instanceof AcceptRequestMessage){
					AcceptRequestMessage arm = (AcceptRequestMessage) obj;
					System.out.println("Received accept request message from Paxos instance " + arm.getProcNo() 
							+ " with sequence number " + arm.getSeqNo());
				}

			} catch (IOException e) {
				System.err.println("Could not receive packet.");		
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
			
}


