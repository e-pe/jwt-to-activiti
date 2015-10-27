package org.eclipse.jwt.monitoring.activiti;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jwt.core.activiti.IProjectPerformer;
import org.eclipse.jwt.core.activiti.ITemplateRender;
import org.eclipse.jwt.generation.activiti.ActivitiGenerationProject;
import org.eclipse.jwt.generation.activiti.ActivitiGenerationProjectFileTemplateFactory;
import org.eclipse.jwt.transformations.activiti.util.ActivitiGenerationProjectOptions;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringConfigSerializer;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringDescriptor;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringEventType;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringListener;
import org.eclipse.jwt.transformations.activiti.util.monitoring.test.ActivitiMonitoringTestConfig;
import org.xml.sax.SAXException;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringFileUtility {
	/**
	 * 
	 * @param activitiProject
	 * @throws XMLStreamException
	 * @throws Exception
	 */
	public static String createMonitoringListeners(ActivitiGenerationProject activitiProject, IProjectPerformer performer) throws XMLStreamException, Exception {
		ActivitiMonitoringConfig config = new ActivitiMonitoringConfigSerializer().deserialize(
				ActivitiMonitoringConfig.getDefaultConfig());
		
		//generates for each event type the specific class
		ArrayList<String> eventTypes = ActivitiMonitoringEventType.getAvailableEventTypes();			
		for(int i = 0; i < eventTypes.size(); i++) {
			String eventType = eventTypes.get(i);
			ActivitiMonitoringDescriptor descriptor = config.findDescriptorByEventType(eventType);
			
			final String monitoringListenerName = generateMonitoringListenerClassName(
					descriptor.getModelElement(), descriptor.getModelElementListenerEvent());
			
			ActivitiMonitoringListener listener = new ActivitiMonitoringListener();
			listener.setListenerClass(ActivitiGenerationProjectOptions.monitoringListenersPackageName + "." + monitoringListenerName);
			
			if(ActivitiMonitoringEventType.isTaskListener(eventType))
				activitiProject.createFileFromTemplate(
						ActivitiGenerationProjectFileTemplateFactory.createMonitoringTaskListenerFileTemplate(monitoringListenerName + ".java", new ITemplateRender(){
							public String onRender(String template) {
								return String.format(template, ActivitiGenerationProjectOptions.monitoringListenersPackageName, monitoringListenerName);
							}
						}));
			else {
				activitiProject.createFileFromTemplate(
						ActivitiGenerationProjectFileTemplateFactory.createMonitoringExecutionListenerFileTemplate(monitoringListenerName + ".java", new ITemplateRender(){
							public String onRender(String template) {
								return String.format(template, ActivitiGenerationProjectOptions.monitoringListenersPackageName, monitoringListenerName);
							}
						}));
			}	
			
			descriptor.getAdditionalListeners().add(listener);
		}
		
		activitiProject.createFileFromTemplate(
				ActivitiGenerationProjectFileTemplateFactory.createMonitoringFileTemplate(
						new ActivitiMonitoringConfigSerializer().serialize(config)));
		
		return performer.onPerform();
	}
	
	/**
	 * 
	 * @param modelElementName
	 * @param modelElementType
	 * @return
	 */
	private static String generateMonitoringListenerClassName(String modelElementName, String modelElementType){
		char[] elementName = modelElementName.toCharArray();
		elementName[0] = Character.toUpperCase(elementName[0]);
		modelElementName = new String(elementName);
		
		char[] elementType = modelElementType.toCharArray();
		elementType[0] = Character.toUpperCase(elementType[0]);
		modelElementType = new String(elementType);
		
		String monitoringListenerName =  
				modelElementName + modelElementType + ActivitiGenerationProjectOptions.monitoringListenerFileName;
				
		return monitoringListenerName;		
	}
	
	/**
	 * 
	 */
	public static String createMonitoringUnitTestFile(final String monitoringUnitTestName, ActivitiGenerationProject activitiProject, IProjectPerformer performer) {		
		final IFolder testResourceFolder = activitiProject.getProject()
				.getFolder(ActivitiGenerationProjectOptions.testResourceFolderPath);
		
		final String monitoringTestConfigPath = testResourceFolder.getRawLocation().toOSString().replaceAll("\\\\", "/") + "/"
				+ ActivitiMonitoringTestConfig.monitoringTestConfigName;
		
		activitiProject.createFileFromTemplate(
				ActivitiGenerationProjectFileTemplateFactory.createMonitoringUnitTestFileTemplate(monitoringUnitTestName, new ITemplateRender(){ 
					public String onRender(String template) {
						return String.format(template, 
								ActivitiGenerationProjectOptions.monitoringUnitTestPackageName, 
								monitoringUnitTestName,
								monitoringTestConfigPath);
					}
		}));
		
		return performer.onPerform();
	}
}
