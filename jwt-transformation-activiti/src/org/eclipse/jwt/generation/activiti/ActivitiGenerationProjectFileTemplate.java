package org.eclipse.jwt.generation.activiti;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiGenerationProjectFileTemplate {
	private String path;
	private Object content;
	
	/**
	 * 
	 * @param path
	 * @param content
	 */
	public ActivitiGenerationProjectFileTemplate(String path, Object content) {
		this.path = path;
		this.content = content;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPath() {
		return this.path;
	}
	
	/**
	 * 
	 * @return
	 */
	public Object getContent() {
		return this.content;
	}
}
