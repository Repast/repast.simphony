/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greifs Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. Norths Modifications are Copyright 2007 Under
 * the Repast Simphony License, All Rights Reserved)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of the Flow4J-Eclipse project nor
 * the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package repast.simphony.agents.designer.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.variables.IStringVariable;
import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import repast.simphony.agents.base.Util;
import repast.simphony.agents.designer.ui.editors.AgentEditor;
//import org.acm.seguin.util.FileSettings;

/**
 * The main plugin class to be used in the desktop.
 */
@SuppressWarnings({ "unchecked" })
public class AgentBuilderPlugin extends AbstractUIPlugin {


  
  public static final String AGENT_BUILDER_PLUGIN_ID = "repast.simphony.flowchart";
  public static final String AGENT_BUILDER_RESOURCES_VIEW_ID = AGENT_BUILDER_PLUGIN_ID
      + ".agentbuilder_resources_view";
  public static final String AGENT_BUILDER_BUILDER_ID = AGENT_BUILDER_PLUGIN_ID
      + ".repast_simphony_flowchart_builder";
  public static final String AGENT_BUILDER_REPOSITORY_BUILDER_ID = AGENT_BUILDER_PLUGIN_ID
      + ".repast_simphony_repository_builder";
  public static final String RS_FILE_EXTENSION_CODE = "REPAST_FILE_EXTENSION_";
  public static final ArrayList<String> AGENT_BUILDER_FILE_EXTENSIONS = new ArrayList<String>();
  public static final String AGENT_BUILDER_NATURE_ID = AGENT_BUILDER_PLUGIN_ID + ".repast_simphony_flowchart_nature";
  {
    IStringVariable[] variables = VariablesPlugin.getDefault().getStringVariableManager()
        .getValueVariables();
    for (int i = 0; i < variables.length; i++) {
      if (variables[i].getName().startsWith(RS_FILE_EXTENSION_CODE)
          && variables[i] instanceof IValueVariable) {
        AGENT_BUILDER_FILE_EXTENSIONS.add(((IValueVariable) variables[i]).getValue());
      }
    }
  }
  
  // The shared instance.
  private static AgentBuilderPlugin plugin;
  // Resource bundle.
  private ResourceBundle resourceBundle;
  // private static FileSettings prettySettings;
  private static IType agentBaseType; // Peter Friese

 

  /**
   * The constructor.
   */
  public AgentBuilderPlugin() {
    super();
    plugin = this;
    try {
      resourceBundle = ResourceBundle.getBundle("AgentBuilderPluginResources");
    } catch (MissingResourceException x) {
      resourceBundle = null;
      x.printStackTrace();
    }
  }

  /**
   * Returns the shared instance.
   */
  public static AgentBuilderPlugin getDefault() {
    return plugin;
  }

  /*
   * public static List getProjectClasspathPaths(IProject project) { List
   * classpathPaths = new ArrayList();
   * AgentBuilderPlugin.getResolvedClasspath(project, classpathList); for
   * (Iterator iter = classpathList.iterator(); iter.hasNext();) { IPath path =
   * (IPath) iter.next(); runtimeLibs.add(path.toFile().toURL()); } }
   */

  /**
   * Converts Paths to URLs
   */
  public static List convertPaths2URLs(List pathList) throws MalformedURLException {
    List result = new ArrayList();
    for (Iterator iter = pathList.iterator(); iter.hasNext();) {
      IPath path = (IPath) iter.next();
      if (path != null)
        result.add(path.toFile().toURI().toURL());
    }
    return result;
  }

  /**
   * Converts Files to URLs
   */
  public static List convertPaths2Files(List pathList) throws MalformedURLException {
    List result = new ArrayList();
    for (Iterator iter = pathList.iterator(); iter.hasNext();) {
      IPath path = (IPath) iter.next();
      if (path != null)
        result.add(path.toFile().getAbsoluteFile());
    }
    return result;
  }

