package org.eclipse.jwt.transformations.activiti.util.monitoring.test;

import java.util.ArrayList;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringTestDescriptor {
	private String testClass;
	private String testOutputPath;
	private String testParamsPath;
	private String testQueriesPath;
	
	private ArrayList<ActivitiMonitoringTestActivation> activations;
	
	/**
	 * 
	 */
	public ActivitiMonitoringTestDescriptor() {
		this.activations = new ArrayList<ActivitiMonitoringTestActivation>();
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<ActivitiMonitoringTestActivation> getActivations() {
		return this.activations;
	}
	
	/**
	 * 
	 */
	public String getTestClass() {
		return this.testClass;
	}
	
	/**
	 * 
	 * @param testClass
	 */
	public void setTestClass(String testClass) {
		this.testClass = testClass;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTestOutputPath() {
		return this.testOutputPath;
	}
	
	/**
	 * 
	 * @param testOutput
	 * @return
	 */
	public void setTestOutputPath(String testOutputPath) {
		this.testOutputPath = testOutputPath;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTestParamsPath() {
		return this.testParamsPath;
	}
	
	/**
	 * 
	 */
	public void setTestParamsPath(String testParamsPath) {
		this.testParamsPath = testParamsPath;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTestQueriesPath() {
		return this.testQueriesPath;
	}
	
	/**
	 * 
	 */
	public void setTestQueriesPath(String testQueriesPath) {
		this.testQueriesPath = testQueriesPath;
	}
}
