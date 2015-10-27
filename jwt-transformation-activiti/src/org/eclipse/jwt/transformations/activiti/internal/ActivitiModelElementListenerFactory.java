package org.eclipse.jwt.transformations.activiti.internal;

import java.util.ArrayList;
import java.util.List;

import org.activiti.designer.bpmn2.model.ActivitiListener;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringEventType;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiModelElementListenerFactory {
	/**
	 * 
	 * @return
	 */
	public static List<ActivitiListener> createProcessListeners() {
		ActivitiListener startListener = new ActivitiListener();
		startListener.setEvent("start");
		startListener.setImplementation(ActivitiMonitoringEventType.processStart);
		
		ActivitiListener endListener = new ActivitiListener();
		endListener.setEvent("end");
		endListener.setImplementation(ActivitiMonitoringEventType.processEnd);
		
		List<ActivitiListener> listeners = new ArrayList<ActivitiListener>();
		listeners.add(startListener);
		listeners.add(endListener);
		
		return listeners;
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<ActivitiListener> createSequenceFlowListeners() {
		ActivitiListener takeListener = new ActivitiListener();
		takeListener.setEvent("take");
		takeListener.setImplementation(ActivitiMonitoringEventType.sequenceFlowTake);
		
		List<ActivitiListener> listeners = new ArrayList<ActivitiListener>();
		listeners.add(takeListener);
		
		return listeners;
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<ActivitiListener> createServiceTaskListeners() {
		ActivitiListener startListener = new ActivitiListener();
		startListener.setEvent("start");
		startListener.setImplementation(ActivitiMonitoringEventType.serviceTaskStart);
		
		ActivitiListener endListener = new ActivitiListener();
		endListener.setEvent("end");
		endListener.setImplementation(ActivitiMonitoringEventType.serviceTaskEnd);
		
		List<ActivitiListener> listeners = new ArrayList<ActivitiListener>();
		listeners.add(startListener);
		listeners.add(endListener);
		
		return listeners;
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<ActivitiListener> createUserTaskListeners() {
		ActivitiListener createListener = new ActivitiListener();
		createListener.setEvent("create");
		createListener.setImplementation(ActivitiMonitoringEventType.userTaskCreate);
		
		ActivitiListener assignmentListener = new ActivitiListener();
		assignmentListener.setEvent("assignment");
		assignmentListener.setImplementation(ActivitiMonitoringEventType.userTaskAssignment);
		
		ActivitiListener completeListener = new ActivitiListener();
		completeListener.setEvent("complete");
		completeListener.setImplementation(ActivitiMonitoringEventType.userTaskComplete);
		
		List<ActivitiListener> listeners = new ArrayList<ActivitiListener>();
		listeners.add(createListener);
		listeners.add(assignmentListener);
		listeners.add(completeListener);
		
		return listeners;
	}
}
