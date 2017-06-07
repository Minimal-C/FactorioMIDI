public class Connection {
	private Wires wires1;
	private Wires wires2;

	public Connection(Wires wires) {
		this.wires1 = wires;
	}

	public Connection(Wires wires1, Wires wires2) {
		this.wires1 = wires1;
		this.wires2 = wires2;
	}
}
