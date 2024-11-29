package ktu.kaganndemirr;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class Solution {
	public Solution(DataLoader dataLoader) {
        streams = new ArrayList<>();
        ES = new ArrayList<>();
        SW = new ArrayList<>();
        costValues = new ArrayList<>();
        Apps = new ArrayList<>();
        Create(dataLoader.getMessages(), dataLoader.getRoutes(), dataLoader.getApps(), dataLoader.getSwitches());
        Initialize();
	}
	public Solution(List<Stream> _streams, List<EndSystems> _es, List<Switches> _sw, List<App> _apps, List<Long> _costs, int _hyperperiod, int _variables) {
        streams = new ArrayList<>();
        ES = new ArrayList<>();
        SW = new ArrayList<>();
        Apps = new ArrayList<>();
        costValues = new ArrayList<>();
        Variables = _variables;

        costValues.addAll(_costs);
        for (Stream s : _streams) {
			streams.add(s.Clone());
		}
        for (EndSystems es : _es) {
			ES.add(es.Clone());
		}
        for (Switches sw : _sw) {
			SW.add(sw.Clone());
		}
        for (App _app : _apps) {
			Apps.add(_app.Clone());
		}
        Hyperperiod = _hyperperiod;
	}
    
	public List<Stream> streams;
    public List<EndSystems> ES;
    public List<Switches> SW;
    public List<App> Apps;
    public List<Long> costValues;  
    public int Hyperperiod = 1;
    public int Variables = 0;
    
    private void Create(List<Messages> _messages, List<Routes> routes, List<ControlApp> CAs, List<NetSwitch> SWs){

        for (Messages item : _messages) {
            streams.add(new Stream(item.id, item.name, item.period, item.deadline, item.size, item.priority, item.offset));
        }
        for (Routes item : routes) {
        	
        	setSourceES(item);
        	setSinkES(item);
        	for (int i = 1; i < (item.nodes.size()-1); i++) {
        		String name = item.nodes.get(i);
        		Optional<Switches> tempSwitch = SW.stream().filter(x -> x.Name.equals(name)).findFirst();
        		Switches crrSwitches;
        		if (tempSwitch.isEmpty()) {
        			crrSwitches = new Switches(name);
					SW.add(crrSwitches);
				}else {
					crrSwitches = tempSwitch.get();
				}
        		
        		for ( int mID : item.messsageIDs) {
        			Optional<Stream> tempStream = streams.stream().filter(x -> (x.Id == mID)).findFirst();
					Stream mStream = tempStream.get();
					crrSwitches.addStreams(mStream);
				}
        		
			}
		}
        for (Switches sw : SW) {
        	for (Routes route : routes) {
				sw.setGraph(route.nodes, route.messsageIDs);
			}
			
		}
        for (Routes routes2 : routes) {
			for (int ID : routes2.messsageIDs) {
				for (Stream s : streams) {
					if(s.Id == ID) {
						s.SetRouting(routes2.nodes);
					}
				}
			}
		}
        
        for (ControlApp controlApp : CAs) {
        	App tc = new App(controlApp.id);
        	tc.AddWCET(controlApp.wcet);
        	for (int id : controlApp.inIDs) {
				tc.AddInMessage(id);
			}
        	for (int id : controlApp.outIDs) {
				tc.AddOutMessage(id);
			}
        	Apps.add(tc);
		}
        for (NetSwitch netSwitch : SWs) {
    		Optional<Switches> tempSwitch = SW.stream().filter(x -> x.Name.equals(netSwitch.Name)).findFirst();

    		if (tempSwitch.isPresent()) {
    			Switches crrSwitches = tempSwitch.get();
    			crrSwitches.addHashTable(netSwitch.delayTable);
			}
    		
		}
        
    }

    private void setSourceES(Routes item) {
    	Optional<EndSystems> tempSourceEndSystems = ES.stream().filter(x -> x.Name.equals(item.nodes.getFirst())).findFirst();
    	if(tempSourceEndSystems.isEmpty()) {
    		EndSystems temp = new EndSystems(item.nodes.getFirst());
    		for (int ID : item.messsageIDs) {
    			temp.addOutID(ID);
			}
    		ES.add(temp);
    	}else {
    		for (int ID : item.messsageIDs) {
				tempSourceEndSystems.get().addOutID(ID);
			}
    		
    	}
    }

    private void setSinkES(Routes item) {
    	int NodeSize = item.nodes.size();
    	Optional<EndSystems> tempSinkEndSystems = ES.stream().filter(x -> x.Name.equals(item.nodes.get(NodeSize-1))).findFirst();
    	if(tempSinkEndSystems.isEmpty()) {
    		EndSystems temp = new EndSystems(item.nodes.get(NodeSize-1));
    		for (int ID : item.messsageIDs) {
    			temp.addInID(ID);
			}
    		ES.add(temp);
    	}else {
    		for (int ID : item.messsageIDs) {
    			tempSinkEndSystems.get().addInID(ID);
			}
    	}
    }

    private void Initialize(){
    	for (Stream s : streams) {
    		Hyperperiod = LCM(Hyperperiod, s.Period);
		}
    	for (Stream s : streams) {
    		s.initiate(Hyperperiod);
		}
    	for (Switches sw : SW) {
			
			sw.initiate();
		}
    }

	// LCM (Least Common Multiple) hesaplama fonksiyonu
	public static int LCM(int a, int b) {
		// Checking for the largest
		// Number between them
		int ans = Math.max(a, b);

		// Checking for a smallest number that
		// can de divided by both numbers
        while (ans % a != 0 || ans % b != 0) {
            ans++;
        }

		return ans;
	}

    public int getNOutPorts() {
    	int Outports = 0;
		for (Switches sw : SW) {
			for (Port port : sw.ports) {
				if(port.outPort) {
					Outports++;
				}
			}
		}
		return Outports;
    }


    public List<String> getOutPorts(){
    	List<String> nameStrings = new ArrayList<>();
		for (Switches sw : SW) {
			for (Port port : sw.ports) {
				if(port.outPort) {
					nameStrings.add("[" + sw.Name + " : " + port.connectedTo + "]");
				}
			}
		}
		return nameStrings;
    }

    public Solution Clone() {
    	return new Solution(streams, ES, SW, Apps, costValues, (Hyperperiod), Variables);
    }

    public List<Integer> getCosts() {
    	List<Integer> CostTerms = new ArrayList<>();
    	for (Long temp : costValues) {
    		CostTerms.add(temp.intValue());
		}
    	return CostTerms;
    }

}
