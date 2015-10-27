package org.eclipse.jwt.generation.activiti;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jwt.core.activiti.ActivitiProjectFolderMap;
import org.eclipse.jwt.transformations.activiti.util.ActivitiGenerationProjectOptions;

public class ActivitiGenerationProjectFolderMap extends ActivitiProjectFolderMap {	
	private IFolder tempBarFolder = null;
	private IFolder deploymentFolder = null;
	
	/**
	 * 
	 * @param project
	 */
	public ActivitiGenerationProjectFolderMap(IProject project) {
		super(project);
	}
	
	/**
	 * 
	 * @return
	 */
	public IFolder getTempBarFolder() {
		return this.tempBarFolder;
	}
	
	/**
	 * 
	 * @return
	 */
	public IFolder getDeploymentFolder() {
		return this.deploymentFolder;
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
		
		
		this.tempBarFolder = this.project.getFolder(
				ActivitiGenerationProjectOptions.tempBarFolderPath);
		
		if (this.tempBarFolder.exists()) {
			this.tempBarFolder.delete(true, new NullProgressMonitor());
		}

		this.tempBarFolder.create(true, true, new NullProgressMonitor());
		
		
		this.deploymentFolder = this.project.getFolder(
				ActivitiGenerationProjectOptions.deploymentFolderPath);
		
		if (this.deploymentFolder.exists()) {		
			this.deploymentFolder.delete(true, new NullProgressMonitor());			
		}

		this.deploymentFolder.create(true, true, new NullProgressMonitor());
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
		
		
		this.tempBarFolder = this.project.getFolder(
				ActivitiGenerationProjectOptions.tempBarFolderPath);
		
		if (this.tempBarFolder.exists()) {
			this.tempBarFolder.delete(true, new NullProgressMonitor());
		}
				
        //refresh the output folder to reflect changes
        this.deploymentFolder.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
	}
}
