package ktu.kaganndemirr;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

class DataLoader {
	private GraphExporter graphicExporter = new GraphExporter();
	private List<Messages> messages = new ArrayList<Messages>();
	private List<Routes> routings = new ArrayList<Routes>();
	private List<ControlApp> CAs = new ArrayList<ControlApp>();
	private List<NetSwitch> SWs = new ArrayList<NetSwitch>();
	//Constructor
    public DataLoader(){

    }
    // Load method for single input file
    public void Load(String path){
        try{
            File fXmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
                    
            NodeList messageList = doc.getElementsByTagName("Message");
            NodeList routingList = doc.getElementsByTagName("Route");
            NodeList appList = doc.getElementsByTagName("APP");
            NodeList switchList = doc.getElementsByTagName("Switches");
            
            for (int temp = 0; temp < messageList.getLength(); temp++) {

                Node nNode = messageList.item(temp);
                       
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        
                    Element eElement = (Element) nNode;
                    int _id = Integer.parseInt(eElement.getAttribute("id"));
                    int _deadline = Integer.parseInt(eElement.getAttribute("deadline"));
                    int _period = Integer.parseInt(eElement.getAttribute("period"));
                    int _size = Integer.parseInt(eElement.getAttribute("size"));
                    int _priority = Integer.parseInt(eElement.getAttribute("priority"));
                    int _offset = Integer.parseInt(eElement.getAttribute("offset"));
                    Messages tc = new Messages(_id,_period,_deadline,_size, _priority, _offset);
                    messages.add(tc);
                }
            }
            for (int temp = 0; temp < routingList.getLength(); temp++) {

                Node nNode = routingList.item(temp);
                List<String> nodenameList = new ArrayList<String>();
                List<Integer> IDnameList = new ArrayList<Integer>();
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        
                    Element eElement = (Element) nNode;
                    int _id = Integer.parseInt(eElement.getAttribute("id"));
                    NodeList listednotes = eElement.getElementsByTagName("node");
                    for (int i = 0; i < listednotes.getLength(); i++) {
						nodenameList.add(listednotes.item(i).getTextContent());
					}
                    NodeList listedID = eElement.getElementsByTagName("messageID");
                    for (int i = 0; i < listedID.getLength(); i++) {
                    	IDnameList.add(Integer.parseInt( listedID.item(i).getTextContent()));
					}


                    Routes tc = new Routes(_id,nodenameList,IDnameList);
                    routings.add(tc);  
                }
            }
            for (int i = 0; i < appList.getLength(); i++) {
            	Node nNode = appList.item(i);
            	List<Integer> inIDList = new ArrayList<Integer>();
            	List<Integer> outIDList = new ArrayList<Integer>();
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                	Element eElement = (Element) nNode;
                	int _id = Integer.parseInt(eElement.getAttribute("id"));
                	int wcet = Integer.parseInt(eElement.getAttribute("C"));
                    NodeList listednotes = eElement.getElementsByTagName("in");
                    for (int j = 0; j < listednotes.getLength(); j++) {
                    	inIDList.add(Integer.valueOf(listednotes.item(j).getTextContent()));
					}
                    NodeList listednotes2 = eElement.getElementsByTagName("out");
                    for (int j = 0; j < listednotes2.getLength(); j++) {
                    	outIDList.add(Integer.valueOf(listednotes2.item(j).getTextContent()));
					}
                	ControlApp tc = new ControlApp(_id, wcet, inIDList, outIDList);
                	CAs.add(tc);
                }
			}
            for (int i = 0; i < switchList.getLength(); i++) {
            	Node nNode = switchList.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                	Element eElement = (Element) nNode;
                	String id = eElement.getAttribute("Name");
                	NetSwitch tc = new NetSwitch(id);
                	NodeList delays = eElement.getElementsByTagName("Delay");
                	for (int j = 0; j < delays.getLength(); j++) {
                		Node newNode = delays.item(j);
                		if (newNode.getNodeType() == Node.ELEMENT_NODE) {
                			Element eNewElement = (Element) newNode;
                			int Key = Integer.valueOf(eNewElement.getAttribute("Size"));
                			int Value = Integer.valueOf(eNewElement.getAttribute("Value"));
                			tc.delayTable.put(Key, Value);
						}
					}
                	SWs.add(tc);
                }
			}
            
            CreateReport();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    // Load method for 
    public void Load(String pathA, String pathB){
        try{
        	String tempPath = "remp.xml";
        	DataConverter converter = new DataConverter();
    		converter.txt2xml(pathA, pathB, tempPath);
            File fXmlFile = new File(tempPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
                    
            NodeList messageList = doc.getElementsByTagName("Message");
            NodeList routingList = doc.getElementsByTagName("Route");
            NodeList appList = doc.getElementsByTagName("APP");
            
            for (int temp = 0; temp < messageList.getLength(); temp++) {

                Node nNode = messageList.item(temp);
                       
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        
                    Element eElement = (Element) nNode;
                    int _id = Integer.parseInt(eElement.getAttribute("id"));
                    int _deadline = Integer.parseInt(eElement.getAttribute("deadline"));
                    int _period = Integer.parseInt(eElement.getAttribute("period"));
                    int _size = Integer.parseInt(eElement.getAttribute("size"));
                    int _priority = Integer.parseInt(eElement.getAttribute("priority"));
                    int _offset = Integer.parseInt(eElement.getAttribute("offset"));
                    Messages tc = new Messages(_id,_period,_deadline,_size, _priority, _offset);
                    messages.add(tc);
                }
            }
            for (int temp = 0; temp < routingList.getLength(); temp++) {

                Node nNode = routingList.item(temp);
                List<String> nodenameList = new ArrayList<String>();
                List<Integer> IDnameList = new ArrayList<Integer>();
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        
                    Element eElement = (Element) nNode;
                    int _id = Integer.parseInt(eElement.getAttribute("id"));
                    NodeList listednotes = eElement.getElementsByTagName("node");
                    for (int i = 0; i < listednotes.getLength(); i++) {
						nodenameList.add(listednotes.item(i).getTextContent());
					}
                    NodeList listedID = eElement.getElementsByTagName("messageID");
                    for (int i = 0; i < listedID.getLength(); i++) {
                    	IDnameList.add(Integer.parseInt( listedID.item(i).getTextContent()));
					}


                    Routes tc = new Routes(_id,nodenameList,IDnameList);
                    routings.add(tc);  
                }
            }
            for (int i = 0; i < appList.getLength(); i++) {
            	Node nNode = appList.item(i);
            	List<Integer> inIDList = new ArrayList<Integer>();
            	List<Integer> outIDList = new ArrayList<Integer>();
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                	Element eElement = (Element) nNode;
                	int _id = Integer.parseInt(eElement.getAttribute("id"));
                	int wcet = Integer.parseInt(eElement.getAttribute("C"));
                    NodeList listednotes = eElement.getElementsByTagName("in");
                    for (int j = 0; j < listednotes.getLength(); j++) {
                    	inIDList.add(Integer.valueOf(listednotes.item(i).getTextContent()));
					}
                    NodeList listednotes2 = eElement.getElementsByTagName("out");
                    for (int j = 0; j < listednotes2.getLength(); j++) {
                    	outIDList.add(Integer.valueOf(listednotes2.item(i).getTextContent()));
					}
                	ControlApp tc = new ControlApp(_id, wcet, inIDList, outIDList);
                	CAs.add(tc);
                }
			}
            
            CreateReport();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //Creates Reports relevant to Input
    private void CreateReport() {
    	CreateLuxiInterface();
    	exportNetworkGraph();
    }
    //Creates Graphviz File for Network Graph
    private void exportNetworkGraph() {
    	graphicExporter.Export( messages,routings, "Results");
    }
    //Creates text File for Luxi's Tool
    private void CreateLuxiInterface() {
    	try {
    		int linecounter = 0;
    		String DirPath = "usecases/NetCal/in";
    		Files.createDirectories(Paths.get(DirPath));
    		PrintWriter writer = new PrintWriter(DirPath + "/vls.txt", "UTF-8");
    		for (Routes r : routings) {
    			String routeLink = "" ;
    			if(linecounter !=0) {
    				routeLink = "\n";
    			}
        		routeLink += String.valueOf(r.id) + " :";
        		for (int i = 0; i < (r.nodes.size() - 1); i++) {
        			routeLink += " " + r.nodes.get(i) + "," + r.nodes.get(i+1) + " ;";
				}
        		writer.print(routeLink);
        		linecounter++;
			}
    		writer.close();
    		
    		PrintWriter mwriter = new PrintWriter(DirPath + "/msg.txt", "UTF-8");
    		linecounter = 0;
    		for (Messages m : messages) {
    			String routingID = null;
    			for (Routes r : routings) {
					for (int mId : r.messsageIDs) {
						if(mId == m.id) {
							routingID = String.valueOf(r.id);
						}
					}
				}
    			String mLink ="";
    			if(linecounter != 0) {
    				mLink = "\n";
    			}
    			mLink += String.valueOf(m.id) + ", " + String.valueOf(m.size) + ", " + String.valueOf(m.deadline) + ", ";
    			mLink += routingID + ", TT, " + String.valueOf(m.priority) + ", " + String.valueOf(m.period) + ", 0.0";
    			mwriter.print(mLink);
    			linecounter++;
			}

    		mwriter.close();
    		
    	} catch (Exception e){
            e.printStackTrace();
        }
    }
    // Read Luxi WCDelay file
    public HashMap<Integer, Integer> LoadLuxiReport(String folderPath) {
    	String fileName = folderPath + "/out/WCEndtoEndDelay.txt";
    	HashMap<Integer, Integer> delays = new HashMap<Integer, Integer>();
    	try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            
            while ((line = bufferedReader.readLine()) != null) {
            	String[] parts = line.split(",");
            	int StreamID = (int) ToNumber(parts[0]);
            	String[] parts2 = parts[1].split(":");
            	int converted_D = (int) Math.ceil(ToNumber(parts2[1]));
            	
            	delays.put(StreamID, converted_D);	
			}
            bufferedReader.close();
			
		} catch (Exception e) {
			 //e.printStackTrace();
		}
    	return delays;
    	
    	
    }
    // Convert to Number
    private double ToNumber(String s) {
    	double number = -1;
    	try {
    		number  = Double.parseDouble(s);
			
		} catch (Exception e) {
			//e.printStackTrace();
		}
    	return number;
    }
	public List<Messages> getMessages(){
        return messages;
    }
    public List<Routes> getRoutes(){
    	return routings;
    }
    public List<ControlApp> getApps(){
    	return CAs;
    }
    public List<NetSwitch> getSwitches(){
    	return SWs;
    }
    
}
//Input Messages Class
class Messages{
 Messages(int _id, int _period,int _deadline, int _size, int _priority, int _offset){
     id = _id;
     period = _period;
     deadline = _deadline;
     size = _size;
     priority = _priority;
     offset = _offset;
     
 }
 int id;
 int period;
 int deadline;
 int size;
 int priority;
 int offset;
 public Messages DeepClone(){
     return new Messages(id, period, deadline, size, priority, offset);
 }    
}
//Input Routing Class
class Routes{
	Routes(int _id, List<String> _nodes, List<Integer> _IDs){
		id = _id;
		for (String item : _nodes) {
			nodes.add(item);
		}
		for (Integer item : _IDs) {
			messsageIDs.add(item);
			
		}
		
	}
	int id;
	List<String> nodes = new ArrayList<String>();
	List<Integer> messsageIDs = new ArrayList<Integer>();
	public Routes DeepClone() {
		return new Routes(id , nodes, messsageIDs);
	}
}
//Input Control Applications Class
class ControlApp{
	ControlApp(int _id, int _wcet, List<Integer> _inIDs, List<Integer> _outIDs){
		id = _id;
		wcet = _wcet;
		for (Integer item : _inIDs) {
			inIDs.add(item);
			
		}
		for (Integer item : _outIDs) {
			outIDs.add(item);
			
		}
	}
	int id;
	int wcet;
	List<Integer> inIDs = new ArrayList<Integer>();
	List<Integer> outIDs = new ArrayList<Integer>();
}
class NetSwitch{
	NetSwitch(String name){
		Name = name;
	}
	String Name;
	Map<Integer, Integer> delayTable = new HashMap<Integer, Integer>();
}