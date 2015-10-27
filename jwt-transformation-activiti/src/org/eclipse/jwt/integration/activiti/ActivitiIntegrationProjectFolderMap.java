package org.eclipse.jwt.integration.activiti;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jwt.core.activiti.ActivitiProjectFolderMap;
import org.eclipse.jwt.transformations.activiti.util.ActivitiGenerationProjectOptions;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiIntegrationProjectFolderMap extends ActivitiProjectFolderMap {
	
	/**
	 * 
	 */
	public ActivitiIntegrationProjectFolderMap(IProject project) {
		super(project);
	}
	
	@Override
	/**
	 * 
	 */
	public void load() throws CoreException {
		this.tempClassesFolder = this.project.getFolder(
				ActivitiGenerationProjectOptions.tempClassesFolderPath);
		
		if (this.tempClassesFolder.exists()) {
			this.tempClassesFolder.delete(true, new NullProgressMonitor());
		}

		this.tempClassesFolder.create(true, true, new NullProgressMonitor());
	}
	
	@Override
	/**
	 * 
	 */
	public void dispose() throws CoreException {
		this.tempClassesFolder = this.project.getFolder(
				ActivitiGenerationProjectOptions.tempClassesFolderPath);
		
		if (this.tempClassesFolder.exists()) {
			this.tempClassesFolder.delete(true, new NullProgressMonitor());
		}
	}
}
