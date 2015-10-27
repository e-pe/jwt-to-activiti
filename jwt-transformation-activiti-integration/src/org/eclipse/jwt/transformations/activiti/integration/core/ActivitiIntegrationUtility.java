package org.eclipse.jwt.transformations.activiti.integration.core;

import java.lang.reflect.Field;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiIntegrationUtility {	
	
	/**
	 * 
	 * @param execution
	 * @return
	 */
	public static String getProcessDefinitionId(Object context) {
		try {
			Field processDefinitionId = context.getClass()
					.getDeclaredField("processDefinitionId");
			
			processDefinitionId.setAccessible(true);
			
			String processDefinitionIdValue =  String.valueOf(processDefinitionId.get(context));
			
			return processDefinitionIdValue.substring(
					processDefinitionIdValue.indexOf("_"), 
					processDefinitionIdValue.indexOf(":"));
			
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null; 
	}

}
