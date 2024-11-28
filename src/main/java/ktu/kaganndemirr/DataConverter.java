package ktu.kaganndemirr;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


class DataConverter {

	public static void txt2xml(String streamInputPath, String routeInputPath, String outputPath) {
		Map<String, String> messageMap = new HashMap<>();
		Path tempFilePath = Path.of(outputPath); // Geçici dosya

		try (
				var streamLines = Files.lines(Path.of(streamInputPath));
				var routeLines = Files.lines(Path.of(routeInputPath));
				var writer = new FileWriter(tempFilePath.toFile())
		) {
			XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newFactory();
			XMLStreamWriter xmlWriter = xmlOutputFactory.createXMLStreamWriter(writer);

			// XML Belgesini oluştur
			xmlWriter.writeStartDocument("UTF-8", "1.0");
			xmlWriter.writeStartElement("Network");

			// Messages
			xmlWriter.writeStartElement("Messages");
			int messageId = 1;
			for (String line : streamLines.toList()) {
				if (!line.startsWith("#") && !line.isBlank()) {
					String[] parts = line.split(", ");
					xmlWriter.writeStartElement("Message");
					xmlWriter.writeAttribute("id", String.valueOf(messageId));
					xmlWriter.writeAttribute("name", parts[0]);
					xmlWriter.writeAttribute("size", parts[1]);
					xmlWriter.writeAttribute("deadline", String.valueOf((int) Double.parseDouble(parts[2])));
					xmlWriter.writeAttribute("period", String.valueOf((int) Double.parseDouble(parts[5])));
					xmlWriter.writeAttribute("priority", Constant.trafficTypePriorityMap.get(parts[4]));
					xmlWriter.writeAttribute("offset", String.valueOf((int) Double.parseDouble(parts[6])));

					messageMap.put(String.valueOf(messageId), parts[3]);
					xmlWriter.writeEndElement(); // </Message>
					messageId++;
				}
			}
			xmlWriter.writeEndElement(); // </Messages>

			// Routing
			xmlWriter.writeStartElement("Routing");
			int routeId = 1;
			for (String line : routeLines.toList()) {
				if (!line.startsWith("#") && !line.isBlank()) {
					String[] firstParts = line.split(" : ");
					String[] secondParts = firstParts[1].split(" ; ");

					xmlWriter.writeStartElement("Route");
					xmlWriter.writeAttribute("id", String.valueOf(routeId));

					xmlWriter.writeStartElement("Nodes");
					for (int i = 0; i < secondParts.length; i++) {
						String[] nodeParts = secondParts[i].split(",");
						xmlWriter.writeStartElement("node");
						xmlWriter.writeCharacters(nodeParts[0]);
						xmlWriter.writeEndElement(); // </node>
						if (i == secondParts.length - 1) {
							String[] lastNodeParts = nodeParts[1].split(" ");
							xmlWriter.writeStartElement("node");
							xmlWriter.writeCharacters(lastNodeParts[0]);
							xmlWriter.writeEndElement(); // </node>
						}
					}
					xmlWriter.writeEndElement(); // </Nodes>

					xmlWriter.writeStartElement("messages");
					for (var entry : messageMap.entrySet()) {
						if (entry.getValue().equals(firstParts[0])) {
							xmlWriter.writeStartElement("messageID");
							xmlWriter.writeCharacters(entry.getKey());
							xmlWriter.writeEndElement(); // </messageID>
						}
					}
					xmlWriter.writeEndElement(); // </messages>

					xmlWriter.writeEndElement(); // </Route>
					routeId++;
				}
			}
			xmlWriter.writeEndElement(); // </Routing>

			xmlWriter.writeEndElement(); // </Network>
			xmlWriter.writeEndDocument();
			xmlWriter.flush();

			// Girintili XML'e dönüştür
			formatXml(tempFilePath.toString(), outputPath);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void formatXml(String inputPath, String outputPath) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

			DOMSource source = new DOMSource(
					DocumentBuilderFactory.newInstance()
							.newDocumentBuilder()
							.parse(new File(inputPath))
			);
			StreamResult result = new StreamResult(new File(outputPath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
