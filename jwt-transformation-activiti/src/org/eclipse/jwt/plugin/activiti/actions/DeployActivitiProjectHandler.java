package org.eclipse.jwt.plugin.activiti.actions;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jwt.generation.activiti.ActivitiGenerationProject;
import org.eclipse.jwt.generation.activiti.ActivitiGenerationProjectFactory;
import org.eclipse.jwt.transformations.activiti.internal.JwtToActivitiTransformationUtility;
import org.eclipse.jwt.we.editors.actions.WEActionHandler;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class DeployActivitiProjectHandler extends WEActionHandler {

	public DeployActivitiProjectHandler() {
		super(false);
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ActivitiGenerationProject activitiProject = ActivitiGenerationProjectFactory.findProject(
				ActivitiGenerationProject.resolveProjectName(JwtToActivitiTransformationUtility.getCurrentJwtModelFileName()));
						
		if(activitiProject != null) 
			activitiProject.createDeploymentArtifacts(null);
			
		return null;
	}

}
