package org.eclipse.jwt.transformations.activiti.internal;

import java.util.Collection;

import org.activiti.designer.validation.bpmn20.validation.worker.ProcessValidationWorkerMarker;

public class ActivitiModelValidatorResult {
	private boolean isModelValid;	
	private Collection<ProcessValidationWorkerMarker> markers;
	
	/**
	 * 
	 * @param isModelValid
	 * @param markers
	 */
	public ActivitiModelValidatorResult(
			boolean isModelValid, 
			Collection<ProcessValidationWorkerMarker> markers){
		
		this.isModelValid = isModelValid;
		this.markers = markers;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean getIsModelValid(){
		return this.isModelValid;
	}
	
	/**
	 * 
	 * @return
	 */
	public Collection<ProcessValidationWorkerMarker> getMarkers(){
		return this.markers;
	}
}
