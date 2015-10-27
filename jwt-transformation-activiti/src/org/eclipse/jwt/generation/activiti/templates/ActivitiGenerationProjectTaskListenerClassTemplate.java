package org.eclipse.jwt.generation.activiti.templates;

import org.eclipse.jwt.core.activiti.ICodeTemplate;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiGenerationProjectTaskListenerClassTemplate implements ICodeTemplate {

	/**
	 * 
	 */
	public String getCode() {
		return "package %s;\n" +
				"\n" +
				"import org.activiti.engine.delegate.DelegateTask;\n" + 
				"import org.activiti.engine.delegate.TaskListener;\n" +
				"\n" +
				"public class %s implements TaskListener {\n" +
					"\t@Override\n" +
					"\tpublic void notify(DelegateTask task) {" +
					"\n" +
					"\t}\n" +
				"}\n";
	}

}	