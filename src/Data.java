import java.util.HashMap;
import java.util.Vector;


public class Data {
	
	private static HashMap<String,String> data = new  HashMap<String,String>();
	private static Vector<Integer> SeqNumbersProcessed = new Vector<Integer>();
	private static AcceptRequestMessage promised; // Wait for Accept notification before commiting
	
	public static int process(AcceptRequestMessage ARM) {
		System.out.println("Should send promise");
		promised = ARM;
		return 0;
	}
	
	@SuppressWarnings("unused")
	private static  boolean isReadOperation(Command cmd) {
		return cmd.getOperation().equals("READ");
	}
	
	private static int write(Command cmd) {
		data.put(cmd.getVariable(), cmd.getValue());
		return 0;
	}
	
	public static int getLargestSeqNumber() {
		if (SeqNumbersProcessed.size() > 0 ) {
			return SeqNumbersProcessed.lastElement();
		} else {
			return 0;
		}
		
	}
	
	public static int commit(AcceptNotificationMessage ANM) {
		if(promised.getSeqNo() == ANM.getSeqNo()) {
			SeqNumbersProcessed.add(promised.getSeqNo());
			return write(promised.getCommand());
		} else { // Should never happen
			return -1;
		}
		
	}

}
