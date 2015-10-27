package org.eclipse.jwt.transformations.activiti.integration;

import org.eclipse.jwt.transformations.activiti.integration.core.ActivitiIntegrationExecutionListener;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.events.ActivitiMonitoringEventPublisher;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringEventType;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ServiceTaskStartExecutionListener extends ActivitiIntegrationExecutionListener {
	
	@Override
	/**
	 * 
	 */
	public String getEventType() {
		return ActivitiMonitoringEventType.serviceTaskStart;
	}
	
	@Override
	/**
	 * 
	 */
	public void publishMonitoringEvents(ActivitiMonitoringEventPublisher publisher) {
		
	}
}
