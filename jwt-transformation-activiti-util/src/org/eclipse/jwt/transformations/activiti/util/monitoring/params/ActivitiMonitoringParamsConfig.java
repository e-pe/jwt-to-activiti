package org.eclipse.jwt.transformations.activiti.util.monitoring.params;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import org.eclipse.jwt.transformations.activiti.util.monitoring.test.ActivitiMonitoringTestConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.test.ActivitiMonitoringTestConfigSerializer;


/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringParamsConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1639323013349555314L;

	/**
	 * 
	 */
	public static String monitoringParamsConfigName = "activiti.monitoring.params.cfg.xml";
	
	private ArrayList<ActivitiMonitoringParamsProcessDescriptor> descriptors;
	
	/**
	 * 
	 */
	public ActivitiMonitoringParamsConfig() {
		this.descriptors = new ArrayList<ActivitiMonitoringParamsProcessDescriptor>();
	}
	
	/**
	 * 
	 * @return
	 */
	public ArrayList<ActivitiMonitoringParamsProcessDescriptor> getDescriptors() {
		return this.descriptors;
	}
	
	/**
	 * 
	 * @return
	 */
	public ActivitiMonitoringParamsProcessDescriptor findDescriptorByKey(String key) {
		for(int i = 0; i < this.descriptors.size(); i++) {
			ActivitiMonitoringParamsProcessDescriptor descriptor = this.descriptors.get(i);
			
			if(descriptor.getKey().equals(key))
				return descriptor;
		}
		
		return null;
	}
	
	/**
	 * 
	 */
	public static ActivitiMonitoringParamsConfig loadFrom(String configPath) {
		try {
			
			InputStream stream = new FileInputStream(configPath);
			
			ActivitiMonitoringParamsConfig config = new ActivitiMonitoringParamsConfigSerializer()
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
		return ActivitiMonitoringParamsConfig.class.getResourceAsStream("/org/eclipse/jwt/transformations/activiti/util/monitoring/resources/" + monitoringParamsConfigName);
	}
}
