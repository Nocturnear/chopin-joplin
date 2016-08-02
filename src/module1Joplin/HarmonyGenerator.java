package module1Joplin;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import module1Joplin.Harmony;
import module1Joplin.HPattern;
import jm.JMC;
import jm.music.data.*;
import jm.util.Read;
import jm.util.Write;

public class HarmonyGenerator implements JMC {

	HashMap<String, ArrayList<HPattern>> candidates;
	
	public HarmonyGenerator(){
		candidates = new HashMap<String, ArrayList<HPattern>>();
		File[] pieces = new File("lib/joplin").listFiles();
		for(File piece : pieces)
			if(piece.getName().endsWith(".mid"))
				candidates.put(piece.getName(), new ArrayList<HPattern>());
	}
	
	/* This function takes in a music file as an input, scrapes the file for 
	 * 2-measure HarmonyPatterns, and adds them to the list of candidates.
	 */
 	void analyzeMIDI(Score score){
		CPhrase[] measures = makeMeasures(score);
		ArrayList<HPattern> patterns = convertMeasures(score, measures);
		patterns = findRepeats(patterns);
		int newPatterns = addNewPatterns(patterns);
		if  (newPatterns <= 0){
			System.out.print("No new patterns added.");
		}
		else{
			System.out.printf("There are %d new patterns added for %s.\n", newPatterns, score.getTitle());
		}
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
	    	  double noteLen = localPhrase.getNoteArray()[n].getRhythmValue();
	    	  if (Mod1Utils.compareLength(noteLen, 0.05) < 0 || Mod1Utils.compareLength(noteLen, paramDouble2-paramDouble1) > 0) {
	    		  localPhrase.removeNote(n);
	    		  n--;
	    	  }
	      }
	      if(localPhrase.getSize() > 0)
	    	  localVector.addElement(localPhrase);
	    }
	    localCPhrase.setPhraseList(localVector);
	    localCPhrase.setAppend(phrase.getAppend());
	    localCPhrase.setLinkedPhrase(phrase.getLinkedPhrase());
	    return localCPhrase;
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
		
		for(HPattern pattern : patterns) {
			if (candidates.containsKey(pattern.title))
				candidates.get(pattern.title).add(pattern);
			else {
			}
				
				
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
		ArrayList<Integer> ids = new ArrayList<Integer>();
		int dominantKey = rand.nextInt(12) - 4;
		int subdominantKey = (dominantKey != -4) ? dominantKey - 1 : 7;
		
		int numMovements = (rand.nextInt(2)) == 0 ? 3 : 5;
		boolean makingDominantTheme = true;
		HPattern[] dominantTheme = new HPattern[Mod1Utils.THEME];
		ArrayList<String> candidateKeys = new ArrayList<String>(candidates.keySet());
		String randKey = candidateKeys.get(rand.nextInt(candidateKeys.size()));
		System.out.println("Generating harmony based off of " + randKey);
		if(candidates.get(randKey).size() == 0) {
			Score score = new Score(randKey);
			Read.midi(score, "lib/joplin/" + randKey);
			analyzeMIDI(score);
		}
			
		ArrayList<HPattern> randPool = candidates.get(randKey);
		double tempo = randPool.get(0).tempo;
		double startTime = 0.0;
		
		for(int i = 0; i < numMovements; i++) {
			int key;
			
			if (i % 2 == 0) {
				if(!makingDominantTheme) {
					for (HPattern hPat : dominantTheme) {
						addToPart(music, new HPattern(hPat), startTime);
						startTime += 4;
					}
					continue;
				}
				key = dominantKey;
			}
			else {
				key = subdominantKey;
			}
			
			for(int j = 0; j < Mod1Utils.THEME; j++) {
				
				HPattern randPattern = new HPattern(randPool.get(rand.nextInt(randPool.size())));
				randPattern.changeKey(key);
				
				addToPart(music, randPattern, startTime);
				startTime += 4;
				ids.add(randPattern.id);
				if (makingDominantTheme)
					dominantTheme[j] = randPattern;
			}
			makingDominantTheme = false;
			
		}
		music.setPan(PAN_CENTER);
		return new Harmony(music, dominantKey, tempo, ids);
	}
	
	private Part addToPart(Part music, HPattern pattern, double startTime) {
		pattern.m1.setStartTime(startTime);
		pattern.m2.setStartTime(startTime + 2);
		music.addCPhrase(pattern.m1);
		music.addCPhrase(pattern.m2);
		return music;
	}
	
	public static void main(String[] args) {
		HarmonyGenerator hg = new HarmonyGenerator();
		Harmony gen = hg.generateHarmony();
		Write.midi(gen.music, "test.mid");
	}
}

