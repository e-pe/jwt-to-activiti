package org.eclipse.jwt.plugin.activiti.actions;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jwt.core.activiti.IProjectPerformer;
import org.eclipse.jwt.execution.activiti.ActivitiServiceTaskFileUtility;
import org.eclipse.jwt.execution.activiti.ActivitiServiceTaskParser;
import org.eclipse.jwt.generation.activiti.ActivitiGenerationProject;
import org.eclipse.jwt.generation.activiti.ActivitiGenerationProjectFactory;
import org.eclipse.jwt.generation.activiti.ActivitiGenerationProjectFileTemplateFactory;
import org.eclipse.jwt.meta.model.core.Model;
import org.eclipse.jwt.monitoring.activiti.ActivitiMonitoringFileUtility;
import org.eclipse.jwt.transformations.activiti.internal.ActivitiMemoryModel;
import org.eclipse.jwt.transformations.activiti.internal.JwtMemoryModel;
import org.eclipse.jwt.transformations.activiti.internal.JwtToActivitiTransformation;
import org.eclipse.jwt.transformations.activiti.internal.JwtToActivitiTransformationUtility;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringConfigParser;
import org.eclipse.jwt.we.editors.WEEditor;
import org.eclipse.jwt.we.editors.actions.WEActionHandler;
import org.eclipse.jwt.we.misc.util.GeneralHelper;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.xml.sax.SAXException;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class GenerateActivitiProjectHandler extends WEActionHandler {

	/**
	 * 
	 */
	public GenerateActivitiProjectHandler() {
		super(false);
	}

	/**
	 * 
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			ActivitiGenerationProject activitiProject = ActivitiGenerationProjectFactory.createProject(getInputValueFromDialog());
						
			final String modelFileName = JwtToActivitiTransformationUtility.getCurrentJwtModelFileName();
			final String modelAsBpmn = JwtToActivitiTransformationUtility.getCurrentJwtModelAsBpmn();
			
			activitiProject.createFileFromTemplate(
					ActivitiGenerationProjectFileTemplateFactory.createPomFileTemplate());
			
			activitiProject.createFileFromTemplate(
					ActivitiGenerationProjectFileTemplateFactory.createMonitoringParamsFileTemplate());
			
			activitiProject.createFileFromTemplate(
					ActivitiGenerationProjectFileTemplateFactory.createMonitoringQueriesFileTemplate());
		
			final String outputBpmnWithMonitoring = ActivitiMonitoringFileUtility.createMonitoringListeners(activitiProject, new IProjectPerformer(){
				public String onPerform()  {					
					try {
						ActivitiMonitoringConfigParser configParser = new ActivitiMonitoringConfigParser();
						return configParser.parseByType(new ByteArrayInputStream(modelAsBpmn.getBytes()), 
								ActivitiMonitoringConfig.getDefaultConfig());
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					return null; 
				}
			});	
			
			final String outputBpmnWithSeviceTasks = ActivitiServiceTaskFileUtility.createServiceTaskListeners(activitiProject, outputBpmnWithMonitoring, new IProjectPerformer(){
				public String onPerform() {
					ActivitiServiceTaskParser serviceTaskParser = new ActivitiServiceTaskParser();
			        return serviceTaskParser.parseByType(outputBpmnWithMonitoring);
				}
			});
			
			activitiProject.createFileFromTemplate(
					ActivitiGenerationProjectFileTemplateFactory.createBpmnFileTemplate(
							modelFileName, outputBpmnWithSeviceTasks));	
					
						
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	private String getInputValueFromDialog() {
		//String init_value = "project_" + System.currentTimeMillis();
		String init_value = ActivitiGenerationProject.resolveProjectName(
				JwtToActivitiTransformationUtility.getCurrentJwtModelFileName());
	    
		InputDialog dialog = new InputDialog(
	    		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
	    		"Java Project", "Provide a project name ..", init_value, 
	    		new IInputValidator() {
			        public String isValid(String newText)
			        {
//			            char[] array = newText.toCharArray();
//			            for (int i = 0; i < array.length; i++)
//			            {
//			                if(!Character.isJavaIdentifierPart(array[i]))
//			                    return "Cannot contain special characters!";
//			            }
			 
			            IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			            IProject[] projs = root.getProjects();
			            for (int i = 0; i < projs.length; i++) {
			                if(projs[i].getName().equalsIgnoreCase(newText))
			                    return "Project already exist!";
			            }
			 
			            /*
			                Not checking for special Win32 names like con etc.
			            */
			            return null;
			        }
	    });
	 
	    if(dialog.open() == Dialog.CANCEL)
	        return null;
	    
	    return dialog.getValue();
	}
}
