package module1Joplin;

import java.util.ArrayList;

import module1Joplin.Harmony;
import module1Joplin.HPattern;
import module1Joplin.Measure;
import jm.JMC;
import jm.music.data.*;
import jm.util.Play;
import jm.util.Read;
import jm.util.View;

public class HarmonyGeneratorOld implements JMC {

	static ArrayList<HPattern> candidates;
	static ArrayList<HPattern> beginnings;
	static ArrayList<HPattern> ends;
	
	/* This function takes in a music file as an input, scrapes the file for 
	 * 2-measure HarmonyPatterns, and adds them to the list of candidates.
	 */
	int analyzeMIDI(Score score){
		
		int numPatterns = 0;
		
		Measure[] measures = makeMeasures(score);
		
		test(measures);
		
		return numPatterns;
	}	
	private Measure[] makeMeasures(Score score) {
		double duration = score.getEndTime();
		int beatsPerMeasure = score.getNumerator();
		double numMeasures = duration/beatsPerMeasure;
		double beatsToSkip = duration % (Mod1Utils.THEME*beatsPerMeasure);
		numMeasures -= beatsToSkip/beatsPerMeasure;
		Part part = getHarmony(score);
		if (part == null)
			return null;
		
		//Play.midi(part);	//TODO
		
		Phrase[] phrases = part.getPhraseArray();
		
		Measure[] measures = new Measure[(int)numMeasures];
		for(int i = 0; i < measures.length; i++) {
			measures[i] = new Measure(beatsPerMeasure, phrases.length, i);
		}
		
		return fillMeasures(beatsToSkip, measures, phrases);
	}
	private Part getHarmony(Score score) {
		Part[] partArr = score.getPartArray();
		if(partArr.length < 1 || partArr[1].getPhraseArray().length == 0){
			System.out.println(score.getTitle() + " is invalid");
			return null;
		}
		
		return partArr[1];
	}
	private Measure[] fillMeasures(double beatsToSkip, Measure[] measures, Phrase[] phrases) {
		int measCount = 0;
		
		for(int i = 0; i < phrases.length; i++){
			double skipped = 0.0;
			Note[] notes = phrases[i].getNoteArray();
			for(int j = 0; j < notes.length; j++) {
				double noteDur = notes[j].getRhythmValue();
				//This if-block filters out "introduction" notes that should be skipped
				if(phrases[i].getStartTime() < beatsToSkip && skipped < beatsToSkip)
					if(Mod1Utils.compareLength(skipped + noteDur, beatsToSkip) < 0)
						skipped += noteDur;
					else
						skipped = beatsToSkip;
				//Add notes to measures
				else{
					int success = measures[measCount].addNote(notes[j], i);
					if(success == 0){
						measCount++;
						j--;
					}
					else {
						if(measures[measCount].isFull())
							measCount++;
					}
				}
			}
		}
		return measures;
	}
	/* This function will generate a Harmony object, which contains the 
	 * left hand musical data, by stringing together multiple HarmonyPatterns 
	 * from the candidates list.
	 */
	Harmony generateHarmony(){
		return null;
	}
	
	//TODO
	private void test(Measure[] meas){
		/*Score s = new Score();
		Part p = new Part();
		for(Measure mea : meas){
			p.addCPhrase(mea.makeCPhrase());
		}
		s.add(p);
		
		System.out.println("Output:");
		Play.midi(s);*/
	}
	
	public static void main(String[] args) {
		HarmonyGeneratorOld hg = new HarmonyGeneratorOld();
		Score s = new Score("MapleLeafRag");
		Read.midi(s, "lib/joplin/MapleLeafRag.mid");
		if (hg.analyzeMIDI(s) <= 0){
			//System.out.print("No new patterns added.");
		}
	}
}

