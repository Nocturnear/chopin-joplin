package module1Joplin;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import module1Joplin.Harmony;
import module1Joplin.HPattern;
import module1Joplin.Measure;
import jm.JMC;
import jm.music.data.*;
import jm.util.Play;
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
		
		int numPatterns = 0;
		CPhrase[] measures = makeMeasures(score);
		System.out.println("Output harmony:"); 
		for(int i = 0; i < measures.length; i++){ 
			System.out.println("Measure " + i);
			Part p = new Part(); 
			p.addCPhrase(measures[i]);
			Play.midi(p);
		}
		return numPatterns;
	}	
	private CPhrase[] makeMeasures(Score score) {
		double duration = score.getEndTime();
		int beatsPerMeasure = score.getNumerator();
		double numMeasures = duration/beatsPerMeasure;
		double beatOffset = duration % (Mod1Utils.THEME*beatsPerMeasure);
		numMeasures -= beatOffset/beatsPerMeasure;
		
		CPhrase harmony = getHarmonyPart(score);
		CPhrase[] measures = new CPhrase[(int) numMeasures];
		for(int m = 0; m < numMeasures; m++) {
			double measureStart = beatOffset + (m*beatsPerMeasure);
			double measureEnd = beatOffset + ((m+1)*beatsPerMeasure);
			measures[m] = copySection(harmony, measureStart, measureEnd);
		}
		return measures;
	}
	/* A lot of this method was scavenged from the copy(double startLoc, double endLoc) method in the CPhrase class */
	private CPhrase copySection(CPhrase phrase, double paramDouble1, double paramDouble2) {
		Vector localVector = new Vector();
	    CPhrase localCPhrase = new CPhrase(phrase.getTitle() + " copy", paramDouble1, phrase.getInstrument());
	    Enumeration localEnumeration = phrase.getPhraseList().elements();
	    while (localEnumeration.hasMoreElements())
	    {
	      Phrase localPhrase = ((Phrase)localEnumeration.nextElement()).copy(paramDouble1, paramDouble2);
	      if(localPhrase == null)
	    	  continue;
	      localPhrase.setStartTime(paramDouble1);
	      localVector.addElement(localPhrase);
	    }
	    localCPhrase.setPhraseList(localVector);
	    localCPhrase.setAppend(phrase.getAppend());
	    localCPhrase.setLinkedPhrase(phrase.getLinkedPhrase());
	    return localCPhrase;
	}
	private CPhrase getHarmonyPart(Score score) {
		Part[] partArr = score.getPartArray();
		if(partArr.length < 1 || partArr[1].getPhraseArray().length == 0){
			System.out.println(score.getTitle() + " is invalid");
			return null;
		}
		CPhrase harmony = new CPhrase(partArr[1].getPhrase(0).getStartTime());
		for(Phrase phrase : partArr[1].getPhraseArray())
			harmony.addPhrase(phrase);
		return harmony;
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
			//System.out.print("No new patterns added.");
		}
	}
}

