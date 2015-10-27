package org.eclipse.jwt.transformations.activiti.util.monitoring.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.eclipse.jwt.transformations.activiti.util.IndentingXMLStreamWriter;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringDescriptor;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringListener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringTestConfigSerializer {
	private static String processTag = "process-config";
	private static String monitoringTestTag = "process-monitoring-test";
	private static String monitoringActivationTag = "process-monitoring-activation";
	private static String instanceActivationTag = "process-instance-activation";
	private static String initialActivationTag = "initial-activation";
	private static String userTaskActivationTag = "user-task-activation";
	private static String addTag = "add";
	
	private static String activatedAttr = "activated";
	private static String classAttr = "class";
	
	private static String outputPathAttr = "outputPath";
	private static String paramsPathAttr = "paramsPath";
	private static String queriesPathAttr = "queriesPath";
	
	private static String modelAttr = "model";
	private static String modelKeyAttr = "modelKey";
	private static String modelParamsRefAttr = "modelParamsRef";
	
	private static String numberOfCurrentTypeAttr = "numberOfCurrentType";
	
	private static String keyAttr = "key";
	private static String valueAttr = "value";
	
	
	
	/**
	 * 
	 * @return
	 * @throws XMLStreamException 
	 * @throws IOException 
	 */
	public String serialize(ActivitiMonitoringTestConfig config) throws XMLStreamException, IOException {
		String generatedXMLAsString = "";
		
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
		
		XMLOutputFactory xof = XMLOutputFactory.newInstance();

	    XMLStreamWriter writer = xof.createXMLStreamWriter(byteArrayOutputStream, "UTF-8");
	    IndentingXMLStreamWriter xtw = new IndentingXMLStreamWriter(writer);

	    xtw.writeStartDocument("UTF-8", "1.0");
	    
	    xtw.writeStartElement(processTag);
	    
	    ArrayList<ActivitiMonitoringTestDescriptor> descriptors = config.getDescriptors();
	    for(int i = 0; i < config.getDescriptors().size(); i++) {
	    	ActivitiMonitoringTestDescriptor descriptor = descriptors.get(i);
	    	
	    	xtw.writeStartElement(monitoringTestTag);
	    	xtw.writeAttribute(classAttr, descriptor.getTestClass());
	    	xtw.writeAttribute(outputPathAttr, descriptor.getTestOutputPath());
	    	xtw.writeAttribute(paramsPathAttr, descriptor.getTestParamsPath());
	    	xtw.writeAttribute(queriesPathAttr, descriptor.getTestQueriesPath());
	    	
	    	
	    	ArrayList<ActivitiMonitoringTestActivation> activations = descriptor.getActivations();
	    	for(int j = 0; j < activations.size(); j++) {
	    		ActivitiMonitoringTestActivation activation = activations.get(j);
	    		
	    		xtw.writeStartElement(monitoringActivationTag);
		    	xtw.writeAttribute(modelAttr, activation.getModel());
		    	xtw.writeAttribute(modelKeyAttr, activation.getModelKey());
		    	xtw.writeAttribute(modelParamsRefAttr, activation.getModelParamsKey());
		    	//xtw.writeAttribute(numberOfInstancesAttr, activation.getNumberOfInstances().toString());
		    	
		    	ArrayList<ActivitiMonitoringTestProcessInstanceActivation> instanceActivations = 
		    			activation.getProcessInstanceActivations();
		    	
		    	for(int k = 0; k < instanceActivations.size(); k++) {
		    		ActivitiMonitoringTestProcessInstanceActivation instanceActivation = instanceActivations.get(k);
		    		
		    		xtw.writeStartElement(instanceActivationTag);
		    		xtw.writeAttribute(numberOfCurrentTypeAttr, instanceActivation.getNumberOfCurrentType().toString());
		    		
		    		
		    		xtw.writeStartElement(initialActivationTag);
		    		
		    		ArrayList<ActivitiMonitoringTestProcessInstanceInitialActivation> initialActivations = instanceActivation.getInitialActivations();
		    		for(int l = 0; l < initialActivations.size(); l++) {
		    			ActivitiMonitoringTestProcessInstanceInitialActivation initialActivation = initialActivations.get(l);
		    			
		    			xtw.writeStartElement(addTag);
			    		xtw.writeAttribute(numberOfCurrentTypeAttr, "");
			    		xtw.writeEndElement();
		    		}
		    		
		    		xtw.writeEndElement();
		    		
		    		
		    		xtw.writeStartElement(userTaskActivationTag);
		    		
		    		ArrayList<ActivitiMonitoringTestProcessInstanceUserTaskActivation> userTaskActivations = instanceActivation.getUserTaskActivations();
		    		for(int m = 0; m < userTaskActivations.size(); m++) {
		    			ActivitiMonitoringTestProcessInstanceUserTaskActivation userTaskActivation = userTaskActivations.get(m);
		    			
		    			xtw.writeStartElement(addTag);
			    		xtw.writeAttribute(numberOfCurrentTypeAttr, "");
			    		xtw.writeEndElement();
		    		}
		    		
		    		xtw.writeEndElement();
		    		
		    		
		    		xtw.writeEndElement();
		    	}
		    	
		    	xtw.writeEndElement();
	    	}	
	    	
	    	xtw.writeEndElement();
	    }
	    
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
	public ActivitiMonitoringTestConfig deserialize(InputStream config) throws ParserConfigurationException, SAXException, IOException {
		if(config != null)
			return this.getMonitoringTestConfig(config);
		
		return null;
	}
	
	/**
	 * 
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private ActivitiMonitoringTestConfig getMonitoringTestConfig(InputStream activitiMonitoring) throws ParserConfigurationException, SAXException, IOException{
		ActivitiMonitoringTestConfig config = new ActivitiMonitoringTestConfig();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(activitiMonitoring);
		
		doc.getDocumentElement().normalize();
		
		NodeList monitoringTestList = doc.getElementsByTagName(monitoringTestTag);
		
		for (int i = 0; i < monitoringTestList.getLength(); i++) {
			Node monitoringTestNode = monitoringTestList.item(i);
			
			if (monitoringTestNode.getNodeType() == Node.ELEMENT_NODE) {
				Element monitoringTestElement = (Element) monitoringTestNode;
				
				ActivitiMonitoringTestDescriptor descriptor = new ActivitiMonitoringTestDescriptor();
				descriptor.setTestClass(monitoringTestElement.getAttribute(classAttr));
				descriptor.setTestOutputPath(monitoringTestElement.getAttribute(outputPathAttr));
				descriptor.setTestParamsPath(monitoringTestElement.getAttribute(paramsPathAttr));
				descriptor.setTestQueriesPath(monitoringTestElement.getAttribute(queriesPathAttr));
				
				config.getDescriptors().add(descriptor);
				
				NodeList monitoringActivationList = monitoringTestElement.getElementsByTagName(monitoringActivationTag);

				for(int j = 0; j < monitoringActivationList.getLength(); j++) {
					Node monitoringActivationNode = monitoringActivationList.item(j);
					
					if(monitoringActivationNode.getNodeType() == Node.ELEMENT_NODE) {
						Element monitoringActivationElement = (Element) monitoringActivationNode;
						
						ActivitiMonitoringTestActivation activation = new ActivitiMonitoringTestActivation();
						activation.setModel(monitoringActivationElement.getAttribute(modelAttr));
						activation.setModelKey(monitoringActivationElement.getAttribute(modelKeyAttr));
						activation.setModelParamsKey(monitoringActivationElement.getAttribute(modelParamsRefAttr));
						
						descriptor.getActivations().add(activation);
						
						NodeList instanceActivationList = monitoringActivationElement.getElementsByTagName(instanceActivationTag);
						
						for(int k = 0; k < instanceActivationList.getLength(); k++) {
							Node instanceActivationNode = instanceActivationList.item(k);
							
							if(instanceActivationNode.getNodeType() == Node.ELEMENT_NODE) {
								Element instanceActivationElement = (Element) instanceActivationNode;
								
								ActivitiMonitoringTestProcessInstanceActivation instanceActivation = new ActivitiMonitoringTestProcessInstanceActivation();
								instanceActivation.setNumberOfCurrentType(
										Integer.parseInt(instanceActivationElement.getAttribute(numberOfCurrentTypeAttr)));
								
								activation.getProcessInstanceActivations().add(instanceActivation);
								
								NodeList initialActivationList = instanceActivationElement.getChildNodes()/*.getElementsByTagName(initialActivationTag)*/;
								
								
								for(int l = 0; l < initialActivationList.getLength(); l++) {
									Node initialActivationNode = initialActivationList.item(l);
									
									if(initialActivationNode.getNodeType() == Node.ELEMENT_NODE) {
										Element initialActivationElement = (Element) initialActivationNode;
										
										if(initialActivationElement.getTagName().equals(initialActivationTag)) {
											
											NodeList addList = initialActivationElement.getElementsByTagName(addTag);
											
											for(int n = 0; n < addList.getLength(); n++) {
												Node addNode = addList.item(n);
												
												if(addNode.getNodeType() == Node.ELEMENT_NODE) {
													Element addElement = (Element) addNode;
													
													ActivitiMonitoringTestProcessInstanceInitialActivation initialActivation = 
															new ActivitiMonitoringTestProcessInstanceInitialActivation(); 
													
													initialActivation.setKey(addElement.getAttribute(keyAttr));
													initialActivation.setValue(addElement.getAttribute(valueAttr));
													
													instanceActivation.getInitialActivations().add(initialActivation);
												}
											}										
										}
									}
								}
								
								
								NodeList userTaskActivationList = instanceActivationElement.getChildNodes();
								
								for(int m = 0; m < userTaskActivationList.getLength(); m++) {
									Node userTaskActivationNode = initialActivationList.item(m);
									
									if(userTaskActivationNode.getNodeType() == Node.ELEMENT_NODE) {
										Element userTaskActivationElement = (Element) userTaskActivationNode;
										
										if(userTaskActivationElement.getTagName().equals(userTaskActivationTag)) {
											
											NodeList addList = userTaskActivationElement.getElementsByTagName(addTag);
											
											for(int n = 0; n < addList.getLength(); n++) {
												Node addNode = addList.item(n);
												
												if(addNode.getNodeType() == Node.ELEMENT_NODE) {
													Element addElement = (Element) addNode;
													
													ActivitiMonitoringTestProcessInstanceUserTaskActivation userTaskActivation = 
															new ActivitiMonitoringTestProcessInstanceUserTaskActivation();
													
													instanceActivation.getUserTaskActivations().add(userTaskActivation);
												}
											}
										}
									}
								}
								
							}
						}						
					}
				}
			}
		}
		
		return config;
	}
}

