/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif’s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North’s Modifications are Copyright 2007 Under
 * the Repast Simphony License, All Rights Reserved)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. * Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. * Neither the name of the
 * Flow4J-Eclipse project nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
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

package repast.simphony.agents.tools.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.MatchingTask;

import repast.simphony.agents.model.codegen.Consts;
import repast.simphony.agents.model.codegen.JavaSrcModelDigester;

/**
 * <h3>Description</h3>
 * <p>
 * Genarates the java source code of a flow4j model
 * </p>
 * 
 * <p>
 * The set of files to be transformed can be refined with the <i>includes</i>,
 * <i>includesfile</i>, <i>excludes</i>, <i>excludesfile</i> and
 * <i>defaultexcludes</i> attributes. Patterns provided through the <i>includes</i>
 * or <i>includesfile</i> attributes specify files to be included. Patterns
 * provided through the <i>exclude</i> or <i>excludesfile</i> attribute
 * specify files to be excluded. Additionally, default exclusions can be
 * specified with the <i>defaultexcludes</i> attribute. Look at the ant
 * documentation for details of file inclusion/exclusion patterns and their
 * usage.
 * </p>
 * 
 * <p>
 * This task forms an implicit <a href="../CoreTypes/fileset.html">FileSet</a>
 * and supports all attributes of <code>&lt;fileset&gt;</code> (<code>dir</code>
 * becomes <code>srcdir</code>) as well as the nested
 * <code>&lt;include&gt;</code>, <code>&lt;exclude&gt;</code> and
 * 
 * <code>&lt;patternset&gt;</code> elements.
 * </p>
 * 
 * <h3>Parameters</h3>
 * <table border="1" cellpadding="2" cellspacing="0">
 * <tr>
 * <td valign="top"><b>Attribute</b></td>
 * <td valign="top"><b>Description</b></td>
 * <td align="center" valign="top"><b>Required</b></td>
 * </tr>
 * <tr>
 * <td valign="top">srcDir</td>
 * <td valign="top">Where to find the files to be fixed up.</td>
 * <td valign="top" align="center">Yes</td>
 * </tr>
 * <tr>
 * <td valign="top">destDir</td>
 * <td valign="top">Where to place the corrected files. Defaults to srcDir
 * (replacing the original file)</td>
 * <td valign="top" align="center">No</td>
 * </tr>
 * <!--tr>
 * <td valign="top">mappingFile</td>
 * <td valign="top">Where to find the castor mapping file</td>
 * <td valign="top" align="center">Yes</td>
 * </tr-->
 * <tr>
 * <td valign="top">includes</td>
 * <td valign="top">comma- or space-separated list of patterns of files that
 * must be included. All files are included when omitted.</td>
 * <td valign="top" align="center">No</td>
 * </tr>
 * <tr>
 * <td valign="top">includesfile</td>
 * <td valign="top">the name of a file. Each line of this file is taken to be
 * an include pattern</td>
 * <td valign="top" align="center">No</td>
 * </tr>
 * <tr>
 * <td valign="top">excludes</td>
 * <td valign="top">comma- or space-separated list of patterns of files that
 * must be excluded. No files (except default excludes) are excluded when
 * omitted.</td>
 * <td valign="top" align="center">No</td>
 * </tr>
 * <tr>
 * <td valign="top">excludesfile</td>
 * <td valign="top">the name of a file. Each line of this file is taken to be
 * an exclude pattern</td>
 * <td valign="top" align="center">No</td>
 * </tr>
 * <tr>
 * <td valign="top">defaultexcludes</td>
 * <td valign="top">indicates whether default excludes should be used or not
 * (&quot;yes&quot;/&quot;no&quot;). Default excludes are used when omitted.</td>
 * <td valign="top" align="center">No</td>
 * </tr>
 * </table>
 * <h3>Example</h3>
 * 
 * <pre>
 *  &lt;target name=&quot;generate-flow-java-src&quot; depends=&quot;compile-plugin&quot;&gt;
 *  &lt;taskdef name=&quot;flow2java&quot; classname=&quot;repast.simphony.agents.tools.ant.AgentBuilderJavaSourceGenerator&quot;&gt;
 *  &lt;classpath&gt;
 *  &lt;pathelement path=&quot;${build.dir}&quot;/&gt;
 *  &lt;fileset dir=&quot;lib&quot;&gt;
 *  &lt;include name=&quot;** / *.jar&quot;/&gt;
 *  &lt;/fileset&gt;
 *  &lt;fileset dir=&quot;${eclipse.plugins}&quot;&gt;
 *  &lt;include name=&quot;org.apache.xerces* /*.jar&quot;/&gt;
 *  &lt;include name=&quot;org.eclipse.jdt.core* /*.jar&quot;/&gt;
 *  &lt;include name=&quot;org.eclipse.core.runtime* /*.jar&quot;/&gt;
 *  &lt;include name=&quot;org.eclipse.core.resources* /*.jar&quot;/&gt;
 *  &lt;/fileset&gt;
 *  &lt;/classpath&gt;			
 *  &lt;/taskdef&gt;
 *  &lt;flow2java destDir=&quot;${src.dir}&quot;
 *  srcDir=&quot;${src.dir}&quot;
 *  mappingFile=&quot;${src.dir}/mapping.xml&quot;
 *  includes=&quot;** /*.stencil&quot;&gt;
 *  &lt;/flow2java&gt;
 *  &lt;/target&gt;
 * </pre>
 * 
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 */
public class AgentBuilderJavaSourceGenerator extends MatchingTask {

