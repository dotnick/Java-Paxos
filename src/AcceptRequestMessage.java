public class AcceptRequestMessage extends Message {
	
	private static final long serialVersionUID = 5141719313977492338L;
	private Command cmd;
	
	public AcceptRequestMessage(int seqn, Command cmd) {
		this.setSeqNo(seqn);
		this.cmd = cmd;
	}
	
	public Command getCommand() {
		return this.cmd;
	}
	
}
