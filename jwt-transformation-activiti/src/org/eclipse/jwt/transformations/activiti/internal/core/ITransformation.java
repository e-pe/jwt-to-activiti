package org.eclipse.jwt.transformations.activiti.internal.core;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public interface ITransformation {
	/**
	 * 
	 */
	public void onTransformModel(TransformationPerformer performer);
	
	/**
	 * 
	 */
	public void onTransformDiagramInterchange(TransformationPerformer performer);
}
