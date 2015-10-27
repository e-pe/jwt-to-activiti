package org.eclipse.jwt.transformations.activiti.util.monitoring;

import java.util.ArrayList;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringEventType {
	public final static String processStart = "{{ processStartExecutionListener }}";
	public final static String processEnd = "{{ processEndExectionListener }}";	
	public final static String sequenceFlowTake = "{{ sequenceFlowTakeExecutionListener }}";
	public final static String serviceTaskStart = "{{ serviceTaskStartExecutionListener }}";
	public final static String serviceTaskEnd = "{{ serviceTaskEndExecutionListener }}";
	public final static String userTaskCreate = "{{ userTaskCreateExecutionListener }}";
	public final static String userTaskAssignment = "{{ userTaskAssignmentExecutionListener }}";
	public final static String userTaskComplete = "{{ userTaskCompleteExecutionListener }}";
	
	/**
	 * 
	 * @return
	 */
	public static ArrayList<String> getAvailableEventTypes() {
		ArrayList<String> eventTypes = new ArrayList<String>();
		eventTypes.add(processStart);
		eventTypes.add(processEnd);
		eventTypes.add(sequenceFlowTake);
		eventTypes.add(serviceTaskStart);
		eventTypes.add(serviceTaskEnd);
		eventTypes.add(userTaskCreate);
		eventTypes.add(userTaskAssignment);
		eventTypes.add(userTaskComplete);
		
		return eventTypes;
	}
	
	/**
	 *
	 */
	public static Boolean isTaskListener(String eventType) {
		if(eventType.equals(userTaskCreate))
			return true;
		else if(eventType.equals(userTaskAssignment))
			return true;
		else if(eventType.equals(userTaskComplete))
			return true;
		
		return false;
	}
}
