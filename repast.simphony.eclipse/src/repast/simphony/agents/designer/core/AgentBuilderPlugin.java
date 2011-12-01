/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif’s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North’s Modifications are Copyright 2007 Under
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
import java.util.ListIterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.acm.seguin.util.FileSettings;
import org.apache.commons.lang.ArrayUtils;
import org.codehaus.groovy.eclipse.core.model.GroovyRuntime;
import org.eclipse.core.resources.ICommand;
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
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
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

/**
 * The main plugin class to be used in the desktop.
 */
@SuppressWarnings( { "unchecked" })
public class AgentBuilderPlugin extends AbstractUIPlugin {

	@Deprecated
	public static final String REPAST_SIMPHONY_VERSION_1_NATURE = "repast.simphony.agents.repast_simphony_nature";
	@Deprecated
	public static final String REPAST_SIMPHONY_VERSION_1_BUILDER = "repast.simphony.agents.repast_simphony_builder";

	public static final String AGENT_BUILDER_PLUGIN_ID = "repast.simphony.eclipse";
	public static final String AGENT_BUILDER_PLUGIN_VERSION = "2.0.1";
	public static final String AGENT_BUILDER_RESOURCES_VIEW_ID = AGENT_BUILDER_PLUGIN_ID
			+ ".agentbuilder_resources_view";
	public static final String AGENT_BUILDER_NATURE_ID = AGENT_BUILDER_PLUGIN_ID
			+ ".repast_simphony_nature";
	public static final String AGENT_BUILDER_BUILDER_ID = AGENT_BUILDER_PLUGIN_ID
			+ ".repast_simphony_builder";
	public static final String AGENT_BUILDER_REPOSITORY_BUILDER_ID = AGENT_BUILDER_PLUGIN_ID
			+ ".repast_simphony_repository_builder";
	public static final String RS_FILE_EXTENSION_CODE = "REPAST_FILE_EXTENSION_";
	public static final ArrayList<String> AGENT_BUILDER_FILE_EXTENSIONS = new ArrayList<String>();
	{
		IStringVariable[] variables = VariablesPlugin.getDefault()
				.getStringVariableManager().getValueVariables();
		for (int i = 0; i < variables.length; i++) {
			if (variables[i].getName().startsWith(RS_FILE_EXTENSION_CODE)
					&& variables[i] instanceof IValueVariable) {
				AGENT_BUILDER_FILE_EXTENSIONS
						.add(((IValueVariable) variables[i]).getValue());
			}
		}
	}
	public static final String RS_EXTRA_JAR_CODE = "REPAST_EXTRA_JAR_";
	public static final ArrayList<String> AGENT_BUILDER_EXTRA_JARS = new ArrayList<String>();
	{
		IStringVariable[] variables = VariablesPlugin.getDefault()
				.getStringVariableManager().getValueVariables();
		for (int i = 0; i < variables.length; i++) {
			if (variables[i].getName().startsWith(RS_EXTRA_JAR_CODE)
					&& variables[i] instanceof IValueVariable) {
				AGENT_BUILDER_EXTRA_JARS.add(((IValueVariable) variables[i])
						.getValue());
			}
		}
	}
	public static final String JAR_FILE = "repast.simphony.bin_and_src.jar";
	public static final String JAR_PROJECT = "repast.simphony.bin_and_src_"
			+ AGENT_BUILDER_PLUGIN_VERSION;
	public static final String JAR_PATH_RELATIVE = JAR_PROJECT + "/" + JAR_FILE;
	public static final String SCORE_AGENTS_PROJECT = "repast.simphony.eclipse_"
			+ AGENT_BUILDER_PLUGIN_VERSION;
	// The shared instance.
	private static AgentBuilderPlugin plugin;
	// Resource bundle.
	private ResourceBundle resourceBundle;
	private static FileSettings prettySettings;
	private static IType agentBaseType; // Peter Friese

	private static String[] finalJarClassPathList = null;