  /**
   * Creates a folder in the outputFolder
   * 
   * @param outputFolder
   *          the root folder
   * @param folderPath
   *          the relative path of the folder to create
   * @return the created folder
   * @throws CoreException
   *           if the creation fails
   */
  public static IContainer createFolder(IContainer outputFolder, IPath folderPath, boolean derived)
      throws CoreException {
    if (folderPath.isEmpty())
      return outputFolder;
    IFolder folder = outputFolder.getFolder(folderPath);
    if (!folder.exists()) {
      createFolder(outputFolder, folderPath.removeLastSegments(1), derived);
      folder.create(true, true, null);
      // if set to "true" then deleting the package only deletes
      // the last path element
      // "false" -> on deletion of the package the complete folder tree is
      // deleted
      folder.setDerived(derived);
    }
    return folder;
  }

  /**
   * Convenience method to get the java model.
   */
  public static IJavaModel getJavaModel() {
    return JavaCore.create(getWorkspaceRoot());
  }

  /**
   * Returns the workspace instance.
   */
  public static IWorkspace getWorkspace() {
    return ResourcesPlugin.getWorkspace();
  }

  /**
   * Convenience method to get the workspace root.
   */
  public static IWorkspaceRoot getWorkspaceRoot() {
    return AgentBuilderPlugin.getWorkspace().getRoot();
  }

  /**
   * Convenience method to get the active workbench window.
   * 
   * @return the active workbench window.
   */
  public static IWorkbenchWindow getActiveWorkbenchWindow() {
    return getDefault().getWorkbench().getActiveWorkbenchWindow();
  }

  /**
   * Returns the active workbench page.
   * 
   * @return the active workbench page.
   */
  public static IWorkbenchPage getActivePage() {
    AgentBuilderPlugin plugin = getDefault();
    IWorkbenchWindow window = plugin.getWorkbench().getActiveWorkbenchWindow();
    if (window == null)
      return null;

    return plugin.getWorkbench().getActiveWorkbenchWindow().getActivePage();
  }

  /**
   * Convenience method to retrieve the Java project containing file in the
   * currently opened editor.
   * 
   * @return The active Java project.
   * @author Peter Friese
   */
  /*
   * public static IJavaProject getActiveJavaProject() { AgentEditor editor =
   * AgentBuilderPlugin.getActiveFlowEditor(); if (editor != null) {
   * IEditorInput input = editor.getEditorInput(); if (input != null) { Object
   * adapter = input.getAdapter(IResource.class); if ((adapter != null) &&
   * (adapter instanceof IResource)) { IResource resource = (IResource) adapter;
   * IProject project = resource.getProject(); IJavaProject javaProject =
   * JavaCore.create(project); return javaProject; } } } return null; }
   */

  /**
   * Convenience method to retrieve the Java project containing file in the
   * currently opened editor.
   * 
   * @return The active Java project.
   */
  public static IJavaProject getActiveJavaProject() {
    return AgentBuilderPlugin.getJavaProject(AgentBuilderPlugin.getActiveProject());
  }

  /**
   * Convenience method to retrieve the project containing file in the currently
   * opened editor.
   * 
   * @return The active project.
   */
  public static IProject getActiveProject() {
    return AgentBuilderPlugin.getActiveFlowEditor().getFlowFile().getProject();
  }

  /**
   * Retrieves the source type of agents.
   * 
   * @return The source type of agents.
   * @author Peter Friese (Updated by Michael J. North)
   */
  public static IType getAgentBaseType() {
    if (AgentBuilderPlugin.agentBaseType == null) {
      try {
        AgentBuilderPlugin.agentBaseType = AgentBuilderPlugin.getActiveJavaProject().findType(
            "java.lang.Object");
      } catch (JavaModelException e) {
        e.printStackTrace();
      }
    }
    return AgentBuilderPlugin.agentBaseType;
  }

