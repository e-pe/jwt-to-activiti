package org.eclipse.jwt.transformations.activiti.integration;

import java.util.ArrayList;

import org.eclipse.jwt.transformations.activiti.integration.core.ActivitiIntegrationExecutionListener;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.events.ActivitiMonitoringEventActivator;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.events.ActivitiMonitoringEventPublisher;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.events.IActivitiMonitoringEventPublication;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringEventType;
import org.eclipse.jwt.transformations.activiti.util.monitoring.params.ActivitiMonitoringParamsProcessDescriptor;
import org.eclipse.jwt.transformations.activiti.util.monitoring.params.ActivitiMonitoringParamsProcessExecution;
import org.eclipse.jwt.transformations.activiti.util.monitoring.queries.ActivitiMonitoringQueriesConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.queries.ActivitiMonitoringQueriesEventType;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ProcessStartExecutionListener extends ActivitiIntegrationExecutionListener {
	
	@Override
	/**
	 * 
	 */
	public String getEventType() {
		return ActivitiMonitoringEventType.processStart;
	}
	
	@Override
	/**
	 * 
	 */
	public void publishMonitoringEvents(final ActivitiMonitoringEventPublisher publisher) {		
		publisher.activate(new IActivitiMonitoringEventPublication() {
			
			@Override
			public void onPerform(
					ActivitiMonitoringParamsProcessDescriptor monitoringParams, 
					ActivitiMonitoringQueriesConfig monitoringTypes) {
								
				ArrayList<ActivitiMonitoringParamsProcessExecution> beforeExecutions = 
						monitoringParams.getBeforeExecutions();
				
				for(int i = 0; i < beforeExecutions.size(); i++) {
					ActivitiMonitoringParamsProcessExecution beforeExecution = beforeExecutions.get(i);
					ActivitiMonitoringQueriesEventType eventType = monitoringTypes.findEventTypeByName(
							beforeExecution.getEventTypeRef());
					
					publisher.publish(new ActivitiMonitoringEventActivator(eventType));
				}
			}
			
		});
	}
}
