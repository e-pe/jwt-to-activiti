package org.eclipse.jwt.transformations.activiti.internal.utils;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ClassPatternUtility {
	/**
	 * 
	 * @return
	 */
	public static String generatePattern(String className) {
		return "{{" + className.replaceAll("\\s+", "") + "}}";
	}
}
