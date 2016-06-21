package module1Joplin;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

import module1Joplin.Mod1Utils.Harmony;
import module1Joplin.Mod1Utils.HPattern;
import module1Joplin.Mod1Utils.Measure;
import jm.JMC;
import jm.music.data.*;
import jm.util.Read;
import jm.util.View;

public class HarmonyGenerator implements JMC {

	static ArrayList<HPattern> candidates;
	static ArrayList<HPattern> beginnings;
	static ArrayList<HPattern> ends;
	
	/* This function takes in a music file as an input, scrapes the file for 
	 * 2-measure HarmonyPatterns, and adds them to the list of candidates.
	 */
	int analyzeMIDI(Score score){
		
		//TODO: Change to 0 after logic is put in for numPatterns.
		int numPatterns = 1;
		
		ArrayList<Measure> measures = makeMeasures(score);
		
		return numPatterns;
	}	
	private ArrayList<Measure> makeMeasures(Score score) {
		ArrayList<Measure> measures = new ArrayList<Measure>();
		
		double duration = score.getEndTime();
		int beatsPerMeasure = score.getNumerator();
		double numMeasures = duration/beatsPerMeasure;
		Part part = getHarmony(score);
		if (part == null)
			return null;
		
		for(Phrase phrase : part.getPhraseArray()){
			
			Note[] notes = phrase.getNoteArray();
			for(Note note : notes) {
				double noteDur = Mod1Utils.getLength(note);
			}
		}
		
		return measures;
	}
	private Part getHarmony(Score score) {
		Part[] partArr = score.getPartArray();
		if(partArr.length < 1 || partArr[1].getPhraseArray().length == 0){
			System.out.println(score.getTitle() + " is invalid");
			return null;
		}
		return partArr[1];
	}
	
	/* This function will generate a Harmony object, which contains the 
	 * left hand musical data, by stringing together multiple HarmonyPatterns 
	 * from the candidates list.
	 */
	Harmony generateHarmony(){
		return null;
	}
	
	public static void main(String[] args) {
		HarmonyGenerator hg = new HarmonyGenerator();
		Score s = new Score("MapleLeafRag");
		Read.midi(s, "lib/joplin/MapleLeafRag.mid");
		if (hg.analyzeMIDI(s) <= 0){
			System.out.print("No new patterns added.");
		}
		View.show(s);
	}
}

