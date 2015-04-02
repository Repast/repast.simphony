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
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import repast.simphony.eclipse.RepastSimphonyPlugin;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 */
public class Utilities {

  public static void addNature(IProject project, String natureId) throws CoreException {

    IProjectDescription description = project.getProject().getDescription();
    String[] prevNatures = description.getNatureIds();
    boolean add = true;
    for (String id : prevNatures) {
      if (id.equals(natureId)) {
        add = false;
        break;
      }
    }

    if (add) {
      String[] newNatures = new String[prevNatures.length + 1];
      System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
      newNatures[prevNatures.length] = natureId;
      description.setNatureIds(newNatures);
      project.getProject().setDescription(description, IResource.FORCE, null);
    }
  }

  public static void removeNature(IProject project, String natureId) throws CoreException {
    IProjectDescription description = project.getDescription();
    String[] prevNatures = description.getNatureIds();
    List<String> newNatures = new ArrayList<String>();
    for (String nature : prevNatures) {
      if (!nature.equals(natureId)) {
        newNatures.add(nature);
      }
    }

    description.setNatureIds(newNatures.toArray(new String[0]));
    project.getProject().setDescription(description, IResource.FORCE, null);
  }

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
  
  public static void createModelInstallerFiles(IFolder srcFolder, 
  		IProgressMonitor monitor, String[][] variableMap) throws CoreException{
    IFolder newFolder = srcFolder.getFolder("../installer");
    if (!newFolder.exists())
      newFolder.create(true, false, monitor);
    Utilities.copyFileFromPluginInstallation("installer/installation_components.xml", newFolder,
        "installation_components.xml", variableMap, monitor);
    Utilities.copyFileFromPluginInstallation("installer/shortcuts_Windows.xml", newFolder,
        "shortcuts_Windows.xml", variableMap, monitor);
    Utilities.copyFileFromPluginInstallation("installer/shortcuts_Xnix.xml", newFolder,
        "shortcuts_Xnix.xml", variableMap, monitor);
    Utilities.copyFileFromPluginInstallation("installer/splash_screen.png", newFolder,
        "splash_screen.png", monitor);
    Utilities.copyFileFromPluginInstallation("installer/start_model.bat", newFolder,
        "start_model.bat", variableMap, monitor);
    Utilities.copyFileFromPluginInstallation("installer/start_model.command", newFolder,
        "start_model.command", variableMap, monitor);
    Utilities.copyFileFromPluginInstallation("installer/installation_coordinator.xml", newFolder,
        "installation_coordinator.xml", variableMap, monitor);
  }
}
