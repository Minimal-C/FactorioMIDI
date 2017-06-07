public class SpeakerParameters {
	private double playback_volume;
	private boolean playback_globally;
	private boolean allow_polyphony;

	public SpeakerParameters(double playback_volume, boolean playback_globally, boolean allow_polyphony) {
		this.playback_volume = playback_volume;
		this.playback_globally = playback_globally;
		this.allow_polyphony = allow_polyphony;
	}
}