  /**
   * Returns the java project with the given name
   * 
   * @param projectName
   *          project name
   * @return the java project with the given name
   */
  public static IJavaProject getJavaProject(String projectName) {
    return getJavaModel().getJavaProject(projectName);
  }

  /**
   * Returns the java project with the name of the given project
   * 
   * @param project
   *          project taht is maybe a java project
   * @return the java project with the name of the given project
   */
  public static IJavaProject getJavaProject(IProject project) {
    return getJavaProject(project.getName());
  }

  /**
   * TODO
   * 
   * @param javaProject
   * @return
   */
  public static IProject getProject(IJavaProject javaProject) {
    return getWorkspaceProject(javaProject.getPath());
  }

  /**
   * Returns a sorted list of projects. Sorted means that the longer comes first
   * if two project names have the same prefix. The projects can be open or
   * closed.
   * 
   * @return a sorted list of projects.
   */
  private static IProject[] getWorkspaceProjects() {
    IProject[] result;
    result = getWorkspaceRoot().getProjects();
    // We have to sort the list of project names to make sure that we cut of
    // the longest
    // project from the path, if two projects with the same prefix exist.
    // For example
    // org.eclipse.jdt.ui and org.eclipse.jdt.ui.tests.
    Arrays.sort(result, new Comparator() {
      public int compare(Object o1, Object o2) {
        int l1 = ((IProject) o1).getName().length();
        int l2 = ((IProject) o2).getName().length();
        if (l1 < l2)
          return 1;
        if (l2 < l1)
          return -1;
        return 0;
      }

      @Override
      public boolean equals(Object obj) {
        return super.equals(obj);
      }
    });
    return result;
  }

  /**
   * Returns a project which has the given path or <code>null</code> if none
   * found.
   * 
   * @param path
   *          the path of the project
   * @return a project which has the given path or <code>null</code>
   */
  private static IProject getWorkspaceProject(IPath path) {
    IProject[] projects = getWorkspaceProjects();
    for (int i = 0; i < projects.length; i++) {
      IProject project = projects[i];
      if (project.getFullPath().equals(path))
        return project;
    }
    return null;
  }

  /**
   * Returns a list of <code>IPath</code> classpath entries of the java project.
   * 
   * @param project
   *          the java project
   * @return list of <code>IPath</code> classpath entries of the java project.
   * @throws JavaModelException
   */
  /*
   * public static List getClasspathEntryLocations(IJavaProject project) throws
   * JavaModelException { List locations = new ArrayList(); for (int j = 0; j <
   * project.getAllPackageFragmentRoots().length; j++) { IPackageFragmentRoot
   * frag = project.getAllPackageFragmentRoots()[j]; IClasspathEntry cp =
   * frag.getRawClasspathEntry(); IPath path = cp.getPath(); if
   * (cp.getEntryKind() == IClasspathEntry.CPE_SOURCE || cp.getEntryKind() ==
   * IClasspathEntry.CPE_CONTAINER) continue; if (cp.getEntryKind() ==
   * IClasspathEntry.CPE_VARIABLE) { cp =
   * JavaCore.getResolvedClasspathEntry(cp); path = cp.getPath(); } if
   * (cp.getEntryKind() == IClasspathEntry.CPE_LIBRARY) { IResource resource =
   * AgentBuilderPlugin.getWorkspaceRoot().findMember(path); if (resource !=
   * null) path = resource.getLocation(); } locations.add(path); }
   * 
   * return locations; }
   */

  /**
   * Returns the editor id for the given file.
   */
  public static String getEditorID(String file) {
    IEditorRegistry registry = PlatformUI.getWorkbench().getEditorRegistry();
    IEditorDescriptor descriptor = registry.getDefaultEditor(file);
    if (descriptor != null)
      return descriptor.getId();
    return null;
  }

