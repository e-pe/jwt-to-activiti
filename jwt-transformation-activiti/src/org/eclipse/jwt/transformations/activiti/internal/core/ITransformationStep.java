package org.eclipse.jwt.transformations.activiti.internal.core;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public interface ITransformationStep {
	/**
	 * 
	 */
	public void onPerform();
	
	/**
	 * 
	 */
	public void onFinish();
}
