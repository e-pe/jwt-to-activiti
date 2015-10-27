package org.eclipse.jwt.transformations.activiti.util.monitoring.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringTestConfig {
	public static String monitoringTestConfigName = "activiti.monitoring.test.cfg.xml";
	
	private ArrayList<ActivitiMonitoringTestDescriptor> descriptors;
	
	/**
	 * 
	 */
	public ActivitiMonitoringTestConfig() {
		this.descriptors = new ArrayList<ActivitiMonitoringTestDescriptor>();
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<ActivitiMonitoringTestDescriptor> getDescriptors() {
		return this.descriptors;
	}
	
	/**
	 * 
	 * @return
	 */
	public static ActivitiMonitoringTestConfig loadFrom(String configPath) {
		try {
			
			InputStream stream = new FileInputStream(configPath);
			
			ActivitiMonitoringTestConfig config = new ActivitiMonitoringTestConfigSerializer()
				.deserialize(stream);
			
			stream.close();
			
			return config;
			
		} catch (Exception e) {

		}
		
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public static InputStream getDefaultConfig() {
		return ActivitiMonitoringTestConfig.class.getResourceAsStream("/org/eclipse/jwt/transformations/activiti/util/monitoring/resources/" + monitoringTestConfigName);
	}
}
