/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif���s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North���s Modifications are Copyright 2007 Under
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
package repast.simphony.eclipse.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SystemUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

import repast.simphony.eclipse.RepastLauncherClasspathContainer;
import repast.simphony.eclipse.RepastSimphonyPlugin;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 */
public class Utilities {

  public final static String PREFERRED_LAUNCHERS = "org.eclipse.debug.core.preferred_launchers";
  public final static String RELOGO_LAUNCH_DELEGATE = "repast.simphony.relogo.ide.relogoLaunchDelegate";
  public final static String LOCAL_JAVA_LAUNCH_DELEGATE = "org.eclipse.jdt.launching.localJavaApplication";
  public final static String LAUNCH_DELEGATE_RUN = "[run]";
  public final static String LAUNCH_DELEGATE_DEBUG = "[debug]";

  public static void copyFileFromPluginInstallation(String sourceFileName,
      IFolder destinationFolder, String destinationFileName, String[][] variableMap,
      IProgressMonitor monitor) {

    try {

      File projPath = new File(RepastSimphonyPlugin.getInstance().getPluginInstallationDirectory()
          + RepastSimphonyPlugin.getInstance().getPluginDirectoryName());

      InputStream input = new BufferedInputStream(new FileInputStream(projPath.getAbsolutePath()
          + "/setupfiles/" + sourceFileName));

      IFile output = destinationFolder.getFile(destinationFileName);

      InputStream filteredInput = input;

      if (variableMap != null) {

        String inputString = "";
        while (input.available() > 0)
          inputString += ((char) input.read());

        for (int i = 0; i < variableMap.length; i++) {
          inputString = inputString.replace(variableMap[i][0], variableMap[i][1]);
        }

        filteredInput = new ByteArrayInputStream(inputString.getBytes());

      }
      if (output.exists())
        output.delete(true, monitor);
      output.create(filteredInput, true, monitor);

    } catch (Exception e) {
      System.err.println("Error: Could not find \"" + "/setupfiles/" + sourceFileName + "\"");
    }

  }

  public static void copyFileFromPluginInstallation(String sourceFileName,
      IFolder destinationFolder, String destinationFileName, IProgressMonitor monitor) {
    copyFileFromPluginInstallation(sourceFileName, destinationFolder, destinationFileName, null,
        monitor);
  }

  public static void copyFileFromPluginInstallation(String sourceFileName,
      IFolder destinationFolder, IProgressMonitor monitor) {
    copyFileFromPluginInstallation(sourceFileName, destinationFolder, sourceFileName, monitor);
  }

