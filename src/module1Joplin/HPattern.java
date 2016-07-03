package module1Joplin;

import jm.music.data.CPhrase;

public class HPattern{
	String title;
	double tempo;
	int id;
	int keySig;
	int beatsPerMeasure;
	CPhrase m1;
	CPhrase m2;
	
	public HPattern(String title, double tempo, int id, int keySig,	int beatsPerMeasure, CPhrase m1, CPhrase m2) {
		this.title = title;
		this.tempo = tempo;
		this.id = id;
		this.keySig = keySig;
		this.beatsPerMeasure = beatsPerMeasure;
		this.m1 = m1;
		this.m2 = m2;
	}
	
	
	
	
}