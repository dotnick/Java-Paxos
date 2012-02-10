public class AcceptNotificationMessage extends Message {

	
	private static final long serialVersionUID = 3418462538001206424L;

	public AcceptNotificationMessage(int seqn) {
		this.setSeqNo(seqn);
	}
}
