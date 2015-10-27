package org.eclipse.jwt.transformations.activiti.integration.core;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.ActivitiIntegrationMonitoringEventTypeProvider;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.ActivitiIntegrationMonitoringPublisherProvider;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.ActivitiIntegrationTaskMonitoringInvoker;
import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.events.ActivitiMonitoringEventPublisher;


/**
 * 
 * @author Eugen Petrosean
 *
 */
public abstract class ActivitiIntegrationTaskListener implements TaskListener, ActivitiIntegrationMonitoringPublisherProvider, ActivitiIntegrationMonitoringEventTypeProvider {
	
	@Override
	public void notify(DelegateTask task) {
		ActivitiMonitoringEventPublisher publisher = 
				new ActivitiMonitoringEventPublisher(task.getExecution());
		
		this.publishMonitoringEvents(publisher);

		ActivitiIntegrationTaskMonitoringInvoker invoker = 
				new ActivitiIntegrationTaskMonitoringInvoker(
						this.getEventType(), ActivitiIntegrationUtility.getProcessDefinitionId(task));
		
		invoker.invokeListeners(task);
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
