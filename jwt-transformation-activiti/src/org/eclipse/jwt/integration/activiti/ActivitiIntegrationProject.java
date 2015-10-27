package org.eclipse.jwt.integration.activiti;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jwt.core.activiti.IArtifactBuilder;
import org.eclipse.jwt.core.activiti.IProjectDeployer;
import org.eclipse.jwt.transformations.activiti.util.ActivitiIntegrationProjectOptions;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiIntegrationProject implements IProjectDeployer {	
		
	/**
	 * 
	 * @return
	 */
	public String getJarPath() {
		return ActivitiIntegrationProjectOptions.libFolderPath + "/" + this.getJarName();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getJarName() {
		return ActivitiIntegrationProjectOptions.integrationProjectName + ".jar";
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void createDeploymentArtifacts(IArtifactBuilder builder) {
		try {
			ArrayList<ActivitiIntegrationProjectFile> pluginFiles = new ArrayList<ActivitiIntegrationProjectFile>();
			
			//returns the bin folder path of the jwt-transformation-activiti-integration plugin
			URL integrationBinBundleRoot = Platform.getBundle(ActivitiIntegrationProjectOptions.integrationPluginName)
					.getEntry("/"+ ActivitiIntegrationProjectOptions.binFolderPath +"/");
			
			//returns the src folder path of the jwt-transformation-activiti-integration plugin
			URL integrationSrcBundleRoot = Platform.getBundle(ActivitiIntegrationProjectOptions.integrationPluginName)
					.getEntry("/"+ ActivitiIntegrationProjectOptions.srcFolderPath +"/");
			
			//returns the bin folder path of the jwt-transformation-activiti-util plugin
			URL utilBinBundleRoot = Platform.getBundle(ActivitiIntegrationProjectOptions.utilPluginName)
					.getEntry("/"+ ActivitiIntegrationProjectOptions.binFolderPath +"/");
			
			//returns the src folder path of the jwt-transformation-activiti-util plugin
			URL utilSrcBundleRoot = Platform.getBundle(ActivitiIntegrationProjectOptions.utilPluginName)
					.getEntry("/"+ ActivitiIntegrationProjectOptions.srcFolderPath +"/");
			
			URL utilBinURL = FileLocator.toFileURL(utilBinBundleRoot);	
			URL utilSrcURL = FileLocator.toFileURL(utilSrcBundleRoot);	
			
			URL integrationBinURL = FileLocator.toFileURL(integrationBinBundleRoot); 
			URL integrationSrcURL = FileLocator.toFileURL(integrationSrcBundleRoot);
			
			
			this.findPluginFiles(
					utilBinURL.toURI(), pluginFiles,
					ActivitiIntegrationProjectOptions.utilProjectName + "/" + 
					ActivitiIntegrationProjectOptions.binFolderPath);
			
			this.findPluginFiles(
					utilSrcURL.toURI(), pluginFiles,
					ActivitiIntegrationProjectOptions.utilProjectName + "/" + 
					ActivitiIntegrationProjectOptions.srcFolderPath);
			
			this.findPluginFiles(
					integrationBinURL.toURI(), pluginFiles, 
					ActivitiIntegrationProjectOptions.integrationProjectName + "/" + 
					ActivitiIntegrationProjectOptions.binFolderPath);
			
			this.findPluginFiles(
					integrationSrcURL.toURI(), pluginFiles, 
					ActivitiIntegrationProjectOptions.integrationProjectName + "/" + 
					ActivitiIntegrationProjectOptions.srcFolderPath);
			
			if(builder != null)
				builder.onBuild(pluginFiles);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param path
	 * @param pluginFiles
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private void findPluginFiles(URI path, ArrayList<ActivitiIntegrationProjectFile> pluginFiles, String projectPath) throws IOException, URISyntaxException {	
		 File root = new File(path);
		 File[] list = root.listFiles();

         for (File f : list) {
        	 ActivitiIntegrationProjectFile projectFile = 
        			 new ActivitiIntegrationProjectFile();
        	 
        	 projectFile.setFile(f);
        	 projectFile.setProjectPath(projectPath);
        	 
        	 pluginFiles.add(projectFile);
        	
        	if(f.isDirectory())
        		this.findPluginFiles(f.toURI(), pluginFiles, projectPath);
         }
	}
}
