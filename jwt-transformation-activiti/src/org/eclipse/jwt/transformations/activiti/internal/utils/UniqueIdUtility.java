package org.eclipse.jwt.transformations.activiti.internal.utils;

import java.util.UUID;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class UniqueIdUtility {
	
	/**
	 * 
	 * @param prefix
	 */
	public static String generateId(String prefix) {
		String id = UUID.randomUUID().toString().substring(0, 8);
		
		if(prefix != null)
			return prefix + "_" + id;
		
		return id;
	}
}
