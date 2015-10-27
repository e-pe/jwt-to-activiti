package org.eclipse.jwt.transformations.activiti.util.monitoring;

import java.util.ArrayList;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringDescriptor {
	private String modelElement;
	private String modelElementListenerEvent;
	private String modelElementListenerType;
	private String modelElementListenerClass;		
	private ArrayList<ActivitiMonitoringListener> additionalListeners;
	
	/**
	 * 
	 */
	public ActivitiMonitoringDescriptor() {
		this.additionalListeners = new ArrayList<ActivitiMonitoringListener>();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getModelElement(){
		return this.modelElement;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setModelElement(String value){
		this.modelElement = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getModelElementListenerEvent(){
		return this.modelElementListenerEvent; 
	}
	
	/**
	 * 
	 * @return
	 */
	public void setModelElementListenerEvent(String value){
		this.modelElementListenerEvent = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getModelElementListenerType() {
		return this.modelElementListenerType;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setModelElementListenerType(String value) {
		this.modelElementListenerType = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getModelElementListenerClass(){
		return this.modelElementListenerClass;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setModelElementListenerClass(String value) {
		this.modelElementListenerClass = value;
	}
		
	/**
	 * 
	 * @return
	 */
	public ArrayList<ActivitiMonitoringListener> getAdditionalListeners(){
		return this.additionalListeners;
	}
}
