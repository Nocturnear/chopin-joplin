package data;

import jm.music.data.CPhrase;
import jm.music.tools.Mod;

public class HPattern{
	private String title;
	private double tempo;
	private int id;
	private int keySig;
	private int beatsPerMeasure;
	private CPhrase m1;
	private CPhrase m2;
	
	public HPattern(String title, double tempo, int id,
			int keySig,	int beatsPerMeasure, 
			CPhrase m1, CPhrase m2) {
		this.title = title;
		this.tempo = tempo;
		this.id = id;
		this.keySig = keySig;
		this.beatsPerMeasure = beatsPerMeasure;
		this.m1 = m1;
		this.m2 = m2;
	}
	
	public HPattern(HPattern copy) {
		this.title = copy.title;
		this.tempo = copy.tempo;
		this.id = copy.id;
		this.keySig = copy.keySig;
		this.beatsPerMeasure = copy.beatsPerMeasure;
		this.m1 = copy.m1.copy();
		this.m2 = copy.m2.copy();
	}
	
	public void changeKey(int newKey) {
		Mod.transpose(m1, newKey-keySig);
		Mod.transpose(m2, newKey-keySig);
		keySig = newKey;
	}
	
	public String toString() {
		return m1.toString() + "\n" + m2.toString() + "\n-\n";
	}

	public String getTitle() {
		return title;
	}

	public double getTempo() {
		return tempo;
	}

	public int getId() {
		return id;
	}

	public int getKeySig() {
		return keySig;
	}

	public int getBeatsPerMeasure() {
		return beatsPerMeasure;
	}

	public CPhrase getM1() {
		return m1;
	}

	public CPhrase getM2() {
		return m2;
	}
	
}