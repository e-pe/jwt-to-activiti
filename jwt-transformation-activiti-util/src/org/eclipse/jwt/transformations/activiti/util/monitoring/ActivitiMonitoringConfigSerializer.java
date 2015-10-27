package org.eclipse.jwt.transformations.activiti.util.monitoring;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.eclipse.jwt.transformations.activiti.util.IndentingXMLStreamWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class represents the functionality to serialize/deserialize the activiti.monitoring.cfg.xml file.
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringConfigSerializer {
	private static String processTag = "process-config";
	private static String monitoringTag = "process-monitoring";
	private static String monitoringListenerTag = "process-monitoring-listener";
	private static String invokeAdditionalListenerTag = "invoke-additional-listener";
	private static String elementAttr = "element";
	private static String eventAttr = "event";
	private static String typeAttr = "type";
	private static String classAttr = "class";
	
	/**
	 * 
	 * @return
	 * @throws XMLStreamException 
	 * @throws IOException 
	 */
	public String serialize(ActivitiMonitoringConfig config) throws XMLStreamException, IOException {
		String generatedXMLAsString = "";
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
		
		XMLOutputFactory xof = XMLOutputFactory.newInstance();

	    XMLStreamWriter writer = xof.createXMLStreamWriter(byteArrayOutputStream, "UTF-8");
	    IndentingXMLStreamWriter xtw = new IndentingXMLStreamWriter(writer);

	    xtw.writeStartDocument("UTF-8", "1.0");
	    
	    xtw.writeStartElement(processTag);
	    
	    xtw.writeStartElement(monitoringTag);
	    
	    ArrayList<ActivitiMonitoringDescriptor> descriptors = config.getDescriptors();
	    for(int i = 0; i < config.getDescriptors().size(); i++) {
	    	ActivitiMonitoringDescriptor descriptor = descriptors.get(i);
	    	
	    	xtw.writeStartElement(monitoringListenerTag);
	    	xtw.writeAttribute(elementAttr, descriptor.getModelElement());
	    	xtw.writeAttribute(eventAttr, descriptor.getModelElementListenerEvent());
	    	xtw.writeAttribute(typeAttr, descriptor.getModelElementListenerType());
	    	xtw.writeAttribute(classAttr, descriptor.getModelElementListenerClass());
	    	
	    	ArrayList<ActivitiMonitoringListener> listeners = descriptor.getAdditionalListeners();
	    	for(int j = 0; j < listeners.size(); j++) {
	    		ActivitiMonitoringListener listener = listeners.get(j);
	    		
	    		if(!listener.getListenerClass().equals("") && listeners.size() > 1) {
	    			xtw.writeStartElement(invokeAdditionalListenerTag);
	    			xtw.writeAttribute(classAttr, listener.getListenerClass());
	    			xtw.writeEndElement();
	    		}
	    	}
	    	
	    	xtw.writeEndElement();
	    }
	    
	    xtw.writeEndElement();
	    
	    xtw.writeEndElement();
	    
		
	    generatedXMLAsString = new String(byteArrayOutputStream.toString());
	    
	    byteArrayOutputStream.close();
	    
		return generatedXMLAsString;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public ActivitiMonitoringConfig deserialize(InputStream config) throws ParserConfigurationException, SAXException, IOException {
		if(config != null)
			return this.getMonitoringConfig(config);
		
		return null;
	}
	
	/**
	 * 
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private ActivitiMonitoringConfig getMonitoringConfig(InputStream activitiMonitoring) throws ParserConfigurationException, SAXException, IOException{
		ActivitiMonitoringConfig config = new ActivitiMonitoringConfig();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(activitiMonitoring);
		
		doc.getDocumentElement().normalize();
		
		NodeList monitoringList = doc.getElementsByTagName(monitoringTag);
		
		for (int i = 0; i < monitoringList.getLength(); i++) {
			Node monitoringNode = monitoringList.item(i);
			
			if (monitoringNode.getNodeType() == Node.ELEMENT_NODE) {
				Element monitoringElement = (Element) monitoringNode;
				
				NodeList monitoringListenerList = monitoringElement.getElementsByTagName(monitoringListenerTag);

				for(int j = 0; j < monitoringListenerList.getLength(); j++) {
					Node monitoringListenerNode = monitoringListenerList.item(j);
					
					if(monitoringListenerNode.getNodeType() == Node.ELEMENT_NODE) {
						Element monitoringListenerElement = (Element) monitoringListenerNode;
						
						ActivitiMonitoringDescriptor descriptor = new ActivitiMonitoringDescriptor();
						descriptor.setModelElement(monitoringListenerElement.getAttribute(elementAttr));
						descriptor.setModelElementListenerEvent(monitoringListenerElement.getAttribute(eventAttr));
						descriptor.setModelElementListenerType(monitoringListenerElement.getAttribute(typeAttr));
						descriptor.setModelElementListenerClass(monitoringListenerElement.getAttribute(classAttr));
						
						//gets invoke-additional-listeners
						Node invokeAdditionalListener = monitoringListenerElement.getFirstChild();
						
						while(invokeAdditionalListener != null) {
							Node invokeAdditionalListenerNode = invokeAdditionalListener;
							
							if(invokeAdditionalListenerNode.getNodeType() == Node.ELEMENT_NODE) {
								Element invokeAdditionalListenerElement = (Element) invokeAdditionalListenerNode;
								
								ActivitiMonitoringListener listener = new ActivitiMonitoringListener();
								listener.setListenerClass(invokeAdditionalListenerElement.getAttribute(classAttr));
								
								descriptor.getAdditionalListeners().add(listener);
							}
							
							invokeAdditionalListener = invokeAdditionalListener.getNextSibling();
						}
						
						config.getDescriptors().add(descriptor);
					}
				}
			}
		}
		
		return config;
	}
}
