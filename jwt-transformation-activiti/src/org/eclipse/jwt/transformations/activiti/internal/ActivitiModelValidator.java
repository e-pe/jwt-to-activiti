package org.eclipse.jwt.transformations.activiti.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.designer.bpmn2.model.FlowElement;
import org.activiti.designer.util.editor.Bpmn2MemoryModel;
import org.activiti.designer.validation.bpmn20.bundle.PluginConstants;
import org.activiti.designer.validation.bpmn20.validation.worker.ProcessValidationWorkerInfo;
import org.activiti.designer.validation.bpmn20.validation.worker.ProcessValidationWorkerMarker;
import org.activiti.designer.validation.bpmn20.validation.worker.impl.ScriptTaskValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.impl.SequenceFlowValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.impl.ServiceTaskValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.impl.SubProcessValidationWorker;
import org.activiti.designer.validation.bpmn20.validation.worker.impl.UserTaskValidationWorker;
import org.eclipse.core.resources.IMarker;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiModelValidator {
	/**
	 * 
	 * @param model
	 * @return
	 */
	public static ActivitiModelValidatorResult validateModel(Bpmn2MemoryModel model) {
		return new ActivitiModelValidator().validate(model);
	}
	
	/**
	 * 
	 * @param model
	 * @return
	 */
	public ActivitiModelValidatorResult validate(Bpmn2MemoryModel model){
   	   boolean isValid = true;
   	   
   	   Collection<ProcessValidationWorkerMarker> allMarkers = new ArrayList<ProcessValidationWorkerMarker>();
   	    
	   Map<String, List<Object>> processNodes = extractProcessConstructs(model.getMainProcess().getFlowElements());
	    		
	    List<ProcessValidationWorkerInfo> workers = getWorkers();

	    for (final ProcessValidationWorkerInfo worker : workers) {

	    	Collection<ProcessValidationWorkerMarker> result = worker.getProcessValidationWorker().validate(null, processNodes);
	        
	    	for (final ProcessValidationWorkerMarker marker : result) {
	        	
	    		if(marker.getSeverity() ==  IMarker.SEVERITY_ERROR)	
	    			isValid = false;
	        
	    		allMarkers.add(marker);
	        }
	    }

		return new ActivitiModelValidatorResult(isValid, allMarkers);
	}
	
	/**
	 * 
	 * @return
	 */
	private ArrayList<ProcessValidationWorkerInfo> getWorkers() {

		ArrayList<ProcessValidationWorkerInfo> result = new ArrayList<ProcessValidationWorkerInfo>();

	    result.add(new ProcessValidationWorkerInfo(new UserTaskValidationWorker(), PluginConstants.WORK_USER_TASK));
	    result.add(new ProcessValidationWorkerInfo(new ScriptTaskValidationWorker(), PluginConstants.WORK_SCRIPT_TASK));
	    result.add(new ProcessValidationWorkerInfo(new ServiceTaskValidationWorker(), PluginConstants.WORK_SERVICE_TASK));
	    result.add(new ProcessValidationWorkerInfo(new SequenceFlowValidationWorker(), PluginConstants.WORK_SEQUENCE_FLOW));
	    result.add(new ProcessValidationWorkerInfo(new SubProcessValidationWorker(), PluginConstants.WORK_SUB_PROCESS));

	    return result;

	}
	 
	protected Map<String, List<Object>> extractProcessConstructs(final List<FlowElement> objects) {

		final Map<String, List<Object>> result = new HashMap<String, List<Object>>();

		for (final FlowElement object : objects) {

		      String nodeType = null;

		      nodeType = object.getClass().getCanonicalName();

		      if (nodeType != null) {
		        
		    	if (!result.containsKey(nodeType)) {
		          result.put(nodeType, new ArrayList<Object>());
		        }
		        
		        result.get(nodeType).add(object);
		      }

		}

		return result;
	}
}