  public static void createLaunchConfigurations(IJavaProject javaProject, IFolder newFolder,
      String scenarioDirectory) {

    try {
      ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
      ILaunchConfigurationType launchType = launchManager
          .getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
      ILaunchConfiguration[] launchConfigurations = launchManager
          .getLaunchConfigurations(launchType);

      // Workaround needed to add launch configurations to favorites before
      // the launch history is checked by user
      DebugUIPlugin.getDefault().getLaunchConfigurationManager()
          .getLaunchHistory("org.eclipse.debug.ui.launchGroup.run");
      DebugUIPlugin.getDefault().getLaunchConfigurationManager()
          .getLaunchHistory("org.eclipse.debug.ui.launchGroup.debug");

      List classpath = new ArrayList();

      // ******************************************************************
      // ******
      // * *
      // * Run Model *
      // * *
      // ******************************************************************
      // ******
      for (int i = 0; i < launchConfigurations.length; i++) {
        ILaunchConfiguration launchConfiguration = launchConfigurations[i];
        if (launchConfiguration.getName().equals(javaProject.getElementName() + " Model")) {
          launchConfiguration.delete();
          break;
        }
      }
      ILaunchConfigurationWorkingCopy launchConfigurationWorkingCopy = launchType.newInstance(
          newFolder, javaProject.getElementName() + " Model");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
          "repast.simphony.runtime.RepastMain");
      Map<String, String> preferredLaunchers = new HashMap<String, String>();
      preferredLaunchers.put(LAUNCH_DELEGATE_RUN, LOCAL_JAVA_LAUNCH_DELEGATE);
      preferredLaunchers.put(LAUNCH_DELEGATE_DEBUG, LOCAL_JAVA_LAUNCH_DELEGATE);
      launchConfigurationWorkingCopy.setAttribute(PREFERRED_LAUNCHERS, preferredLaunchers);
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, "\"${workspace_loc:"
              + javaProject.getElementName() + "}/" + scenarioDirectory + "\"");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xss10M -Xmx400M");
      List favoritesList = launchConfigurationWorkingCopy.getAttribute(
          IDebugUIConstants.ATTR_FAVORITE_GROUPS, (List) null);
      if (favoritesList == null)
        favoritesList = new ArrayList();
      favoritesList.add(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
      favoritesList.add(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
          favoritesList);
      IPath systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
      IRuntimeClasspathEntry r = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath,
          IRuntimeClasspathEntry.STANDARD_CLASSES);
      r.setClasspathProperty(IRuntimeClasspathEntry.BOOTSTRAP_CLASSES);
      classpath.add(r.getMemento());
      IPath jarPath = new Path(RepastLauncherClasspathContainer.JAR_CLASSPATH_DEFAULT);
      r = JavaRuntime.newRuntimeContainerClasspathEntry(jarPath, IRuntimeClasspathEntry.CONTAINER);
      r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
      classpath.add(r.getMemento());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH,
          classpath);
      launchConfigurationWorkingCopy.doSave();

      // ******************************************************************
      // ******
      // * *
      // * Build Installer *
      // * *
      //
      // ******************************************************************
      // ******
      classpath.clear();
      for (int i = 0; i < launchConfigurations.length; i++) {
        ILaunchConfiguration launchConfiguration = launchConfigurations[i];
        if (launchConfiguration.getName().equals(
            "Build Installer for " + javaProject.getElementName() + " Model")) {
          launchConfiguration.delete();
          break;
        }
      }
      launchConfigurationWorkingCopy = launchType.newInstance(newFolder, "Build Installer for "
          + javaProject.getElementName() + " Model");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
          "org.apache.tools.ant.launch.Launcher");
      launchConfigurationWorkingCopy.setAttribute(PREFERRED_LAUNCHERS, preferredLaunchers);
      if (SystemUtils.IS_OS_MAC)
        launchConfigurationWorkingCopy
            .setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
                "-buildfile \"${workspace_loc:"
                    + javaProject.getElementName()
                    + "}"
                    + "/installer/installation_coordinator.xml\" -DoutputInstallationFile=\""
                    + "${folder_prompt:the Folder to output the installer (setup.jar) file}/setup.jar\" "
                    + "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
                    + " -DGroovyHomeDirectory=\"${groovy_home}\""
                    + " -DREPAST_VERSION=${REPAST_VERSION}");
      else if (SystemUtils.IS_OS_WINDOWS)
        launchConfigurationWorkingCopy.setAttribute(
            IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
            "-buildfile \"${workspace_loc:" + javaProject.getElementName() + "}"
                + "/installer/installation_coordinator.xml\" -DoutputInstallationFile=\""
                + "${file_prompt:the Installer Output File Name:setup.jar}\" "
                + "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
                + " -DGroovyHomeDirectory=\"${groovy_home}\""
                + " -DREPAST_VERSION=${REPAST_VERSION}");
      // for non-Windows or Mac
      else
        launchConfigurationWorkingCopy.setAttribute(
            IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
            "-buildfile \"${workspace_loc:" + javaProject.getElementName() + "}"
                + "/installer/installation_coordinator.xml\" -DoutputInstallationFile=\""
                + "${file_prompt:the Installer Output File Name:setup.jar}\" "
                + "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
                + " -DGroovyHomeDirectory=\"${groovy_home}\""
                + " -DREPAST_VERSION=${REPAST_VERSION}");

      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xss10M -Xmx400M");

      // add the ant classpath
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER,
          "org.eclipse.ant.ui.AntClasspathProvider");

      favoritesList = launchConfigurationWorkingCopy.getAttribute(
          IDebugUIConstants.ATTR_FAVORITE_GROUPS, (List) null);
      if (favoritesList == null)
        favoritesList = new ArrayList();
      favoritesList.add(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
      favoritesList.add(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
          favoritesList);
      launchConfigurationWorkingCopy.doSave();

      // ******************************************************************
      // ******
      // * *
      // * Batch Run *
      // * *
      //
      // ******************************************************************
      // ******
      classpath.clear();
      for (int i = 0; i < launchConfigurations.length; i++) {
        ILaunchConfiguration launchConfiguration = launchConfigurations[i];
        if (launchConfiguration.getName()
            .equals("Batch " + javaProject.getElementName() + " Model")) {
          launchConfiguration.delete();
          break;
        }
      }
      launchConfigurationWorkingCopy = launchType.newInstance(newFolder,
          "Batch " + javaProject.getElementName() + " Model");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
          "repast.simphony.runtime.RepastBatchMain");
      launchConfigurationWorkingCopy.setAttribute(PREFERRED_LAUNCHERS, preferredLaunchers);
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
          "-params \"${file_prompt:the Batch Run Parameters File Name:params.xml}\""
              + " \"${workspace_loc:" + javaProject.getElementName() + "}" + "/"
              + scenarioDirectory + "\"");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xss10M -Xmx400M");
      favoritesList = launchConfigurationWorkingCopy.getAttribute(
          IDebugUIConstants.ATTR_FAVORITE_GROUPS, (List) null);
      if (favoritesList == null)
        favoritesList = new ArrayList();
      favoritesList.add(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
      favoritesList.add(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
          favoritesList);
      systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
      r = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath,
          IRuntimeClasspathEntry.STANDARD_CLASSES);
      r.setClasspathProperty(IRuntimeClasspathEntry.BOOTSTRAP_CLASSES);
      classpath.add(r.getMemento());
      jarPath = new Path(RepastLauncherClasspathContainer.JAR_CLASSPATH_DEFAULT);
      r = JavaRuntime.newRuntimeContainerClasspathEntry(jarPath, IRuntimeClasspathEntry.CONTAINER);
      r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
      classpath.add(r.getMemento());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH,
          classpath);
      launchConfigurationWorkingCopy.doSave();
      favoritesList = null;

    } catch (CoreException e) {
      e.printStackTrace();
    }
  }
  
  /**
   * Should be identical to above, except for the specification of the launch
   * configuration specific to ReLogo projects.
   * 
   * @param javaProject
   * @param newFolder
   * @param scenarioDirectory
   */
  public static void createReLogoLaunchConfigurations(IJavaProject javaProject, IFolder newFolder,
      String scenarioDirectory) {

    try {
      ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
      ILaunchConfigurationType launchType = launchManager
          .getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
      ILaunchConfiguration[] launchConfigurations = launchManager
          .getLaunchConfigurations(launchType);

      // Workaround needed to add launch configurations to favorites before
      // the launch history is checked by user
      DebugUIPlugin.getDefault().getLaunchConfigurationManager()
          .getLaunchHistory("org.eclipse.debug.ui.launchGroup.run");
      DebugUIPlugin.getDefault().getLaunchConfigurationManager()
          .getLaunchHistory("org.eclipse.debug.ui.launchGroup.debug");

      List classpath = new ArrayList();

      // ******************************************************************
      // ******
      // * *
      // * Run Model *
      // * *
      // ******************************************************************
      // ******
      for (int i = 0; i < launchConfigurations.length; i++) {
        ILaunchConfiguration launchConfiguration = launchConfigurations[i];
        if (launchConfiguration.getName().equals(javaProject.getElementName() + " Model")) {
          launchConfiguration.delete();
          break;
        }
      }
      ILaunchConfigurationWorkingCopy launchConfigurationWorkingCopy = launchType.newInstance(
          newFolder, javaProject.getElementName() + " Model");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
          "repast.simphony.runtime.RepastMain");
      Map<String, String> preferredLaunchers = new HashMap<String, String>();
      preferredLaunchers.put(LAUNCH_DELEGATE_RUN, RELOGO_LAUNCH_DELEGATE);
      preferredLaunchers.put(LAUNCH_DELEGATE_DEBUG, RELOGO_LAUNCH_DELEGATE);
      launchConfigurationWorkingCopy.setAttribute(PREFERRED_LAUNCHERS, preferredLaunchers);
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, "\"${workspace_loc:"
              + javaProject.getElementName() + "}/" + scenarioDirectory + "\"");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xss10M -Xmx400M");
      List favoritesList = launchConfigurationWorkingCopy.getAttribute(
          IDebugUIConstants.ATTR_FAVORITE_GROUPS, (List) null);
      if (favoritesList == null)
        favoritesList = new ArrayList();
      favoritesList.add(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
      favoritesList.add(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
          favoritesList);
      IPath systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
      IRuntimeClasspathEntry r = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath,
          IRuntimeClasspathEntry.STANDARD_CLASSES);
      r.setClasspathProperty(IRuntimeClasspathEntry.BOOTSTRAP_CLASSES);
      classpath.add(r.getMemento());
      IPath jarPath = new Path(RepastLauncherClasspathContainer.JAR_CLASSPATH_DEFAULT);
      r = JavaRuntime.newRuntimeContainerClasspathEntry(jarPath, IRuntimeClasspathEntry.CONTAINER);
      r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
      classpath.add(r.getMemento());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH,
          classpath);
      launchConfigurationWorkingCopy.doSave();

      // ******************************************************************
      // ******
      // * *
      // * Build Installer *
      // * *
      //
      // ******************************************************************
      // ******
      classpath.clear();
      for (int i = 0; i < launchConfigurations.length; i++) {
        ILaunchConfiguration launchConfiguration = launchConfigurations[i];
        if (launchConfiguration.getName().equals(
            "Build Installer for " + javaProject.getElementName() + " Model")) {
          launchConfiguration.delete();
          break;
        }
      }
      launchConfigurationWorkingCopy = launchType.newInstance(newFolder, "Build Installer for "
          + javaProject.getElementName() + " Model");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
          "org.apache.tools.ant.launch.Launcher");
      launchConfigurationWorkingCopy.setAttribute(PREFERRED_LAUNCHERS, preferredLaunchers);
      if (SystemUtils.IS_OS_MAC)
        launchConfigurationWorkingCopy
            .setAttribute(
                IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
                "-buildfile \"${workspace_loc:"
                    + javaProject.getElementName()
                    + "}"
                    + "/installer/installation_coordinator.xml\" -DoutputInstallationFile=\""
                    + "${folder_prompt:the Folder to output the installer (setup.jar) file}/setup.jar\" "
                    + "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
                    + " -DGroovyHomeDirectory=\"${groovy_home}\""
                    + " -DREPAST_VERSION=${REPAST_VERSION}");
      else if (SystemUtils.IS_OS_WINDOWS)
        launchConfigurationWorkingCopy.setAttribute(
            IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
            "-buildfile \"${workspace_loc:" + javaProject.getElementName() + "}"
                + "/installer/installation_coordinator.xml\" -DoutputInstallationFile=\""
                + "${file_prompt:the Installer Output File Name:setup.jar}\" "
                + "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
                + " -DGroovyHomeDirectory=\"${groovy_home}\""
                + " -DREPAST_VERSION=${REPAST_VERSION}");
      // for non-Windows or Mac
      else
        launchConfigurationWorkingCopy.setAttribute(
            IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
            "-buildfile \"${workspace_loc:" + javaProject.getElementName() + "}"
                + "/installer/installation_coordinator.xml\" -DoutputInstallationFile=\""
                + "${file_prompt:the Installer Output File Name:setup.jar}\" "
                + "-DEclipsePluginsDirectory=\"${eclipse_home}plugins\""
                + " -DGroovyHomeDirectory=\"${groovy_home}\""
                + " -DREPAST_VERSION=${REPAST_VERSION}");

      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xss10M -Xmx400M");

      // add the ant classpath
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER,
          "org.eclipse.ant.ui.AntClasspathProvider");

      favoritesList = launchConfigurationWorkingCopy.getAttribute(
          IDebugUIConstants.ATTR_FAVORITE_GROUPS, (List) null);
      if (favoritesList == null)
        favoritesList = new ArrayList();
      favoritesList.add(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
      favoritesList.add(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
          favoritesList);
      launchConfigurationWorkingCopy.doSave();

      // ******************************************************************
      // ******
      // * *
      // * Batch Run *
      // * *
      //
      // ******************************************************************
      // ******
      classpath.clear();
      for (int i = 0; i < launchConfigurations.length; i++) {
        ILaunchConfiguration launchConfiguration = launchConfigurations[i];
        if (launchConfiguration.getName()
            .equals("Batch " + javaProject.getElementName() + " Model")) {
          launchConfiguration.delete();
          break;
        }
      }
      launchConfigurationWorkingCopy = launchType.newInstance(newFolder,
          "Batch " + javaProject.getElementName() + " Model");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, javaProject.getElementName());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
          "repast.simphony.runtime.RepastBatchMain");
      launchConfigurationWorkingCopy.setAttribute(PREFERRED_LAUNCHERS, preferredLaunchers);
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
          "-params ${file_prompt:the Batch Run Parameters File Name:params.xml} \"${workspace_loc:"
              + javaProject.getElementName() + "}" + "/" + scenarioDirectory + "\"");
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "-Xss10M -Xmx400M");
      favoritesList = launchConfigurationWorkingCopy.getAttribute(
          IDebugUIConstants.ATTR_FAVORITE_GROUPS, (List) null);
      if (favoritesList == null)
        favoritesList = new ArrayList();
      favoritesList.add(IDebugUIConstants.ID_DEBUG_LAUNCH_GROUP);
      favoritesList.add(IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
      launchConfigurationWorkingCopy.setAttribute(IDebugUIConstants.ATTR_FAVORITE_GROUPS,
          favoritesList);
      systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
      r = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath,
          IRuntimeClasspathEntry.STANDARD_CLASSES);
      r.setClasspathProperty(IRuntimeClasspathEntry.BOOTSTRAP_CLASSES);
      classpath.add(r.getMemento());
      jarPath = new Path(RepastLauncherClasspathContainer.JAR_CLASSPATH_DEFAULT);
      r = JavaRuntime.newRuntimeContainerClasspathEntry(jarPath, IRuntimeClasspathEntry.CONTAINER);
      r.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
      classpath.add(r.getMemento());
      launchConfigurationWorkingCopy.setAttribute(
          IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
      launchConfigurationWorkingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH,
          classpath);
      launchConfigurationWorkingCopy.doSave();
      favoritesList = null;

    } catch (CoreException e) {
      e.printStackTrace();
    }
  }
}
