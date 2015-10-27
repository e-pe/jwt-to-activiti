package org.eclipse.jwt.generation.activiti;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.activiti.designer.eclipse.common.ActivitiProjectNature;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;
import org.eclipse.jwt.core.activiti.ActivitiProjectFolderMap;
import org.eclipse.jwt.core.activiti.IArtifactBuilder;
import org.eclipse.jwt.core.activiti.IProjectDeployer;
import org.eclipse.jwt.core.activiti.IProjectProvider;
import org.eclipse.jwt.integration.activiti.ActivitiIntegrationProject;
import org.eclipse.jwt.integration.activiti.ActivitiIntegrationProjectFile;
import org.eclipse.jwt.integration.activiti.ActivitiIntegrationProjectFolderMap;
import org.eclipse.jwt.transformations.activiti.util.ActivitiGenerationProjectOptions;
import org.eclipse.jwt.transformations.activiti.util.ActivitiIntegrationProjectOptions;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringConfig;
import org.eclipse.jwt.transformations.activiti.util.monitoring.ActivitiMonitoringConfigParser;
import org.xml.sax.SAXException;

/**
 * 
 * @author Eugen Petrosean
 *
 */
public class ActivitiGenerationProject implements IProjectProvider, IProjectDeployer {
	private String projectName;
	private IProject project;
	private IJavaProject javaProject;
	private ActivitiIntegrationProject integrationProject;
		 
	/**
	 * 
	 * @return
	 */
	public static String resolveProjectName(String projectName){
		return "jwt-" + projectName + "-activiti";
	}
	