  /**
   * TODO
   * 
   * @param project
   * @return
   * @throws JavaModelException
   */
  public static IPath[] getClasspathSourceContainers(IProject project) throws JavaModelException {
    IJavaProject javaProject = AgentBuilderPlugin.getJavaProject(project);
    List folders = new ArrayList();
    IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);
    for (int i = 0; i < entries.length; i++) {
      IClasspathEntry entry = entries[i];
      if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
        folders.add(entry.getPath());
      }
    }
    return (IPath[]) folders.toArray(new Path[folders.size()]);
  }

  /**
   * Returns the classpath of the java file.
   * 
   * @param file
   *          the java file
   * @return the classpath of the java file or <code>null</code> if it cannot
   *         determined.
   * @throws JavaModelException
   *           if the classpath cannot be determined
   */
  static public IPath getClassPath(IFile file) throws JavaModelException {
    IPath filePath = file.getProjectRelativePath();
    IPath[] scPaths = AgentBuilderPlugin.getClasspathSourceContainers(file.getProject());
    for (int i = 0; i < scPaths.length; i++) {
      IPath scPath = scPaths[i];
      // remove project segment at the beginning
      scPath = scPath.removeFirstSegments(1);
      if (scPath.isPrefixOf(filePath)) {
        return filePath.removeFirstSegments(scPath.segmentCount());
      }
    }
    return null;
  }

  /**
   * TODO
   * 
   * @param agentEditor
   * @param pathEntries
   */
  public static void getResolvedClasspath(AgentEditor agentEditor, List pathEntries) {
    getResolvedClasspath(((FileEditorInput) agentEditor.getEditorInput()).getFile(), pathEntries);
  }

  /**
   * TODO
   * 
   * @param file
   * @param pathEntries
   */
  public static void getResolvedClasspath(IFile file, List pathEntries) {
    getResolvedClasspath(file.getProject(), pathEntries);
  }

  /**
   * TODO
   * 
   * @param project
   * @param pathEntries
   */
  public static void getResolvedClasspath(IProject project, List pathEntries) {
    getResolvedClasspath(project.getName(), pathEntries);
  }

  /**
   * Adds classpath entries to the supported list.
   * 
   * @param projectName
   * @param pathEntries
   */
  public static void getResolvedClasspath(String projectName, List pathEntries) {

    IJavaProject javaProject = AgentBuilderPlugin.getJavaProject(projectName);
    IPath path = null;
    try {
      IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);
      for (int i = 0; i < entries.length; i++) {
        IClasspathEntry entry = entries[i];
        path = null;
        if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
          path = getWorkspaceRoot().getLocation().append(
              JavaCore.getResolvedClasspathEntry(entry).getPath());
        } else if (entry.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
          path = JavaCore.getResolvedClasspathEntry(entry).getPath();
        } else if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
          path = entry.getPath().makeAbsolute();
          if (!path.toFile().getAbsoluteFile().exists()) {
            IPath location = getWorkspaceRoot().getProject(entry.getPath().segment(0))
                .getFile(entry.getPath().removeFirstSegments(1)).getLocation();
            if (location != null) {
              File tmpFile = location.toFile();
              if (tmpFile.exists())
                path = location;
            }
          }
        }
        if (path != null && !pathEntries.contains(path))
          pathEntries.add(path);

        if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
          IProject requiredProject = getWorkspaceProject(entry.getPath());
          // recurse into projects
          if (requiredProject != null)
            getResolvedClasspath(requiredProject, pathEntries);
        }
      }
      IPath outputPath = javaProject.getOutputLocation();
      if (outputPath.segmentCount() == 1)
        outputPath = javaProject.getResource().getLocation();
      else
        outputPath = javaProject.getProject().getFile(outputPath.removeFirstSegments(1))
            .getLocation();
      if (outputPath != null && !pathEntries.contains(outputPath))
        pathEntries.add(outputPath);
    } catch (JavaModelException e) {
      AgentBuilderPlugin.log(e);
    }
  }

  /**
   * Returns true if the given project is accessible and it has a java nature,
   * otherwise false.
   */
  public static boolean hasJavaNature(IProject project) {
    try {
      return project.hasNature(JavaCore.NATURE_ID);
    } catch (CoreException e) {
      // project does not exist or is not open
    }
    return false;
  }

  /**
   * Returns the active editor open in this page.
   * <p>
   * This is the visible editor on the page, or, if there is more than one
   * visible editor, this is the one most recently brought to top.
   * </p>
   * 
   * @return the active editor, or <code>null</code> if no editor is active
   */
  public static IEditorPart getActiveEditor() {
    AgentBuilderPlugin plugin = AgentBuilderPlugin.getDefault();
    IWorkbench wb = plugin.getWorkbench();
    IWorkbenchWindow ww = wb.getActiveWorkbenchWindow();
    IWorkbenchPage wp = ww.getActivePage();
    if (wp == null) {
      return null;
    } else {
      IEditorPart ep = wp.getActiveEditor();
      return ep;
    }
  }

  /**
   * Returns the active AgentEditor open in this page or <code>null</code> if
   * the active editor is not of type AgentEditor.
   * 
   * @return the active AgentEditor open in this page or <code>null</code> if
   *         the active editor is not of type AgentEditor.
   */
  public static AgentEditor getActiveFlowEditor() {
    IEditorPart editor = getActiveEditor();

    return (editor instanceof AgentEditor) ? (AgentEditor) editor : null;
  }

 

  public static String pluginInstallationDirectory = null;

  public static String getPluginInstallationDirectory() {
    if (pluginInstallationDirectory == null) {

      try {
        URL relativeURL = FileLocator.find(AgentBuilderPlugin.getDefault().getBundle(),
            new Path(""), null);
        URL absoluteURL = FileLocator.resolve(relativeURL);
        String tempURLString = URLDecoder.decode(absoluteURL.getFile(),
            System.getProperty("file.encoding"));
        String mainDataSourcePluginFile = new File(tempURLString).getPath();
        IPath mainDataSourcePluginFilePath = new Path(mainDataSourcePluginFile);
        IPath mainDataSourcePluginDirectoryPath = mainDataSourcePluginFilePath
            .removeLastSegments(1);

        pluginInstallationDirectory = mainDataSourcePluginDirectoryPath.toFile().toString()
            + System.getProperty("file.separator");

      } catch (Exception e) {
        e.printStackTrace();
        Util.printMessageBox("Exception in getPluginInstallationDirectory " + e.getMessage());
      }

    }
    return pluginInstallationDirectory;
  }

  static public String getSymbolicName() {
    return AgentBuilderPlugin.getDefault().getBundle().getSymbolicName();
  }

  static public URL getInstallURL() {
    return AgentBuilderPlugin.getDefault().getBundle().getEntry("/");
  }

  static public String getInstallFolderPathStr() throws IOException {
    return FileLocator.resolve(AgentBuilderPlugin.getInstallURL()).getPath();
  }

  static public URL getInstallFolderPathURL() throws IOException {
    return FileLocator.resolve(AgentBuilderPlugin.getInstallURL());
  }

  static public IPath getInstallFolderPath() throws IOException {
    return new Path(getInstallFolderPathStr());
  }

  static public String getPluginInstallFolderPathStr(String pluginId) throws IOException {
    return FileLocator.resolve(Platform.getBundle(pluginId).getEntry("/")).getPath();
  }

  static public URL getPluginInstallFolderPathURL(String pluginId) throws IOException {
    return FileLocator.resolve(Platform.getBundle(pluginId).getEntry("/"));
  }

  static public IPath getPluginInstallFolderPath(String pluginId) throws IOException {
    return new Path(getPluginInstallFolderPathStr(pluginId));
  }

  /**
   * Returns the string from the plugin's resource bundle, or 'key' if not
   * found.
   */
  public static String getResourceString(String key) {
    ResourceBundle bundle = AgentBuilderPlugin.getDefault().getResourceBundle();
    try {
      return (bundle != null ? bundle.getString(key) : key);
    } catch (MissingResourceException e) {
      return key;
    }
  }

  /**
   * Returns the plugin's resource bundle,
   */
  public ResourceBundle getResourceBundle() {
    return resourceBundle;
  }

  /**
   * TODO
   * 
   * @param ex
   */
  static public void log(CoreException ex) {
    ex.printStackTrace();
    IStatus cause = ex.getStatus();
    if (cause.getException() != null) {
      System.err.println("cause: " + cause.getMessage());
      cause.getException().printStackTrace();
    }
  }

  /**
   * TODO
   * 
   * @param ex
   */
  public static void log(Throwable e) {
    log(new Status(IStatus.ERROR, AgentBuilderPlugin.getDefault().getBundle().getSymbolicName(),
        IStatus.ERROR, "Error", e)); //$NON-NLS-1$
  }

  public static void log(IStatus status) {
    getDefault().getLog().log(status);
  }

  /*
   * public static void log(IStatus status) {
   * ResourcesPlugin.getPlugin().getLog().log(status); }
   * 
   * public static void logErrorMessage(String message) { log(new
   * Status(IStatus.ERROR, getPluginId(), IStatus.ERROR, message, null)); }
   * 
   * public static void logException( Throwable e, final String title, String
   * message) { if (e instanceof InvocationTargetException) { e =
   * ((InvocationTargetException) e).getTargetException(); } IStatus status =
   * null; if (e instanceof CoreException) status = ((CoreException)
   * e).getStatus(); else { if (message == null) message = e.getMessage(); if
   * (message == null) message = e.toString(); status = new
   * Status(IStatus.ERROR, getPluginId(), IStatus.OK, message, e); }
   * ResourcesPlugin.getPlugin().getLog().log(status); }
   * 
   * public static void logException(Throwable e) { logException(e, null, null);
   * }
   * 
   * public static void log(Throwable e) { if (e instanceof
   * InvocationTargetException) e = ((InvocationTargetException)
   * e).getTargetException(); IStatus status = null; if (e instanceof
   * CoreException) status = ((CoreException) e).getStatus(); else status = new
   * Status(IStatus.ERROR, getPluginId(), IStatus.OK, e.getMessage(), e);
   * log(status); }
   */

  /**
   * Opens a message Dialog window with the given messge.
   * 
   * @param message
   *          the message
   */
  static public void message(String message) {
    MessageDialog.openInformation(new Shell(), "Repast Simphony Plug-in", message);
  }
  
  public static boolean hasFlowchartNature(IProject project) {
 // Be flexible with 1.0 IDs - check all combinations
    if (!project.isOpen())
      return false;
    try {
      return project.hasNature(AGENT_BUILDER_NATURE_ID);
    } catch (CoreException e) {
      log(e);
    }
    return false;
  }

  /**
   * Returns the pretty printer Settings
   * 
   * @return the pretty printer Settings
   */
  // public static FileSettings getPrettySettings() {
  // return prettySettings;
  // }

  public void setResourceBundle(ResourceBundle resourceBundle) {
    this.resourceBundle = resourceBundle;
  }

  public static String findExtension(String variableName) {
    IValueVariable variable = VariablesPlugin.getDefault().getStringVariableManager()
        .getValueVariable(variableName);
    if (variable != null) {
      return variable.getValue();
    } else {
      return null;
    }
  }

  public static void removeFromBuildSpec(IProject project, String builderID) throws CoreException {
    IProjectDescription description = project.getDescription();
    ICommand[] commands = description.getBuildSpec();
    for (int counter = 0; counter < commands.length; ++counter) {
      if (commands[counter].getBuilderName().equals(builderID)) {
        ICommand[] updatedCommands = new ICommand[commands.length - 1];
        System.arraycopy(commands, 0, updatedCommands, 0, counter);
        System.arraycopy(commands, counter + 1, updatedCommands, counter, commands.length - counter
            - 1);
        description.setBuildSpec(updatedCommands);
        project.setDescription(description, null);
        return;
      }
    }
  }

}
