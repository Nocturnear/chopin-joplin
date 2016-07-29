package module1Joplin;

import jm.music.data.CPhrase;
import jm.music.tools.Mod;

public class HPattern{
	String title;
	double tempo;
	int id;
	int keySig;
	int beatsPerMeasure;
	CPhrase m1;
	CPhrase m2;
	
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
	
	public void changeTempo(double scaleFactor) {
		Mod.elongate(m1, scaleFactor);
		Mod.elongate(m2, scaleFactor);
		tempo *= scaleFactor;
	}
	
	public void changeKey(int newKey) {
		Mod.transpose(m1, newKey-keySig);
		Mod.transpose(m2, newKey-keySig);
		keySig = newKey;
	}
}