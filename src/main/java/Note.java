import java.util.Comparator;

public class Note {

	private static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

	public long tick;
	public String noteName;
	public int octave;
	public int velocity;

	public Note(long tick, String noteName, int octave, int velocity) {
		this.tick = tick;
		this.noteName = noteName;
		this.octave = octave;
		this.velocity = velocity;
	}

	@Override
	public String toString() {
		return "@" + tick + "," + noteName + octave + "," + velocity;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!Note.class.isAssignableFrom(obj.getClass())) {
			return false;
		}
		Note o = (Note) obj;

		return this.velocity == o.velocity &&
				this.octave == o.octave &&
				this.noteName.equalsIgnoreCase(o.noteName);
	}

	public static class VelocityComparator implements Comparator<Note> {

		@Override
		public int compare(Note o1, Note o2) {

			return o1.velocity > o2.velocity ? o1.velocity : o2.velocity;
		}
	}
}
