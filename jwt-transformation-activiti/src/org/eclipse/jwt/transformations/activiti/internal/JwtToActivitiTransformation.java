package org.eclipse.jwt.transformations.activiti.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jwt.meta.model.core.Model;
import org.eclipse.jwt.transformations.activiti.internal.core.ITransformation;
import org.eclipse.jwt.transformations.activiti.internal.core.TransformationManager;
import org.eclipse.jwt.transformations.activiti.internal.core.TransformationPerformer;
import org.eclipse.jwt.transformations.activiti.internal.listeners.JwtToActivitiTransformationErrorListener;
import org.eclipse.jwt.we.model.view.Diagram;


/**
 * 
 * @author Eugen Petrosean
 *
 */
public class JwtToActivitiTransformation {
	private JwtToActivitiTransformationErrorListener errorListener;
	
	/**
	 * 
	 */
	public JwtToActivitiTransformation() {
		
	}
	
	/**
	 * 
	 */
	public void addErrorListener(
			JwtToActivitiTransformationErrorListener errorListener){
		
		this.errorListener = errorListener;
	}
	
	/**
	 * 
	 * @param inJwtModelFilePath
	 * @param outActivitiModelFilePath
	 */
	public void transform(
			String inJwtModelFilePath, 
			String outActivitiModelFilePath) {
		
		JwtMemoryModel jwtMemoryModel = this.loadJwtMemoryModel(
				inJwtModelFilePath, 
				inJwtModelFilePath + "_view");
		
		ActivitiMemoryModel activitiMemoryModel = new ActivitiMemoryModel();
		
		this.transform(jwtMemoryModel, activitiMemoryModel);
		
		activitiMemoryModel.serialize(outActivitiModelFilePath);	
	}
	
	/**
	 * 
	 */
	public void transform(
			JwtMemoryModel inJwtMemoryModel, 
			ActivitiMemoryModel outActivitiMemoryModel) {
		
		this.transformModel(inJwtMemoryModel, outActivitiMemoryModel);	
		
		this.validateModel(outActivitiMemoryModel);
	}
	
	/**
	 * 
	 * @param inJwtModelFilePath
	 * @return
	 */
	private void transformModel(
			final JwtMemoryModel jwtMemoryModel,
			final ActivitiMemoryModel activitiMemoryModel){
		
		TransformationManager transformationManager = new TransformationManager();
		transformationManager.defineTransformation(jwtMemoryModel, activitiMemoryModel, 
				new ITransformation() {
					public void onTransformModel(
							TransformationPerformer performer) {
							
						JwtToActivitiModelFormatter formatter = 
								new JwtToActivitiModelFormatter(performer);
						
						formatter.transform(
								jwtMemoryModel.getModel(), 
								activitiMemoryModel.getModel());						
					}
					
					public void onTransformDiagramInterchange(
							TransformationPerformer performer) {
						
						JwtToActivitiDiagramInterchangeFormatter formatter = 
								new JwtToActivitiDiagramInterchangeFormatter(performer);
						
						formatter.transform(
								jwtMemoryModel.getDiagramInterchange(), 
								activitiMemoryModel.getDiagramInterchange());						
					}			
		});
		
		transformationManager.startTransformation();
	}	
	
	/**
	 * 
	 * @param activitiModel
	 */
	private void validateModel(
			ActivitiMemoryModel activitiMemoryModel){
		
		ActivitiModelValidatorResult validatorResult = activitiMemoryModel.validate();
		
		if(!validatorResult.getIsModelValid()){
			
			if(this.errorListener != null)
				this.errorListener.onError();
		}
	}
	
	/**
	 * 
	 * @param jwtModelFilePath
	 * @param jwtDiagramInterchangeFilePath
	 * @return
	 */
	private JwtMemoryModel loadJwtMemoryModel(
			String jwtModelFilePath, 
			String jwtDiagramInterchangeFilePath){
		
		try {
			ResourceSet resourceSet = new ResourceSetImpl();
			URI jwtModelURI = URI.createFileURI(jwtModelFilePath);
			URI jwtDiagramInterchangeURI = URI.createFileURI(jwtDiagramInterchangeFilePath);
		
			Resource modelResource = resourceSet.createResource(jwtModelURI);
			Resource diagramInterchangeResource = resourceSet.createResource(jwtDiagramInterchangeURI);
			
			modelResource.load(null);
			diagramInterchangeResource.load(null);
			
			File memoryModelFile = new File("jwtMemoryModel.jwtr");
			Resource memoryModelResource = resourceSet.createResource(
					URI.createFileURI(memoryModelFile.getAbsolutePath()));
			
			//move all objects into one resource
	        Collection<EObject> cl = new ArrayList<EObject>();
	        cl.addAll(modelResource.getContents());
	        cl.addAll(diagramInterchangeResource.getContents());
	        
	        memoryModelResource.getContents().addAll(EcoreUtil.copyAll(cl));
	        
	        return new JwtMemoryModel(
	        		(Model)memoryModelResource.getContents().get(0),
	        		(Diagram)memoryModelResource.getContents().get(1));
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return null;
	}	
}