	private File destDir = null;
	private File srcDir;
	// private File mappingFile;
	private File scriptRootFolder;

	/**
	 * Set the source dir to find the source text files.
	 */
	public void setSrcdir(File srcDir) {
		this.srcDir = srcDir;
	}

	/**
	 * Set the destination where the fixed files should be placed. Default is to
	 * replace the original file.
	 */
	public void setDestdir(File destDir) {
		this.destDir = destDir;
	}

	/**
	 * @param file
	 */
	public void setScriptRootFolder(File file) {
		scriptRootFolder = file;
	}

	/**
	 * Transforms the flow model file into java source code
	 * 
	 * @param file
	 *            the flow model file
	 * @throws BuildException
	 */
	private void processFile(String file) throws BuildException {

		File srcFile = new File(srcDir, file);
		// File destD = destDir == null ? srcDir : destDir;

		File relFile = new File(file);
		String packagePath = relFile.getParentFile().toString().replace('/',
				'.').replace('\\', '.');
		if (packagePath.startsWith("."))
			packagePath = packagePath.substring(1);
		if (packagePath.endsWith("."))
			packagePath = packagePath.substring(0, packagePath.length());

		try {
			String fileName = srcFile.getName();
			fileName = fileName.substring(0, fileName.lastIndexOf(".") + 1)
					+ Consts.FILE_EXTENSION_GROOVY;
			new File(new File(destDir, packagePath.replace('.',
					File.separatorChar)), fileName);

			JavaSrcModelDigester.generateCode(srcFile, packagePath, destDir,
					scriptRootFolder);
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

	/**
	 * Executes the task.
	 */
	@Override
	public void execute() throws BuildException {

		// first off, make sure that we've got a srcdir and destdir
		if (srcDir == null) {
			throw new BuildException("srcdir attribute must be set!");
		}
		if (!srcDir.exists()) {
			throw new BuildException("srcdir does not exist!");
		}
		if (!srcDir.isDirectory()) {
			throw new BuildException("srcdir is not a directory!");
		}
		if (destDir != null) {
			if (!destDir.exists()) {
				throw new BuildException("destdir does not exist!");
			}
			if (!destDir.isDirectory()) {
				throw new BuildException("destdir is not a directory!");
			}
		}

		DirectoryScanner ds = super.getDirectoryScanner(srcDir);
		String[] files = ds.getIncludedFiles();

		log("destDir: " + destDir, Project.MSG_VERBOSE);
		log("srcDir: " + srcDir, Project.MSG_VERBOSE);
		// castor log("mappingFile: " + mappingFile, Project.MSG_VERBOSE);

		for (int i = 0; i < files.length; i++) {
			processFile(files[i]);
		}
	}

	/*
	 * static public void main(String[] args) { AgentBuilderJavaSourceGenerator
	 * gen = new AgentBuilderJavaSourceGenerator(); gen.setDestDir(new
	 * File("/Users/agreif/project/flow4j/eclipse/repast.simphony.agents/src"));
	 * gen.setFlowFile(new
	 * File("/Users/agreif/project/flow4j/eclipse/repast.simphony.agents/src/net/orthanc/flow4j/flows/FlowFileUpdate.stencil"));
	 * gen.setMappingFile(new
	 * File("/Users/agreif/project/flow4j/eclipse/repast.simphony.agents/src/mapping.xml"));
	 * gen.setPackagePath("repast.simphony.agents.flows"); gen.execute(); }
	 */

}
