public class DeciderConditions {
	private Signal first_signal;
	private int constant;
	private String comparator;
	private Signal output_signal;
	private boolean copy_count_from_input;

	public DeciderConditions(Signal first_signal, int constant, String comparator, Signal output_signal, boolean copy_count_from_input) {
		this.first_signal = first_signal;
		this.constant = constant;
		this.comparator = comparator;
		this.output_signal = output_signal;
		this.copy_count_from_input = copy_count_from_input;
	}
}
