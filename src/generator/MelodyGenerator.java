package generator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import data.*;
import jm.JMC;
import jm.music.data.*;
import jm.music.tools.Mod;
import jm.util.Read;
import jm.util.Write;

public class MelodyGenerator implements JMC {

	Harmony genHarmony;
	ArrayList<Note[]> grams;
	Node head;
	int domKeyDiff;
	int subDomKeyDiff;
	
	class Node {
		Note note;
		ArrayList<Node> children;
		
		public Node(Note note) {
			this.note = note;
			children = new ArrayList<Node>();
		}
		
		public String toString(){
			return note.getPitch() + ", " + note.getRhythmValue();
		}
		
	}
	
	public MelodyGenerator(Harmony generated){
		genHarmony = generated;
		grams = new ArrayList<Note[]>();
		head = new Node(null);
	}
	
	public Melody generateMelody() {
		Part music = new Part();
		scrapeForGrams();
		buildGramTree();
		
		Phrase dominantTheme = new Phrase();
		boolean makingDominantTheme = true;
		Random rand = new Random();
		
		for(int i = 0; i < genHarmony.getNumThemes(); i++) {
			int key;
			Phrase theme = new Phrase();
			
			if(i % 2 == 0) {
				if(!makingDominantTheme) {
					music.appendPhrase(dominantTheme);
					continue;
				}
				key = domKeyDiff;
			}
			else {
				key = subDomKeyDiff;
			}
			
			double themeDuration = 0.0;
			while(themeDuration < Utils.THEME_LENGTH) {
				Node root = head;
				Note[] gramAdd = new Note[Utils.GRAM_LENGTH];
				for(int n = 0; n < Utils.GRAM_LENGTH; n++) {
					root = root.children.get(rand.nextInt(root.children.size()));
					if(themeDuration + root.note.getRhythmValue() <= Utils.THEME_LENGTH){
						gramAdd[n] = root.note.copy();
						themeDuration += root.note.getRhythmValue();
					}
					else{
						if(n > 0) {
							gramAdd = Arrays.copyOfRange(gramAdd, 0, n);
							theme.addNoteList(gramAdd);
						}
						themeDuration = Utils.THEME_LENGTH;
						gramAdd = null;
						break;
					}
				}
				if(gramAdd != null){
					theme.addNoteList(gramAdd);
				}
			}
			
			Mod.transpose(theme, key);
			
			if(makingDominantTheme){
				dominantTheme = theme.copy();
				makingDominantTheme = false;
			}
			
			music.appendPhrase(theme);
		}
		music.setDynamic((Note.DEFAULT_DYNAMIC*4)/7);
		return new Melody(music);
	}
	
	private void scrapeForGrams() {
		Score score = new Score("Tempos");
		Read.midi(score, "lib/chopin/chpn_op66.mid");
		score.setPan((PAN_CENTER + PAN_RIGHT)/2);
		
		score.setTempo(genHarmony.getTempo());
		
		domKeyDiff = genHarmony.getDomKey() - score.getKeySignature();
		subDomKeyDiff = genHarmony.getSubDomKey() - score.getKeySignature();
		
		Note[] origMelody = score.getPart(0).getPhrase(0).getNoteArray();
		for (int n = 0; n < origMelody.length-(Utils.GRAM_LENGTH-1); n++) {
			Note[] gram = new Note[Utils.GRAM_LENGTH];
			for (int i = 0; i < Utils.GRAM_LENGTH; i++) {
				gram[i] = origMelody[n+i];
			}
			grams.add(gram);
		}	
		System.out.println("Finished building grams");
	}
	
	private void buildGramTree(){
		for(Note[] gram : grams) {
			Node root = head;
			for(int i = 0; i < gram.length; i++) {
				Node n = new Node(gram[i]);
				root.children.add(n);
				root = n;
			}
		}
		grams = null;
	}
	
	public static void main(String[] args) {
		HarmonyGenerator hg = new HarmonyGenerator();
		Harmony genH = hg.generateHarmony();
		Write.midi(genH.getMusic(), "test1.mid");
		
		MelodyGenerator mg = new MelodyGenerator(genH);
		Melody genM = mg.generateMelody();
		Write.midi(genM.getMusic(), "test2.mid");
	}
	
}
