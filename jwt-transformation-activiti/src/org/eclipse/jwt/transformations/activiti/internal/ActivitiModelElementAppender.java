package org.eclipse.jwt.transformations.activiti.internal;

import org.activiti.designer.bpmn2.model.CallActivity;
import org.activiti.designer.bpmn2.model.FlowElement;
import org.activiti.designer.bpmn2.model.Process;
import org.activiti.designer.bpmn2.model.EndEvent;
import org.activiti.designer.bpmn2.model.ExclusiveGateway;
import org.activiti.designer.bpmn2.model.ParallelGateway;
import org.activiti.designer.bpmn2.model.ScriptTask;
import org.activiti.designer.bpmn2.model.SequenceFlow;
import org.activiti.designer.bpmn2.model.ServiceTask;
import org.activiti.designer.bpmn2.model.StartEvent;
import org.activiti.designer.bpmn2.model.SubProcess;
import org.activiti.designer.bpmn2.model.UserTask;
import org.activiti.designer.util.editor.Bpmn2MemoryModel;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiModelElementAppender {

	/**
	 * 
	 * @param model
	 */
	public static void appendProcess(
			Process process, 
			Bpmn2MemoryModel appendTo){
		
		appendTo.addProcess(process);
	}
	
	/**
	 * 
	 * @param process
	 */
	public static void appendStartEvent(
			StartEvent startEvent, 
			Process appendTo){
		
		appendTo.getFlowElements().add(startEvent);
	}
	
	/**
	 * 
	 * @param process
	 */
	public static void appendEndEvent(
			EndEvent endEvent, 
			Process appendTo){
		
		appendTo.getFlowElements().add(endEvent);
	}
	
	/**
	 * 
	 * @param process
	 */
	public static void appendScriptTask(
			ScriptTask scriptTask, 
			Process appendTo){
		
		appendTo.getFlowElements().add(scriptTask);
	}
	
	/**
	 * 
	 * @param userTask
	 * @param appendTo
	 */
	public static void appendUserTask(
			UserTask userTask, 
			Process appendTo){
		
		appendTo.getFlowElements().add(userTask);
	}
	
	/**
	 * 
	 * @param serviceTask
	 * @param appendTo
	 */
	public static void appendServiceTask(
			ServiceTask serviceTask, 
			Process appendTo){
		
		appendTo.getFlowElements().add(serviceTask);
	}
	
	/**
	 * 
	 * @param process
	 */
	public static void appendParallelGateway(
			ParallelGateway parallelGateway, 
			Process appendTo){
		
		appendTo.getFlowElements().add(parallelGateway);
	}
	
	/**
	 * 
	 * @param process
	 */
	public static void appendExclusiveGateway(
			ExclusiveGateway exclusiveGateway, 
			Process appendTo){
		
		appendTo.getFlowElements().add(exclusiveGateway);
	}
	
	/**
	 * 
	 * @param process
	 */
	public static void appendSequenceFlow(
			SequenceFlow sequenceFlow, 
			Process appendTo){
		
		SubProcess subProcessContainsFlow = null;
	    
		for (FlowElement flowElement : appendTo.getFlowElements()) {
		      
	    	if(flowElement instanceof SubProcess) {
	    		SubProcess subProcess = (SubProcess)flowElement;
		      	
	    		if(subProcess.getFlowElements().contains(sequenceFlow.getSourceRef())) 
	    			subProcessContainsFlow = subProcess;
		    }
	    }
	          
	    if(subProcessContainsFlow != null) 
	      	subProcessContainsFlow.getFlowElements().add(sequenceFlow);
	    else 
	    	appendTo.getFlowElements().add(sequenceFlow);	      
	}
	
	/**
	 * 
	 * @param subProcess
	 * @param appendTo
	 */
	public static void appendSubProcess(
			SubProcess subProcess, 
			Process appendTo) {
		
	}
	
	/**
	 * 
	 * @param callActivity
	 * @param appendTo
	 */
	public static void appendCallActivity(
			CallActivity callActivity, 
			Process appendTo){
		
	}
}
