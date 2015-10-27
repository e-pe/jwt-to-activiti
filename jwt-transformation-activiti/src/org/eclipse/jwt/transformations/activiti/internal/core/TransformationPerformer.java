package org.eclipse.jwt.transformations.activiti.internal.core;

import java.util.ArrayList;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class TransformationPerformer {
	private ArrayList<TransformationElement> elements;
	
	/**
	 * 
	 */
	public TransformationPerformer(){
		this.elements = new ArrayList<TransformationElement>();
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<TransformationElement> getElements(){
		return this.elements;
	}
	
	/**
	 * 
	 */
	public void transform(Object source, Object target, ITransformationStep transformationStep){
		this.elements.add(new TransformationElement(source, target));
	
		if(transformationStep != null) {
			transformationStep.onPerform();
			transformationStep.onFinish();
		}
	}
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	public TransformationElement findElementBySource(Object source){
		for(TransformationElement element : this.elements) {
			if(element.getSource().equals(source))
				return element;
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param source
	 * @return
	 */
	public Object findTargetBySource(Object source){
		TransformationElement element = this.findElementBySource(source);
		
		if(element != null)
			return element.getTarget();
		
		return null;
	}
	
	/**
	 * 
	 */
	public void dispose() {
		this.elements.clear();
	}
}
