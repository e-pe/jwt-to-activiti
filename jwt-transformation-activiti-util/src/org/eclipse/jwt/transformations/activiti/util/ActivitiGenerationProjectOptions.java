package org.eclipse.jwt.transformations.activiti.util;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiGenerationProjectOptions {
	public static final String monitoringListenersPackageName = "monitoringListeners";
	public static final String serviceTaskListenersPackageName = "serviceTaskListeners";
	public static final String monitoringUnitTestPackageName = "org.activiti.designer.test";
	
	public static final String monitoringListenerFileName = "MonitoringListener";
	public static final String serviceTaskListenerFileName = "ServiceTaskListener";
	
	public static final String libsFolderPath = "libs";
	public static final String srcFolderPath ="src";
	public static final String mainFolderPath = "src/main";
	public static final String javaFolderPath = "src/main/java";
	public static final String monitoringListenerJavaFolderPath = javaFolderPath + "/" + monitoringListenersPackageName;
	public static final String serviceTaskJavaFolderPath = javaFolderPath + "/" + serviceTaskListenersPackageName;
	
	public static final String resourceFolderPath = "src/main/resources/";
	public static final String diagramFolderPath = "src/main/resources/diagrams/";
	public static final String monitoringFolderPath = "src/main/resources/monitoring/";
	
	public static final String testFolderPath = "src/test";
	public static final String testJavaFolderPath = "src/test/java/";
	public static final String testResourceFolderPath = "src/test/resources";
	public static final String testOutputFolderPath = "src/test/output";
	
	public static final String testPackageName = "org.activiti.designer.test";
	public static final String testPackageFolderPath = "org/activiti/designer/test";
	public static final String testMonitoringUnitTestFolderPath = testJavaFolderPath + testPackageFolderPath;
	
	public static final String deploymentFolderPath = "deployment";
	public static final String tempBarFolderPath = "tempbar";
	public static final String tempClassesFolderPath = "tempclasses";
		
	public static final String userLibraryNameExtensions = "Activiti Extension Library";	  	
	public static final String activitiExtensionsUserLibPath = "org.eclipse.jdt.USER_LIBRARY/" + userLibraryNameExtensions;
	
}
