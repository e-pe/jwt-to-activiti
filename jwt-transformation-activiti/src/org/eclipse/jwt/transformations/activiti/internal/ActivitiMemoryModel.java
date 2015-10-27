package org.eclipse.jwt.transformations.activiti.internal;

import org.activiti.designer.util.editor.Bpmn2MemoryModel;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMemoryModel {
	private Bpmn2MemoryModel model;
	
	/**
	 * 
	 */
	public ActivitiMemoryModel() {
		this.model = new Bpmn2MemoryModel(null, null);
	}
	
	/**
	 * 
	 * @return
	 */
	public Bpmn2MemoryModel getModel() {
		return this.model;
	}
	
	/**
	 * 
	 * @return
	 */
	public Bpmn2MemoryModel getDiagramInterchange() {
		return this.model;
	}
	
	/**
	 * 
	 * @return
	 */
	public ActivitiModelValidatorResult validate() {
		return ActivitiModelValidator.validateModel(this.model);
	}
	
	/**
	 * 
	 * @return
	 */
	public String serialize() {
		try {			
			return ActivitiModelSerializer.serializeToString(this.model);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public void serialize(String filePath) {
		ActivitiModelSerializer.serializeToFile(this.model, filePath);
	}	
}
