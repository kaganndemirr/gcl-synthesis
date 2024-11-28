package ktu.kaganndemirr;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

class DataLoader {
	private final List<Messages> messages = new ArrayList<>();
	private final List<Routes> routings = new ArrayList<>();
	private final List<ControlApp> CAs = new ArrayList<>();
	private final List<NetSwitch> SWs = new ArrayList<>();



    // Load method for 
    public void Load(String pathA, String pathB){
        try{
        	String tempPath = "temp.xml";
    		DataConverter.txt2xml(pathA, pathB, tempPath);
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

                    Messages tc = getMessages((Element) nNode);
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

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Messages getMessages(Element nNode) {
        int _id = Integer.parseInt(nNode.getAttribute("id"));
        String name = nNode.getAttribute("name");
        int _deadline = Integer.parseInt(nNode.getAttribute("deadline"));
        int _period = Integer.parseInt(nNode.getAttribute("period"));
        int _size = Integer.parseInt(nNode.getAttribute("size"));
        int _priority = Integer.parseInt(nNode.getAttribute("priority"));
        int _offset = Integer.parseInt(nNode.getAttribute("offset"));
        return new Messages(_id, name, _period,_deadline,_size, _priority, _offset);
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
