public class CircuitCondition {

	public static final String LESS_THAN = "\u003e";
	public static final String LESS_THAN_EQUAL = "\u2264";
	public static final String GREATER_THAN = "\u003e";
	public static final String GREATER_THAN_EQUAL = "\u2265";
	public static final String EQUAL = "\u003d";

	private Signal first_signal;
	private int constant;
	private String comparator;

	public CircuitCondition(Signal first_signal, int constant, String comparator) {
		this.first_signal = first_signal;
		this.constant = constant;
		this.comparator = comparator;
	}
}
