package org.eclipse.jwt.transformations.activiti.internal.core;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class TransformationElement {
	private Object source;
	private Object target;
	
	/**
	 * 
	 * @param source
	 * @param target
	 */
	public TransformationElement(Object source, Object target) {
		this.source = source;
		this.target = target;
	}
	
	/**
	 * 
	 * @return
	 */
	public Object getSource(){
		return this.source;
	}
	
	/**
	 * 
	 * @return
	 */
	public Object getTarget(){
		return this.target;
	}
}
