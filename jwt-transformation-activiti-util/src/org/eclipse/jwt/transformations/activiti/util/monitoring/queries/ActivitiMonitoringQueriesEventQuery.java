package org.eclipse.jwt.transformations.activiti.util.monitoring.queries;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringQueriesEventQuery implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1754734730286106954L;
	
	private String name;
	private String text;
	private ArrayList<ActivitiMonitoringQueriesEventQueryIncludedType> includedTypes;
	
	/**
	 * 
	 */
	public ActivitiMonitoringQueriesEventQuery() {
		this.includedTypes = new ArrayList<ActivitiMonitoringQueriesEventQueryIncludedType>();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getText(){
		return this.text;
	}
	
	/**
	 * 
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<ActivitiMonitoringQueriesEventQueryIncludedType> getIncludedTypes() {
		return this.includedTypes;
	}
}
