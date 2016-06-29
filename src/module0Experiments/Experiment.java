package module0Experiments;

import java.awt.FileDialog;
import java.awt.Frame;

import jm.JMC;
import jm.music.data.CPhrase;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Read;
import jm.util.View;
import jm.util.Write;

public class Experiment extends Frame implements JMC {

	public static void main(String[] args) {
		FileDialog fd;
		Frame f = new Frame();

		fd = new FileDialog(f, "Choose a MIDI file to analyze or cancel.",
				FileDialog.LOAD);
		fd.setVisible(true);

		if (fd.getFile() == null)
			System.exit(0);
		
		Score s = new Score();
		Read.midi(s, fd.getDirectory() + fd.getFile());
		s.setTitle(fd.getFile());
		
		Part[] arr = s.getPartArray();
		for(int i = 0; i < arr.length; i++ ){
			System.out.println("Part " + i + " has " + 
					arr[i].getPhraseArray().length + " phrases.");
			Phrase[] pArr = arr[i].getPhraseArray();
			CPhrase chord = new CPhrase(pArr[0].getStartTime());
			for(int j = 0; j < pArr.length; j++){
				System.out.println("Phrase " + j);
				chord.addPhrase(pArr[j]);
				try {
				    Thread.sleep(1000);
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}
			View.show(chord.copy(0, 5));
		}
	}

}