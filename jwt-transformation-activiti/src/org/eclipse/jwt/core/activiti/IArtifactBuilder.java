package org.eclipse.jwt.core.activiti;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public interface IArtifactBuilder<T> {
	/**
	 * 
	 */
	public void onBuild(T parameter);
}
