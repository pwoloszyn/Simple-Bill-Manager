import java.io.Serializable;

public class StringPair implements Serializable {

	private static final long serialVersionUID = 1379690373683203342L;
	
	// Class variables
	public String a;
	public String b;
	
	/**
	 * The constructor
	 * @param a
	 * @param b
	 */
	public StringPair(String a, String b) {
		this.a = a;
		this.b = b;
	}
	
}
