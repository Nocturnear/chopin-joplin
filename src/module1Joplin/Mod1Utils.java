package module1Joplin;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import jm.JMC;
import jm.music.data.*;

public final class Mod1Utils implements JMC {
	static int THEME = 16;

	static class NoteComp{
		int pitch;
		double offset;
		
		public NoteComp(int pitch,double offset) {
			this.pitch = pitch;
			this.offset = offset;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof NoteComp) {
				NoteComp nc = (NoteComp)o;
				return (pitch == nc.pitch) && (compareLength(offset, nc.offset) == 0);
			}
			else {
				return false;
			}
		}
		
		public String toString(){
			return String.format("[%.3f, %d]",offset,pitch);
		}
	}
	
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
	static boolean equals(CPhrase m, CPhrase n) {
		Vector<Phrase> mVector = m.getPhraseList();
		Phrase[] mPhrases = mVector.toArray(new Phrase[mVector.size()]);
		Vector<Phrase> nVector = n.getPhraseList();
		Phrase[] nPhrases = nVector.toArray(new Phrase[nVector.size()]);
		
		ArrayList<NoteComp> allNotesM = new ArrayList<NoteComp>();
		ArrayList<NoteComp> allNotesN = new ArrayList<NoteComp>();
		
		for (Phrase mP : mPhrases) {
			double off = 0.0;
			for (Note mN : mP.getNoteArray()) {
				if (mN.getPitch() >= 0)
					allNotesM.add(new NoteComp(mN.getPitch(), off));
				off += mN.getRhythmValue();
			}
		}
		for (Phrase nP : nPhrases) {
			double off = 0.0;
			for (Note nN : nP.getNoteArray()) {
				if (nN.getPitch() >= 0)
					allNotesN.add(new NoteComp(nN.getPitch(), off));
				off += nN.getRhythmValue();
			}
		}
		
		Collections.sort(allNotesM, new Comparator<NoteComp>() {
			@Override
			public int compare(NoteComp o1, NoteComp o2) {
				int offDiff = compareLength(o1.offset, o2.offset); 
				if (offDiff == 0) {
					return compareLength(o1.pitch, o2.pitch);
				}
				else {
					return offDiff;
				}
			}
		});
		Collections.sort(allNotesN, new Comparator<NoteComp>() {
			@Override
			public int compare(NoteComp o1, NoteComp o2) {
				return compareLength(o1.offset, o2.offset);
			}
		});
		
		for(NoteComp nc : allNotesM) {
			if (!(allNotesN.contains(nc))) {
				return false;
			}
		}
		return true;
	}	
	static boolean equals(HPattern hp1, HPattern hp2) {
		if (!(hp1.title.equals(hp2.title)))
			return false;
		if (hp1.id != hp2.id)
			return false;
		return contentEquals(hp1, hp2);
	}
	static boolean contentEquals(HPattern hp1, HPattern hp2) {
		if (compareLength(hp1.tempo, hp2.tempo) != 0)
			return false;
		if (hp1.keySig != hp2.keySig)
			return false;
		if (hp1.beatsPerMeasure != hp2.beatsPerMeasure)
			return false;
		return (equals(hp1.m1, hp2.m1) && equals(hp1.m2, hp2.m2));
	}

}
