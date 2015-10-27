package org.eclipse.jwt.generation.activiti.templates;

import org.eclipse.jwt.core.activiti.ICodeTemplate;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiGenerationProjectMonitoringUnitTestClassTemplate implements ICodeTemplate {

	/**
	 * 
	 */
	public String getCode() {
		return "package %s;\n" +
				"\n" +
				"import org.junit.Test;\n" +
				"\n" +
				"import org.eclipse.jwt.transformations.activiti.util.monitoring.test.ActivitiMonitoringTestConfig;\n" +
				"import org.eclipse.jwt.transformations.activiti.integration.core.monitoring.test.ActivitiIntegrationUnitTestMonitoringService;\n" +
				
				"\n" +
				"public class %s {\n" +					
					"\n" +
					"private static String monitoringTestConfigPath = \"%s\";"+
					"\n" +
					"\n" +
					"\t@Test\n" +
					"\tpublic void run() throws Exception {" +
						"\n" +
						"\t\tActivitiIntegrationUnitTestMonitoringService service = " + 
								"\n"+
								"\t\t\tnew ActivitiIntegrationUnitTestMonitoringService();" +
						"\n" +
						"\n" +
						"\t\tservice.setExecutingUnitTest(this.getClass());" +
						"\n" +
						"\t\tservice.setExecutingConfig(" +
							"\n" +
							"\t\t\tActivitiMonitoringTestConfig.loadFrom(monitoringTestConfigPath));" +
						"\n" +
						"\n" +
						"\t\tservice.run();" +
					"\n" +
					"\t}\n" +
				"}\n";
	}

}
