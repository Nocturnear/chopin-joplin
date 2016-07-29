package module1Joplin;

import java.awt.Point;

import jm.music.data.Part;

public class Harmony{
	Part music;
	int keySignature;
	double tempo;
	
	public Harmony(Part music, int keySignature, double tempo) {
		this.music = music;
		this.keySignature = keySignature;
		this.tempo = tempo;
	}

	public Part getMusic() {
		return music;
	}

	public void setMusic(Part music) {
		this.music = music;
	}

	public int getKeySignature() {
		return keySignature;
	}

	public void setKeySignature(int keySignature) {
		this.keySignature = keySignature;
	}

	public double getTempo() {
		return tempo;
	}

	public void setTempo(double tempo) {
		this.tempo = tempo;
	}
	
}
