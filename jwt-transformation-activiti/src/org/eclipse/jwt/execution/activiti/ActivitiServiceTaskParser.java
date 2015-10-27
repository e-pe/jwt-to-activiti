package org.eclipse.jwt.execution.activiti;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jwt.generation.activiti.ActivitiGenerationProject;
import org.eclipse.jwt.transformations.activiti.util.ActivitiGenerationProjectOptions;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiServiceTaskParser {
	/**
	 * 
	 * @param activitiBpmn
	 * @return
	 */
	public String parseByType(String activitiBpmn) {
		String outputBpmn = activitiBpmn;
		ArrayList<String> classNames = this.getServiceTaskClassNames(activitiBpmn);
		
		for(int i = 0; i < classNames.size(); i++) {
			String className = classNames.get(i);
			outputBpmn = outputBpmn.replaceAll("\\{\\{" + className + "\\}\\}", 
					ActivitiGenerationProjectOptions.serviceTaskListenersPackageName + "."  + className + ActivitiGenerationProjectOptions.serviceTaskListenerFileName);
		}
	
		return outputBpmn;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getServiceTaskClassNames(String activitiBpmn) {
		ArrayList<String> classNames = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");	
		Matcher matcher = pattern.matcher(activitiBpmn);
		
		while (matcher.find()) {
			classNames.add(matcher.group(1));
		}
				
		return classNames;		
	}
}
