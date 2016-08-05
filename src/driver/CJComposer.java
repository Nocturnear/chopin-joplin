package driver;

import generator.*;
import data.*;
import jm.JMC;
import jm.music.data.*;
import jm.util.Write;

public class CJComposer implements JMC{
	
	private Score makeMusic(){
		HarmonyGenerator hg = new HarmonyGenerator();
		Harmony genH = hg.generateHarmony();
		
		
		MelodyGenerator mg = new MelodyGenerator(genH);
		Melody genM = mg.generateMelody();
		
		Score score = new Score();
		score.add(genH.getMusic());
		score.add(genM.getMusic());
		
		return score;
	}
	
	public static void main(String[] args) {
		CJComposer cjc = new CJComposer();
		Score music = cjc.makeMusic();
		
		Write.midi(music, "lib/generated/chopin-joplin.mid");
		
	}
}
