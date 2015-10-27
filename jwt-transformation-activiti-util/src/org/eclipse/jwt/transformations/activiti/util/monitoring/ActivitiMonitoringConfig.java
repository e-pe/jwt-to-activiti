package org.eclipse.jwt.transformations.activiti.util.monitoring;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringConfig {
	private ArrayList<ActivitiMonitoringDescriptor> descriptors;
	public static String monitoringConfigName = "activiti.monitoring.cfg.xml";
	
	public static String serviceTaskListenerPackageName = "activiti.extensions.serviceTasks";
	public static String monitoringListenerPackageName = "activiti.extensions.monitoringListeners";
	
	/**
	 * 
	 */
	public ActivitiMonitoringConfig() {
		this.descriptors = new ArrayList<ActivitiMonitoringDescriptor>();
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<ActivitiMonitoringDescriptor> getDescriptors() {
		return this.descriptors;
	}
	
	/**
	 * 
	 * @return
	 */
	public ActivitiMonitoringDescriptor findDescriptorByEventType(String eventType) {
		for(int i = 0; i < this.descriptors.size(); i++) {
			ActivitiMonitoringDescriptor descriptor = this.descriptors.get(i);
			
			if(descriptor.getModelElementListenerType().equals(eventType)) {
				return descriptor;
			}
		}
		
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public static InputStream getDefaultConfig() {
		return ActivitiMonitoringConfig.class.getResourceAsStream("/org/eclipse/jwt/transformations/activiti/util/monitoring/resources/" + monitoringConfigName);
	}
}
