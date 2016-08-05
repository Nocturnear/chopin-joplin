package data;

import jm.music.data.Part;

public class Harmony{
	
	private  Part music;
	private  int domKey;
	private  int subDomKey;
	private  int numThemes;
	private  double tempo;

	public Harmony(Part music, int domKey, int subDomKey, 
			int numThemes, double tempo) {
		this.music = music;
		this.domKey = domKey;
		this.subDomKey = subDomKey;
		this.numThemes = numThemes;
		this.tempo = tempo;
	}

	public Part getMusic() {
		return music;
	}

	public int getDomKey() {
		return domKey;
	}

	public int getSubDomKey() {
		return subDomKey;
	}

	public int getNumThemes() {
		return numThemes;
	}

	public double getTempo() {
		return tempo;
	}
	
}
