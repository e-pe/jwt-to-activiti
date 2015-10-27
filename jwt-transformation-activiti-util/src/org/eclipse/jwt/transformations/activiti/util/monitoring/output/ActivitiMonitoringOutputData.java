package org.eclipse.jwt.transformations.activiti.util.monitoring.output;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringOutputData {
	private String key;
	private String value;
	
	/**
	 * 
	 * @return
	 */
	public String getKey() {
		return this.key;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
