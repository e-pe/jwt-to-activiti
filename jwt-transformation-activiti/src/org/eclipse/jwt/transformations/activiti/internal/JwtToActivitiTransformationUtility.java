package org.eclipse.jwt.transformations.activiti.internal;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jwt.meta.model.core.Model;
import org.eclipse.jwt.we.editors.WEEditor;
import org.eclipse.jwt.we.misc.util.GeneralHelper;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class JwtToActivitiTransformationUtility {
	/**
	 * 
	 * @return
	 */
	public static String getCurrentJwtModelAsBpmn() {
		WEEditor editor = GeneralHelper.getActiveInstance();
		
		JwtMemoryModel jwtMemoryModel = new JwtMemoryModel(
				(Model)editor.getModel(), editor.getDiagramData());
		
		ActivitiMemoryModel activitiMemoryModel = new ActivitiMemoryModel();
		
		JwtToActivitiTransformation transformation = new JwtToActivitiTransformation();
		transformation.transform(jwtMemoryModel, activitiMemoryModel);
		
		return activitiMemoryModel.serialize();
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getCurrentJwtModelFileName() {
		WEEditor editor = GeneralHelper.getActiveInstance();
		
		Resource resource = editor.getMainModelResource();
		String uri = resource.getURI().trimFileExtension().toString();
		
		return uri.substring(uri.lastIndexOf("/") + 1, uri.length());
	}
}
