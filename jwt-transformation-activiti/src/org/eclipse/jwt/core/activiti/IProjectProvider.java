package org.eclipse.jwt.core.activiti;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public interface IProjectProvider {
	/**
	 * 
	 * @return
	 */
	public String getProjectName();
	
	/**
	 * 
	 * @return
	 */
	public IProject getProject();
	
	/**
	 * 
	 * @return
	 */
	public IJavaProject getJavaProject();
}
