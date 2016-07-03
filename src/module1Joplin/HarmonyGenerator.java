package module1Joplin;

import java.awt.FileDialog;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import module1Joplin.Harmony;
import module1Joplin.HPattern;
import jm.JMC;
import jm.music.data.*;
import jm.music.tools.Mod;
import jm.util.Play;
import jm.util.Read;
import jm.util.View;

public class HarmonyGenerator implements JMC {

	static ArrayList<HPattern> candidates;
	
	/* This function takes in a music file as an input, scrapes the file for 
	 * 2-measure HarmonyPatterns, and adds them to the list of candidates.
	 */
	int analyzeMIDI(Score score){
		CPhrase[] measures = makeMeasures(score);
		ArrayList<HPattern> patterns = convertMeasures(score, measures);
		return addNewPatterns(patterns);
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
	private CPhrase copySection(CPhrase phrase, 
			double paramDouble1, double paramDouble2) {
		/* A lot of this method was scavenged from the copy(double startLoc, double endLoc) method in the CPhrase class */
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
	
	private ArrayList<HPattern> convertMeasures(Score score, CPhrase[] measures) {
		ArrayList<HPattern> patterns = new ArrayList<HPattern>();
		for(int i = 0; i < measures.length; i += 2)
			patterns.add(new HPattern(score.getTitle(), score.getTempo(), i/2, score.getKeySignature(), score.getNumerator(), measures[i], measures[i+1]));
		return patterns;
	}
	
	private int addNewPatterns(ArrayList<HPattern> patterns) {
		
		return 0;
	}
	
	/* This function will generate a Harmony object, which contains the 
	 * left hand musical data, by stringing together multiple HarmonyPatterns 
	 * from the candidates list.
	 */
	Harmony generateHarmony(){
		return null;
	}
	
	private Score getScore(){
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
		
		return s;
	}

	public static void main(String[] args) {
		HarmonyGenerator hg = new HarmonyGenerator();
		//Score s = getScore();
		Score score = new Score("MapleLeafRag");
		Read.midi(score, "lib/joplin/MapleLeafRag.mid");
		
		if (hg.analyzeMIDI(score) <= 0){
			System.out.print("No new patterns added.");
		}
	}
}

