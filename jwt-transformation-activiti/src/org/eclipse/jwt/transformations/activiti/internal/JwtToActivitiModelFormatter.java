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
import org.activiti.designer.util.editor.Bpmn2MemoryModel;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jwt.meta.model.core.Model;
import org.eclipse.jwt.meta.model.core.PackageableElement;
import org.eclipse.jwt.meta.model.processes.Action;
import org.eclipse.jwt.meta.model.processes.Activity;
import org.eclipse.jwt.meta.model.processes.ActivityEdge;
import org.eclipse.jwt.meta.model.processes.ActivityLinkNode;
import org.eclipse.jwt.meta.model.processes.ActivityNode;
import org.eclipse.jwt.meta.model.processes.DecisionNode;
import org.eclipse.jwt.meta.model.processes.FinalNode;
import org.eclipse.jwt.meta.model.processes.ForkNode;
import org.eclipse.jwt.meta.model.processes.InitialNode;
import org.eclipse.jwt.meta.model.processes.JoinNode;
import org.eclipse.jwt.meta.model.processes.MergeNode;
import org.eclipse.jwt.meta.model.processes.StructuredActivityNode;
import org.eclipse.jwt.transformations.activiti.internal.core.ITransformationStep;
import org.eclipse.jwt.transformations.activiti.internal.core.TransformationPerformer;


/**
 * 
 * @author Eugen Petrosean
 *
 */
public class JwtToActivitiModelFormatter {
	TransformationPerformer performer;
			
	/**
	 * 
	 */
	public JwtToActivitiModelFormatter(TransformationPerformer performer) {
		this.performer = performer;
	}
	
		
	/**
	 * 
	 * @param inJwtModel
	 * @param outActivitiModel
	 */
	public void transform(
			final Model inJwtModel, 
			final Bpmn2MemoryModel outActivitiModel) {
		
		EList<PackageableElement> elements = inJwtModel.getElements();
		
		for(PackageableElement element : elements){
			
			if(element instanceof Activity){
				Activity activity = (Activity)element;
	
				EList<ActivityNode> activityNodes = activity.getNodes();
				EList<ActivityEdge> activityEdges = activity.getEdges();
				
				Process process = this.transformActivity(activity, outActivitiModel);
												
				for(ActivityNode node : activityNodes) {
					//Process start
					if(node instanceof InitialNode) 						
						this.transformInitialNode((InitialNode)node, process);
					
					//Process finish
					else if(node instanceof FinalNode) 					
						this.transformFinalNode((FinalNode)node, process);
					
					//AND-Split
					else if(node instanceof ForkNode)
						this.transformForkNode((ForkNode)node, process);
					
					//AND-Join
					else if(node instanceof JoinNode)
						this.transformJoinNode((JoinNode)node, process);
					
					//XOR-Split
					else if(node instanceof DecisionNode)
						this.transformDecisionNode((DecisionNode)node, process);
					
					//XOR-Join
					else if(node instanceof MergeNode)
						this.transformMergeNode((MergeNode)node, process);
					
					//Activity with Role
					else if((node instanceof Action) && ((Action)node).getPerformedBy() != null)
						this.transformActionWithRole((Action)node, process);
					
					//Activity with Application
					else if((node instanceof Action) && ((Action)node).getExecutedBy() != null)
						this.transformActionWithApplication((Action)node, process);
					
					//Activity
					else if((node instanceof Action) && ((Action)node).getPerformedBy() == null 
							&& ((Action)node).getExecutedBy() == null)
						this.transformAction((Action)node, process);
						
					//Included process
					else if(node instanceof StructuredActivityNode)
						this.transformStructuredActivityNode((StructuredActivityNode)node, process);
					
					//Link to another process
					else if(node instanceof ActivityLinkNode)
						this.transformActivityLinkNode((ActivityLinkNode)node, process);
				}
				
				//Transition
				for(final ActivityEdge activityEdge : activityEdges)
					this.transformActivityEdge(activityEdge, process);
			}	
		}
	}
	
	/**
	 * 
	 * @param activity
	 * @param model
	 * @return
	 */
	private Process transformActivity(
			final Activity activity, 
			final Bpmn2MemoryModel model){
		
		//transforms the jwt activity element to the activiti process element
		final Process process = ActivitiModelElementFactory.createProcess(); 
						
		this.performer.transform(activity, process, 
			new ITransformationStep(){
				public void onPerform() {
					JwtToActivitiModelElementMapper.mapActivityToProcess(activity, process);
				}

				public void onFinish() {
					ActivitiModelElementAppender.appendProcess(process, model);
				}
			});
		
		return process;
	}
	
