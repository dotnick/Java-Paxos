
public class PrepareMessage extends Message {

	private static final long serialVersionUID = 4291835295987067981L;
	
	@SuppressWarnings("unused")
	private final String message = "prepare";

	public PrepareMessage(int seqn){
		this.setSeqNo(seqn);
	}


}
