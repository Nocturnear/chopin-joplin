package module1Joplin;

import java.util.ArrayList;

import jm.music.data.Part;

public class Harmony{
	Part music;
	int keySignature;
	double tempo;
	ArrayList<Integer> ids;
	
	public Harmony(Part music, int keySignature, double tempo,
			ArrayList<Integer> ids) {
		this.music = music;
		this.keySignature = keySignature;
		this.tempo = tempo;
		this.ids = ids;
	}
	
	
}