	/**
	 * 
	 * @param intialNode
	 * @param process
	 * @return
	 */
	private StartEvent transformInitialNode(
			final InitialNode intialNode, 
			final Process process) {
		
		//transforms the jwt initial node element to the activiti start event element
		final StartEvent startEvent = ActivitiModelElementFactory.createStartEvent();
		
		this.performer.transform(intialNode, startEvent, 
			new ITransformationStep(){
				public void onPerform() {
					JwtToActivitiModelElementMapper.mapInitialNodeToStartEvent(intialNode, startEvent);
				}

				public void onFinish() {
					ActivitiModelElementAppender.appendStartEvent(startEvent, process);
				}	
			});
		
		return startEvent;
	}
	
	/**
	 * 
	 * @param finalNode
	 * @param process
	 * @return
	 */
	private EndEvent transformFinalNode(
			final FinalNode finalNode, 
			final Process process){
		
		//transforms the jwt final node element to the activiti end event element
		final EndEvent endEvent = ActivitiModelElementFactory.createEndEvent();
		
		this.performer.transform(finalNode, endEvent, 
			new ITransformationStep(){
				public void onPerform() {
					JwtToActivitiModelElementMapper.mapFinalNodeToEndEvent(finalNode, endEvent);
				}

				public void onFinish() {
					ActivitiModelElementAppender.appendEndEvent(endEvent, process);
				}
		});
		
		return endEvent;
	}
	
	/**
	 * 
	 * @param forkNode
	 * @param process
	 * @return
	 */
	private ParallelGateway transformForkNode(
			final ForkNode forkNode, 
			final Process process) {
		
		//transforms the jwt fork node element to the activiti parallel gateway element
		final ParallelGateway parallelGateway = ActivitiModelElementFactory.createParallelGateway();
		
		this.performer.transform(forkNode, parallelGateway, 
				new ITransformationStep(){
					public void onPerform() {
						JwtToActivitiModelElementMapper.mapForkNodeToParallelGateway(forkNode, parallelGateway);
					}

					public void onFinish() {
						ActivitiModelElementAppender.appendParallelGateway(parallelGateway, process);
					}
		});
		
		return parallelGateway;
	}
	
	/**
	 * 
	 * @param joinNode
	 * @param process
	 * @return
	 */
	private ParallelGateway transformJoinNode(
			final JoinNode joinNode, 
			final Process process) {
		
		//transforms the jwt join node element to the activiti parallel gateway element
		final ParallelGateway parallelGateway = ActivitiModelElementFactory.createParallelGateway();
				
		this.performer.transform(joinNode, parallelGateway, 
				new ITransformationStep(){
					public void onPerform() {
						JwtToActivitiModelElementMapper.mapJoinNodeToParallelGateway(joinNode, parallelGateway);
					}

					public void onFinish() {
						ActivitiModelElementAppender.appendParallelGateway(parallelGateway, process);
					}
		});
				
		return parallelGateway;
	}
	
	/**
	 * 
	 * @param decisionNode
	 * @param process
	 * @return
	 */
	private ExclusiveGateway transformDecisionNode(
			final DecisionNode decisionNode, 
			final Process process) {
		
		//transforms the jwt decision node element to the activiti exclusive gateway element
		final ExclusiveGateway exclusiveGateway = ActivitiModelElementFactory.createExclusiveGateway();
				
		this.performer.transform(decisionNode, exclusiveGateway, 
				new ITransformationStep(){
					public void onPerform() {
						JwtToActivitiModelElementMapper.mapDecisionNodeToExclusiveGateway(decisionNode, exclusiveGateway);
					}

					public void onFinish() {
						ActivitiModelElementAppender.appendExclusiveGateway(exclusiveGateway, process);
					}
		});
				
		return exclusiveGateway;
	}
	
	/**
	 * 
	 * @param mergeNode
	 * @param process
	 * @return
	 */
	private ExclusiveGateway transformMergeNode(
			final MergeNode mergeNode, 
			final Process process) {
		
		//transforms the jwt merge node element to the activiti exclusive gateway element
		final ExclusiveGateway exclusiveGateway = ActivitiModelElementFactory.createExclusiveGateway();
				
		this.performer.transform(mergeNode, exclusiveGateway, 
				new ITransformationStep(){
					public void onPerform() {
						JwtToActivitiModelElementMapper.mapMergeNodeToExclusiveGateway(mergeNode, exclusiveGateway);
					}

					public void onFinish() {
						ActivitiModelElementAppender.appendExclusiveGateway(exclusiveGateway, process);
					}
		});
				
		return exclusiveGateway;
	}
	
