package org.eclipse.jwt.transformations.activiti.util.monitoring.queries;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringQueriesEventType implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2135079257115299640L;
	
	private String name;
	private ArrayList<ActivitiMonitoringQueriesEventTypeField> fields;
	
	/**
	 * 
	 */
	public ActivitiMonitoringQueriesEventType() {
		this.fields = new ArrayList<ActivitiMonitoringQueriesEventTypeField>();
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
	public ArrayList<ActivitiMonitoringQueriesEventTypeField> getFields() {
		return this.fields;
	}
}
