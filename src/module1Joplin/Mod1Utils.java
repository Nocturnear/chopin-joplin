package module1Joplin;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

import jm.JMC;
import jm.music.data.*;

public final class Mod1Utils implements JMC {
	class Harmony{
		Part music;
		Point timeSignature;
		int keySignature;
		double tempo;
		double endTime;
	}
	class Measure {
		ArrayList<Phrase> phrases;
		int beatsPerMeasure;
		int currBeats;
		
		Measure(){
			phrases = new ArrayList<Phrase>();
			beatsPerMeasure = 4;
			currBeats = 0;
		}
		Measure(int beatsPerMeasure){
			phrases = new ArrayList<Phrase>();
			this.beatsPerMeasure = beatsPerMeasure;
			currBeats = 0;
		}
		void addPhrase(Phrase phrase){
			phrases.add(phrase);
		}
	
	}
	class HPattern{
		String title;
		Measure m1;
		Measure m2;
	}
	
	static double getLength(Note n){
		return Math.round(n.getRhythmValue()*10)/10.0;
	}
	static boolean equals(Phrase p, Phrase q) {
		Note[] pn = p.getNoteArray();
		Note[] qn = q.getNoteArray();
		if (pn.length != qn.length)
			return false;
		for (int i = 0; i < pn.length; i++) {
			if (!(pn[i].samePitch(qn[i])))
				return false;
			double pr = getLength(pn[i]);
			double qr = getLength(qn[i]);
			
			if(pr != qr)
				return false;
		}
		return true;
	}
	static boolean equals(Measure m, Measure n) {
		if (m.phrases.size() != n.phrases.size())
			return false;
		for(int i = 0; i < m.phrases.size(); i++) {
			if (!(equals(m.phrases.get(i), n.phrases.get(i))))
				return false;
		}
		return true;
	}	
	static boolean equals(HPattern hp1, HPattern hp2) {
		return (equals(hp1.m1, hp2.m1) && equals(hp1.m2, hp2.m2));
	}

}
