package org.eclipse.jwt.transformations.activiti.util.monitoring.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiMonitoringOutputConfig {
	private ActivitiMonitoringOutputDescriptor descriptor;
	
	public static String monitoringOutputConfigName = "activiti.monitoring.output.cfg.xml";
	
	/**
	 * 
	 */
	public ActivitiMonitoringOutputConfig () {
		this.descriptor = new ActivitiMonitoringOutputDescriptor();
	}
	
	/**
	 * 
	 * @return
	 */
	public ActivitiMonitoringOutputDescriptor getDescriptor() {
		return this.descriptor;
	}
	
	/**
	 * 
	 */
	public void saveTo(String outputPath) {
		BufferedWriter writer = null;
		
		try {
			String outputContent = new ActivitiMonitoringOutputConfigSerializer()
				.serialize(this);
		
			File outputConfigFile = new File(outputPath);
			
			if(!outputConfigFile.exists()) 
				outputConfigFile.createNewFile();

			writer = new BufferedWriter(
					new FileWriter(outputConfigFile));
			
			writer.write(outputContent);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 
	 */
	public static ActivitiMonitoringOutputConfig loadFrom(String configPath) {
		try {
			
			InputStream stream = new FileInputStream(configPath);
			
			ActivitiMonitoringOutputConfig config = new ActivitiMonitoringOutputConfigSerializer()
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
		return ActivitiMonitoringOutputConfig.class.getResourceAsStream("/org/eclipse/jwt/transformations/activiti/util/monitoring/resources/" + monitoringOutputConfigName);
	}
}