	/**
	 * 
	 * @param action
	 * @param process
	 * @return
	 */
	private ScriptTask transformAction(
			final Action action, 
			final Process process) {
		
		//transforms the jwt final node element to the activiti end event element
		final ScriptTask scriptTask = ActivitiModelElementFactory.createScriptTask();
				
		this.performer.transform(action, scriptTask, 
			new ITransformationStep(){
				public void onPerform() {
					JwtToActivitiModelElementMapper.mapActionToScriptTask(action, scriptTask);
				}

				public void onFinish() {
					ActivitiModelElementAppender.appendScriptTask(scriptTask, process);
				}
		});
				
		return scriptTask;
	}
	
	/**
	 * 
	 * @param action
	 * @param process
	 * @return
	 */
	private UserTask transformActionWithRole(
			final Action action, 
			final Process process){
		
		//transforms the jwt action node element with a role reference to the activiti user task element
		final UserTask userTask = ActivitiModelElementFactory.createUserTask();
		
		this.performer.transform(action, userTask, 
			new ITransformationStep(){
				public void onPerform() {
					JwtToActivitiModelElementMapper.mapActionToUserTask(action, userTask);
				}
				
				public void onFinish() {
					ActivitiModelElementAppender.appendUserTask(userTask, process);
				}
		});
		
		return userTask;
	}
	
	/**
	 * 
	 * @param action
	 * @param process
	 * @return
	 */
	private ServiceTask transformActionWithApplication(
			final Action action, 
			final Process process) {
		
		//transforms the jwt action node element with an application reference to the activiti service task element
		final ServiceTask serviceTask = ActivitiModelElementFactory.createServiceTask();
				
		this.performer.transform(action, serviceTask, 
			new ITransformationStep(){
				public void onPerform() {
					JwtToActivitiModelElementMapper.mapActionToServiceTask(action, serviceTask);
				}
				
				public void onFinish() {
					ActivitiModelElementAppender.appendServiceTask(serviceTask, process);
				}
		});
		
		return serviceTask;
	}
	
	/**
	 * 
	 * @param activityEdge
	 * @param process
	 * @return
	 */
	private SequenceFlow transformActivityEdge(
			final ActivityEdge activityEdge, 
			final Process process){
		
		//transforms the jwt activity edge element to the activiti sequence flow element
		final SequenceFlow sequenceFlow = ActivitiModelElementFactory.createSequenceFlow();
		
		this.performer.transform(activityEdge, sequenceFlow, 
			new ITransformationStep(){
				public void onPerform() {
					JwtToActivitiModelElementMapper.mapActivityEdgeToSequenceFlow(
						activityEdge, sequenceFlow, 
						(FlowNode)performer.findTargetBySource(activityEdge.getSource()),
						(FlowNode)performer.findTargetBySource(activityEdge.getTarget()));
				}

				public void onFinish() {
					ActivitiModelElementAppender.appendSequenceFlow(sequenceFlow, process);
				}
		});
		
		return sequenceFlow;
	}
	
	/**
	 * 
	 * @param structuredActivityNode
	 * @param process
	 * @return
	 */
	private SubProcess transformStructuredActivityNode(
			final StructuredActivityNode structuredActivityNode, 
			final Process process){
		
		//transforms the jwt structured activity node element to the activiti subprocess element
		final SubProcess subProcess = ActivitiModelElementFactory.createSubProcess();
		
		this.performer.transform(structuredActivityNode, subProcess, 
			new ITransformationStep(){
				public void onPerform() {
					JwtToActivitiModelElementMapper.mapStructuredActivityNodeToSubProcess(structuredActivityNode, subProcess);
				}

				public void onFinish() {
					ActivitiModelElementAppender.appendSubProcess(subProcess, process);
				}
		});
		
		return subProcess;
	}
	
	/**
	 * 
	 * @param activityLinkNode
	 * @param process
	 * @return
	 */
	private CallActivity transformActivityLinkNode(
			final ActivityLinkNode activityLinkNode, 
			final Process process){
		
		//transforms the jwt activity link node element to the activiti call activity element
		final CallActivity callActivity = ActivitiModelElementFactory.createCallActivity();
		
		this.performer.transform(activityLinkNode, callActivity, 
			new ITransformationStep(){
				public void onPerform() {
					JwtToActivitiModelElementMapper.mapActivityLinkNodeToCallActivity(activityLinkNode, callActivity);
				}

				public void onFinish() {
					ActivitiModelElementAppender.appendCallActivity(callActivity, process);
				}
		});
		
		return callActivity;
	}
	
}
