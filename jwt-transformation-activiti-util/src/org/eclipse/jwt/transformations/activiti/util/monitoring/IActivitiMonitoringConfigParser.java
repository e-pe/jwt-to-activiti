package org.eclipse.jwt.transformations.activiti.util.monitoring;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public interface IActivitiMonitoringConfigParser {
	/**
	 * 
	 */
	public String onParse(String sourceToParse, ActivitiMonitoringDescriptor descriptor);
}
