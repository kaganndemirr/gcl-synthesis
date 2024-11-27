package ktu.kaganndemirr;

import java.nio.channels.NonReadableChannelException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.PrimitiveIterator.OfDouble;

import com.sun.security.auth.NTDomainPrincipal;




public class Switches {
	String Name;
	int Hyperperiod = 1;
	List<Stream> streams =  new ArrayList<Stream>();
	List<Port> ports = new ArrayList<Port>();
	int clockAsync = 0;
	int microtick = 1;
	Map<Integer, Integer> delayTable = new HashMap<Integer, Integer>();

	public Switches(String _name) {
		Name = _name;
		Random rndRandom = new Random();
		//clockAsync =rndRandom.nextInt(10);
		
	}
	public Switches(String _name, int _hyperperiod, int _clock, List<Stream> _s, List<Port> _ports, Map<Integer, Integer> _delayTable) {
		Name = _name;
		Hyperperiod = _hyperperiod;
		clockAsync = _clock;
		for (Stream stream : _s) {
			streams.add(stream.Clone());
		}
		for (Port port : _ports) {
			ports.add(port.Clone());
		}
		addHashTable(_delayTable);
	}

	public void addHashTable(Map<Integer, Integer> inTable) {
		delayTable.clear();
        delayTable.putAll(inTable);
	}

	public void addSteams(Stream s) {
		Hyperperiod = LCM(Hyperperiod, s.Period);
		streams.add(s);
	}

	public int LCM(int a, int b) {
		int lcm = Math.max(a, b);
        while (lcm % a != 0 || lcm % b != 0) {
            ++lcm;
        }
		return lcm;
	}

	public void setGraph(List<String> nodes, List<Integer> Ids) {
		int index = isIncluded(nodes);
		if(index != -1) {
			if (!isExistInPort(nodes.get(index -1))) {
				//System.out.println(nodes.get(index));
				ports.add(new Port(nodes.get(index-1), false, microtick));
			}
			if(!isExistOutPort(nodes.get(index+1))) {
				Port temPort = new Port(nodes.get(index+1), true, microtick);
				for (int _id : Ids) {
					Optional<Stream> s = streams.stream().filter(x -> x.Id == _id).findFirst();
                    s.ifPresent(temPort::AssignStream);
				}			
				ports.add(temPort);
			}else {
				Optional<Port> temPort = ports.stream().filter(x -> (x.connectedTo.equals(nodes.get(index+1)) && x.outPort)).findFirst();
				if(temPort.isPresent()) {
					for (int _id : Ids) {
						Optional<Stream> s = streams.stream().filter(x -> x.Id == _id).findFirst();
                        s.ifPresent(stream -> temPort.get().AssignStream(stream));
					}
				}
			}
			
		}
	}
	public int isIncluded(List<String> nodes) {
		int index = -1;
		for (String node : nodes) {
			if(Name.equals(node)) {
				index = nodes.indexOf(node);
			}
		}
		return index;
	}

	public boolean isExistOutPort(String sideName) {
		Optional<Port> tempPort = ports.stream().filter(x -> (x.connectedTo.equals(sideName) && x.outPort)).findFirst();
        return tempPort.isPresent();
    }

	public boolean isExistInPort(String sideName) {
		Optional<Port> tempPort = ports.stream().filter(x -> (x.connectedTo.equals(sideName)&& !x.outPort)).findFirst();
        return tempPort.isPresent();
    }

	public void initiate() {
		for (Port port : ports) {
			port.initiate();
		}
	}

	public int getNOutPort() {
		int counter =0;
		for (Port port : ports) {
			if(port.outPort) {
				counter++;
			}
		}
		return counter;
	}

	public int getPortIndex(Port p) {
		boolean isOut = p.outPort;
		int index = 0;
		for (Port port : ports) {
			if(port.outPort == isOut) {
				if(port.equals(p)) {
					if(isOut) {
						return index;
					}else {
						return (index+getNOutPort());
					}
					
				}
				index++;
			}
		}
		return -1;
	}

	public Switches Clone() {
		return new Switches(Name, Hyperperiod, clockAsync, streams, ports, delayTable);
	}

}

