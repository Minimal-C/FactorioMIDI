public class Signal {

	public static final String SIG_TYPE_VIRTUAL = "virtual";
	public static final String SIG_0 = "signal-0";
	public static final String SIG_1 = "signal-1";

	private String type;
	private String name;

	public Signal(String type, String name) {
		this.type = type;
		this.name = name;
	}
}