	/**
	 * 
	 */
	public ActivitiGenerationProject(String projectName) {		
		this.projectName = projectName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getProjectName() {
		return this.projectName;
	}
	
	/**
	 * 
	 * @return
	 */
	public IProject getProject() {
		return this.project;
	}
	
	/**
	 * 
	 * @return
	 */
	public IJavaProject getJavaProject() {
		return this.javaProject;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean exists() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();		
	    return root.getProject(this.projectName).exists();
	}
	
	/**
	 * 
	 */
	public ActivitiGenerationProject load() {
		try {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();		
			//create eclipse project
		    IProject project = root.getProject(this.projectName);
		    if(project.exists()){
		    	if (project.hasNature(JavaCore.NATURE_ID)) {
		    		this.project = project;
		    		this.javaProject = JavaCore.create(this.project);
		    	}    
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		return this;
	}
	
	/**
	 * 
	 */
	public ActivitiGenerationProject create() {
		try {
			
			this.createProject();			
			this.createSourceFolders();
			this.createIntegrationProject();
			this.createClasspathEntries();
			this.createOutputLocation();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
			        
	    return this;
	}
	
	/**
	 * @throws IOException 
	 * 
	 */
	private void createProject() throws CoreException, IOException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();	  
	    //create eclipse project
	    IProject project = root.getProject(this.projectName);
	    if(project.exists())
	        project.delete(true, null);

	    project.create(null);
	    project.open(null);

	    //set the java project nature
	    IProjectDescription description = project.getDescription();
	    
	    try {	    	
	    	description.setNatureIds(new String[] { 
	    			JavaCore.NATURE_ID, ActivitiProjectNature.NATURE_ID});
	    	project.setDescription(description, null);	    	
	    } catch (Exception ex) {	    
	    	description.setNatureIds(new String[] { 
	    			JavaCore.NATURE_ID });
	    	project.setDescription(description, null);
	    }
	    
	    //create java project
	    IJavaProject javaProject = JavaCore.create(project);	    
	    
	    this.project = project;
	    this.javaProject = javaProject;
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException 
	 */
	private void createIntegrationProject() throws IOException {    
		ActivitiIntegrationProject integrationProject = new ActivitiIntegrationProject();
		
		try { 
			ActivitiIntegrationProjectFolderMap folderMap = 
					new ActivitiIntegrationProjectFolderMap(this.project);
			
			folderMap.load();
								
			IFolder libsFolder = this.project.getFolder(ActivitiGenerationProjectOptions.libsFolderPath);
			
			this.createDeploymentArtifactsFromActivitiIntegrationProject(folderMap);
									
			IFile jarFile = this.project.getFile(ActivitiGenerationProjectOptions.libsFolderPath + "/" + integrationProject.getJarName());
			jarFile.create(null, true, null);
			
			this.compressPackage(libsFolder, 
					folderMap.getTempClassesFolder(), integrationProject.getJarName());
								
			folderMap.dispose();						
		} catch (Exception ex) {
			  ex.printStackTrace();
			  
		} 
		
		this.integrationProject = integrationProject;
	}
	
	/**
	 * 
	 */
	private IClasspathEntry[] createClasspathEntries() throws JavaModelException {
		IPath srcPathMainJava = this.javaProject.getPath().append("src/main/java");
		IPath srcPathMainResources = this.javaProject.getPath().append("src/main/resources");
		IPath srcPathTestJava = this.javaProject.getPath().append("src/test/java");
		IPath srcPathTestResources = this.javaProject.getPath().append("src/test/resources");
		IPath srcPathTestOutput = this.javaProject.getPath().append("src/test/output"); 		
		
		IPath srcPath5 = this.javaProject.getPath().append(
				ActivitiGenerationProjectOptions.libsFolderPath + "/" + 
						this.integrationProject.getJarName());
		
		IPath[] javaPath = new IPath[] { new Path("**/*.java") };
		IPath testOutputLocation = javaProject.getPath().append("target/test-classes");

		//IPath srcPathUserLibrary = new Path(ActivitiGenerationProject.activitiExtensionsUserLibPath);

		IClasspathEntry[] entries = { 
				JavaCore.newSourceEntry(srcPathMainJava, javaPath, null, null),
				JavaCore.newSourceEntry(srcPathMainResources, javaPath),
				JavaCore.newSourceEntry(srcPathTestJava, javaPath, null, testOutputLocation),
				JavaCore.newSourceEntry(srcPathTestResources, javaPath, testOutputLocation),
				JavaCore.newSourceEntry(srcPathTestOutput, javaPath),
				JavaCore.newLibraryEntry(srcPath5, null, null),
				JavaRuntime.getDefaultJREContainerEntry()/*, 
				JavaCore.newContainerEntry(srcPathUserLibrary)*/ };
		
	    this.javaProject.setRawClasspath(entries, null);
		
		return entries;
	}
	
	private void createOutputLocation() throws JavaModelException {
		IPath targetPath = this.javaProject.getPath().append("target/classes");
		this.javaProject.setOutputLocation(targetPath, null);
	}
	
	/**
	 * 
	 * @param fileTemplate
	 * @return
	 */
	public ActivitiGenerationProject createFileFromTemplate(ActivitiGenerationProjectFileTemplate fileTemplate){
		try {
			
			InputStream source = null;
			Object content = fileTemplate.getContent();
			IFile file = this.project.getFile(fileTemplate.getPath());
			
			if(content instanceof String) 
				source = new ByteArrayInputStream(((String)content).getBytes()); 
			
			else if(content instanceof InputStream)
				source = (InputStream)content;
				
			file.create(source, true, null);
			source.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return this;
	}
	
	/**
	 * 
	 * @throws CoreException
	 */
	private void createSourceFolders() throws CoreException{
		List<String> sourceFolders = Collections.synchronizedList(new LinkedList<String>());
	
		sourceFolders.add(ActivitiGenerationProjectOptions.libsFolderPath);
		sourceFolders.add(ActivitiGenerationProjectOptions.srcFolderPath);
		sourceFolders.add(ActivitiGenerationProjectOptions.mainFolderPath);
		sourceFolders.add(ActivitiGenerationProjectOptions.javaFolderPath);
		sourceFolders.add(ActivitiGenerationProjectOptions.monitoringListenerJavaFolderPath);
		sourceFolders.add(ActivitiGenerationProjectOptions.serviceTaskJavaFolderPath);
		sourceFolders.add(ActivitiGenerationProjectOptions.resourceFolderPath);
		sourceFolders.add(ActivitiGenerationProjectOptions.diagramFolderPath);
		sourceFolders.add(ActivitiGenerationProjectOptions.monitoringFolderPath);
		sourceFolders.add(ActivitiGenerationProjectOptions.testFolderPath);
		sourceFolders.add(ActivitiGenerationProjectOptions.testJavaFolderPath);
		sourceFolders.add(ActivitiGenerationProjectOptions.testResourceFolderPath);
		sourceFolders.add(ActivitiGenerationProjectOptions.testOutputFolderPath);
		
		for (String folder : sourceFolders) {
			IFolder sourceFolder = this.project.getFolder(folder);
			sourceFolder.create(false, true, null);
		}
	}
	
	/**
	 * 
	 */
	public void createDeploymentArtifacts(IArtifactBuilder builder) {
		try {
			IFolder diagramFolder = this.project.getFolder(
					ActivitiGenerationProjectOptions.diagramFolderPath);
		
			if(diagramFolder == null) 
				return;	
		
			ActivitiGenerationProjectFolderMap folderMap = 
					new ActivitiGenerationProjectFolderMap(this.project);
			
			folderMap.load();
			
			// the order of invocation of the following methods is important 
			// because the second method generates a jar file with required classes
			this.createDeploymentArtifactsFromActivitiIntegrationProject(folderMap);
			this.createDeploymentArtifactsFromActivitiGenerationProject(folderMap);

			
			folderMap.dispose();
		
		} catch(Exception ex) {
			
		}
	}
	
	/**
	 * 
	 */
	private void createDeploymentArtifactsFromActivitiGenerationProject(ActivitiGenerationProjectFolderMap folderMap){
		try {						
			fillTempBarFolderWithBpmn(
					folderMap.getTempBarFolder());	
			
			fillTempBarFolderWithPng(
					folderMap.getTempBarFolder());		
			
			compressPackage(
					folderMap.getDeploymentFolder(), 
					folderMap.getTempBarFolder(), 
					this.projectName + ".bar");
			
			boolean hasToCreateJar = fillTempClassesFolderWithClasses(
					folderMap.getTempClassesFolder());	
			
			if(hasToCreateJar) {
				compressPackage(folderMap.getDeploymentFolder(),					 
						folderMap.getTempClassesFolder(), this.projectName + ".jar"); 
						
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	private void createDeploymentArtifactsFromActivitiIntegrationProject(final ActivitiProjectFolderMap folderMap){
		ActivitiIntegrationProject integrationProject = new ActivitiIntegrationProject();
		
		integrationProject.createDeploymentArtifacts(new IArtifactBuilder<ArrayList<ActivitiIntegrationProjectFile>>() {
			public void onBuild(ArrayList<ActivitiIntegrationProjectFile> projectFiles) {
				try {
					HashMap<String, IFolder> createdFolders = new HashMap<String, IFolder>();
					
					IFolder lastProjectFolder = folderMap.getTempClassesFolder();
					
					for(int i = 0; i < projectFiles.size(); i++) {
						ActivitiIntegrationProjectFile projectFile = projectFiles.get(i);
						
						File file = projectFile.getFile();						
						String filePath = projectFile.getNormalizedFilePath();
					
						if(file.isDirectory()) {
							String folderPath = filePath;
							
							//gets the project folder which is relative to the last one
							IFolder projectFolder = lastProjectFolder.getFolder(file.getName());
							
							if(createdFolders.containsKey(folderPath))
								projectFolder = createdFolders.get(folderPath).getFolder(file.getName());
														
							if(!projectFolder.exists()){
								//projectFolder.delete(true, new NullProgressMonitor());								
								projectFolder.create(true, true, new NullProgressMonitor());
							}
							
							//lastProjectFolder = projectFolder;
							
							createdFolders.put(
									normalizeProjectFolderPath(
											folderMap.getTempClassesFolder(), projectFolder), projectFolder);
						
						} else {
							String folderPath = filePath;
							
							IFolder projFolder = createdFolders.get(folderPath);
							
							IFile projFile = projFolder.getFile(file.getName());
							
							if(projFile.exists())
								projFile.delete(true, new NullProgressMonitor());
							
							projFile.create(new FileInputStream(file), true, new NullProgressMonitor());							
						}
					}
					
					copyMonitoringConfigFile(createdFolders);
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
				
			}
		});
	}
	
	/**
	 * @throws CoreException 
	 * 
	 */
	private void copyMonitoringConfigFile(HashMap<String, IFolder> folders) throws CoreException {
		IFolder monitoringFolder = this.project.getFolder(ActivitiGenerationProjectOptions.monitoringFolderPath);
		IFile monitoringFile = monitoringFolder.getFile(ActivitiMonitoringConfig.monitoringConfigName);
		
		if(!monitoringFolder.exists() || !monitoringFile.exists())
			return;
		
		// copies the monitoring file to the right place within the integration project
		Iterator<String> ieKeys = folders.keySet().iterator();
		
		while(ieKeys.hasNext()) {
			String key = ieKeys.next();
			
			if(key.contains(ActivitiIntegrationProjectOptions.resourcesFolderPath)) {
				IFolder resourceFolder = folders.get(key);
										
				IFile resourceFile = resourceFolder.getFile(ActivitiMonitoringConfig.monitoringConfigName);
																							
				monitoringFile.copy(resourceFile.getFullPath(), true, new NullProgressMonitor());
			
			}
				
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private String normalizeProjectFolderPath(IFolder rootFolder, IFolder currentFolder) {
		String rootFolderPath =  rootFolder.getFullPath().toString();
		String currentFolderPath = currentFolder.getFullPath().toString();
		
		String path = currentFolderPath.replace(rootFolderPath, "");
		
		return path;
	}
		
	/**
	 * @throws CoreException 
	 * @throws SAXException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * 
	 */
	private void fillTempBarFolderWithBpmn(IFolder tempBarFolder) throws CoreException, IOException, ParserConfigurationException, SAXException {
        ArrayList<IFile> members = new ArrayList<IFile>();
        
        this.getMembersWithFilter(this.project, ".bpmn", members);
        this.getMembersWithFilter(this.project, ".bpmn20.xml", members);

        for (IFile bpmnResource : members) {
            String bpmnFilename = bpmnResource.getName();
                        
            //TODO temp fix because .bpmn files are not parsed by the Activiti Engine version 5.9. This is fixed for 5.10
            if(bpmnFilename.endsWith(".bpmn")) {
            	bpmnFilename = bpmnFilename.substring(0, bpmnFilename.lastIndexOf(".")) + ".bpmn20.xml";
            }
            
            ActivitiMonitoringConfigParser configParser = new ActivitiMonitoringConfigParser();
            String outputBpmn = configParser.parseByClass(
            		bpmnResource.getContents(), this.project.getFile(
            				ActivitiGenerationProjectOptions.monitoringFolderPath + ActivitiMonitoringConfig.monitoringConfigName).getContents());
            
            IFile deployBpmn = tempBarFolder.getFile(bpmnFilename);
            deployBpmn.create(new ByteArrayInputStream(outputBpmn.getBytes()), true, null);
            
            //bpmnResource.copy(tempBarFolder.getFile(bpmnFilename).getFullPath(), true, new NullProgressMonitor());

        }
	}
	
	/**
	 * 
	 * @param tempBarFolder
	 * @throws CoreException
	 */
	private void fillTempBarFolderWithPng(IFolder tempBarFolder) throws CoreException {
		ArrayList<IFile> members = new ArrayList<IFile>();
		
        this.getMembersWithFilter(this.project, ".png", members);
        
        for (IFile pngResource : members) {
          String pngFilename = pngResource.getName();
          
          pngResource.copy(tempBarFolder.getFile(pngFilename).getFullPath(), true, new NullProgressMonitor());
        }
	}
	
	/**
	 * 
	 * @param tempClassesFolder
	 * @throws Exception 
	 */
	private boolean fillTempClassesFolderWithClasses(IFolder tempClassesFolder) throws Exception {
		 IFolder classesFolder = this.project.getFolder("target/classes");
		 ArrayList<IFile> members = new ArrayList<IFile>();
         
         this.getMembersWithFilter(classesFolder, ".class", members);
         this.getMembersWithFilter(classesFolder, ".gif", members);
         
         for (IFile classResource : members) {
        	 String classFilename = classResource.getName();
             IPath packagePath = classResource.getFullPath().removeFirstSegments(3).removeLastSegments(1);
             IFolder newPackageFolder = tempClassesFolder.getFolder(packagePath);
             createFolderStructure(newPackageFolder);
             
             classResource.copy(newPackageFolder.getFile(classFilename).getFullPath(), true, new NullProgressMonitor());
         }
          
         return !members.isEmpty();  
	}
	
	/**
	 * 
	 * @param root
	 * @param extension
	 * @param members
	 */
	private void getMembersWithFilter(IContainer root, String extension, ArrayList<IFile> members) {		
		try {
			for (IResource resource : root.members()) {
				if (resource instanceof IFile) {
					if(resource.getName().endsWith(extension)) {
						members.add((IFile) resource);
					}
				} else if(resource instanceof IFolder && ((IFolder) resource).getName().contains("target") == false) {
					getMembersWithFilter((IFolder) resource, extension, members);
				}
			}
		  } catch(Exception e) {
		    e.printStackTrace();
		  }
	}
	
	/**
	 * 
	 * @param destination
	 * @param folderToPackage
	 * @param fileName
	 * @throws Exception
	 */
	private void compressPackage(final IFolder destination, final IFolder folderToPackage, final String fileName) throws Exception {
	    final IWorkspace workspace = ResourcesPlugin.getWorkspace();
	    
	    File base = folderToPackage.getLocation().toFile();
	    final IFile archiveFile = workspace.getRoot().getFile(
	            destination.getFile(fileName).getFullPath());
	    
	    final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(archiveFile.getLocation().toFile()));
	    final String absoluteDirPathToStrip = folderToPackage.getLocation().toFile().getAbsolutePath() + File.separator;
	    
	    try {
	    	zipDirectory(out, base, absoluteDirPathToStrip);
	    } finally {
	    	if (out != null) {
	    		out.close();
	    	}
	    }
	}
	
	/**
	 * 
	 * @param out
	 * @param base
	 * @param absoluteDirPathToStrip
	 * @throws Exception
	 */
	private void zipDirectory(final ZipOutputStream out, final File base, final String absoluteDirPathToStrip) throws Exception {
	    File[] reportFiles = base.listFiles();
	    for (final File file : reportFiles) {
	    	
	    	if (file.isDirectory()) {
	    		zipDirectory(out, file, absoluteDirPathToStrip);
	    		continue;
	    	}
	      
	    	String entryName = StringUtils.removeStart(file.getAbsolutePath(), absoluteDirPathToStrip);
	    	entryName = backlashReplace(entryName);
	    	ZipEntry entry = new ZipEntry(entryName);
	        out.putNextEntry(entry);
	        
	        if (file.isFile()) {
	        	FileInputStream fin = new FileInputStream(file);
	        	byte[] fileContent = new byte[(int)file.length()];
	        	fin.read(fileContent);
	        	out.write(fileContent);
	        	fin.close();
	        }
	        
	        out.closeEntry();
	    }
	}
	
	/**
	 * 
	 * @param myStr
	 * @return
	 */
	private String backlashReplace(String myStr){
		  final StringBuilder result = new StringBuilder();
		  final StringCharacterIterator iterator = new StringCharacterIterator(myStr);
		  char character =  iterator.current();
		  while (character != CharacterIterator.DONE ){
		     
			  if (character == '\\') {
		         result.append("/");
		      }
		      else {
		        result.append(character);
		      }
     
		      character = iterator.next();
		  }
		  return result.toString();
	}
	
	/**
	 * 
	 * @param newFolder
	 * @throws Exception
	 */
	private void createFolderStructure(IFolder newFolder) throws Exception {
		if(newFolder.exists()) return;
		  
		if(newFolder.getParent().exists() == false) {
			createFolderStructure((IFolder) newFolder.getParent());
		}
		  
		newFolder.create(true, true, new NullProgressMonitor());
	}					
}
