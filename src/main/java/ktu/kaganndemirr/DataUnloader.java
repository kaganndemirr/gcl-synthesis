package ktu.kaganndemirr;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

class DataUnloader {
	
	boolean LuxiInterface;
	String defaltDirPath = "Results";
	List<List<Integer>> costValues;
	List<Long> SolutionTimes;
	int hyperperiod = 0;
	int variables = 0;
    String defaultPath = "Results";

    public DataUnloader(){
    	LuxiInterface = true;
		costValues = new ArrayList<>();
		SolutionTimes = new ArrayList<>();

    }

    public void setDirPath(String _path) {
    	defaltDirPath = _path;
    }

    public void CaptureSolution(Solution solution, long Tnow) {
    	if(solution != null) {
        	getCostValues(solution);
        	SolutionTimes.add(Tnow);
        	variables = solution.Variables;
    	}

    }

    public void WriteData(Solution solution, String name, int counter) {
    	if(solution == null) {
    		return;
    	}
    	hyperperiod = solution.Hyperperiod;
		if (!defaultPath.contains(name)) {
    		defaultPath = defaultPath + "/" + name; 
    	}

		String LuxiToolPath = defaultPath + "/LuxiInterface/S_" + counter;


		if (LuxiInterface) {
			UnloadLuxi(solution, LuxiToolPath);
		}

    }

    private void getCostValues(Solution solution) {
        List<Integer> costs = new ArrayList<>(solution.getCosts());
    	costValues.add(costs);
    	System.out.println("Current Cost is: " + costs.get(0) + " , " + costs.get(1));
    }

    private void UnloadLuxi(Solution solution, String DirPath){
    	try {
    		Files.createDirectories(Paths.get(DirPath));
			BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(DirPath + "/historySCHED1.txt"));
			List<HistorySched> historySchedList = new ArrayList<>();
    		for (Switches sw : solution.SW) {
				for (Port port : sw.ports) {
					if(port.outPort) {
						HistorySched historySched = new HistorySched(sw.Name, port.connectedTo, new ArrayList<>());

						List<Integer> frequencyList = new ArrayList<>();
						for(Stream stream: port.AssignedStreams){
							frequencyList.add(stream.N_instances);
						}
						List<Integer> tOpenList = Arrays.stream(port.Topen).boxed().toList();
						List<Integer> tCloseList = Arrays.stream(port.Tclose).boxed().toList();

						List<List<Integer>> openResultList = new ArrayList<>();
						List<List<Integer>> closeResultList = new ArrayList<>();
						int startIndex = 0;

						for(Integer frequency: frequencyList){
							List<Integer> openSublist = tOpenList.subList(startIndex, startIndex + frequency);
							openResultList.add(new ArrayList<>(openSublist));

							List<Integer> closeSublist = tCloseList.subList(startIndex, startIndex + frequency);
							closeResultList.add(new ArrayList<>(closeSublist));
							startIndex += frequency;
						}

						List<GCE> gceList = new ArrayList<>();
						for(int i = 0; i < port.AssignedStreams.size(); i++){
							for(int j = 0; j < openResultList.get(i).size(); j++){
								GCE gce = new GCE(openResultList.get(i).get(j), closeResultList.get(i).get(j), port.AssignedStreams.get(i).name, j);
								gceList.add(gce);
							}
						}
						historySched.setGCEList(gceList);
						historySchedList.add(historySched);
					}
					else {
						if(port.connectedToES){
							HistorySched historySched = new HistorySched(port.connectedTo, sw.Name, new ArrayList<>());
							historySchedList.add(historySched);
						}
					}
				}
			}

			for (HistorySched sched : historySchedList) {
				sched.gceList.sort(Comparator.comparingInt(gce -> gce.tOpen));
			}

			for (HistorySched sched : historySchedList) {
				writer.write(sched.source + "," + sched.target + "\n");
				if (sched.gceList.isEmpty()){
					writer.newLine();
				}
				else {
					for(GCE gce: sched.gceList){
						writer.write(gce.tOpen + "\t" + gce.tClose + "\t" + gce.streamName + "\t" + gce.streamIndex + "\n");
					}
					writer.newLine();
				}

			}

    		writer.write("#");
    		writer.close();

    	} catch (Exception e){
            e.printStackTrace();
        }
    }

}