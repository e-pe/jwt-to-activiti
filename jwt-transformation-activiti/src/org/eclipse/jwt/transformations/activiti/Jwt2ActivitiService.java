

package org.eclipse.jwt.transformations.activiti;


import java.io.IOException;

import org.eclipse.jwt.transformations.activiti.internal.JwtToActivitiTransformation;
import org.eclipse.jwt.transformations.api.TransformationException;
import org.eclipse.jwt.transformations.api.TransformationService;

/**
 * 
 * 
 */
public class Jwt2ActivitiService extends TransformationService {

	@Override
	public void transform(String inJwtModelFilePath, String outActivitiModelFilePath)
			throws IOException, TransformationException {
		
		JwtToActivitiTransformation transformation = new JwtToActivitiTransformation();
		transformation.transform(inJwtModelFilePath, outActivitiModelFilePath);		
	}
}
