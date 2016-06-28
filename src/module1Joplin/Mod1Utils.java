package module1Joplin;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

import jm.JMC;
import jm.music.data.*;

public final class Mod1Utils implements JMC {
	static int THEME = 16;

	static int compareLength(double n, double m){
		double diff = n - m;
		double epsilon = Math.min(n, m) * .1;
		if(Math.abs(diff) <= epsilon)
			return 0;
		else
			return (int)Math.signum(diff);
	}
	
	static boolean equals(Note n, Note m){
		if (n.getPitch() != m.getPitch())
			return false;
		if(compareLength(n.getRhythmValue(),m.getRhythmValue()) != 0)
			return false;
		return true;
	}
	static boolean equals(Phrase p, Phrase q) {
		Note[] pn = p.getNoteArray();
		Note[] qn = q.getNoteArray();
		if (pn.length != qn.length)
			return false;
		for (int i = 0; i < pn.length; i++) {
			if(!(equals(pn[i], qn[i])))
				return false;
		}
		return true;
	}
	static boolean equals(Measure m, Measure n) {
		if (m.phrases.length != n.phrases.length)
			return false;
		for(int i = 0; i < m.phrases.length; i++) {
			if (!(equals(m.phrases[i], n.phrases[i])))
				return false;
		}
		return true;
	}	
	static boolean equals(HPattern hp1, HPattern hp2) {
		return (equals(hp1.m1, hp2.m1) && equals(hp1.m2, hp2.m2));
	}

}
