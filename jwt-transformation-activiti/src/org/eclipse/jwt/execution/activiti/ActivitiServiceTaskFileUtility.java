package org.eclipse.jwt.execution.activiti;

import java.util.ArrayList;

import org.eclipse.jwt.core.activiti.IProjectPerformer;
import org.eclipse.jwt.core.activiti.ITemplateRender;
import org.eclipse.jwt.generation.activiti.ActivitiGenerationProject;
import org.eclipse.jwt.generation.activiti.ActivitiGenerationProjectFileTemplateFactory;
import org.eclipse.jwt.transformations.activiti.util.ActivitiGenerationProjectOptions;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiServiceTaskFileUtility {
	/**
	 * 
	 */
	public static String createServiceTaskListeners(ActivitiGenerationProject activitiProject, String outputBpmn, IProjectPerformer performer) {
		ActivitiServiceTaskParser serviceTaskParser = new ActivitiServiceTaskParser();
		
		ArrayList<String> classNames = serviceTaskParser.getServiceTaskClassNames(outputBpmn);
		
		for(int i = 0; i < classNames.size(); i++) {
			final String serviceTaskClassName = classNames.get(i) + ActivitiGenerationProjectOptions.serviceTaskListenerFileName;
			activitiProject.createFileFromTemplate(
				ActivitiGenerationProjectFileTemplateFactory.createServiceTaskFileTemplate(
						serviceTaskClassName + ".java", new ITemplateRender(){
							public String onRender(String template) {
								return String.format(template, ActivitiGenerationProjectOptions.serviceTaskListenersPackageName, serviceTaskClassName);
							}
						}));
		}
		
		return performer.onPerform();
	}	
}
