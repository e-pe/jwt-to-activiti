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
public class ProcessEndExecutionListener extends ActivitiIntegrationExecutionListener {

	@Override
	/**
	 * 
	 */
	public String getEventType(){
		return ActivitiMonitoringEventType.processEnd;
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
								
				ArrayList<ActivitiMonitoringParamsProcessExecution> afterExecutions = 
						monitoringParams.getAfterExecutions();
				
				for(int i = 0; i < afterExecutions.size(); i++) {
					ActivitiMonitoringParamsProcessExecution afterExecution = afterExecutions.get(i);
					ActivitiMonitoringQueriesEventType eventType = monitoringTypes.findEventTypeByName(
							afterExecution.getEventTypeRef());
					
					publisher.publish(new ActivitiMonitoringEventActivator(eventType));
				}
			}
			
		});
	}
}
