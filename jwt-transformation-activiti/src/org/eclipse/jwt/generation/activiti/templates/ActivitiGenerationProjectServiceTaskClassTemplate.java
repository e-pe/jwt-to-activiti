package org.eclipse.jwt.generation.activiti.templates;

import org.eclipse.jwt.core.activiti.ICodeTemplate;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiGenerationProjectServiceTaskClassTemplate implements ICodeTemplate {

	public String getCode() {
		return "package %s;\n" +
				"\n" +
				"import org.activiti.engine.delegate.DelegateExecution;\n" + 
				"import org.activiti.engine.delegate.JavaDelegate;\n" +
				"\n" +
				"public class %s implements JavaDelegate {\n" +
					"\t@Override\n" +
					"\tpublic void execute(DelegateExecution execution) throws Exception {" +
					"\n" +
					"\t}\n" +
				"}\n";
				
	}

}
