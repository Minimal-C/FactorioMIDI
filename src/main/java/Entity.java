public class Entity {

	public static final String ITEM = "item";

	public static final String CONSTANT_COMBINATOR = "constant-combinator";
	public static final String DECIDER_COMBINATOR = "decider-combinator";
	public static final String PROGRAMMABLE_SPEAKER = "programmable-speaker";

	public static final byte DIRECTION_NORTH = 1;
	public static final byte DIRECTION_EAST = 2;
	public static final byte DIRECTION_WEST = 3;
	public static final byte DIRECTION_SOUTH = 4;


	private int entity_number;
	private String name;
	private Position position;
	private byte direction;
	private ControlBehaviour control_behavior;
	private Connection connections;
	private SpeakerParameters parameters;
	private AlertParameters alertParams;

	public Entity(int entityNumber, String name, Position position, byte direction, ControlBehaviour controlBehaviour,
				  Connection connection, SpeakerParameters speakerParams,
				  AlertParameters alertParams) {
		this.entity_number = entityNumber;
		this.name = name;
		this.position = position;
		this.direction = direction;
		this.control_behavior = controlBehaviour;
		this.connections = connection;
		this.parameters = speakerParams;
		this.alertParams = alertParams;
	}

	public Entity(int entityNumber, String name, Position position, byte direction, ControlBehaviour controlBehaviour, Connection connection) {
		this.entity_number = entityNumber;
		this.name = name;
		this.position = position;
		this.direction = direction;
		this.control_behavior = controlBehaviour;
		this.connections = connection;
	}

}


