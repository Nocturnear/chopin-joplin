package module1Joplin;

import java.util.ArrayList;

import jm.music.data.*;

public class Measure {
	CPhrase chords;
	Phrase[] phrases;
	int beatsPerMeasure;
	int currBeats;

	Measure(int beatsPer, int num, double measNdx) {
		double startTime = measNdx * beatsPer;
		chords = new CPhrase(startTime);
		phrases = new Phrase[num];
		for (int i = 0; i < num; i++)
			phrases[i] = new Phrase(startTime);
		this.beatsPerMeasure = beatsPer;
		currBeats = 0;
	}

	boolean isFull() {
		return (currBeats == beatsPerMeasure);
	}

	int addNote(Note n, int phrase) {
		if (isFull())
			return 0;
		double addDur = currBeats + n.getRhythmValue();
		if (Mod1Utils.compareLength(addDur, beatsPerMeasure) > 1) {
			currBeats = beatsPerMeasure;
			return 0;
		} else {
			phrases[phrase].add(n);
			currBeats = (int) addDur;
			if (Mod1Utils.compareLength(addDur, beatsPerMeasure) == 0)
				currBeats = beatsPerMeasure;
			return 1;
		}
	}
	
	CPhrase makeCPhrase(){
		for (Phrase phrase : phrases){
			chords.addPhrase(phrase);
		}
		return chords;
	}
}
