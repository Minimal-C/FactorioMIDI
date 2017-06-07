import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Music {

	private static final int NOTE_ON = 0x90;
	private static final int NOTE_OFF = 0x80;
	private static final int SET_TEMPO = 0x51;
	private static final int INSTRUMENT = 0x04;
	private static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

	Note[] notes;
	double tickTime;

	private Music(Note[] notes, double tickTime) {
		this.notes = notes;
		this.tickTime = tickTime;
	}
	public static Music midiToNotes(File midiFile) {

		ArrayList<Note> notes = new ArrayList<>();
		double tickSize = -1;

		try {
			Sequence sequence = MidiSystem.getSequence(midiFile);
//			for (Patch patch : sequence.getPatchList()) {
//				System.out.println("Patch: [Bank:"+patch.getBank()+" | Instrument:"+patch.getProgram()+"]");
//			}
			int resolution = sequence.getResolution();
			int trackNumber = 0;
			for (Track track : sequence.getTracks()) {
				trackNumber++;
				for (int i = 0; i < track.size(); i++) {
					MidiEvent event = track.get(i);
					MidiMessage message = event.getMessage();
					if (message instanceof ShortMessage) {
						ShortMessage sm = (ShortMessage) message;
						if (sm.getCommand() == NOTE_ON) {
							int key = sm.getData1();
							int octave = (key / 12) - 1;
							int note = key % 12;
							String noteName = NOTE_NAMES[note];
							int velocity = sm.getData2();

							notes.add(new Note(event.getTick(), noteName, octave, velocity));

						}
//						else if (sm.getCommand() == NOTE_OFF) {
//
//						} else {
////							System.out.println("Command:" + sm.getCommand());
//
//						}
					} else if (message instanceof MetaMessage) {
						if (((MetaMessage) message).getType() == SET_TEMPO) {
							byte[] data = ((MetaMessage) message).getData();
							int tempo = (data[0] & 0xff) << 16 | (data[1] & 0xff) << 8 | (data[2] & 0xff);
							int bpm = 60000000 / tempo;

							double ticksPerSecond = resolution * (bpm / 60.0);
							tickSize = 1.0 / ticksPerSecond;
						}
//						else if (((MetaMessage) message).getType() == INSTRUMENT) {
////							System.out.println("INSTRUMENT: "+new String(((MetaMessage) message).getData()));
//						}
					}


				}
			}

		} catch (InvalidMidiDataException | IOException e) {
			e.printStackTrace();
		}

		return new Music(notes.toArray(new Note[0]), tickSize);
	}
}
