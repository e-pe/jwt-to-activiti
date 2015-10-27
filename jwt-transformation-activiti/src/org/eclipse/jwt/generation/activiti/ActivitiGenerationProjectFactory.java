package org.eclipse.jwt.generation.activiti;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiGenerationProjectFactory {
	
	/**
	 * 
	 * @return
	 */
	public static ActivitiGenerationProject createProject(String projectName) throws Exception {	
	    return new ActivitiGenerationProject(projectName).create();
	} 
	
	/**
	 * 
	 * @param projectName
	 * @return
	 */
	public static ActivitiGenerationProject findProject(String projectName) {
		ActivitiGenerationProject activitiProject = new ActivitiGenerationProject(projectName);
		
		if(activitiProject.exists())
			return activitiProject.load();
			
		return null;	
	}
}
