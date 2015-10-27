package org.eclipse.jwt.generation.activiti.templates;

import org.eclipse.jwt.core.activiti.ICodeTemplate;
import org.eclipse.jwt.transformations.activiti.util.ActivitiGenerationProjectOptions;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiGenerationProjectPomXmlTemplate implements ICodeTemplate  {

	public String getCode() {
		
		return this.createPomXml();
	}
	
	/**
	 * 
	 * @return
	 */
	private String createPomXml() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		buffer.append("    xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd\">\n");
		buffer.append("  <modelVersion>4.0.0</modelVersion>\n");
		buffer.append("  <groupId>org.activiti.examples</groupId>\n");
		buffer.append("  <artifactId>activiti-examples</artifactId>\n");
		buffer.append("  <version>1.0-SNAPSHOT</version>\n");
		buffer.append("  <packaging>jar</packaging>\n");
		buffer.append("  <name>BPMN 2.0 with Activiti - Examples</name>\n");
		buffer.append("  <properties>\n");
		buffer.append("    <activiti-version>5.13</activiti-version>\n");
		buffer.append("  </properties>\n");
		buffer.append("  <dependencies>\n");
		
		addDependency(buffer, "org.activiti", "activiti-engine", "${activiti-version}");
		addDependency(buffer, "org.activiti", "activiti-spring", "${activiti-version}");
		addDependency(buffer, "org.codehaus.groovy", "groovy", "1.7.5");
		addDependency(buffer, "hsqldb", "hsqldb", "1.8.0.7");
		addDependency(buffer, "com.h2database", "h2", "1.2.132");
		addDependency(buffer, "junit", "junit", "4.8.1");
		addDependency(buffer, "com.espertech", "esper", "4.8.0");
		
		buffer.append("  </dependencies>\n");
		
		buffer.append("	 <repositories>\n");
		buffer.append("    <repository>\n");
		buffer.append("      <id>Activiti</id>\n");
		buffer.append("      <url>http://maven.alfresco.com/nexus/content/repositories/activiti</url>\n");
		buffer.append("	   </repository>\n");
		buffer.append("    <repository>\n");
		buffer.append("		 <id>Esper</id>\n");
		buffer.append("		 <url>http://repository.codehaus.org/com/espertech/esper</url>\n");
		buffer.append("    </repository>\n");
		buffer.append("	 </repositories>\n");
		
		buffer.append("	 <build>\n");
		buffer.append("    <plugins>\n");
		buffer.append("      <plugin>\n");
		buffer.append("        <groupId>org.apache.maven.plugins</groupId>\n");
		buffer.append("        <artifactId>maven-compiler-plugin</artifactId>\n");
		buffer.append("	       <version>2.3.2</version>\n");
		buffer.append("        <configuration>\n");
		buffer.append("	         <source>1.6</source>\n");
		buffer.append("	         <target>1.6</target>\n");
		buffer.append("	       </configuration>\n");
		buffer.append("	     </plugin>\n");
		buffer.append("	     <plugin>\n");
		buffer.append("        <groupId>org.apache.maven.plugins</groupId>\n");
		buffer.append("        <artifactId>maven-eclipse-plugin</artifactId>\n");
		buffer.append("        <inherited>true</inherited>\n");
		buffer.append("        <configuration>\n");
		buffer.append("	         <classpathContainers>\n");
		buffer.append("	           <classpathContainer>");
		buffer.append(ActivitiGenerationProjectOptions.activitiExtensionsUserLibPath);
		buffer.append("</classpathContainer>\n");
		buffer.append("	         </classpathContainers>\n");
		buffer.append("	       </configuration>\n");
		buffer.append("	     </plugin>\n");
		buffer.append("    </plugins>\n");
		buffer.append("	 </build>\n");
		
		buffer.append("</project>\n");
		return buffer.toString();
	}
	
	/**
	 * 
	 * @param buffer
	 * @param groupId
	 * @param artifactId
	 * @param version
	 */
	private void addDependency(StringBuilder buffer, String groupId, String artifactId, String version) {
		buffer.append("    <dependency>\n")
			.append("      <groupId>")
			.append(groupId)
			.append("</groupId>\n")
			.append("      <artifactId>")
			.append(artifactId)
			.append("</artifactId>\n")
			.append("      <version>")
			.append(version)
			.append("</version>\n")
			.append("    </dependency>\n");
	}
}
