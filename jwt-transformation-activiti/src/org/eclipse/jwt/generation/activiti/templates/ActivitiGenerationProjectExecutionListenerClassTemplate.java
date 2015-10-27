package org.eclipse.jwt.generation.activiti.templates;

import org.eclipse.jwt.core.activiti.ICodeTemplate;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiGenerationProjectExecutionListenerClassTemplate implements ICodeTemplate {

	/**
	 * 
	 */
	public String getCode() {
		return "package %s;\n" +
				"\n" +
				"import org.activiti.engine.delegate.DelegateExecution;\n" + 
				"import org.activiti.engine.delegate.ExecutionListener;\n" +
				"\n" +
				"public class %s implements ExecutionListener {\n" +
					"\t@Override\n" +
					"\tpublic void notify(DelegateExecution execution) throws Exception {" +
					"\n" +
					"\t}\n" +
				"}\n";
	}	
}
