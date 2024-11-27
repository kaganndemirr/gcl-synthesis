package ktu.kaganndemirr;

import java.util.ArrayList;
import java.util.List;


public class EndSystems {
	String Name;
	List<Integer> inStreamsIDs = new ArrayList<Integer>();
	List<Integer> outStreamsIDs = new ArrayList<Integer>();
	public EndSystems(String _name) {
		Name = _name;
	}
	public EndSystems(String _name, List<Integer> _in, List<Integer> _out) {
		Name = _name;
        inStreamsIDs.addAll(_in);
        outStreamsIDs.addAll(_out);
		
	}
	
	public void addInID(int ID) {
		inStreamsIDs.add(ID);
	}
	public void addOutID(int ID) {
		outStreamsIDs.add(ID);
	}

	public EndSystems Clone() {
		return new EndSystems(Name, inStreamsIDs, outStreamsIDs);
	}
	
	
	

}
