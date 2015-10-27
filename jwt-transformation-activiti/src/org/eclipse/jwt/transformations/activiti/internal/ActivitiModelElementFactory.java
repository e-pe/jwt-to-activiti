package org.eclipse.jwt.transformations.activiti.internal;

import org.activiti.designer.bpmn2.model.CallActivity;
import org.activiti.designer.bpmn2.model.EndEvent;
import org.activiti.designer.bpmn2.model.ExclusiveGateway;
import org.activiti.designer.bpmn2.model.ParallelGateway;
import org.activiti.designer.bpmn2.model.Process;
import org.activiti.designer.bpmn2.model.ScriptTask;
import org.activiti.designer.bpmn2.model.SequenceFlow;
import org.activiti.designer.bpmn2.model.ServiceTask;
import org.activiti.designer.bpmn2.model.StartEvent;
import org.activiti.designer.bpmn2.model.SubProcess;
import org.activiti.designer.bpmn2.model.UserTask;
import org.eclipse.jwt.transformations.activiti.internal.utils.UniqueIdUtility;

public class ActivitiModelElementFactory {
	
	/**
	 * 
	 */
	public ActivitiModelElementFactory() {

	}
		
	/**
	 * 
	 * @param model
	 * @return
	 */
	public static Process createProcess(){
		Process process = new Process();
		process.setId(UniqueIdUtility.generateId("processId"));
		process.setName(UniqueIdUtility.generateId("processName"));	
		process.getExecutionListeners().addAll(
				ActivitiModelElementListenerFactory.createProcessListeners());
		
		return process;
	}
	
	/**
	 * 
	 * @param process
	 * @return
	 */
	public static StartEvent createStartEvent(){
		StartEvent startEvent = new StartEvent();
		startEvent.setId(UniqueIdUtility.generateId("startEventId"));
		startEvent.setName(UniqueIdUtility.generateId("startEventName"));
		
		
		return startEvent;
	}
	
	/**
	 * 
	 * @param process
	 * @return
	 */
	public static EndEvent createEndEvent(){
		EndEvent endEvent = new EndEvent();
		endEvent.setId(UniqueIdUtility.generateId("endEventId"));
		endEvent.setName(UniqueIdUtility.generateId("endEventName"));
		
		
		return endEvent;
	}
	
	/**
	 * 
	 * @param process
	 * @return
	 */
	public static SequenceFlow createSequenceFlow(){
		SequenceFlow sequenceFlow = new SequenceFlow();
		sequenceFlow.setId(UniqueIdUtility.generateId("sequenceFlowId"));
		sequenceFlow.setName(UniqueIdUtility.generateId("sequenceFlowName"));
		sequenceFlow.getExecutionListeners().addAll(
				ActivitiModelElementListenerFactory.createSequenceFlowListeners());
		
		return sequenceFlow;
	}
	
	/**
	 * 
	 * @param process
	 * @return
	 */
	public static ScriptTask createScriptTask() {
		ScriptTask scriptTask = new ScriptTask();
		scriptTask.setId(UniqueIdUtility.generateId("scriptTaskId"));
		scriptTask.setName(UniqueIdUtility.generateId("scriptTaskName"));
	
		return scriptTask;
	}
	
	/**
	 * 
	 * @return
	 */
	public static UserTask createUserTask() {
		UserTask userTask = new UserTask();
		userTask.setId(UniqueIdUtility.generateId("userTaskId"));
		userTask.setName(UniqueIdUtility.generateId("userTaskName"));
		userTask.getTaskListeners().addAll(
				ActivitiModelElementListenerFactory.createUserTaskListeners());
		
		return userTask;
	}
	
	/**
	 * 
	 * @return
	 */
	public static ServiceTask createServiceTask() {
		ServiceTask serviceTask = new ServiceTask();
		serviceTask.setId(UniqueIdUtility.generateId("serviceTaskId"));
		serviceTask.setName(UniqueIdUtility.generateId("serviceTaskName"));
		serviceTask.getExecutionListeners().addAll(
				ActivitiModelElementListenerFactory.createServiceTaskListeners());
		
		return serviceTask;
	}
	
	/**
	 * 
	 * @param process
	 * @return
	 */
	public static ParallelGateway createParallelGateway() {
		ParallelGateway parallelGateway = new ParallelGateway();
		parallelGateway.setId(UniqueIdUtility.generateId("parallelGatewayId"));
		parallelGateway.setName(UniqueIdUtility.generateId("parallelGatewayName"));
			
		return parallelGateway;
	}
	
	/**
	 * 
	 * @param process
	 * @return
	 */
	public static ExclusiveGateway createExclusiveGateway() {
		ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
		exclusiveGateway.setId(UniqueIdUtility.generateId("exclusiveGatewayId"));
		exclusiveGateway.setName(UniqueIdUtility.generateId("exclusiveGatewayName"));
				
		return exclusiveGateway;
	}
	
	/**
	 * 
	 * @return
	 */
	public static SubProcess createSubProcess() {
		SubProcess subProcess = new SubProcess();
		subProcess.setId(UniqueIdUtility.generateId("subProcessId"));
		subProcess.setName(UniqueIdUtility.generateId("subProcessName"));
		
		return subProcess;
	}
	
	/**
	 * 
	 * @return
	 */
	public static CallActivity createCallActivity(){
		CallActivity callActivity = new CallActivity();
		callActivity.setId(UniqueIdUtility.generateId("callActivityId"));
		callActivity.setId(UniqueIdUtility.generateId("callActivityName"));
		
		return callActivity;
	}
}
