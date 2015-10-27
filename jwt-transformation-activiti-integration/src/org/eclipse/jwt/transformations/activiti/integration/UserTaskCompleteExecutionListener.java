package org.eclipse.jwt.transformations.activiti.integration;


import org.eclipse.jwt.transformations.activiti.integration.core.ActivitiIntegrationTaskListener;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.events.ActivitiMonitoringEventPublisher;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringEventType;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class UserTaskCompleteExecutionListener extends ActivitiIntegrationTaskListener {
	
	@Override
	/**
	 * 
	 */
	public String getEventType() {
		return ActivitiMonitoringEventType.userTaskComplete;
	}
	
	@Override
	/**
	 * 
	 */
	public void publishMonitoringEvents(ActivitiMonitoringEventPublisher publisher) {
		
	}
}
