
public class Command {

	private String operation;
	private String variable;
	private String value;
	
	public Command(String operation, String variable, String value) {
		this.operation = operation;
		this.variable = variable;
		this.value = value;
	}
	
	public String getOperation() {
		return this.operation;
	}
	
	public String getVariable() {
		return this.variable;
	}
	
	public String getValue() {
		return this.value;
	}
}
