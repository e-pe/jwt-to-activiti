package org.eclipse.jwt.transformations.activiti.integration.core;


import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.ActivitiIntegrationExecutionMonitoringInvoker;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.ActivitiIntegrationMonitoringEventTypeProvider;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.ActivitiIntegrationMonitoringPublisherProvider;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.events.ActivitiMonitoringEventPublisher;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public abstract class ActivitiIntegrationExecutionListener implements ExecutionListener, ActivitiIntegrationMonitoringPublisherProvider, ActivitiIntegrationMonitoringEventTypeProvider {

	@Override
	public void notify(DelegateExecution execution) throws Exception {						
		ActivitiMonitoringEventPublisher publisher = 
				new ActivitiMonitoringEventPublisher(execution);
		
		this.publishMonitoringEvents(publisher); 
		
		
		ActivitiIntegrationExecutionMonitoringInvoker invoker = 
				new ActivitiIntegrationExecutionMonitoringInvoker(
						this.getEventType(), ActivitiIntegrationUtility.getProcessDefinitionId(execution));
		
		invoker.invokeListeners(execution);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getEventType() {
		return null;
	}
	
	/**
	 * 
	 */
	public void publishMonitoringEvents(ActivitiMonitoringEventPublisher publisher) {
		
	}	
}
