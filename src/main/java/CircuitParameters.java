import java.util.ArrayList;
import java.util.Arrays;

public class CircuitParameters {
	public static final String[] INSTRUMENTS = {"alarms","drumkit","piano"};
	public static final ArrayList<String> PIANO_NOTES = new ArrayList<>(Arrays.asList("F3","F#3","G3","G#3","A3","A#3","B3","C4","C#4","D4","D#4","E4","F4","F#4",
			"G4","G#4","A4","A#4","B4","C5","C#5","D5","D#5","E5","F5","F#5","G5","G#5","A5","A#5","B5","C6","C#6","D6","D#6",
			"E6","F6","F#6","G6","G#6","A6","A#6","B6","C7","C#7","D7","D#7","E7"));

	private boolean signal_value_is_pitch;
	private int instrument_id;
	private int note_id;

	public CircuitParameters(boolean signalValueIsPitch, int instrumentID, int noteId) {
		this.signal_value_is_pitch = signalValueIsPitch;
		this.instrument_id = instrumentID;
		this.note_id = noteId;
	}
}
