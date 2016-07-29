package module1Joplin;

import java.awt.FileDialog;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import module1Joplin.Harmony;
import module1Joplin.HPattern;
import jm.JMC;
import jm.music.data.*;
import jm.util.Play;
import jm.util.Read;
import jm.util.View;

public class HarmonyGenerator implements JMC {

	HashMap<String, ArrayList<HPattern>> candidates;
	Set<Double> tempos;
	
	public HarmonyGenerator(){
		candidates = new HashMap<String, ArrayList<HPattern>>();
		tempos = new HashSet<Double>();
	}
	
	/* This function takes in a music file as an input, scrapes the file for 
	 * 2-measure HarmonyPatterns, and adds them to the list of candidates.
	 */
 	int analyzeMIDI(Score score){
		CPhrase[] measures = makeMeasures(score);
		ArrayList<HPattern> patterns = convertMeasures(score, measures);
		patterns = findRepeats(patterns);
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
	      for(int n = 0; n < localPhrase.getNoteArray().length; n++) {
	    	  if (Mod1Utils.compareLength(localPhrase.getNoteArray()[n].getRhythmValue(), 0.05) < 0) {
	    		  localPhrase.removeNote(n);
	    		  n--;
	    	  }
	      }
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
			patterns.add(new HPattern(score.getTitle(), score.getTempo(), i/2, score.getKeySignature(), 
					score.getNumerator(), measures[i], measures[i+1]));
		return patterns;
	}
	
	private ArrayList<HPattern> findRepeats(ArrayList<HPattern> patterns) {
		ArrayList<HPattern> repeats = new ArrayList<HPattern>();
		for(int p = 0; p < patterns.size(); p++) {
			HPattern curr = patterns.get(p);
			int rCount = 0;
			
			for(int r = p; r < patterns.size(); r++) {
				if( p != r) {
					if (Mod1Utils.contentEquals(curr, patterns.get(r))) {
						patterns.remove(r);
						r--;
						rCount++;
					}
				}
			}
			
			if (rCount > 0) {
				repeats.add(curr);
			}
		}
		
		return repeats;
	}
	
	private int addNewPatterns(ArrayList<HPattern> patterns) {
		for(int p = 0; p < patterns.size(); p++) {
			String key = patterns.get(p).title;
			if(candidates.containsKey(key)) {
				for(HPattern candidate : candidates.get(key)) {
					if (Mod1Utils.equals(candidate, patterns.get(p))) {
						patterns.remove(p);
						p--;
						break;
					}
				}
			}
			else
				candidates.put(key, new ArrayList<HPattern>());
			
		}
		
		for(HPattern pattern : patterns) {
			candidates.get(pattern.title).add(pattern);
			tempos.add(pattern.tempo);
		}
		
		return patterns.size();
	}

	/* This function will generate a Harmony object, which contains the 
	 * left hand musical data, by stringing together multiple HarmonyPatterns 
	 * from the candidates list.
	 */
	Harmony generateHarmony(){
		Random rand = new Random();
		Part music = new Part();
		
		int dominantKey = rand.nextInt(12) - 4;
		int subdominantKey = (dominantKey != -4) ? dominantKey - 1 : 7;
		
		double tempo = new ArrayList<Double>(tempos).get(rand.nextInt(tempos.size()));
		
		int numThemes = rand.nextInt(2) + 3;
		ArrayList<String> candidateKeys = new ArrayList<String>(candidates.keySet());
		for(int i = 0; i < numThemes; i++) {
			int key = (i % 2 == 0) ? dominantKey : subdominantKey;
			
			for(int j = 0; j < Mod1Utils.THEME; j++) {
				String randKey = candidateKeys.get(rand.nextInt(candidateKeys.size()));
				ArrayList<HPattern> randPool = candidates.get(randKey);
				HPattern randPattern = randPool.get(rand.nextInt(randPool.size()));
				randPattern.changeTempo(tempo/randPattern.tempo);
				randPattern.changeKey(key);
				addToPart(music, randPattern);
			}
			
		}
		
		return new Harmony(music, dominantKey, tempo);
	}
	private Part addToPart(Part music, HPattern pattern) {
		//TODO: Add CPhrases to the Part, ensuring that they are appended to each other properly.
		return music;
	}
	
	/*
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
	*/
	private void printCandidates(){
		for (String key : candidates.keySet()) {
			System.out.println("\"" + key + "\"");
			for (HPattern patt : candidates.get(key)) {
				System.out.println("\t" + patt.id);
			}
		}
	}
	public static void main(String[] args) {
		HarmonyGenerator hg = new HarmonyGenerator();
		Score score = new Score("MapleLeafRag");
		Read.midi(score, "lib/joplin/MapleLeafRag.mid");
		int newPatterns = hg.analyzeMIDI(score); 
		if  (newPatterns <= 0){
			System.out.print("No new patterns added.");
		}
		else{
			System.out.printf("There are %d new patterns added.\n", newPatterns);
			hg.printCandidates();
		}
		
	}
}

