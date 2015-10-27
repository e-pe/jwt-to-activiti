package org.eclipse.jwt.transformations.activiti.internal;

import org.activiti.designer.bpmn2.model.CallActivity;
import org.activiti.designer.bpmn2.model.EndEvent;
import org.activiti.designer.bpmn2.model.ExclusiveGateway;
import org.activiti.designer.bpmn2.model.FlowNode;
import org.activiti.designer.bpmn2.model.ParallelGateway;
import org.activiti.designer.bpmn2.model.Process;
import org.activiti.designer.bpmn2.model.ScriptTask;
import org.activiti.designer.bpmn2.model.SequenceFlow;
import org.activiti.designer.bpmn2.model.ServiceTask;
import org.activiti.designer.bpmn2.model.StartEvent;
import org.activiti.designer.bpmn2.model.SubProcess;
import org.activiti.designer.bpmn2.model.UserTask;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jwt.meta.model.processes.Action;
import org.eclipse.jwt.meta.model.processes.Activity;
import org.eclipse.jwt.meta.model.processes.ActivityEdge;
import org.eclipse.jwt.meta.model.processes.ActivityLinkNode;
import org.eclipse.jwt.meta.model.processes.DecisionNode;
import org.eclipse.jwt.meta.model.processes.FinalNode;
import org.eclipse.jwt.meta.model.processes.ForkNode;
import org.eclipse.jwt.meta.model.processes.InitialNode;
import org.eclipse.jwt.meta.model.processes.JoinNode;
import org.eclipse.jwt.meta.model.processes.MergeNode;
import org.eclipse.jwt.meta.model.processes.StructuredActivityNode;
import org.eclipse.jwt.transformations.activiti.internal.utils.ClassPatternUtility;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class JwtToActivitiModelElementMapper {
	/**
	 * 
	 * @param jwtActivity
	 * @param activitiProcess
	 */
	public static void mapActivityToProcess(
			Activity jwtActivity, 
			Process activitiProcess) {
		
		if(!StringUtils.isEmpty(jwtActivity.getName())) {
			activitiProcess.setName(jwtActivity.getName());
		}
	}
	
	/**
	 * 
	 * @param jwtInitialNode
	 * @param onCreateActivitiStartEvent
	 */
	public static void mapInitialNodeToStartEvent(
			InitialNode jwtInitialNode, 
			StartEvent activitiStartEvent) {
				
		if(!StringUtils.isEmpty(jwtInitialNode.getName())) {
			activitiStartEvent.setName(jwtInitialNode.getName());
		}		
	}
	
	/**
	 * 
	 * @param jwtFinalNode
	 * @param activitiEndEvent
	 */
	public static void mapFinalNodeToEndEvent(
			FinalNode jwtFinalNode, 
			EndEvent activitiEndEvent){
				
		if(!StringUtils.isEmpty(jwtFinalNode.getName())) {
			activitiEndEvent.setName(jwtFinalNode.getName());
		}
	}
	
	/**
	 * 
	 * @param jwtAction
	 * @param activitiScriptTask
	 */
	public static void mapActionToScriptTask(
			Action jwtAction, 
			ScriptTask activitiScriptTask) {
		
		if(!StringUtils.isEmpty(jwtAction.getName())) {
			activitiScriptTask.setName(jwtAction.getName());
		}
	}
	
	/**
	 * 
	 * @param jwtAction
	 * @param activitiUserTask
	 */
	public static void mapActionToUserTask(
			Action jwtAction, 
			UserTask activitiUserTask){
		
		if(!StringUtils.isEmpty(jwtAction.getName())) {
			activitiUserTask.setName(jwtAction.getName());
		}
		
		activitiUserTask.setAssignee(jwtAction.getPerformedBy().getName());
	}
	
	/**
	 * 
	 * @param jwtAction
	 * @param activitiServiceTask
	 */
	public static void mapActionToServiceTask(
			Action jwtAction, 
			ServiceTask activitiServiceTask){
		
		if(!StringUtils.isEmpty(jwtAction.getName())) {
			activitiServiceTask.setName(jwtAction.getName());
		}
				
		activitiServiceTask.setImplementation(
				ClassPatternUtility.generatePattern(jwtAction.getExecutedBy().getName()));
		
		//is the keyword for executing service task as a java class 
		activitiServiceTask.setImplementationType("classType");
	}
	
	/**
	 * 
	 * @param jwtActivityEdge
	 * @param activitiSequenceFlow
	 * @param activitiSequenceFlowSource
	 * @param activitiSequenceFlowTarget
	 */
	public static void mapActivityEdgeToSequenceFlow(
			ActivityEdge jwtActivityEdge, 
			SequenceFlow activitiSequenceFlow,
			FlowNode activitiSequenceFlowSource,
			FlowNode activitiSequenceFlowTarget) {
			
		activitiSequenceFlow.setSourceRef(activitiSequenceFlowSource);
		activitiSequenceFlow.setTargetRef(activitiSequenceFlowTarget);
			
		activitiSequenceFlowSource.getOutgoing().add(activitiSequenceFlow);
		activitiSequenceFlowTarget.getIncoming().add(activitiSequenceFlow);
	}
	
	/**
	 * 
	 * @param jwtForkNode
	 * @param activitiParallelGateway
	 */
	public static void mapForkNodeToParallelGateway(
			ForkNode jwtForkNode, 
			ParallelGateway activitiParallelGateway) {
		
		if(!StringUtils.isEmpty(jwtForkNode.getName())) {
			activitiParallelGateway.setName(jwtForkNode.getName());
		}
	}
	
	/**
	 * 
	 * @param jwtJoinNode
	 * @param activitiParallelGateway
	 */
	public static void mapJoinNodeToParallelGateway(
			JoinNode jwtJoinNode, 
			ParallelGateway activitiParallelGateway) {
		
		if(!StringUtils.isEmpty(jwtJoinNode.getName())) {
			activitiParallelGateway.setName(jwtJoinNode.getName());
		}
	}
	
	/**
	 * 
	 * @param jwtDecisionNode
	 * @param activitiExclusiveGateway
	 */
	public static void mapDecisionNodeToExclusiveGateway(
			DecisionNode jwtDecisionNode, 
			ExclusiveGateway activitiExclusiveGateway){
		
		if(!StringUtils.isEmpty(jwtDecisionNode.getName())) {
			activitiExclusiveGateway.setName(jwtDecisionNode.getName());
		}
	}
	
	/**
	 * 
	 * @param jwtMergeNode
	 * @param activitiExclusiveGateway
	 */
	public static void mapMergeNodeToExclusiveGateway(
			MergeNode jwtMergeNode, 
			ExclusiveGateway activitiExclusiveGateway){
		
		if(!StringUtils.isEmpty(jwtMergeNode.getName())) {
			activitiExclusiveGateway.setName(jwtMergeNode.getName());
		}
	}
	
	/**
	 * 
	 * @param jwtStructuredActivityNode
	 * @param activitiSubProcess
	 */
	public static void mapStructuredActivityNodeToSubProcess(
			StructuredActivityNode jwtStructuredActivityNode, 
			SubProcess activitiSubProcess){
	}
	
	/**
	 * 
	 * @param jwtActivityLinkNode
	 * @param activitiCallActivity
	 */
	public static void mapActivityLinkNodeToCallActivity(
			ActivityLinkNode jwtActivityLinkNode, 
			CallActivity activitiCallActivity){
		
	}
}
