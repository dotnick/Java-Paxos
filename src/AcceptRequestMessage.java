public class AcceptRequestMessage extends Message {
	
	private static final long serialVersionUID = 5141719313977492338L;
	
	@SuppressWarnings("unused")
	private final String message = "accept";

	public AcceptRequestMessage(int seqn){
		this.setSeqNo(seqn);
	}

}
