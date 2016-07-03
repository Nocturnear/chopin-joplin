package module0Experiments;

import java.awt.FileDialog;
import java.awt.Frame;

import jm.JMC;
import jm.music.data.CPhrase;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;
import jm.util.Read;
import jm.util.View;
import jm.util.Write;

public class Experiment implements JMC {

	public static void main(String[] args) {
		Score score = new Score("MapleLeafRag");
		Read.midi(score, "lib/joplin/MapleLeafRag.mid");
		Mod.quantise(score, 0.01);
		
		Mod.elongate(score, 2);
		score.setTempo(score.getTempo() * 2);
		
		System.out.println("Done");
		
	}

}