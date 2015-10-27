package org.eclipse.jwt.transformations.activiti.util.monitoring.test;

import java.util.ArrayList;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringTestActivation {
	private String model;
	private String modelKey;
	private String modelParamsKey;
	//private Integer numberOfInstances = 0;
	
	private ArrayList<ActivitiMonitoringTestProcessInstanceActivation> processInstanceActivations;
	
	/**
	 * 
	 */
	public ActivitiMonitoringTestActivation(){
		this.processInstanceActivations = new ArrayList<ActivitiMonitoringTestProcessInstanceActivation>();
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<ActivitiMonitoringTestProcessInstanceActivation> getProcessInstanceActivations() {
		return this.processInstanceActivations;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getModel() {
		return this.model;
	}
	
	/**
	 * 
	 * @param model
	 */
	public void setModel(String model) {
		this.model = model;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getModelKey() {
		return this.modelKey;
	}
	
	/**
	 * 
	 * @param modelKey
	 */
	public void setModelKey(String modelKey) {
		this.modelKey = modelKey;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getModelParamsKey() {
		return this.modelParamsKey;
	}
	
	/**
	 * 
	 */
	public void setModelParamsKey(String modelParamsKey) {
		this.modelParamsKey = modelParamsKey;
	}	
}
