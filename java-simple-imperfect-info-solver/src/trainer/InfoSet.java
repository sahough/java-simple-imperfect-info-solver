package trainer;

public class InfoSet implements Comparable<InfoSet> {

	int handCode;
	History h;
	
	public InfoSet(int _handCode, History _h) {
		handCode = _handCode;
		h = new History(_h);
	}
	
	public String toString() {
		String out = "";
		out += "InfoSet:\nHand: "+handCode+"\n";
		out += h;
		return out;
	}
	
	public int compareTo(InfoSet obj) {
		
		//First, compare hands
		if(handCode < obj.handCode)
			return -1;
		if(handCode > obj.handCode)
			return 1;
		
		//Then history sizes
		History _h = obj.h;
		if(h.streets.size() < _h.streets.size())
			return -1;
		if(h.streets.size() > _h.streets.size())
			return 1;
		
		//Then streets
		for(int street = 0; street < h.streets.size(); street ++) {
			
			StreetHistory sh = h.streets.get(street);
			StreetHistory _sh = _h.streets.get(street);
			
			//Street sizes
			if(sh.size() < _sh.size())
				return -1;
			if(sh.size() > _sh.size())
				return 1;
			
			//Street contents
			for(int i = 0; i < sh.size(); i ++) {
				if(sh.line.get(i) < _sh.line.get(i))
					return -1;
				if(sh.line.get(i) > _sh.line.get(i))
					return 1;
			}
			
		}
		
		//Then flop code
		if(h.flopCode < _h.flopCode)
			return -1;
		if(h.flopCode > _h.flopCode)
			return 1;
		
		return 0;
	}
	
}
