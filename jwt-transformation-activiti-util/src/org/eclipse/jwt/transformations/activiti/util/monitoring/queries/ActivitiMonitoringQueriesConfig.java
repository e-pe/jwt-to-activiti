package org.eclipse.jwt.transformations.activiti.util.monitoring.queries;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;


/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringQueriesConfig implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7861073454742509987L;
	
	public static String monitoringQueriesConfigName = "activiti.monitoring.queries.cfg.xml";
	
	private ArrayList<ActivitiMonitoringQueriesEventType> eventTypes;
	private ArrayList<ActivitiMonitoringQueriesEventQuery> eventQueries;
	
	/**
	 * 
	 */
	public ActivitiMonitoringQueriesConfig () {
		this.eventTypes = new ArrayList<ActivitiMonitoringQueriesEventType>();
		this.eventQueries = new ArrayList<ActivitiMonitoringQueriesEventQuery>();
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<ActivitiMonitoringQueriesEventType> getEventTypes() {
		return this.eventTypes;
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<ActivitiMonitoringQueriesEventQuery> getEventQueries() {
		return this.eventQueries;
	}
	
	/**
	 * 
	 * @param eventTypeName
	 * @return
	 */
	public ActivitiMonitoringQueriesEventType findEventTypeByName(String eventTypeName) {
		for(int i = 0; i < this.eventTypes.size(); i++) {
			ActivitiMonitoringQueriesEventType eventType = this.eventTypes.get(i);
			
			if(eventType.getName().equals(eventTypeName))
				return eventType;
		}
		
		return null;
	}
	
	/**
	 * 
	 */
	public static ActivitiMonitoringQueriesConfig loadFrom(String configPath) {
		try {
			
			InputStream stream = new FileInputStream(configPath);
			
			ActivitiMonitoringQueriesConfig config = new ActivitiMonitoringQueriesConfigSerializer()
				.deserialize(stream);
			
			stream.close();
			
			return config;
			
		} catch (Exception e) {

		}
		
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public static InputStream getDefaultConfig() {
		return ActivitiMonitoringQueriesConfig.class.getResourceAsStream("/org/eclipse/jwt/transformations/activiti/util/monitoring/resources/" + monitoringQueriesConfigName);
	}
}
