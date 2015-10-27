package org.eclipse.jwt.transformations.activiti.integration;

import org.activiti.engine.delegate.DelegateExecution;
import org.eclipse.jwt.transformations.activiti.integration.core.ActivitiIntegrationExecutionListener;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.events.ActivitiMonitoringEventPublisher;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringEventType;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class SequenceFlowTakeExecutionListener extends ActivitiIntegrationExecutionListener {
	
	@Override
	/**
	 * 
	 */
	public String getEventType() {
		return ActivitiMonitoringEventType.sequenceFlowTake;
	}
	
	@Override
	/**
	 * 
	 */
	public void publishMonitoringEvents(ActivitiMonitoringEventPublisher publisher) {
		
	}
}