	private static String[] finalJarClassPathListForLauncher = null;
	private static String[] finalJarClassPathListForLauncherBasics = {
			"${workspace_loc:project_name}/bin",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime_"
					+ AGENT_BUILDER_PLUGIN_VERSION + "/bin",
			"/repast.simphony.runtime_" + AGENT_BUILDER_PLUGIN_VERSION
					+ "/lib/saf.core.runtime.jar",
			"/repast.simphony.runtime_" + AGENT_BUILDER_PLUGIN_VERSION
					+ "/lib/commons-logging-1.0.4.jar",
			"/repast.simphony.runtime_" + AGENT_BUILDER_PLUGIN_VERSION
					+ "/lib/javassist-3.7.0.GA.jar",
			"/repast.simphony.runtime_" + AGENT_BUILDER_PLUGIN_VERSION
					+ "/lib/jpf.jar",
			"/repast.simphony.runtime_" + AGENT_BUILDER_PLUGIN_VERSION
					+ "/lib/jpf-boot.jar",
			"/repast.simphony.runtime_" + AGENT_BUILDER_PLUGIN_VERSION
					+ "/lib/log4j-1.2.13.jar",
			"/repast.simphony.runtime_" + AGENT_BUILDER_PLUGIN_VERSION
					+ "/lib/xpp3_min-1.1.4c.jar",
			"/repast.simphony.runtime_" + AGENT_BUILDER_PLUGIN_VERSION
					+ "/lib/xstream-1.3.jar",
			"/repast.simphony.runtime_" + AGENT_BUILDER_PLUGIN_VERSION
                                        + "/lib/commons-cli-1.0.jar"};

	public static final String[] oldJARFileList = {
			RepastProjectClasspathContainer.JAR_CLASSPATH_DEFAULT,
			"ECLIPSE_HOME/plugins/repast.simphony.bin_and_src.jar",
			"ECLIPSE_HOME/plugins/libs.bsf/lib/bsf.jar",
			"ECLIPSE_HOME/plugins/libs.bsf/lib/bsh-2.0b4.jar",
			"ECLIPSE_HOME/plugins/libs.piccolo/lib/piccolo.jar",
			"ECLIPSE_HOME/plugins/libs.piccolo/lib/piccolox.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.batch/groovy-all-1.5.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.batch/lib/batch_groovy.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.chart/lib/jcommon-1.0.10.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.chart/lib/jfreechart-1.0.6.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/OpenForecast-0.4.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/ProActive.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/cglib-nodep-2.1_3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/collections-generic-4.01.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/colt-1.2.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/commons-cli-1.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/commons-collections-3.2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/commons-lang-2.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/concurrent-1.3.4.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/geoapi-nogenerics-2.1-M2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/gt2-api-2.3.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/gt2-arcgrid-2.3.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/gt2-brewer-2.3.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/gt2-coverage-2.3.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/gt2-epsg-hsql-2.3.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/gt2-indexed-shapefile-2.3.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/gt2-main-2.3.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/gt2-referencing-2.3.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/gt2-render-2.3.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/gt2-shapefile-2.3.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/hsqldb-1.8.0.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/imageioext-asciigrid-1.0-rc1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/jgap.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/joone.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/jscience.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/jsr108-0.01.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/jts-1.7.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/jung-algorithms-2.0-alpha2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/jung-api-2.0-alpha2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/jung-graph-impl-2.0-alpha2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/opencsv-1.5.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/vecmath-1.3.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.core/lib/velocity-1.4.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.data.bsf/lib/jython.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.data/lib/commons-math-1.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.data/lib/log4j-1.2.13.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.gis/lib/forms-1.0.5.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.gis/lib/swingx-2006_07_20.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/ant-1.6.5.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/ant-junit-1.6.5.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/ant-launcher-1.6.5.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/antlr-2.7.5.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/asm-2.2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/asm-analysis-2.2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/asm-attrs-2.2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/asm-tree-2.2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/asm-util-2.2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/axion-1.0-M3-dev.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/bsf-2.4.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/cglib-nodep-2.1_3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/commons-cli-1.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/commons-collections-3.2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/commons-httpclient-3.0.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/commons-logging-1.0.4.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/commons-primitives-1.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/gant-0.3.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/groovy-all-1.5.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/groovy-starter.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/groovy-xmlrpc-0.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/jacob.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/jarjar-0.6.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/jmock-1.1.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/jmock-cglib-1.1.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/jsp-api-2.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/junit-3.8.2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/mockobjects-core-0.09.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/mx4j-3.0.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/nekohtml-0.9.5.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/openejb-loader-1.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/qdox-1.5.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/radeox-0.9.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/radeox-oro-0.9.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/regexp-1.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/scriptom-1.2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/servlet-api-2.4.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/xerces-2.4.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/xml-apis-1.0.b2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/xpp3-1.1.3.4.O.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.groovy/lib/xstream-1.2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.gui/lib/binding-1.1.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.gui/lib/jide-oss-2.1.2.01.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.gui/lib/jmf.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.integration/lib/cglib-nodep-2.1_3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.integration/lib/commons-jxpath-1.2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.integration/lib/exec-1.0-SNAPSHOT.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.integration/lib/jaxen-core-1.0-FCS.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.integration/lib/jaxen-jdom-1.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.integration/lib/jdom-1.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.integration/lib/saxpath-1.0-FCS.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.jasperreports/lib/commons-beanutils-1.7.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.jasperreports/lib/commons-digester-1.7.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.jasperreports/lib/groovy-all-1.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.jasperreports/lib/itext-1.3.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.jasperreports/lib/jasperreports-1.3.4.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.jdbcfreezedryer/lib/commons-dbcp-1.2.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.jdbcfreezedryer/lib/commons-pool-1.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.jdbcfreezedryer/lib/hsqldb.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.jdbcfreezedryer/lib/mysql-connector-java-3.1.12-bin.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.matlab/lib/jmatlink-1.0.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.plugin.util/lib/wizard-0.1.12.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-antlr.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-apache-bcel.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-apache-bsf.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-apache-log4j.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-apache-oro.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-apache-regexp.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-apache-resolver.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-commons-logging.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-commons-net.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-jai.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-javamail.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-jdepend.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-jmf.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-jsch.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-junit.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-launcher.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-netrexx.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-nodeps.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-starteam.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-stylebook.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-swing.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-testutil.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-trax.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant-weblogic.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/ant.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/commons-logging-1.0.4.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/groovy-all-1.5.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/javassist-3.7.0.GA.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/jpf-boot.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/jpf.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/log4j-1.2.13.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/saf.core.runtime.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/xercesImpl.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/xml-apis.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/xpp3_min-1.1.4c.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/xstream-1.3.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/testlib/cenquatasks.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/testlib/jmock-1.0.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/testlib/jmock-cglib-1.0.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/testlib/junit-3.8.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/testlib/junit-addons-1.4.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.score.runtime/lib/org.eclipse.emf.common_2.3.0.v200702221030.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.score.runtime/lib/org.eclipse.emf.ecore.xmi_2.3.0.v200702221030.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.score.runtime/lib/org.eclipse.emf.ecore_2.3.0.v200702221030.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.score/lib/commons-collections-3.2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.score/lib/commons-lang-2.2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.terracotta/lib/commonj-twm-1.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.terracotta/lib/datagrid-1.0-SNAPSHOT.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.terracotta/lib/spider-1.0-SNAPSHOT.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.visualization/lib/MS3DLoader-1.0.8.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.visualization/lib/cytoscape-graph-layout.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.visualization/lib/jung-visualization-2.0-alpha2.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.visualization/lib/phoebe.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.visualization/lib/vecmath-1.3.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.visualization/lib/worldwind.jar",
			"ECLIPSE_HOME/plugins/saf.core.ui/lib/TableLayout.jar",
			"ECLIPSE_HOME/plugins/saf.core.ui/lib/forms-1.0.5.jar",
			"ECLIPSE_HOME/plugins/saf.core.ui/lib/jh-2.0_02.jar",
			"ECLIPSE_HOME/plugins/saf.core.ui/lib/jh.jar",
			"ECLIPSE_HOME/plugins/saf.core.ui/lib/l2fprod-common-all.jar",
			"ECLIPSE_HOME/plugins/saf.core.ui/lib/osx.jar",
			"ECLIPSE_HOME/plugins/saf.core.ui/lib/rs_flexdock-0.4.0.jar",
			"ECLIPSE_HOME/plugins/saf.core.ui/lib/wizard-0.1.12.jar",
			"ECLIPSE_HOME/plugins/saf.core.ui/saf.core.ui.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/javassist-3.1.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/xpp3-1.1.3.4d_b4_min.jar",
			"ECLIPSE_HOME/plugins/repast.simphony.runtime/lib/xstream-1.1.3.jar",
			"GROOVY_ECLIPSE_HOME/groovy-all-1.1-beta-2.jar" };

