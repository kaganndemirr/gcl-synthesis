package ktu.kaganndemirr;

import java.util.ArrayList;
import java.util.List;

public class App {

	public App(int _id) {
		ID = _id;
		WCET = 0;
		inputMessages = new ArrayList<>();
		outputMessages = new ArrayList<>();
	}

	public App(int _id, int _wcet, List<Integer> _in, List<Integer> _out ) {
		ID = _id;
		WCET = _wcet;
		inputMessages = new ArrayList<>();
		outputMessages = new ArrayList<>();
        inputMessages.addAll(_in);
        outputMessages.addAll(_out);
	}
	
	public App Clone() {
		return new App(ID, WCET, inputMessages, outputMessages);
	}

	public int ID;
	private int WCET;
	List<Integer> inputMessages;
	List<Integer> outputMessages;
	
	public void AddWCET(int wcet) {
		WCET = wcet;
	}
	
	public void AddInMessage(int id) {
		inputMessages.add(id);
	}

	public void AddOutMessage(int id) {
		outputMessages.add(id);
	}

}
