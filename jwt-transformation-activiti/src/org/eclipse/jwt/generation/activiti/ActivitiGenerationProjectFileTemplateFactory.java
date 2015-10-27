package org.eclipse.jwt.generation.activiti;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.xml.parsers.ParserConfigurationException;


import org.eclipse.jwt.core.activiti.ITemplateRender;
import org.eclipse.jwt.generation.activiti.templates.ActivitiGenerationProjectExecutionListenerClassTemplate;
import org.eclipse.jwt.generation.activiti.templates.ActivitiGenerationProjectMonitoringUnitTestClassTemplate;
import org.eclipse.jwt.generation.activiti.templates.ActivitiGenerationProjectPomXmlTemplate;
import org.eclipse.jwt.generation.activiti.templates.ActivitiGenerationProjectServiceTaskClassTemplate;
import org.eclipse.jwt.generation.activiti.templates.ActivitiGenerationProjectTaskListenerClassTemplate;
import org.eclipse.jwt.transformations.activiti.util.ActivitiGenerationProjectOptions;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.output.ActivitiMonitoringOutputConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.params.ActivitiMonitoringParamsConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.queries.ActivitiMonitoringQueriesConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.test.ActivitiMonitoringTestConfig;
import org.xml.sax.SAXException;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiGenerationProjectFileTemplateFactory {
	/**
	 * 
	 * @return
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 */
	public static ActivitiGenerationProjectFileTemplate createBpmnFileTemplate(String fileName, String content) throws IOException, ParserConfigurationException, SAXException { 			
		return new ActivitiGenerationProjectFileTemplate(
				ActivitiGenerationProjectOptions.diagramFolderPath + "/" + fileName + ".bpmn", content);
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws Exception 
	 */
	public static ActivitiGenerationProjectFileTemplate createPomFileTemplate() throws Exception {
		ActivitiGenerationProjectPomXmlTemplate pomXml = new ActivitiGenerationProjectPomXmlTemplate();
		
		return new ActivitiGenerationProjectFileTemplate("pom.xml", pomXml.getCode());
	}
	
	/**
	 * 
	 * @return
	 * @throws Exceptions
	 */
	public static ActivitiGenerationProjectFileTemplate createMonitoringFileTemplate() throws Exception {		
		return new ActivitiGenerationProjectFileTemplate(
				ActivitiGenerationProjectOptions.monitoringFolderPath + "/" + ActivitiMonitoringConfig.monitoringConfigName, ActivitiMonitoringConfig.getDefaultConfig());
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static ActivitiGenerationProjectFileTemplate createMonitoringFileTemplate(String content) throws Exception {
		return new ActivitiGenerationProjectFileTemplate(
				ActivitiGenerationProjectOptions.monitoringFolderPath + "/" + ActivitiMonitoringConfig.monitoringConfigName, content);
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static ActivitiGenerationProjectFileTemplate createMonitoringOutputFileTemplate() throws Exception {
		return new ActivitiGenerationProjectFileTemplate(
				ActivitiGenerationProjectOptions.testOutputFolderPath + "/" + ActivitiMonitoringOutputConfig.monitoringOutputConfigName, ActivitiMonitoringOutputConfig.getDefaultConfig());
	}
	
	/**
	 * 
	 * @return
	 */
	public static ActivitiGenerationProjectFileTemplate createMonitoringExecutionListenerFileTemplate(String fileName, ITemplateRender performer) {
		ActivitiGenerationProjectExecutionListenerClassTemplate template = new ActivitiGenerationProjectExecutionListenerClassTemplate();
		
		return new ActivitiGenerationProjectFileTemplate(
				ActivitiGenerationProjectOptions.monitoringListenerJavaFolderPath + "/" + fileName, performer.onRender(template.getCode()));
	}
	
	/**
	 * 
	 * @return
	 */
	public static ActivitiGenerationProjectFileTemplate createMonitoringTaskListenerFileTemplate(String fileName, ITemplateRender performer) {
		ActivitiGenerationProjectTaskListenerClassTemplate template = new ActivitiGenerationProjectTaskListenerClassTemplate();
		
		return new ActivitiGenerationProjectFileTemplate(
				ActivitiGenerationProjectOptions.monitoringListenerJavaFolderPath + "/" + fileName, performer.onRender(template.getCode()));
	}
	
	/**
	 * 
	 * @param fileName
	 * @return
	 */
	public static ActivitiGenerationProjectFileTemplate createServiceTaskFileTemplate(String fileName, ITemplateRender performer) {
		ActivitiGenerationProjectServiceTaskClassTemplate template = new ActivitiGenerationProjectServiceTaskClassTemplate();
		
		return new ActivitiGenerationProjectFileTemplate(
				ActivitiGenerationProjectOptions.serviceTaskJavaFolderPath + "/" + fileName, performer.onRender(template.getCode()));
	}
	
	/**
	 * 
	 * @return
	 */
	public static ActivitiGenerationProjectFileTemplate createMonitoringUnitTestFileTemplate(String fileName, ITemplateRender performer) {
		ActivitiGenerationProjectMonitoringUnitTestClassTemplate template = new ActivitiGenerationProjectMonitoringUnitTestClassTemplate();
		
		return new ActivitiGenerationProjectFileTemplate(
				ActivitiGenerationProjectOptions.testMonitoringUnitTestFolderPath + "/" + fileName + ".java",  performer.onRender(template.getCode()));
	}
	
	/**
	 * 
	 * @return
	 */
	public static ActivitiGenerationProjectFileTemplate createMonitoringTestFileTemplate() {
		return new ActivitiGenerationProjectFileTemplate(
				ActivitiGenerationProjectOptions.testResourceFolderPath + "/" + ActivitiMonitoringTestConfig.monitoringTestConfigName, ActivitiMonitoringTestConfig.getDefaultConfig());
	}
	
	/**
	 * 
	 * @return
	 */
	public static ActivitiGenerationProjectFileTemplate createMonitoringTestFileTemplate(String content) {
		return new ActivitiGenerationProjectFileTemplate(
				ActivitiGenerationProjectOptions.testResourceFolderPath + "/" + ActivitiMonitoringTestConfig.monitoringTestConfigName, content);
	}
	
	/**
	 * 
	 * @return
	 */
	public static ActivitiGenerationProjectFileTemplate createMonitoringParamsFileTemplate() {
		return new ActivitiGenerationProjectFileTemplate(
				ActivitiGenerationProjectOptions.monitoringFolderPath + "/" + ActivitiMonitoringParamsConfig.monitoringParamsConfigName, ActivitiMonitoringParamsConfig.getDefaultConfig());
	}
	
	/**
	 * 
	 * @return
	 */
	public static ActivitiGenerationProjectFileTemplate createMonitoringQueriesFileTemplate() {
		return new ActivitiGenerationProjectFileTemplate(
				ActivitiGenerationProjectOptions.monitoringFolderPath + "/" + ActivitiMonitoringQueriesConfig.monitoringQueriesConfigName, ActivitiMonitoringQueriesConfig.getDefaultConfig());
	}
}
