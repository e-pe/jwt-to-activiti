package org.eclipse.jwt.transformations.activiti.internal.core;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class TransformationManager {
	private Object sourceModel;
	private Object targetModel;
	private TransformationPerformer performer;
	private ITransformation transformation;
	
	public TransformationManager(){
		this.performer = new TransformationPerformer();
	}
	
	/**
	 * 
	 * @param transformationProcess
	 */
	public void defineTransformation(Object sourceModel, Object targetModel, ITransformation transformation){
		this.sourceModel = sourceModel;
		this.targetModel = targetModel;
		this.transformation = transformation;		
	}
	
	/**
	 * 
	 */
	public void startTransformation(){
		if(this.transformation != null){
			this.transformation.onTransformModel(this.performer);
			this.transformation.onTransformDiagramInterchange(this.performer);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Object getSourceModel(){
		return this.sourceModel;
	}
	
	/**
	 * 
	 * @return
	 */
	public Object getTargetModel(){
		return this.targetModel;
	}
	
	/**
	 * 
	 */
	public void dispose() {
		this.performer.dispose();
	}
}
