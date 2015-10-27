package org.eclipse.jwt.transformations.activiti.util.monitoring;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringConfigParser {

	
	/**
	 * 
	 * @param activitiBpmn
	 * @param activitiMonitoring
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 */
	public String parseByType(InputStream activitiBpmn, InputStream activitiMonitoring) throws IOException, ParserConfigurationException, SAXException {
		return this.parse(IOUtils.toString(activitiBpmn), activitiMonitoring, new IActivitiMonitoringConfigParser(){

			public String onParse(String outputBpmn, ActivitiMonitoringDescriptor descriptor) {
				if(descriptor != null && 
						(descriptor.getModelElementListenerType() != null || !descriptor.getModelElementListenerType().equals(""))) {
					String normalizedModelElementListenerType = getNormalizedModelElementListenerType(descriptor.getModelElementListenerType());
					
					return outputBpmn.replaceAll(normalizedModelElementListenerType, descriptor.getModelElementListenerClass());				
				}
				
				return outputBpmn;
			}
			
		});
	}
	
	/**
	 * 
	 * @param activitiBpmn
	 * @param activitiMonitoring
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public String parseByClass(final InputStream activitiBpmn, InputStream activitiMonitoring) throws ParserConfigurationException, SAXException, IOException{
		return this.parse(IOUtils.toString(activitiBpmn), activitiMonitoring, new IActivitiMonitoringConfigParser(){

			public String onParse(String outputBpmn, ActivitiMonitoringDescriptor descriptor) {
				if(descriptor != null && 
						(descriptor.getModelElementListenerClass() != null || !descriptor.getModelElementListenerClass().equals(""))) {
							
					try {
						DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
						factory.setNamespaceAware(true); 
						DocumentBuilder builder = factory.newDocumentBuilder();
						Document doc = builder.parse(new ByteArrayInputStream(outputBpmn.getBytes()));
						
						NamespaceContext ctx = new NamespaceContext() {
			                public String getNamespaceURI(String prefix) {
			                    String uri;
			                    if (prefix.equals("def"))
			                        uri = "http://www.omg.org/spec/BPMN/20100524/MODEL";
			                    else if (prefix.equals("act"))
			                        uri = "http://activiti.org/bpmn";
			                    else
			                        uri = null;
			                    return uri;
			                }
			               
			                // Dummy implementation - not used!
			                public Iterator getPrefixes(String val) {
			                    return null;
			                }
			               
			                // Dummy implementation - not used!
			                public String getPrefix(String uri) {
			                    return null;
			                }
			            };

						
						XPathFactory xpathFactory = XPathFactory.newInstance();
						XPath xpath = xpathFactory.newXPath();
						xpath.setNamespaceContext(ctx);
						XPathExpression expr = xpath.compile(
								getXPathQueryForSelectingClassName(
										descriptor.getModelElement(), 
										descriptor.getModelElementListenerEvent()));
						
						String className = (String)expr.evaluate(doc, XPathConstants.STRING);
																	
						if((className != null && !className.equals("")) && 
								!className.equals(descriptor.getModelElementListenerClass())) {
							
							return outputBpmn.replaceAll(
									className, descriptor.getModelElementListenerClass());
									
						}	
						
					} catch (Exception e) {
						
						e.printStackTrace();
					}
					
				}
				
				return outputBpmn;
			}
			
		});
	}
	
	/**
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * 
	 */
	private String parse(String activitiBpmn, InputStream activitiMonitoring, IActivitiMonitoringConfigParser configParser) throws ParserConfigurationException, SAXException, IOException {
		String outputBpmn = activitiBpmn;
		ArrayList<String> eventTypes = ActivitiMonitoringEventType.getAvailableEventTypes();
		ActivitiMonitoringConfig config = new ActivitiMonitoringConfigSerializer().deserialize(
					ActivitiMonitoringConfig.getDefaultConfig());
		
		ArrayList<ActivitiMonitoringDescriptor> descriptors = config.getDescriptors();
		
		for(int i = 0; i < eventTypes.size(); i++) {
			String modelElementListenerType = eventTypes.get(i);
			ActivitiMonitoringDescriptor descriptor = this.findDescriptorByType(descriptors, modelElementListenerType);
			
			outputBpmn = configParser.onParse(outputBpmn, descriptor);		
		}
		
		return outputBpmn;
	}
	
	/**
	 * 
	 * @return
	 */
	private String getNormalizedModelElementListenerType(String modelElementListenerType) {
		String normalizedPattern = modelElementListenerType.replaceAll("\\{", "\\\\{").replaceAll("\\}", "\\\\}");
		return normalizedPattern;										
	}
	
	/**
	 * 
	 * @return
	 */
	private String getXPathQueryForSelectingClassName(String modelElement, String modelElementListenerEvent) {
		return "//def:"+ modelElement +"/def:extensionElements/act:*[@event='"+ modelElementListenerEvent +"']/@class";
	}
			
	/**
	 * 
	 * @return
	 */
	private ActivitiMonitoringDescriptor findDescriptorByType(ArrayList<ActivitiMonitoringDescriptor> descriptors, String modelElementListenerType) {
		for(int i = 0; i < descriptors.size(); i++) {
			ActivitiMonitoringDescriptor descriptor = descriptors.get(i);
			
			if(descriptor.getModelElementListenerType().equals(modelElementListenerType))
				return descriptor;
		}
		
		return null;
	}
}
