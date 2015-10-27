package org.eclipse.jwt.plugin.activiti.actions;

import java.io.IOException;

import javax.xml.stream.XMLStreamException;

import org.activiti.designer.util.editor.Bpmn2MemoryModel;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jwt.core.activiti.IProjectPerformer;
import org.eclipse.jwt.generation.activiti.ActivitiGenerationProject;
import org.eclipse.jwt.generation.activiti.ActivitiGenerationProjectFactory;
import org.eclipse.jwt.generation.activiti.ActivitiGenerationProjectFileTemplateFactory;
import org.eclipse.jwt.monitoring.activiti.ActivitiMonitoringFileUtility;
import org.eclipse.jwt.transformations.activiti.internal.ActivitiModelSerializer;
import org.eclipse.jwt.transformations.activiti.internal.JwtToActivitiTransformationUtility;
import org.eclipse.jwt.transformations.activiti.util.ActivitiGenerationProjectOptions;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringConfigSerializer;
import org.eclipse.jwt.transformations.activiti.util.monitoring.output.ActivitiMonitoringOutputConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.params.ActivitiMonitoringParamsConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.queries.ActivitiMonitoringQueriesConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.test.ActivitiMonitoringTestActivation;
import org.eclipse.jwt.transformations.activiti.util.monitoring.test.ActivitiMonitoringTestConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.test.ActivitiMonitoringTestConfigSerializer;
import org.eclipse.jwt.transformations.activiti.util.monitoring.test.ActivitiMonitoringTestDescriptor;
import org.eclipse.jwt.transformations.activiti.util.monitoring.test.ActivitiMonitoringTestProcessInstanceActivation;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class GenerateMonitoringUnitTestHandler implements IObjectActionDelegate {

	private IResource bpmnFile;
	
	public void run(IAction action) {
		try {
			final ActivitiGenerationProject activitiProject = ActivitiGenerationProjectFactory.findProject(
					this.bpmnFile.getProject().getName()
					/*ActivitiGenerationProject.resolveProjectName(JwtToActivitiTransformationUtility.getCurrentJwtModelFileName())*/);
			
			if(activitiProject != null) { 
				Bpmn2MemoryModel model = ActivitiModelSerializer.deserializeFromFile(
					this.bpmnFile.getRawLocation().toOSString());
			
				final String processId = model.getMainProcess().getId();
				final String monitoringUnitTestName = "ProcessMonitoringTestProcessId" + processId.substring(processId.indexOf("_") + 1 /* in order to remove the character "_" from the name */, processId.length());
			
				IPackageFragment pack = this.createMonitoringUnitTestPackage();
			
				ActivitiMonitoringFileUtility.createMonitoringUnitTestFile(monitoringUnitTestName, activitiProject, new IProjectPerformer(){
					public String onPerform() {
						try {
							activitiProject.createFileFromTemplate(
									ActivitiGenerationProjectFileTemplateFactory.createMonitoringOutputFileTemplate());
							
							
							activitiProject.createFileFromTemplate(
									ActivitiGenerationProjectFileTemplateFactory.createMonitoringTestFileTemplate(
											createMonitoringTestConfig(activitiProject, processId, monitoringUnitTestName)));
							
						} catch (Exception ex) {
							ex.printStackTrace();
						}
						
						return null;
					}
				});
		}
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		if (selection != null & selection instanceof IStructuredSelection) {
			IStructuredSelection strucSelection = (IStructuredSelection) selection;
			
			this.bpmnFile = (IResource) strucSelection.getFirstElement();
		}
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @throws CoreException 
	 * 
	 */
	private IPackageFragment createMonitoringUnitTestPackage() throws CoreException {
		IProject project = this.bpmnFile.getProject();
	    IFolder sourceFolder = project.getFolder(
	    		ActivitiGenerationProjectOptions.testJavaFolderPath);
	    
	    IJavaProject javaProject = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
	    IPackageFragmentRoot srcRoot = javaProject.getPackageFragmentRoot(sourceFolder);

	    IPackageFragment pack = srcRoot.createPackageFragment(
	    		ActivitiGenerationProjectOptions.testPackageName, false, null);
	    
	    return pack;
	}
	
	/**
	 * 
	 * @param monitoringUnitTestClassName
	 * @return
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	private String createMonitoringTestConfig(ActivitiGenerationProject activitiProject, String processId, String monitoringUnitTestClassName) throws XMLStreamException, IOException {
		// creates the test config file
		ActivitiMonitoringTestConfig config = new ActivitiMonitoringTestConfig();
		ActivitiMonitoringTestDescriptor descriptor = new ActivitiMonitoringTestDescriptor();
		ActivitiMonitoringTestActivation activation = new ActivitiMonitoringTestActivation();
		
		descriptor.setTestClass(monitoringUnitTestClassName);
		
		descriptor.setTestOutputPath("/" + activitiProject.getProject().getName() + "/" +
				ActivitiGenerationProjectOptions.testOutputFolderPath + "/" + ActivitiMonitoringOutputConfig.monitoringOutputConfigName);
		
		descriptor.setTestParamsPath("/" + activitiProject.getProject().getName() + "/" +
				ActivitiGenerationProjectOptions.monitoringFolderPath + "/" + ActivitiMonitoringParamsConfig.monitoringParamsConfigName);
		
		descriptor.setTestQueriesPath("/" + activitiProject.getProject().getName() + "/" +
				ActivitiGenerationProjectOptions.monitoringFolderPath + "/" + ActivitiMonitoringQueriesConfig.monitoringQueriesConfigName);
			
		activation.setModel("/" + activitiProject.getProject().getName() + "/" +
				ActivitiGenerationProjectOptions.diagramFolderPath + "/" + this.bpmnFile.getName());
		
		activation.setModelKey(processId);
		activation.setModelParamsKey(processId);
		
		ActivitiMonitoringTestProcessInstanceActivation instanceActivation = 
				new ActivitiMonitoringTestProcessInstanceActivation();
		
		instanceActivation.setNumberOfCurrentType(1);
		activation.getProcessInstanceActivations().add(instanceActivation);
				
		descriptor.getActivations().add(activation);	
		config.getDescriptors().add(descriptor);
		
		return new ActivitiMonitoringTestConfigSerializer().serialize(config);
	}

}
