package org.eclipse.jwt.transformations.activiti.util.monitoring.output;

import java.util.ArrayList;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringOutputDescriptor {
	private Boolean fetchFromResource;
	private String fetchFromResourceUrl;
	private ArrayList<ActivitiMonitoringOutputData> outputs;
	
	/**
	 * 
	 */
	public ActivitiMonitoringOutputDescriptor() {
		this.outputs = new ArrayList<ActivitiMonitoringOutputData>();
	}
	
	/**
	 * 
	 * @return
	 */
	public Boolean getFetchFromResource() {
		return this.fetchFromResource;
	}
	
	/**
	 * 
	 * @param fetchFromResourceUrl
	 */
	public void setFetchFromResource(Boolean fetchFromResource) {
		this.fetchFromResource = fetchFromResource;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFetchFromResourceUrl() {
		return this.fetchFromResourceUrl;
	}
	
	/**
	 * 
	 * @param fetchFromResourceUrl
	 */
	public void setFetchFromResourceUrl(String fetchFromResourceUrl) {
		this.fetchFromResourceUrl = fetchFromResourceUrl;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<ActivitiMonitoringOutputData> getOutputs() {
		return this.outputs;
	}
}
