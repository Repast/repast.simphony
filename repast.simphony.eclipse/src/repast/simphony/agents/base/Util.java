/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif�s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North�s Modifications are Copyright 2007 Under
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

package repast.simphony.agents.base;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang.SystemUtils;
import org.apache.tools.ant.BuildException;
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
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import repast.simphony.agents.AgentBuilderException;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.core.RepastLauncherClasspathContainer;
import repast.simphony.agents.designer.ui.editors.AgentEditor;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif�s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 *         TODO
 * 
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class Util {

  public final static String SYSTEM_LINE_SEPARATOR = System.getProperty("line.separator");

  public final static String PREFERRED_LAUNCHERS = "org.eclipse.debug.core.preferred_launchers";
  public final static String RELOGO_LAUNCH_DELEGATE = "repast.simphony.relogo.ide.relogoLaunchDelegate";
  public final static String LOCAL_JAVA_LAUNCH_DELEGATE = "org.eclipse.jdt.launching.localJavaApplication";
  public final static String LAUNCH_DELEGATE_RUN = "[run]";
  public final static String LAUNCH_DELEGATE_DEBUG = "[debug]";

  /**
   * Returns the index of the object in the array. If the array does not contain
   * the object, then -1 is returned
   * 
   * @param array
   *          the array in which the test is done
   * @param obj
   *          the object to test for existence
   * @return the objects index if the array contains the object, -1 otherwise
   */
  public static int getIndexInArray(Object obj, Object[] array) {
    for (int i = 0; i < array.length; i++)
      if (obj == array[i])
        return i;
    return -1;
  }

  public final static String escapeXml(String s) {
    // if (s == null)
    // return "";
    // StringBuffer sb = new StringBuffer();
    // for (int i = 0; i < s.length(); i++) {
    // char c = s.charAt(i);
    // if (c == '<') {
    // sb.append("&lt;");
    // } else if (c == '>') {
    // sb.append("&gt;");
    // } else if (c == '\'') {
    // sb.append("&apos;");
    // } else if (c == '&') {
    // sb.append("&amp;");
    // } else if (c == '"') {
    // sb.append("&quot;");
    // } else if (c == '\n') {
    // sb.append("&#09;");
    // } else if (c == '\r') {
    // sb.append("&#09;");
    // } else if (c == '\f') {
    // sb.append("&#09;");
    // } else {
    // sb.append(c);
    // }
    // }
    // return sb.toString();

    // It appears we dont need to manually create the XML escape chars anymore.
    return s;
  }

  /**
   * Replaces <code>${xxx}</code> style constructions in the given value with
   * the string value of the corresponding data types.
   * 
   * @param value
   *          The string to be scanned for property references. May be
   *          <code>null</code>, in which case this method returns immediately
   *          with no effect.
   * @param keys
   *          Mapping (String to String) of property names to their values. Must
   *          not be <code>null</code>.
   * 
   * @exception AgentBuilderException
   *              if the string contains an opening <code>${</code> without a
   *              closing <code>}</code>
   * @return the original string with the properties replaced, or
   *         <code>null</code> if the original string is <code>null</code>.
   */
  public static String replaceProperties(String value, Map keys) throws AgentBuilderException {
    if (value == null)
      return null;

    if (keys == null || keys.isEmpty())
      return value;

    Vector fragments = new Vector();
    Vector propertyRefs = new Vector();
    parsePropertyString(value, fragments, propertyRefs);

    StringBuffer sb = new StringBuffer();
    Enumeration i = fragments.elements();
    Enumeration j = propertyRefs.elements();
    while (i.hasMoreElements()) {
      String fragment = (String) i.nextElement();
      if (fragment == null) {
        String propertyName = (String) j.nextElement();
        fragment = (keys.containsKey(propertyName)) ? (String) keys.get(propertyName) : "${"
            + propertyName + "}";
      }
      sb.append(fragment);
    }

    return sb.toString();
  }

  /**
   * Parses a string containing <code>${xxx}</code> style property references
   * into two lists. The first list is a collection of text fragments, while the
   * other is a set of string property names. <code>null</code> entries in the
   * first list indicate a property reference from the second list.
   * 
   * @param value
   *          Text to parse. Must not be <code>null</code>.
   * @param fragments
   *          List to add text fragments to. Must not be <code>null</code>.
   * @param propertyRefs
   *          List to add property names to. Must not be <code>null</code>.
   * 
   * @exception BuildException
   *              if the string contains an opening <code>${</code> without a
   *              closing <code>}</code>
   */
  public static void parsePropertyString(String value, Vector fragments, Vector propertyRefs)
      throws AgentBuilderException {
    int prev = 0;
    int pos;
    // search for the next instance of $ from the 'prev' position
    while ((pos = value.indexOf("$", prev)) >= 0) {

      // if there was any text before this, add it as a fragment
      // TODO, this check could be modified to go if pos>prev;
      // seems like this current version could stick empty strings
      // into the list
      if (pos > 0) {
        fragments.addElement(value.substring(prev, pos));
      }
      // if we are at the end of the string, we tack on a $
      // then move past it
      if (pos == (value.length() - 1)) {
        fragments.addElement("$");
        prev = pos + 1;
      } else if (value.charAt(pos + 1) != '{') {
        // peek ahead to see if the next char is a property or not
        // not a property: insert the char as a literal
        /*
         * fragments.addElement(value.substring(pos + 1, pos + 2)); prev = pos +
         * 2;
         */
        if (value.charAt(pos + 1) == '$') {
          // backwards compatibility two $ map to one mode
          fragments.addElement("$");
          prev = pos + 2;
        } else {
          // new behaviour: $X maps to $X for all values of X!='$'
          fragments.addElement(value.substring(pos, pos + 2));
          prev = pos + 2;
        }

      } else {
        // property found, extract its name or bail on a typo
        int endName = value.indexOf('}', pos);
        if (endName < 0) {
          throw new AgentBuilderException("Syntax error in property: " + value);
        }
        String propertyName = value.substring(pos + 2, endName);
        fragments.addElement(null);
        propertyRefs.addElement(propertyName);
        prev = endName + 1;
      }
    }
    // no more $ signs found
    // if there is any tail to the file, append it
    if (prev < value.length()) {
      fragments.addElement(value.substring(prev));
    }
  }

  /**
   * Returns the platform line delimiter. Either CR, LF od CRLF.
   */
  public static String getPlatformLineDelimiter() {
    return System.getProperty("line.separator", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public static String convertFromLineFeeds(String source) {

    String destination = "";

    StringTokenizer tokenizer = new StringTokenizer(source, "\n\r\f");
    while (tokenizer.hasMoreTokens()) {
      destination += tokenizer.nextToken();
      if (tokenizer.hasMoreTokens())
        destination += "\\f";
    }

    return destination;

  }

  public static String convertToLineFeeds(String source) {

    String destination = "";
    String separator = "\\\\f*";

    String[] tokens = source.split(separator);
    int lastIndex = tokens.length - 1;
    for (int index = 0; index < tokens.length; index++) {
      destination += tokens[index];
      if (index < lastIndex)
        destination += Util.SYSTEM_LINE_SEPARATOR;
    }

    return destination;

  }

  public static void copyFileFromPluginInstallation(String sourceFileName,
      IFolder destinationFolder, String destinationFileName, String[][] variableMap,
      IProgressMonitor monitor) {

    try {

      File projPath = new File(AgentBuilderPlugin.getPluginInstallationDirectory()
          + AgentBuilderPlugin.getEclipseProject());

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

  public static void printMessageBox(String message) {
    MessageBox messageBox = new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
        .getShell(), SWT.OK | SWT.ICON_WARNING);
    messageBox.setMessage(message);
    messageBox.open();
  }

  public static boolean exportAsImage(AgentEditor agentEditor) {

    if ((agentEditor != null) && (agentEditor.getGraphicalViewer() != null)) {

      return exportAsImage(agentEditor, agentEditor.getGraphicalViewer());

    } else {

      return false;

    }

  }

  public static void exportAsImage(AgentEditor agentEditor, String saveFilePath, int format) {

    if ((agentEditor != null) && (agentEditor.getGraphicalViewer() != null)) {

      exportAsImage(agentEditor, agentEditor.getGraphicalViewer(), saveFilePath, format);

    }

  }

  public static boolean exportAsImage(IEditorPart editorPart, GraphicalViewer graphicalViewer) {

    String path = getSaveFilePath(editorPart, graphicalViewer, -1);
    if (path != null) {

      int outputFormat = SWT.IMAGE_JPEG;
      if (path.toLowerCase().endsWith(".jpg")) {
        outputFormat = SWT.IMAGE_JPEG;
      } else if (path.toLowerCase().endsWith(".bmp")) {
        outputFormat = SWT.IMAGE_BMP;
      } else if (path.toLowerCase().endsWith(".png")) {
        outputFormat = SWT.IMAGE_PNG;
      } else if (path.toLowerCase().endsWith(".ico")) {
        outputFormat = SWT.IMAGE_ICO;
      }

      return exportAsImage(editorPart, graphicalViewer, path, outputFormat);

    } else {

      return false;

    }

  }

  public static boolean exportAsImage(IEditorPart editorPart, GraphicalViewer graphicalViewer,
      String saveFilePath, int format) {

    try {
      ScalableFreeformRootEditPart editPart = (ScalableFreeformRootEditPart) graphicalViewer
          .getEditPartRegistry().get(LayerManager.ID);
      IFigure rootFigure = ((LayerManager) editPart).getLayer(LayerConstants.PRINTABLE_LAYERS);

      Rectangle figureBounds = rootFigure.getBounds();
      Control figureCanvas = graphicalViewer.getControl();
      GC figureCanvasGC = new GC(figureCanvas);

      Image image = new Image(null, figureBounds.width, figureBounds.height);
      GC imageGC = new GC(image);
      imageGC.setBackground(figureCanvasGC.getBackground());
      imageGC.setForeground(figureCanvasGC.getForeground());
      imageGC.setFont(figureCanvasGC.getFont());
      imageGC.setLineStyle(figureCanvasGC.getLineStyle());
      imageGC.setLineWidth(figureCanvasGC.getLineWidth());
      Graphics imgGraphics = new SWTGraphics(imageGC);

      rootFigure.paint(imgGraphics);

      ImageData[] imageData = new ImageData[1];
      imageData[0] = image.getImageData();

      ImageLoader imageLoader = new ImageLoader();
      imageLoader.data = imageData;
      imageLoader.save(saveFilePath, format);

      figureCanvasGC.dispose();
      imageGC.dispose();
      image.dispose();

      return true;

    } catch (Exception e) {

      return false;

    }

  }

  public static String getSaveFilePath(IEditorPart editorPart, GraphicalViewer viewer, int format) {
    FileDialog fileDialog = new FileDialog(editorPart.getEditorSite().getShell(), SWT.SAVE);

    String[] extensions = new String[] { "*.jpg", "*.bmp", "*.ico", "*.png", };
    if (format == SWT.IMAGE_BMP) {
      extensions = new String[] { "*.bmp" };
    } else if (format == SWT.IMAGE_JPEG) {
      extensions = new String[] { "*.jpg" };
    } else if (format == SWT.IMAGE_PNG) {
      extensions = new String[] { "*.png" };
    } else if (format == SWT.IMAGE_ICO) {
      extensions = new String[] { "*.ico" };
    }
    fileDialog.setFilterExtensions(extensions);

    return fileDialog.open();

  }

}
