import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class GUI{

	private String bpString;
	private final String OUTPUT_PANEL_NAME = "OUTPUT";
	private final String START_PANEL_NAME = "START";

	private GUI(){

		JFrame frame = new JFrame("Blueprint String");

		JPanel cards = new JPanel(new CardLayout());
		CardLayout cl = (CardLayout)cards.getLayout();

		JPanel startPanel = new JPanel();
		JPanel outputPanel = new JPanel(new GridBagLayout());

		JTextArea text = new JTextArea();
		text.setLineWrap(true);
		text.setEditable(false);

		JButton selectFileBtn = new JButton("Select Midi File");

		selectFileBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fChooser = new JFileChooser();

				int result = fChooser.showDialog(null, "Select");

				if (result==JFileChooser.APPROVE_OPTION){

					File f = fChooser.getSelectedFile();
					Music music = Music.midiToNotes(f);

					bpString = getBlueprintString(music);

					text.setText(bpString);
					outputPanel.validate();

					cl.show(cards,OUTPUT_PANEL_NAME);

				}
			}
		});
		startPanel.add(selectFileBtn);

		GridBagConstraints gc = new GridBagConstraints();

		JButton copyBtn = new JButton("Copy Blueprint String");

		copyBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StringSelection stringSelection = new StringSelection(bpString);
				Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
				clpbrd.setContents(stringSelection, null);
			}
		});

		JScrollPane scrollPane = new JScrollPane(text);
		scrollPane.setMinimumSize(new Dimension(750,500));
		outputPanel.add(scrollPane, gc);

		gc.gridy = 2;
		outputPanel.add(copyBtn, gc);

		JButton outputBackBtn = new JButton("Back");
		outputBackBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cl.show(cards, START_PANEL_NAME);
			}
		});
		gc.gridy = 3;

		cards.add(startPanel,START_PANEL_NAME);
		cards.add(outputPanel,OUTPUT_PANEL_NAME);

		frame.add(cards);

		frame.setSize(800,600);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		new GUI();
	}

	public static String parseBlueprintStringToJson(String blueprintString) {

		// Remove the Factorio Blueprint version digit at position 0
		byte[] base64Bytes = blueprintString.substring(1).getBytes();
		byte[] inputBytes = Base64.getDecoder().decode(base64Bytes);

		byte[] output = new byte[33554432];

		try {
			Inflater inflater = new Inflater();
			inflater.setInput(inputBytes);
			inflater.inflate(output);
			inflater.end();

		} catch (DataFormatException e) {
			e.printStackTrace();
		}

		return new String(output);
	}

	private static String parseJsonToBlueprintString(String jsonString) {
		// compress
		Deflater compressor = new Deflater();
		compressor.setInput(jsonString.getBytes());

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(jsonString.length());
		compressor.finish();
		byte[] buffer = new byte[1024];
		while (!compressor.finished()) {
			int count = compressor.deflate(buffer); // returns the generated code... index
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] output = outputStream.toByteArray();

		compressor.finish();
		compressor.deflate(output);
		compressor.end();

		// then base64 it
		byte[] outputBytes = Base64.getEncoder().encode(output);
		String outputString = new String(outputBytes);

		// Prepend the Factorio Blueprint version digit to string
		outputString = "0" + outputString;

		return outputString;
	}



	private String getBlueprintString(Music music) {
		int maxVel = Collections.max(Arrays.asList(music.notes), new Note.VelocityComparator()).velocity;
		int minVel = Collections.min(Arrays.asList(music.notes), new Note.VelocityComparator()).velocity;


//			ArrayList<Note> noteSet = new ArrayList<>();

		int badNoteCounter=0;

		for (int i = 0; i < music.notes.length; i++) {


			// Fudge alert: (Factorio piano range: F3 - E7) if note outside range, replace it with nearest valid note.
			if (!CircuitParameters.PIANO_NOTES.contains(music.notes[i].noteName + (music.notes[i].octave))) {
				badNoteCounter++;
				if (music.notes[i].octave <= 3) {
					music.notes[i].noteName = "F";
					music.notes[i].octave = 3;
				}
			}
//				if (!noteSet.contains(music.notes[i])) {
//					noteSet.add(music.notes[i]);
//				}
		}

		int len = music.notes.length;
		int songLength = (int) Math.ceil(music.tickTime * music.notes[len - 1].tick);

		Entity[] entities = new Entity[len];

		Entity[] timerEntities = createTimer(songLength * 60);

		int entityNumberCounter = 3;

		int i;
		for (i = 0; i < len - 1; i++) {
			entities[i] = new Entity(entityNumberCounter, Entity.DECIDER_COMBINATOR, new Position(i, 3.5), Entity.DIRECTION_SOUTH,
					new ControlBehaviour(new DeciderConditions(new Signal(Signal.SIG_TYPE_VIRTUAL, Signal.SIG_0), (int) (music.notes[i].tick * music.tickTime * 60), CircuitCondition.EQUAL, new Signal(Signal.SIG_TYPE_VIRTUAL, Signal.SIG_1), false)),
					new Connection(new Wires(new Red[]{new Red(entityNumberCounter - 1, 1)}), new Wires(new Red[]{new Red(entityNumberCounter + len, 2)})));

			entityNumberCounter++;
		}
		entities[i] = new Entity(entityNumberCounter, Entity.DECIDER_COMBINATOR, new Position(i, 3.5), Entity.DIRECTION_SOUTH,
				new ControlBehaviour(new DeciderConditions(new Signal(Signal.SIG_TYPE_VIRTUAL, Signal.SIG_0), (int) (music.notes[i].tick * music.tickTime * 60), CircuitCondition.EQUAL, new Signal(Signal.SIG_TYPE_VIRTUAL, Signal.SIG_1), false)),
				new Connection(new Wires(new Red[]{new Red(entityNumberCounter - 1, 1)}), new Wires(new Red[]{new Red(entityNumberCounter + len, 2)})));

		entityNumberCounter++;

		Entity[] e2 = new Entity[len];

		for (int j = 0; j < len; j++) {

			int noteIndex = CircuitParameters.PIANO_NOTES.indexOf(music.notes[j].noteName + (music.notes[j].octave));

			double scaledVelocity = scale((double) music.notes[j].velocity, (double) minVel, (double) maxVel, 0.5, 1.0);

			e2[j] = new Entity(entityNumberCounter, Entity.PROGRAMMABLE_SPEAKER, new Position(j, 5), Entity.DIRECTION_NORTH,
					new ControlBehaviour(new CircuitCondition(new Signal(Signal.SIG_TYPE_VIRTUAL, Signal.SIG_1), 1, CircuitCondition.EQUAL),
							new CircuitParameters(false, 2, noteIndex)),
					new Connection(new Wires(new Red[]{new Red(entityNumberCounter - len, 2)})),
					new SpeakerParameters(scaledVelocity, true, true),
					new AlertParameters(false, false, ""));

			entityNumberCounter++;
		}

		ArrayList<Entity> e = new ArrayList<>(Arrays.asList(timerEntities));
		e.addAll(Arrays.asList(entities));
		e.addAll(Arrays.asList(e2));

		entities = e.toArray(new Entity[0]);

		Icon[] icons = {new Icon(new Signal(Entity.ITEM, Entity.PROGRAMMABLE_SPEAKER), 1)};

		Blueprint blueprint = new Blueprint(icons, entities);

		BlueprintOutput out = new BlueprintOutput(blueprint);

		Gson gson = new Gson();
		String jsonString = gson.toJson(out);

		// Extra step to replace "wires1" with number in line with blueprint json,
		// a number cannot be a variable name in java so this is a workaround
		jsonString = jsonString.replace("wires1", "1");
		jsonString = jsonString.replace("wires2", "2");

		JOptionPane.showMessageDialog(null, "Status:\n\nBad Notes: "+badNoteCounter+"/"+music.notes.length+"\nApprox. Length: "+songLength+"s");

		return parseJsonToBlueprintString(jsonString);
	}

	private static Entity[] createTimer(int songLength) {

		Filter[] filters1 = {new Filter(new Signal(Signal.SIG_TYPE_VIRTUAL, Signal.SIG_0), 1, 1)};
		DeciderConditions deciderConditions2 = new DeciderConditions(new Signal(Signal.SIG_TYPE_VIRTUAL, Signal.SIG_0), songLength, "\u003c", new Signal(Signal.SIG_TYPE_VIRTUAL, Signal.SIG_0), true);

		ControlBehaviour controlBehaviour1 = new ControlBehaviour(filters1);
		ControlBehaviour controlBehaviour2 = new ControlBehaviour(deciderConditions2);

		Red[] red1 = {new Red(2, 1)};
		Red[] red2 = {new Red(1, 1), new Red(2, 2)};
		Red[] red3 = {new Red(2, 1), new Red(3, 1)};

		Wires wires1 = new Wires(red1);
		Wires wires2 = new Wires(red2);
		Wires wires3 = new Wires(red3);

		Connection connections1 = new Connection(wires1);
		Connection connections2 = new Connection(wires2, wires3);

		Entity entity1 = new Entity(1, "constant-combinator", new Position(0, 0), Entity.DIRECTION_SOUTH, controlBehaviour1, connections1);
		Entity entity2 = new Entity(2, "decider-combinator", new Position(0, 1.5), Entity.DIRECTION_SOUTH, controlBehaviour2, connections2);

		return new Entity[]{entity1, entity2};
	}

	// For use in rescaling midi volume 0-127, to Factorio volume 0.0-1.0
	private static double scale(final double valueIn, final double baseMin, final double baseMax, final double limitMin, final double limitMax) {
		return ((limitMax - limitMin) * (valueIn - baseMin) / (baseMax - baseMin)) + limitMin;
	}
}