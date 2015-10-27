package org.eclipse.jwt.integration.activiti;

import java.io.File;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiIntegrationProjectFile {
	private File file;
	private String projectPath;
	
	/**
	 * 
	 */
	public ActivitiIntegrationProjectFile() {
		
	}
	
	/**
	 * 
	 * @return
	 */
	public File getFile() {
		return this.file;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setFile(File value) {
		this.file = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getProjectPath() {
		return this.projectPath;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setProjectPath(String value) {
		this.projectPath = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNormalizedFilePath() {
		return this.normalizeProjectFilePath(this.file, this.projectPath);
	}
		
	/**
	 * 
	 * @return
	 */
	private String normalizeProjectFilePath (File currentFile, String projectPath) {
		String filePath = currentFile.getAbsolutePath().replaceAll("\\\\", "/");
				
		String withoutProjectPath = filePath.substring( 
				filePath.indexOf(projectPath)  + projectPath.length(), 
				filePath.length());		
		
		String withoutFileName = withoutProjectPath.replace("/" + currentFile.getName(), "");
		
		return withoutFileName;
	}
}
