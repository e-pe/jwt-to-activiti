package org.eclipse.jwt.core.activiti;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public abstract class ActivitiProjectFolderMap {
	protected IProject project = null;
	
	protected IFolder tempClassesFolder;
	
	/**
	 * 
	 */
	public ActivitiProjectFolderMap(IProject project) {
		this.project = project;
	}
	
	/**
	 * 
	 * @return
	 */
	public IFolder getTempClassesFolder() {
		return this.tempClassesFolder;
	}
	
	/**
	 * 
	 */
	public abstract void load() throws CoreException;
	
	/**
	 * 
	 */
	public abstract void dispose() throws CoreException;
}