	/**
	 * The constructor.
	 */
	public AgentBuilderPlugin() {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle
					.getBundle("AgentBuilderPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
			x.printStackTrace();
		}

	}

	// @@ @Override
	// public void start(BundleContext context) throws Exception {
	//
	// // A workaround for Eclipse Bug 169717 which does not appear to
	// // be fixed despite the status on the bug tracker.
	// // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=169717
	// // try {
	// // PackageAdmin packageAdmin = (PackageAdmin) context.getService(context
	// // .getServiceReference(PackageAdmin.class.getName()));
	// // if (packageAdmin != null) {
	// // packageAdmin.refreshPackages(context.getBundles());
	// // }
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // }
	//
	// super.start(context);
	//
	// IAdapterManager manager = Platform.getAdapterManager();
	// manager.registerAdapters(new AgentBuilderElementAdapterFactory(),
	// IAgentBuilderElement.class);
	// manager.registerAdapters(new AgentBuilderResourceAdapterFactory(),
	// IResource.class);
	// }

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
	 * (Iterator iter = classpathList.iterator(); iter.hasNext();) { IPath path
	 * = (IPath) iter.next(); runtimeLibs.add(path.toFile().toURL()); } }
	 */

	/**
	 * Converts Paths to URLs
	 */
	public static List convertPaths2URLs(List pathList)
			throws MalformedURLException {
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
	public static List convertPaths2Files(List pathList)
			throws MalformedURLException {
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
	 *            the root folder
	 * @param folderPath
	 *            the relative path of the folder to create
	 * @return the created folder
	 * @throws CoreException
	 *             if the creation fails
	 */
	public static IContainer createFolder(IContainer outputFolder,
			IPath folderPath, boolean derived) throws CoreException {
		if (folderPath.isEmpty())
			return outputFolder;
		IFolder folder = outputFolder.getFolder(folderPath);
		if (!folder.exists()) {
			createFolder(outputFolder, folderPath.removeLastSegments(1),
					derived);
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
	 * Returns whether a project has a Repast Simphony nature.
	 * 
	 * @param project
	 * @return
	 */
	public static boolean hasRepastSimphonyNature(IProject project) {
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
		IWorkbenchWindow window = plugin.getWorkbench()
				.getActiveWorkbenchWindow();
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
	 * (adapter instanceof IResource)) { IResource resource = (IResource)
	 * adapter; IProject project = resource.getProject(); IJavaProject
	 * javaProject = JavaCore.create(project); return javaProject; } } } return
	 * null; }
	 */

	/**
	 * Convenience method to retrieve the Java project containing file in the
	 * currently opened editor.
	 * 
	 * @return The active Java project.
	 */
	public static IJavaProject getActiveJavaProject() {
		return AgentBuilderPlugin.getJavaProject(AgentBuilderPlugin
				.getActiveProject());
	}

	/**
	 * Convenience method to retrieve the project containing file in the
	 * currently opened editor.
	 * 
	 * @return The active project.
	 */
	public static IProject getActiveProject() {
		return AgentBuilderPlugin.getActiveFlowEditor().getFlowFile()
				.getProject();
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
				AgentBuilderPlugin.agentBaseType = AgentBuilderPlugin
						.getActiveJavaProject().findType("java.lang.Object");
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
	 *            project name
	 * @return the java project with the given name
	 */
	public static IJavaProject getJavaProject(String projectName) {
		return getJavaModel().getJavaProject(projectName);
	}

	/**
	 * Returns the java project with the name of the given project
	 * 
	 * @param project
	 *            project taht is maybe a java project
	 * @return the java project with the name of the given project
	 */
	public static IJavaProject getJavaProject(IProject project) {
		return getJavaProject(project.getName());
	}

	/**
	 * TODO
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public static AgentBuilderProject getFlow4JProject(IProject project)
			throws CoreException {
		return (AgentBuilderProject) (project
				.getNature(AgentBuilderPlugin.AGENT_BUILDER_NATURE_ID));
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
	 * Returns a sorted list of projects. Sorted means that the longer comes
	 * first if two project names have the same prefix. The projects can be open
	 * or closed.
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
	 *            the path of the project
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
	 * Returns a list of <code>IPath</code> classpath entries of the java
	 * project.
	 * 
	 * @param project
	 *            the java project
	 * @return list of <code>IPath</code> classpath entries of the java project.
	 * @throws JavaModelException
	 */
	/*
	 * public static List getClasspathEntryLocations(IJavaProject project)
	 * throws JavaModelException { List locations = new ArrayList(); for (int j
	 * = 0; j < project.getAllPackageFragmentRoots().length; j++) {
	 * IPackageFragmentRoot frag = project.getAllPackageFragmentRoots()[j];
	 * IClasspathEntry cp = frag.getRawClasspathEntry(); IPath path =
	 * cp.getPath(); if (cp.getEntryKind() == IClasspathEntry.CPE_SOURCE ||
	 * cp.getEntryKind() == IClasspathEntry.CPE_CONTAINER) continue; if
	 * (cp.getEntryKind() == IClasspathEntry.CPE_VARIABLE) { cp =
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
		IEditorRegistry registry = PlatformUI.getWorkbench()
				.getEditorRegistry();
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
	public static IPath[] getClasspathSourceContainers(IProject project)
			throws JavaModelException {
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
	 *            the java file
	 * @return the classpath of the java file or <code>null</code> if it cannot
	 *         determined.
	 * @throws JavaModelException
	 *             if the classpath cannot be determined
	 */
	static public IPath getClassPath(IFile file) throws JavaModelException {
		IPath filePath = file.getProjectRelativePath();
		IPath[] scPaths = AgentBuilderPlugin.getClasspathSourceContainers(file
				.getProject());
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
	public static void getResolvedClasspath(AgentEditor agentEditor,
			List pathEntries) {
		getResolvedClasspath(((FileEditorInput) agentEditor.getEditorInput())
				.getFile(), pathEntries);
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

		IJavaProject javaProject = AgentBuilderPlugin
				.getJavaProject(projectName);
		IPath path = null;
		try {
			IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);
			for (int i = 0; i < entries.length; i++) {
				IClasspathEntry entry = entries[i];
				path = null;
				if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER) {
					path = getWorkspaceRoot().getLocation()
							.append(
									JavaCore.getResolvedClasspathEntry(entry)
											.getPath());
				} else if (entry.getEntryKind() == IClasspathEntry.CPE_VARIABLE) {
					path = JavaCore.getResolvedClasspathEntry(entry).getPath();
				} else if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
					path = entry.getPath().makeAbsolute();
					if (!path.toFile().getAbsoluteFile().exists()) {
						IPath location = getWorkspaceRoot().getProject(
								entry.getPath().segment(0)).getFile(
								entry.getPath().removeFirstSegments(1))
								.getLocation();
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
					IProject requiredProject = getWorkspaceProject(entry
							.getPath());
					// recurse into projects
					if (requiredProject != null)
						getResolvedClasspath(requiredProject, pathEntries);
				}
			}
			IPath outputPath = javaProject.getOutputLocation();
			if (outputPath.segmentCount() == 1)
				outputPath = javaProject.getResource().getLocation();
			else
				outputPath = javaProject.getProject().getFile(
						outputPath.removeFirstSegments(1)).getLocation();
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

	/**
	 * Adds the Repast Simphony nature to the given project. Automatically adds
	 * the builder to the project
	 * 
	 * @param project
	 *            the project which will have the Repast Simphony nature
	 * @param monitor
	 *            the progress monitor
	 * @throws CoreException
	 *             if something goes wrong
	 */
	static public void addRepastSimphonyNature(IProject project,
			IProgressMonitor monitor, boolean checkPreviousVersionFiles,
			boolean configureGroovy) throws CoreException {

		if ((project != null)
				&& project.hasNature(JavaCore.NATURE_ID)
				&& (!project
						.hasNature(AgentBuilderPlugin.AGENT_BUILDER_NATURE_ID))) {

			final IJavaProject javaProject = JavaCore.create(project);

			if (checkPreviousVersionFiles) {

				if (project.hasNature(REPAST_SIMPHONY_VERSION_1_NATURE)) {
					try {

						List<IClasspathEntry> newClassPathEntries = new ArrayList<IClasspathEntry>();
						IPath[] oldJARFileListPaths = new IPath[oldJARFileList.length];
						for (int i = 0; i < oldJARFileListPaths.length; i++) {
							oldJARFileListPaths[i] = new Path(oldJARFileList[i]);
						}
						IClasspathEntry[] oldClassPathEntries = javaProject
								.getRawClasspath();
						if ((oldClassPathEntries != null)
								&& (oldClassPathEntries.length > 0)) {
							for (IClasspathEntry oldClassPathEntry : oldClassPathEntries) {
								if (!oldClassPathEntry.getPath().lastSegment()
										.equals("bin-groovy")) {
									boolean found = false;
									for (int i = 0; i < oldJARFileListPaths.length; i++) {
										if (oldClassPathEntry.getPath()
												.lastSegment().equals(
														oldJARFileListPaths[i]
																.lastSegment())) {
											found = true;
											break;
										}
									}
									if (!found) {
										newClassPathEntries
												.add(oldClassPathEntry);
									}
								}
							}
							if ((0 < newClassPathEntries.size())
									&& (newClassPathEntries.size() < oldClassPathEntries.length)) {
								javaProject
										.setRawClasspath(
												(IClasspathEntry[]) newClassPathEntries
														.toArray(new IClasspathEntry[newClassPathEntries
																.size()]), null);
							}
						}

						GroovyRuntime.removeGroovyNature(project.getProject());
						GroovyRuntime.addGroovyRuntime(project.getProject());

						removeFromBuildSpec(project,
								REPAST_SIMPHONY_VERSION_1_BUILDER);
						removeNature(project, REPAST_SIMPHONY_VERSION_1_NATURE);

					} catch (Exception e) {
					}

				}

				IClasspathEntry list[] = javaProject.getRawClasspath();
				IPath srcPath = null;
				for (IClasspathEntry entry : list) {
					if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
						srcPath = entry.getPath();
						break;
					}
				}
				if (srcPath != null) {

					MessageBox messageBox = new MessageBox(PlatformUI
							.getWorkbench().getActiveWorkbenchWindow()
							.getShell(), SWT.YES | SWT.NO | SWT.ICON_WARNING);
					messageBox
							.setMessage("Overwrite existing launch and deployment files with new versions?");
					if (messageBox.open() == SWT.YES) {

						IFolder srcFolder = javaProject.getProject().getFolder(
								srcPath.removeFirstSegments(1));

						String scenarioFolder = "";
						for (char ch : javaProject.getElementName()
								.toCharArray()) {
							if (Character.isLetterOrDigit(ch)) {
								scenarioFolder = scenarioFolder + ch;
							}
						}
						scenarioFolder = (scenarioFolder + ".rs").toLowerCase();
						IFolder scenarioFolderFolder = srcFolder
								.getFolder("../" + scenarioFolder);

						IFolder srcFolderFolder = srcFolder
								.getFolder("../launchers");
						if ((scenarioFolderFolder.exists())
								&& (srcFolderFolder.exists())) {
							Util.createLaunchConfigurations(javaProject,
									srcFolderFolder, scenarioFolder);
						}

						srcFolderFolder = srcFolder.getFolder("../installer");
						if (srcFolderFolder.exists()) {

							String[][] variableMap = {
									{ "%MODEL_NAME%",
											javaProject.getElementName() },
									{ "%PROJECT_NAME%",
											javaProject.getElementName() },
									{ "%SCENARIO_DIRECTORY%", scenarioFolder },
									{
											"%PACKAGE%",
											javaProject.getElementName()
													.toLowerCase() },
									{
											"%REPAST_SIMPHONY_INSTALL_BUILDER_PLUGIN_DIRECTORY%",
											AgentBuilderPlugin
													.getPluginInstallationDirectory() } };
							Util.copyFileFromPluginInstallation(
									"installer/installation_components.xml",
									srcFolderFolder,
									"installation_components.xml", variableMap,
									monitor);
							Util.copyFileFromPluginInstallation(
									"installer/start_model.bat",
									srcFolderFolder, "start_model.bat",
									variableMap, monitor);
							Util.copyFileFromPluginInstallation(
									"installer/start_model.command",
									srcFolderFolder, "start_model.command",
									variableMap, monitor);
							Util.copyFileFromPluginInstallation(
									"installer/installation_coordinator.xml",
									srcFolderFolder,
									"installation_coordinator.xml",
									variableMap, monitor);
						}

					}

				}

			} else {

				if (configureGroovy) {
					try {
						GroovyRuntime.addGroovyRuntime(project.getProject());
					} catch (Exception e) {
					}
				}

			}

			// Update the class path.
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
			String[] newNatures = new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = AgentBuilderPlugin.AGENT_BUILDER_NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, IResource.FORCE, monitor);

			IPath jarPath = new Path(
					RepastProjectClasspathContainer.JAR_CLASSPATH_DEFAULT);
			IClasspathEntry[] oldClassPathEntries = javaProject
					.getRawClasspath();
			boolean found = false;
			for (int i = 0; i < oldClassPathEntries.length; i++) {
				final IClasspathEntry classPathEntry = oldClassPathEntries[i];
				if (classPathEntry.getPath().lastSegment().equals(
						jarPath.lastSegment())) {
					found = true;
					break;
				}
			}

			if (!found) {
				final IClasspathEntry[] newClassPathEntries = (IClasspathEntry[]) ArrayUtils
						.add(oldClassPathEntries, JavaCore
								.newContainerEntry(jarPath));
				javaProject.setRawClasspath(newClassPathEntries, null);
			}

			javaProject.save(monitor, true);

		} else {
			if (monitor != null)
				monitor.worked(1);
		}

	}

	public static String pluginInstallationDirectory = null;

	public static String getPluginInstallationDirectory() {
		if (pluginInstallationDirectory == null) {

			try {
				URL relativeURL = FileLocator.find(AgentBuilderPlugin
						.getDefault().getBundle(), new Path(""), null);
				URL absoluteURL = FileLocator.resolve(relativeURL);
				String tempURLString = URLDecoder.decode(absoluteURL.getFile(),
						System.getProperty("file.encoding"));
				String mainDataSourcePluginFile = new File(tempURLString)
						.getPath();
				IPath mainDataSourcePluginFilePath = new Path(
						mainDataSourcePluginFile);
				IPath mainDataSourcePluginDirectoryPath = mainDataSourcePluginFilePath
						.removeLastSegments(1);

				pluginInstallationDirectory = mainDataSourcePluginDirectoryPath
						.toFile().toString()
						+ System.getProperty("file.separator");

			} catch (Exception e) {
				e.printStackTrace();
				Util
						.printMessageBox("Exception in getPluginInstallationDirectory "
								+ e.getMessage());
			}

		}
		return pluginInstallationDirectory;
	}

	static public String[] getJarPathList() {

		if (finalJarClassPathList == null) {

			File file = null;
			try {
				String mainDataSourcePluginDirectory = AgentBuilderPlugin
						.getPluginInstallationDirectory();

				if (mainDataSourcePluginDirectory.trim().equals("")) {
					file = new Path(JAR_PATH_RELATIVE).toFile();
				} else {
					file = new Path(mainDataSourcePluginDirectory
							+ JAR_PATH_RELATIVE).toFile();
				}

				JarFile jarFile = new JarFile(file);

				Manifest manifest = jarFile.getManifest();

				Attributes attributes = manifest.getMainAttributes();

				String jarClassPath = attributes.getValue(
						Attributes.Name.CLASS_PATH).trim();

				jarClassPath = jarClassPath.replace("./", "/");
				jarClassPath = jarClassPath.replace(".\\", "\\");
				jarClassPath = jarClassPath.replace("\\", "/");

				String[] jarClassPathList = jarClassPath.split(" ");
				finalJarClassPathList = new String[jarClassPathList.length
						+ AGENT_BUILDER_EXTRA_JARS.size()];
				int i = 0;
				for (String jarElement : jarClassPathList) {
					finalJarClassPathList[i] = JavaCore
							.getClasspathVariable("ECLIPSE_HOME")
							+ "/plugins" + jarElement;
					i++;
				}
				for (int j = 0; j < AGENT_BUILDER_EXTRA_JARS.size(); j++) {
					finalJarClassPathList[i] = JavaCore
							.getClasspathVariable("ECLIPSE_HOME")
							+ "/plugins" + AGENT_BUILDER_EXTRA_JARS.get(j);
					i++;
				}
				if (finalJarClassPathList == null){
					
					message("Error, Error: finalJarClassPathList == null");
					message("mainDataSourcePluginDirectory = " + mainDataSourcePluginDirectory);
					message("file = " + file.toString());
					message("jarFile = " + jarFile.toString());
					message("jarClassPath = " + jarClassPath);
				}
			} catch (Exception e) {
				message("Based on AGENT_BUILDER_PLUGIN_VERSION = " + AGENT_BUILDER_PLUGIN_VERSION + ", could not find " + file);
				e.printStackTrace();
			}
		}

		return finalJarClassPathList;

	}

	static public String[] getJarPathListForLauncher() {

		if (finalJarClassPathListForLauncher == null) {

			try {

				finalJarClassPathListForLauncher = new String[finalJarClassPathListForLauncherBasics.length
						+ AGENT_BUILDER_EXTRA_JARS.size()];
				int i = 0;
				for (String jarElement : finalJarClassPathListForLauncherBasics) {
					if ((jarElement != null)
							&& (jarElement.toLowerCase().endsWith("jar"))) {
						finalJarClassPathListForLauncher[i] = JavaCore
								.getClasspathVariable("ECLIPSE_HOME")
								+ "/plugins" + jarElement;
					} else {
						finalJarClassPathListForLauncher[i] = jarElement;
					}
					i++;
				}
				for (int j = 0; j < AGENT_BUILDER_EXTRA_JARS.size(); j++) {
					String extraJar = AGENT_BUILDER_EXTRA_JARS.get(j);
					if ((extraJar != null)
							&& (extraJar.toLowerCase().endsWith("jar"))) {
						finalJarClassPathListForLauncher[i] = JavaCore
								.getClasspathVariable("ECLIPSE_HOME")
								+ "/plugins" + extraJar;
					} else {
						finalJarClassPathListForLauncher[i] = extraJar;
					}
					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		return finalJarClassPathListForLauncher;

	}

	/**
	 * Removes the Repast Simphony nature to the given project. Automatically
	 * removes the flow builder to the project
	 * 
	 * @param project
	 *            the project which will get rid of its Repast Simphony nature
	 * @param monitor
	 *            the progress monitor
	 * @throws CoreException
	 *             if something goes wrong
	 */
	static public void removeRepastSimphonyNature(IProject project,
			IProgressMonitor monitor, boolean checkNature) throws CoreException {

		if ((project != null)
				&& project.hasNature(JavaCore.NATURE_ID)
				&& ((project
						.hasNature(AgentBuilderPlugin.AGENT_BUILDER_NATURE_ID) || (!checkNature)))) {

			final IJavaProject javaProject = JavaCore.create(project);

			IProjectDescription description = project.getDescription();
			List natures = Arrays.asList(description.getNatureIds());
			List newNatures = new ArrayList(natures.size() - 1);
			for (ListIterator iter = natures.listIterator(); iter.hasNext();) {
				String nature = (String) iter.next();
				if (!nature.equals(AgentBuilderPlugin.AGENT_BUILDER_NATURE_ID)) {
					newNatures.add(nature);
				}
			}
			description.setNatureIds((String[]) newNatures
					.toArray(new String[newNatures.size()]));
			project.setDescription(description, monitor);

			try {
				GroovyRuntime.removeGroovyNature(project);
			} catch (Exception e) {
			}

			List<IClasspathEntry> newClassPathEntries = new ArrayList<IClasspathEntry>();

			IPath[] oldJARFileListPaths = new IPath[oldJARFileList.length];
			for (int i = 0; i < oldJARFileListPaths.length; i++) {
				oldJARFileListPaths[i] = new Path(oldJARFileList[i]);
			}

			IClasspathEntry[] oldClassPathEntries = javaProject
					.getRawClasspath();
			for (IClasspathEntry oldClassPathEntry : oldClassPathEntries) {

				boolean found = false;
				for (int i = 0; i < oldJARFileListPaths.length; i++) {
					if (oldClassPathEntry.getPath().lastSegment().equals(
							oldJARFileListPaths[i].lastSegment())) {
						found = true;
						break;
					}
				}

				if (!found) {
					newClassPathEntries.add(oldClassPathEntry);
				}

			}
			javaProject.setRawClasspath(newClassPathEntries
					.toArray(new IClasspathEntry[0]), null);
			javaProject.save(monitor, true);

		}

	}

	static public String getSymbolicName() {
		return AgentBuilderPlugin.getDefault().getBundle().getSymbolicName();
	}

	static public URL getInstallURL() {
		return AgentBuilderPlugin.getDefault().getBundle().getEntry("/");
	}

	static public String getInstallFolderPathStr() throws IOException {
		return FileLocator.resolve(AgentBuilderPlugin.getInstallURL())
				.getPath();
	}

	static public URL getInstallFolderPathURL() throws IOException {
		return FileLocator.resolve(AgentBuilderPlugin.getInstallURL());
	}

	static public IPath getInstallFolderPath() throws IOException {
		return new Path(getInstallFolderPathStr());
	}

	static public String getPluginInstallFolderPathStr(String pluginId)
			throws IOException {
		return FileLocator.resolve(Platform.getBundle(pluginId).getEntry("/"))
				.getPath();
	}

	static public URL getPluginInstallFolderPathURL(String pluginId)
			throws IOException {
		return FileLocator.resolve(Platform.getBundle(pluginId).getEntry("/"));
	}

	static public IPath getPluginInstallFolderPath(String pluginId)
			throws IOException {
		return new Path(getPluginInstallFolderPathStr(pluginId));
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = AgentBuilderPlugin.getDefault()
				.getResourceBundle();
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
		log(new Status(IStatus.ERROR, AgentBuilderPlugin.getDefault()
				.getBundle().getSymbolicName(), IStatus.ERROR, "Error", e)); //$NON-NLS-1$
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
	 * public static void logException(Throwable e) { logException(e, null,
	 * null); }
	 * 
	 * public static void log(Throwable e) { if (e instanceof
	 * InvocationTargetException) e = ((InvocationTargetException)
	 * e).getTargetException(); IStatus status = null; if (e instanceof
	 * CoreException) status = ((CoreException) e).getStatus(); else status =
	 * new Status(IStatus.ERROR, getPluginId(), IStatus.OK, e.getMessage(), e);
	 * log(status); }
	 */

	/**
	 * Opens a message Dialog window with the given messge.
	 * 
	 * @param message
	 *            the message
	 */
	static public void message(String message) {
		MessageDialog.openInformation(new Shell(), "Repast Simphony Plug-in",
				message);
	}

	/**
	 * Returns the pretty printer Settings
	 * 
	 * @return the pretty printer Settings
	 */
	public static FileSettings getPrettySettings() {
		return prettySettings;
	}

	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}

	public static String findExtension(String variableName) {
		IValueVariable variable = VariablesPlugin.getDefault()
				.getStringVariableManager().getValueVariable(variableName);
		if (variable != null) {
			return variable.getValue();
		} else {
			return null;
		}
	}

	public static void removeFromBuildSpec(IProject project, String builderID)
			throws CoreException {
		IProjectDescription description = project.getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int counter = 0; counter < commands.length; ++counter) {
			if (commands[counter].getBuilderName().equals(builderID)) {
				ICommand[] updatedCommands = new ICommand[commands.length - 1];
				System.arraycopy(commands, 0, updatedCommands, 0, counter);
				System.arraycopy(commands, counter + 1, updatedCommands,
						counter, commands.length - counter - 1);
				description.setBuildSpec(updatedCommands);
				project.setDescription(description, null);
				return;
			}
		}
	}

	public static void removeNature(IProject project, String nature)
			throws CoreException {
		final IProjectDescription description = project.getDescription();
		final String[] natureIds = description.getNatureIds();
		for (int counter = 0; counter < natureIds.length; ++counter) {
			if (natureIds[counter].equals(nature)) {
				final String[] updatedIds = (String[]) ArrayUtils.remove(
						natureIds, counter);
				description.setNatureIds(updatedIds);
				project.setDescription(description, null);
				return;
			}
		}
	}

}
