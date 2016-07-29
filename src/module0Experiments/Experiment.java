package module0Experiments;

import java.awt.FileDialog;
import java.awt.Frame;

import jm.JMC;
import jm.music.data.CPhrase;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;
import jm.util.Play;
import jm.util.Read;
import jm.util.View;
import jm.util.Write;

public class Experiment implements JMC {

	public static void main(String[] args) {
		Score score = new Score("MapleLeafRag");
		Read.midi(score, "lib/joplin/MapleLeafRag.mid");
		
		Mod.elongate(score, 0.5);
		Play.midi(score);
		
		System.out.println("Done");
		
	}

}