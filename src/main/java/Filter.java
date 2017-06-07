public class Filter {
	private Signal signal;
	private int count;
	private int index;

	public Filter(Signal signal, int count, int index) {
		this.signal = signal;
		this.count = count;
		this.index = index;
	}
}
