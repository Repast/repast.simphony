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

package repast.simphony.agents.designer.ui.editors;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ToggleSnapToGeometryAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.commands.ActionHandler;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.ui.part.FileEditorInput;

import repast.simphony.agents.base.Util;
import repast.simphony.agents.designer.core.AgentBuilderPlugin;
import repast.simphony.agents.designer.editparts.AgentBuilderEditPartFactory;
import repast.simphony.agents.designer.model.AgentDiagramModelPart;
import repast.simphony.agents.designer.model.DesignerModelDigester;
import repast.simphony.agents.designer.model.propertycelleditors.EditableComboBoxCellEditor;
import repast.simphony.agents.designer.ui.actions.CopyAction;
import repast.simphony.agents.designer.ui.actions.PasteAction;
import repast.simphony.agents.designer.ui.actions.SaveToImageAction;
import repast.simphony.agents.designer.ui.actions.SelectionCountingAction;
import repast.simphony.agents.designer.ui.actions.ShowPropertiesAction;
import repast.simphony.agents.designer.ui.wizards.NewCodeWizardEntry;
import repast.simphony.agents.flows.tasks.AgentBuilderRuntimeException;
import repast.simphony.agents.model.bind.AgentModelBind;
import repast.simphony.agents.model.bind.BindingHandler;
import repast.simphony.agents.model.codegen.Consts;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * 
 * 
 * 
 */
@SuppressWarnings( { "unchecked" })
public class AgentEditor extends GraphicalEditorWithPalette {

	private PaletteRoot root;
	private AgentDiagramModelPart flowDiagram = new AgentDiagramModelPart();
	private KeyHandler sharedKeyHandler;
	private boolean savePreviouslyNeeded = false;
	private SelectionCountingAction selectionCountingAction = null;
	private CopyAction copyAction = null;
	private int currentGridResolution = 1;
	private static EditableComboBoxCellEditor currentCellEditor = null;
	
	public static EditableComboBoxCellEditor getCurrentCellEditor() {
		return currentCellEditor;
	}

	public static void setCurrentCellEditor(EditableComboBoxCellEditor newCellEditor) {
		currentCellEditor = newCellEditor;
	}

	public int getCurrentGridResolution() {
		return currentGridResolution;
	}

	public void setCurrentGridResolution(int currentGridResolution) {
		this.currentGridResolution = currentGridResolution;
	}

	public static final int DEFAULT_GRID_RESOLUTION = 15;

	protected ArrayList<NewCodeWizardEntry> exampleListIfWizard = 
		DecisionWizardGenerator.createDecisionWizard();

	protected ArrayList<NewCodeWizardEntry> exampleListLoopWizard = 
		LoopWizardGenerator.createLoopWizard();

	protected ArrayList<NewCodeWizardEntry> exampleListTaskWizard = 
		TaskWizardGenerator.createTaskWizard();

	protected ArrayList<String> exampleListIf = new ArrayList<String>();
	{
		exampleListIf
				.add("attribute > 10");
		exampleListIf
				.add("attribute >= 10");
		exampleListIf
				.add("attribute == 10");
		exampleListIf
				.add("attribute <= 10");
		exampleListIf
				.add("attribute < 10");
		exampleListIf
				.add("(attribute < 10) && (time > 5) /* An Example \"And\" Check */");
		exampleListIf
				.add("(attribute < 10) || (time > 5) /* An Example \"Or\" Check */");
		exampleListIf
				.add("!done /* An Example \"Not\" Check */");
		exampleListIf
				.add("attribute == 121.3.volts /* An Example \"Equals\" Check with Units*/");
		exampleListIf
				.add("watchedAgent.attribute > 120 /* An Example Check of a Watched Agent */");
		exampleListIf
				.add("RandomDrawAgainstThreshold(0.3)/* Make a Uniform Random Draw and Check to See If It Is Greater Than a Given Threshold */");
		exampleListIf
				.add("RandomDraw(5.0, 10.0) > 11.0");

		exampleListIf
				.add("new OrQuery(new PropertyEquals(context, \"name\", \"David Healy\"), new GridWithin(grid, agent, 3)).query(optNestedResult)).iterator().hasNext()");
		exampleListIf
				.add("new AndQuery(query1, query2).query(optNestedResult).iterator().hasNext()");
		exampleListIf
				.add("new NotQuery(context, new PropertyLessThan(context, \"wealth\", 3.14)).iterator().hasNext()");
		exampleListIf
				.add("new NetPathWithin(network, node, 3).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new NetworkAdjacent(network, node).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new NetworkPredecessor(network, node).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new NetworkSuccessor(network, node).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new GridWithin(grid, agent, 3).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new MooreQuery(grid, agent, 3, 3).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new VNQuery(grid, agent, 2, 1, 1).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new ContinuousWithin(continuousSpace, agent, 11.5).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new ContainsQuery(geography, geometry).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new ItersectsQuery(geography, geometry).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new GeographyWithin(geography, geometry).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new TouchesQuery(geography, geometry).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new WithinQuery(geography, geometry).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new PropertyEquals(context, \"name\", \"David Healy\").query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new PropertyGreaterThan(context, \"wealth\", 3.14).query(optNestedQueryResult).iterator().hasNext()");
		exampleListIf
				.add("new PropertyLessThan(context, \"wealth\", 3.14).query(optNestedQueryResult).iterator().hasNext()");

	}

	protected ArrayList<String> exampleListLoop = new ArrayList<String>();
	{
		exampleListLoop
				.add("i in 1..10");
		exampleListLoop
				.add("i in [2, 4, 6, 7, 12, 19]");
		exampleListLoop
				.add("a in [\"Item One\", \"Item Two\", \"Item Three\", \"Item Four\"]");
		exampleListLoop
				.add("character in \"abcdefg\"");
		exampleListLoop
				.add("int i = 0; i < 10; i++");
		exampleListLoop
				.add("a in List");
		exampleListLoop
				.add("attribute > 10");
		exampleListLoop
				.add("attribute >= 10");
		exampleListLoop
				.add("attribute == 10");
		exampleListLoop
				.add("attribute <= 10");
		exampleListLoop
				.add("attribute < 10");
		exampleListLoop
				.add("(attribute < 10) && (time > 5) /* An Example \"And\" Check */");
		exampleListLoop
				.add("(attribute < 10) || (time > 5) /* An Example \"Or\" Check */");
		exampleListLoop
				.add("!done /* An Example \"Not\" Check */");
		exampleListLoop
				.add("attribute == 121.3.volts /* An Example \"Equals\" Check with Units*/");
		exampleListLoop
				.add("watchedAgent.attribute > 120 /* An Example Check of a Watched Agent */");
		exampleListLoop
				.add("RandomDrawAgainstThreshold(0.3) /* Make a Uniform Random Draw and Check to See If It Is Greater Than a Given Threshold */");
		exampleListLoop
				.add("RandomDraw(5.0, 10.0) > 11.0");

		exampleListLoop
				.add("neighbor in (new OrQuery(new PropertyEquals(context, \"name\", \"David Healy\"), new GridWithin(grid, agent, 3)).query(optNestedResult)))");
		exampleListLoop
				.add("neighbor in (new AndQuery(query1, query2).query(optNestedResult))");
		exampleListLoop
				.add("neighbor in (new NotQuery(context, new PropertyLessThan(context, \"wealth\", 3.14)))");
		exampleListLoop
				.add("neighbor in (new NetPathWithin(network, node, 3).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new NetworkAdjacent(network, node).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new NetworkPredecessor(network, node).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new NetworkSuccessor(network, node).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new GridWithin(grid, agent, 3).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new MooreQuery(grid, agent, 3, 3).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new VNQuery(grid, agent, 2, 1, 1).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new ContinuousWithin(continuousSpace, agent, 11.5).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new ContainsQuery(geography, geometry).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new ItersectsQuery(geography, geometry).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new GeographyWithin(geography, geometry).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new TouchesQuery(geography, geometry).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new WithinQuery(geography, geometry).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new PropertyEquals(context, \"name\", \"David Healy\").query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new PropertyGreaterThan(context, \"wealth\", 3.14).query(optNestedQueryResult))");
		exampleListLoop
				.add("neighbor in (new PropertyLessThan(context, \"wealth\", 3.14).query(optNestedQueryResult))");

	}

	protected ArrayList<String> exampleListTask1 = new ArrayList<String>();
	{
		exampleListTask1
				.add("Assignment: Assign a Variable [variableName = value]");
		exampleListTask1
				.add("    Assign a Variable from a Watched Agent [variableName = watchedAgent.value]");
		exampleListTask1
				.add("    Assign an Agent Field [fieldName = value]");
		exampleListTask1
				.add("    Assign an Agent Field from a Watched Agent [value = watchedAgent.value]");
		exampleListTask1
				.add("    Assign a Behavior Step Return Value from a Calculation [returnValue = value]");
		exampleListTask1
				.add("    Assign a Behavior Step Return Value from a Watched Agent [returnValue = watchedAgent.value]");

		exampleListTask1
				.add("Agents: Create an Agent [Object CreateAgent(String parentContextPath, String newAgentType)]");
		exampleListTask1
				.add("    Add an Agent to a Context [Context AddAgentToContext(String context, Object agent)]");
		exampleListTask1
				.add("    Remove an Agent from a Context [Context RemoveAgentFromContext(String context, Object agent)]");
		exampleListTask1
				.add("    Remove an Agent from the Entire Model [Context RemoveAgentFromModel(Object agent)]");
		exampleListTask1
				.add("    Move an Agent in a Grid or Continuous Space [boolean MoveAgent(String projection, Object agent, double... newLocation)]");
		exampleListTask1
				.add("    Get All Agents of a type from a Context [IndexedIterable GetObjects(String contextPath, String className)]");
		exampleListTask1
				.add("    Get Random Agents of a type from a Context [Iterable GetRandomObjects(String contextPath, String className, long numToGet)]");

		exampleListTask1
				.add("Statistics: Assign a Uniform Random Number Between 0.0 and 1.0");
		exampleListTask1
				.add("    Assign a Uniform Random Number Between 5.0 and 15.0");
		exampleListTask1
				.add("    Initialize the default Beta distribution");
		exampleListTask1
				.add("    Initialize the default Binomial distribution");
		exampleListTask1
				.add("    Initialize the default Chi Square distribution");
		exampleListTask1
				.add("    Initialize the default Empirical distribution");
		exampleListTask1
				.add("    Initialize the default Exponential distribution");
		exampleListTask1
				.add("    Initialize the default Gamma distribution");
		exampleListTask1
				.add("    Initialize the default Logarithmic distribution");
		exampleListTask1
				.add("    Initialize the default Normal distribution");
		exampleListTask1
				.add("    Initialize the default Poisson distribution");
		exampleListTask1
				.add("    Initialize the default Student T distribution");
		exampleListTask1
				.add("    Initialize the default von Mises distribution");
		exampleListTask1
				.add("    Initialize the default Zeta distribution");
		exampleListTask1
				.add("    Assign a random draw from the default Beta distribution");
		exampleListTask1
				.add("    Assign a random draw from the default Binomial distribution");
		exampleListTask1
				.add("    Assign a random draw from the default Chi Square distribution");
		exampleListTask1
				.add("    Assign a random draw from the default Empirical distribution");
		exampleListTask1
				.add("    Assign a random draw from the default Exponential distribution");
		exampleListTask1
				.add("    Assign a random draw from the default Gamma distribution");
		exampleListTask1
				.add("    Assign a random draw from the default Logarithmic distribution");
		exampleListTask1
				.add("    Assign a random draw from the default Normal distribution");
		exampleListTask1
				.add("    Assign a random draw from the default Poisson distribution");
		exampleListTask1
				.add("    Assign a random draw from the default Student T distribution");
		exampleListTask1
				.add("    Assign a random draw from the default von Mises distribution");
		exampleListTask1
				.add("    Assign a random draw from the default Zeta distribution");

		exampleListTask1
				.add("    Create an instance of a (Beta) distribution object");
		exampleListTask1
				.add("    Obtain a random draw from a distribution (Beta) object");

		exampleListTask1
				.add("Math: Create a 1 x 3 Matrix");
		exampleListTask1
				.add("    Create a 3 x 2 Matrix");
		exampleListTask1
				.add("    Create a 3 x 2 Matrix");
		exampleListTask1
				.add("    Matrix Addition [matrix1 + matrix2]");
		exampleListTask1
				.add("    Matrix Subtraction [matrix1 - matrix2]");
		exampleListTask1
				.add("    Matrix Multiplication [matrix1 * matrix2]");
		exampleListTask1
				.add("    Get the Inverse of a Matrix [matrix.inverse()]");
		exampleListTask1
				.add("    Solve a Matrix [x = matrix.solve(y) such that matrix * x = y]");
		exampleListTask1
				.add("    Find a Derivative [derivative(targetFunction, Amount point)]");
		exampleListTask1
				.add("    Find a Derivative [derivative(targetFunction, Amount point, Amount secantLength)]");
		exampleListTask1
				.add("    Compute an Integral [integral(targetFunction, Amount lowerBound, Amount upperBound)]");

		exampleListTask1
				.add("Queries: Gets the itemsList that are the union of the results of two queries [Iterable OrQuery(Query query1, Query query2).query([queryResult]");
		exampleListTask1
				.add("    Gets the itemsList that are the intersection of the results of two queries [Iterable AndQuery(Query query1, Query query2).query([queryResult]");
		exampleListTask1
				.add("    Gets the itemsList in a context that are not returned by a query [Iterable NotQuery(Context context, Query query).query([queryResult]");

		exampleListTask1
				.add("Queries for Networks: Get all nodes within some given path length of node [Iterable NetPathWithin([Network network | Context context], Object node, double distance).query([queryResult])]");
		exampleListTask1
				.add("    Get all nodes adjacent to a give node [Iterable NetworkAdjacent([Network network | Context context], Object node).query([queryResult])]");
		exampleListTask1
				.add("    Get all nodes that are predeccesors of a given node [Iterable NetworkPredecessor([Network network | Context context], Object node).query([queryResult])]");
		exampleListTask1
				.add("    Get all nodes that are successors of a given node [Iterable NetworkSuccessor([Network network | Context context], Object node).query([queryResult])]");

		exampleListTask1
				.add("Queries for Grids: Get all the itemsList within X distance of a given item [Iterable GridWithin([Context context | Grid grid], Object item, double distance).query([queryResult])]");
		exampleListTask1
				.add("    Get all the itemsList in the Moore neighborhood of a given item [Iterable MooreQuery(Grid grid, Object item, int... extent).query([queryResult])]");
		exampleListTask1
				.add("    Get all the itemsList in the Von Neumann neighborhood of a given item [Iterable VNQuery(Grid grid, Object item, int... extent).query([queryResult])]");

		exampleListTask1
				.add("Queries for Continuous Spaces: Get all the itemsList with X distance of a given item [Iterable ContinuousWithin([Context context | ContinuousSpace space], Object item, double distance).query([queryResult])]");

		exampleListTask1
				.add("Queries for Geographies: Get the itemsList contained by a Geometry [Iterable ContainsQuery(geography, geometry).query([queryResult])]");
		exampleListTask1
				.add("    Get the itemsList intersected by a Geometry [Iterable IntersectsQuery(geography, geometry).query([queryResult])]");
		exampleListTask1
				.add("    Get the itemsList within X meters of a centroid [Iterable GeographyWithin(geography, geometry).query([queryResult])]");
		exampleListTask1
				.add("    Get the itemsList touched by Geometry [Iterable TouchesQuery(geography, geometry).query([queryResult])]");
		exampleListTask1
				.add("    Get the itemsList a Geometry is within [Iterable WithinQuery(geography, geometry).query([queryResult])]");

		exampleListTask1
				.add("Queries for Properties: Get all itemsList in a context with a property equal to some value [Iterable PropertyEquals(Context context, String propName, Object propVal).query([queryResult])]");
		exampleListTask1
				.add("    Get all itemsList in a context with a property greater than some value [Iterable PropertyEquals(Context context, String propName, Number propVal).query([queryResult])]");
		exampleListTask1
				.add("    Get all itemsList in a context with a property less than some value [Iterable PropertyEquals(Context context, String propName, Number propVal).query([queryResult])]");

		exampleListTask1
				.add("SQL Queries: Find Agents with an SQL Query [List FindAgentsInContext(String contextPath, String sqlQuery)]");
		exampleListTask1
				.add("    Find Agents with an SQL Query with a JoSQL Function Handler [List FindAgentsInContext(String contextPath, String sqlQuery, Object functionHandler)]");
		exampleListTask1
				.add("    Find Network Edges with an SQL Query [List FindAgentsInNetwork(String contextPath, String sqlQuery)]");
		exampleListTask1
				.add("    Find Network Edges with an SQL Query with a JoSQL Function Handler [List FindAgentsInNetwork(String contextPath, String sqlQuery, Object functionHandler)]");

		exampleListTask1
				.add("Networks: Create an Edge with a Default Weight of 0 [RepastEdge CreateEdge(String projection, Object source, Object target)]");
		exampleListTask1
				.add("    Create an Edge [RepastEdge CreateEdge(String projection, Object source, Object target, double optWeight)]");
		exampleListTask1
				.add("    Find an Edge between Two Agents [RepastEdge FindEdge(String projection, Object source, Object target)]");
		exampleListTask1
				.add("    Get the Weight of an Edge [double GetEdgeWeight(String projection, Object source, Object target)]");
		exampleListTask1
				.add("    Set the Weight of an Edge [RepastEdge SetEdgeWeight(String projection, Object source, Object target, double weight)]");
		exampleListTask1
				.add("    Remove an Edge [RepastEdge RemoveEdge(String projection, Object source, Object target)]");
		exampleListTask1
				.add("    Get the List of Predecessors of an Agent [List GetPredecessors(String projection, Object agent)]");
		exampleListTask1
				.add("    Get the List of Successors of an Agent [List GetSuccessors(String projection, Object agent)]");
		exampleListTask1
				.add("    Get the List of all Agents connected to an Agent [List GetAdjacent(String projection, Object agent)]");
		exampleListTask1
				.add("    Get the List of all Incoming Edges for an Agent [List<RepastEdge> GetInEdges(String projection, Object agent)]");
		exampleListTask1
				.add("    Get the List of all Outgoing Edges for an Agent [List<RepastEdge> GetOutEdges(String projection, Object agent)]");
		exampleListTask1
				.add("    Get the List of all Edges for an Agent [List<RepastEdge> GetEdges(String projection, Object agent)]");
		exampleListTask1
				.add("    Create a Network [Network CreateRandomDensityNetwork(String context, String newNetworkName, boolean directed, double density, boolean allowSelfLoops, boolean symmetric)]");
		exampleListTask1
				.add("    Create a Network [Network Create1DLatticeNetwork(String context, String newNetworkName, boolean directed, boolean toroidal, boolean symmetrical)]");
		exampleListTask1
				.add("    Create a Network [Network Create2DLatticeNetwork(String context, String newNetworkName, boolean directed, boolean toroidal)]");
		exampleListTask1
				.add("    Create a Network [Network CreateWattsBetaSmallWorldNetwork(String context, String newNetworkName, boolean directed, double beta, int nghSize, boolean symmetrical)]");
		exampleListTask1
				.add("    Create a Network from a File [Network CreateNetwork(String contextPath, String name, boolean isDirected, String agentClassName, String fileName, NetworkFileFormat format)]");

		exampleListTask1
				.add("Scheduling: Get the Current Tick Count [double GetTickCount()]");
		exampleListTask1
				.add("    Get the Current Tick Count as an Amount [time]");
		exampleListTask1
				.add("    End the simulation now [EndSimulationRun()]");
		exampleListTask1
				.add("    End the simulation at a specific tick [EndSimulationRunAt(double tick)]");
		exampleListTask1
				.add("    Pause the simulation now [PauseSimulationRun()]");
		exampleListTask1
				.add("    Pause the simulation at a specific tick [PauseSimulationRunAt(double tick)]");
		exampleListTask1
				.add("    Schedule a Non-Repeating Agent Method at Tick 1 [ISchedulableAction ScheduleAction(Object agent, String method, Object... params)]");
		exampleListTask1
				.add("    Schedule a Non-Repeating Agent Method [ISchedulableAction ScheduleAction(Object agent, double optTime, String method, Object... params)]");
		exampleListTask1
				.add("    Schedule an Agent Method [ISchedulableAction ScheduleAction(Object agent, double optStart, double optInterval, String method, Object... params)]");
		exampleListTask1
				.add("    Cancel a Scheduled Action [IAction CancelAction(ISchedulableAction actionToCancel)]");

		exampleListTask1
				.add("Contexts: Find a Context [Context FindContext(String contextPath)]");
		exampleListTask1
				.add("    Find a Parent Context [Context FindParentContext(String contextPath)]");
		exampleListTask1
				.add("    Create a Context [CreateContext(String parentContextPath, String newContextName)]");
		exampleListTask1
				.add("    Remove a Context [RemoveContext(String contextPath)]");

		exampleListTask1
				.add("Projections: Find a Projection [Projection FindProjection(String path)]");
		exampleListTask1
				.add("    Find a Grid Projection [Grid FindGrid(String path)]");
		exampleListTask1
				.add("    Find a Continuous Space Projection [ContinuousSpace FindContinuousSpace(String path)]");
		exampleListTask1
				.add("    Find a Network Projection [Network FindNetwork(String path)]");
		exampleListTask1
				.add("    Find a Geography Projection [Geography FindGeography(String path)]");
		exampleListTask1
				.add("    Create a Projection [Projection CreateProjection(String context, String name, String type)]");
		exampleListTask1
				.add("    Create a Projection [Projection CreateProjection(String context, String name, String type, boolean optKind)]");
		exampleListTask1
				.add("    Create a Projection [Projection CreateProjection(String context, String name, String type, int... optDims)]");
		exampleListTask1
				.add("    Create a Projection [Projection CreateProjection(String context, String name, String type, String optBorders, int... optDims)]");
		exampleListTask1
				.add("    Create a Projection [Projection CreateProjection(String context, String name, String type, boolean optKind, String optBorders, int... optDims)]");
		exampleListTask1
				.add("    Remove a Projection [Projection RemoveProjection(String projectionPath)]");

		exampleListTask1
				.add("ValueLayers: Find a Value Layer [ValueLayer FindValueLayer(String path)]");
		exampleListTask1
				.add("    Add a Value Layer [AddValueLayer(String contextPath, ValueLayer layer)]");
		exampleListTask1
				.add("    Create a Grid Value Layer [CreateGridValueLayer(String contextPath, String name, int... dimensions)]");
		exampleListTask1
				.add("    Create a Continuous Value Layer [CreateContinuousValueLayer(String contextPath, String name, double... dimensions)]");

		exampleListTask1
				.add("Advanced Adaptation: Make a regression forecast");

		exampleListTask1
				.add("    Store a result for future regression forecasts");

		exampleListTask1
				.add("    Make a neural network forecast [double[] outputDataArray = JooneTools.interrogate(neuralNetwork, double[] inputDataArray)]");

		exampleListTask1
				.add("    Train a neural network [JooneTools.train(neuralNetwork, double[] inputDataArray, double[] desiredOutputDataArray, epochs, convergenceThreshold, 0, null, false)]");

		exampleListTask1
				.add("    Get the Best Solution from a Genetic Algorithm [solution = ga.getBestSolution(populationCycles)]");

		exampleListTask1
				.add("    Reset the Population of a Genetic Algorithm [ga.reset()] ");

		exampleListTask1
				.add("Parameters: Get a Model Parameter [GetParameter(\"parameterName\"]");

		exampleListTask1
				.add("Data Files: Write Object Contents to a File [boolean WriteExternalFile(Object source, String xmlDescriptionFile, String targetFile)]");
		exampleListTask1
				.add("    Read Object Contents from a File [boolean ReadExternalFile(Object target, String xmlDescriptionFile, String sourceFile)]");
		exampleListTask1
				.add("    Delete a File [boolean DeleteExternalFile(String fileName)]");

		exampleListTask1
				.add("General: Check if a Uniform Random Draw Is Greater Than a Given Threshold [boolean RandomDrawAgainstThreshold(double threshold)]");
		exampleListTask1
				.add("    Execute an External Program [int ExecuteProgram(String workingDirectory, String commandLine)]");
		exampleListTask1
				.add("    Create a Variable");
		exampleListTask1
				.add("    Access a Model Parameter");

		exampleListTask1
				.add("Model Initializer: Initialize Units, Matrices, and Calculus");
		
		exampleListTask1
				.add("MATLAB: Call a MATLAB Command [String CallMATLAB(String command)]");
		exampleListTask1
				.add("    Call a MATLAB Command that Returns a Double Value [double GetMATLABValue(String command)]");
		exampleListTask1
				.add("    Reset MATLAB [void ResetMATLAB()]");
	}

	protected ArrayList<String> exampleListTask2 = new ArrayList<String>();

	{
		exampleListTask2
				.add("a = 1");
		exampleListTask2
				.add("a = watchedAgent.value");
		exampleListTask2
				.add("value = 1");
		exampleListTask2
				.add("value = watchedAgent.value");
		exampleListTask2
				.add("returnValue = 1");
		exampleListTask2
				.add("returnValue = watchedAgent.value");

		exampleListTask2
				.add("Object agent = CreateAgent(\"root\", \"anl.example.MyAgent\")");
		exampleListTask2
				.add("Context context = AddAgentToContext(\"root\", myAgent)");
		exampleListTask2
				.add("Context context = RemoveAgentFromContext(\"root\", myAgent)");
		exampleListTask2
				.add("Context context = RemoveAgentFromModel(myAgent)");
		exampleListTask2
				.add("MoveAgent(\"root/aGrid\", myAgent, 10, 30, 0)");
		exampleListTask2
				.add("IndexedIterable iter = GetObjects(\"root\", \"anl.example.MyAgent\")");
		exampleListTask2
				.add("Iterable iter = GetRandomObjects(\"root\", \"anl.example.MyAgent\", 10)");

		exampleListTask2
				.add("x = RandomDraw()");
		exampleListTask2
				.add("x = RandomDraw(5.0, 10.0)");

		exampleListTask2
				.add("RandomHelper.createBeta(alpha, beta)");
		exampleListTask2
				.add("RandomHelper.createBinomial(n, p)");
		exampleListTask2
				.add("RandomHelper.createChiSquare(freedom)");
		exampleListTask2
				.add("RandomHelper.createEmpirical(pdf, interpolationType)");
		exampleListTask2
				.add("RandomHelper.createExponential(lambda)");
		exampleListTask2
				.add("RandomHelper.createGamma(alpha,lambda)");
		exampleListTask2
				.add("RandomHelper.createLogarithmic(p)");
		exampleListTask2
				.add("RandomHelper.createNormal(mean, standardDeviation)");
		exampleListTask2
				.add("RandomHelper.createPoisson(mean)");
		exampleListTask2
				.add("RandomHelper.createStudentT(freedom)");
		exampleListTask2
				.add("RandomHelper.createVonMises(freedom)");
		exampleListTask2
				.add("RandomHelper.createZeta(ro,pk)");

		exampleListTask2
				.add("x = RandomHelper.getBeta().nextDouble()");
		exampleListTask2
				.add("x = RandomHelper.getBinomial().nextInt()");
		exampleListTask2
				.add("x = RandomHelper.getChiSquare().nextDouble()");
		exampleListTask2
				.add("x = RandomHelper.getEmpirical().nextDouble()");
		exampleListTask2
				.add("x = RandomHelper.getExponential().nextDouble()");
		exampleListTask2
				.add("x = RandomHelper.getGamma().nextDouble()");
		exampleListTask2
				.add("x = RandomHelper.getLogarithmic().nextDouble()");
		exampleListTask2
				.add("x = RandomHelper.getNormal().nextDouble()");
		exampleListTask2
				.add("x = RandomHelper.getPoisson().nextInt()");
		exampleListTask2
				.add("x = RandomHelper.getStudentT().nextDouble()");
		exampleListTask2
				.add("x = RandomHelper.getVonMises().nextDouble()");
		exampleListTask2
				.add("x = RandomHelper.getZeta().nextInt()");

		exampleListTask2
				.add("Beta beta = RandomHelper.createBeta(alpha, beta)");
		exampleListTask2
				.add("x = beta.nextDouble()");

		exampleListTask2
				.add("Matrix matrix = [1.meters, 2.meters, 3.meters].matrix");
		exampleListTask2
				.add("Matrix matrix = [[1.meters, 2.meters, 3.meters], [4.meters, 5.meters, 6.meters]].matrix");
		exampleListTask2
				.add("Matrix matrix = [[1.pure, 2.pure, 3.pure], [4.pure, 5.pure, 6.pure]].matrix");
		exampleListTask2
				.add("Matrix matrix2 = matrix1 + matrix2");
		exampleListTask2
				.add("Matrix matrix2 = matrix1 - matrix2");
		exampleListTask2
				.add("Matrix matrix2 = matrix1 * matrix2");
		exampleListTask2
				.add("Matrix matrix2 = matrix1.inverse()");
		exampleListTask2
				.add("Matrix matrix3 = matrix1.solve(matrix2)");
		exampleListTask2
				.add("def result = derivative({ 2.meters / 1.0.seconds * it - 3.meters }, 4.1.seconds)");
		exampleListTask2
				.add("def result = derivative({ 2.meters / 1.0.seconds * it - 3.meters }, 4.1.seconds,  0.0001.seconds)");
		exampleListTask2
				.add("def result = integral({(1.pure - it ** 2).sqrt()}, 0.pure, 1.pure)");

		exampleListTask2
				.add("Iterator list = new OrQuery(new PropertyEquals(context, \"name\", \"David Healy\"), new GridWithin(grid, agent, 3)).query(optNestedResult).iterator()");
		exampleListTask2
				.add("Iterator list = new AndQuery(query1, query2).query(optNestedResult).iterator()");
		exampleListTask2
				.add("Iterator list = new NotQuery(context, new PropertyLessThan(context, \"wealth\", 3.14)).query(optNestedResult).iterator()");

		exampleListTask2
				.add("Iterator list = new NetPathWithin(network, node, 3).query(optNestedQueryResult).iterator()");
		exampleListTask2
				.add("Iterator list = new NetworkAdjacent(network, node).query(optNestedQueryResult).iterator()");
		exampleListTask2
				.add("Iterator list = new NetworkPredecessor(network, node).query(optNestedQueryResult).iterator()");
		exampleListTask2
				.add("Iterator list = new NetworkSuccessor(network, node).query(optNestedQueryResult).iterator()");

		exampleListTask2
				.add("Iterator list = new GridWithin(grid, agent, 3).query(optNestedQueryResult).iterator()");
		exampleListTask2
				.add("Iterator list = new MooreQuery(grid, agent, 3, 3).query(optNestedQueryResult).iterator()");
		exampleListTask2
				.add("Iterator list = new VNQuery(grid, agent, 2, 1, 1).query(optNestedQueryResult).iterator()");

		exampleListTask2
				.add("Iterator list = new ContinuousWithin(continuousSpace, agent, 11.5).query(optNestedQueryResult).iterator()");

		exampleListTask2
				.add("Iterator list = new ContainsQuery(geography, geometry).query(optNestedQueryResult).iterator()");
		exampleListTask2
				.add("Iterator list = new ItersectsQuery(geography, geometry).query(optNestedQueryResult).iterator()");
		exampleListTask2
				.add("Iterator list = new GeographyWithin(geography, geometry).query(optNestedQueryResult).iterator()");
		exampleListTask2
				.add("Iterator list = new TouchesQuery(geography, geometry).query(optNestedQueryResult).iterator()");
		exampleListTask2
				.add("Iterator list = new WithinQuery(geography, geometry).query(optNestedQueryResult.iterator())");

		exampleListTask2
				.add("Iterator list = new PropertyEquals(context, \"name\", \"David Healy\").query(optNestedQueryResult).iterator()");
		exampleListTask2
				.add("Iterator list = new PropertyGreaterThan(context, \"wealth\", 3.14).query(optNestedQueryResult).iterator()");
		exampleListTask2
				.add("Iterator list = new PropertyLessThan(context, \"wealth\", 3.14).query(optNestedQueryResult).iterator()");

		exampleListTask2
				.add("List resultList = FindAgentsInContext(\"main\", \"SELECT * FROM java.lang.Object\")");
		exampleListTask2
				.add("List resultList = FindAgentsInContext(\"main\", \"SELECT * FROM java.lang.Object\", new FunctionHandler())");
		exampleListTask2
				.add("List resultList = FindAgentsInNetwork(\"main/friends\", \"SELECT toString Name FROM java.lang.Object\")");
		exampleListTask2
				.add("List resultList = FindAgentsInNetwork(\"main/friends\", \"SELECT toString Name FROM java.lang.Object\", new FunctionHandler())");

		exampleListTask2
				.add("RepastEdge newEdge = CreateEdge(\"root/familyNet\", fatherAgent, sonAgent)");
		exampleListTask2
				.add("RepastEdge newEdge = CreateEdge(\"root/familyNet\", fatherAgent, sonAgent, 2.0)");
		exampleListTask2
				.add("RepastEdge edge = FindEdge(\"root/familyNet\", fatherAgent, sonAgent)");
		exampleListTask2
				.add("double edgeWeight = GetEdgeWeight(\"root/familyNet\", fatherAgent, sonAgent)");
		exampleListTask2
				.add("RepastEdge edge = SetEdgeWeight(\"root/familyNet\", fatherAgent, sonAgent, 2.5)");
		exampleListTask2
				.add("RepastEdge removedEdge = RemoveEdge(\"root/familyNet\", fatherAgent, sonAgent)");
		exampleListTask2
				.add("List predecessors = GetPredecessors(\"root/familyNet\", fatherAgent)");
		exampleListTask2
				.add("List successors = GetSuccessors(\"root/familyNet\", fatherAgent)");
		exampleListTask2
				.add("List adjacent = GetAdjacent(\"root/familyNet\", fatherAgent)");
		exampleListTask2
				.add("List inEdges = GetInEdges(\"root/familyNet\", fatherAgent)");
		exampleListTask2
				.add("List outEdges = GetOutEdges(\"root/familyNet\", fatherAgent)");
		exampleListTask2
				.add("List edges = GetEdges(\"root/familyNet\", fatherAgent)");
		exampleListTask2
				.add("Network newNetwork = CreateRandomDensityNetwork(\"mainContext\", \"MyNetwork\", true, 0.25, false, false)");
		exampleListTask2
				.add("Network newNetwork = Create1DLatticeNetwork(\"mainContext\", \"MyNetwork\", true, false, false)");
		exampleListTask2
				.add("Network newNetwork = Create2DLatticeNetwork(\"mainContext\", \"MyNetwork\", true, false)");
		exampleListTask2
				.add("Network newNetwork = CreateWattsBetaSmallWorldNetwork(\"mainContext\", \"MyNetwork\", true, 0.1, 4, true)");
		exampleListTask2
				.add("Network newNetwork = CreateNetwork(\"mainContext\", \"MyNetwork\", true, \"boids.BoidAgent\", \"files\\boidNet.dl\", NetworkFileFormat.DL)");

		exampleListTask2
				.add("time");
		exampleListTask2
				.add("time");
		exampleListTask2
				.add("EndSimulationRun()");
		exampleListTask2
				.add("EndSimulationRunAt(endTime)");
		exampleListTask2
				.add("PauseSimulationRun()");
		exampleListTask2
				.add("PauseSimulationRunAt(pauseTime)");
		exampleListTask2
				.add("ScheduleAction(myAgent,\"calculate\",  12)");
		exampleListTask2
				.add("ScheduleAction(myAgent, 4, \"calculate\",  12)");
		exampleListTask2
				.add("ScheduleAction(myAgent, 4, 1, \"calculate\",  12)");
		exampleListTask2
				.add("CancelAction(actionToCancel)");

		exampleListTask2
				.add("Context context = FindContext(\"root/sub1\")");
		exampleListTask2
				.add("Context context = FindParentContext(\"root/sub1\")");
		exampleListTask2
				.add("Context subContext = CreateContext(\"root/sub2\")");
		exampleListTask2
				.add("Context removedContext = RemoveContext(\"root/sub2\")");

		exampleListTask2
				.add("Projection proj = FindProjection(\"root/projName\")");
		exampleListTask2
				.add("Grid grid = FindGrid(\"root/gridName\")");
		exampleListTask2
				.add("ContinuousSpace space = FindContinuousSpace(\"root/spaceName\")");
		exampleListTask2
				.add("Network net = FindNetwork(\"root/netName\")");
		exampleListTask2
				.add("Geography geog = FindGeography(\"root/geogName\")");
		exampleListTask2
				.add("Projection proj = CreateProjection(\"root\", \"field\", [\"Grid\"|\"Graph\"|\"Continuous\"])");
		exampleListTask2
				.add("Projection proj = CreateProjection(\"root\", \"field\", [\"Grid\"|\"Graph\"|\"Continuous\"], [true | false])");
		exampleListTask2
				.add("Projection proj = CreateProjection(\"root\", \"field\", [\"Grid\"|\"Graph\"|\"Continuous\"], 10, 20, 10)");
		exampleListTask2
				.add("Projection proj = CreateProjection(\"root\", \"field\", [\"Grid\"|\"Graph\"|\"Continuous\"], [\"StickyBorders\"|\"WrapAroundBorders\"|\"InfiniteBorders\"|\"StrictBorders\"|\"BouncyBorders\"], 10, 10)");
		exampleListTask2
				.add("Projection proj = CreateProjection(\"root\", \"field\", [\"Grid\"|\"Graph\"|\"Continuous\"], [true | false], [\"StickyBorders\"|\"WrapAroundBorders\"|\"InfiniteBorders\"|\"StrictBorders\"|\"BouncyBorders\"], 10, 10)");
		exampleListTask2
				.add("Projection removedProj = RemoveProjection(\"root/proj\")");

		exampleListTask2
				.add("ValueLayer layer = FindValueLayer(\"root/layerName\")");
		exampleListTask2
				.add("AddValueLayer(\"root\", myValueLayer)");
		exampleListTask2
				.add("GridValueLayer layer = CreateGridValueLayer(\"root\", \"FoodValueLayer\", 100, 100)");
		exampleListTask2
				.add("ContinuousValueLayer layer = CreateContinuousValueLayer(\"root\", \"FoodValueLayer\", 100.5, 75.5)");

		exampleListTask2
		    .add("x = model.forecast(y1, y2, y3)");
		exampleListTask2
		    .add("model.add(y, x1, x2, x3)");
		exampleListTask2
				.add("    double[] outputDataArray = JooneTools.interrogate(neuralNetwork, inputDataArray)");
		exampleListTask2
				.add("    JooneTools.train(neuralNetwork, inputDataArray, desiredOutputDataArray, 5000, 0.01, 0, null, false)]");
		exampleListTask2
		    .add("solution = ga.getBestSolution(100)");
		exampleListTask2
		    .add("ga.reset()");
		exampleListTask2
		    .add("GetParameter(\"agentCount\")");

		exampleListTask2
				.add("WriteExternalFile(this, \"integration\\\\output.datadescriptor\", \"integration\\\\output.txt\")");
		exampleListTask2
				.add("ReadExternalFile(this, \"integration\\\\input.datadescriptor\", \"integration\\\\input.txt\")");
		exampleListTask2
		    .add("DeleteExternalFile(\"integration\\temp.txt\")");

		exampleListTask2
		    .add("RandomDrawAgainstThreshold([0,1])");
		exampleListTask2
				.add("ExecuteProgram(\"integration\", \"LegacyProgram.exe\")");
		exampleListTask2
		    .add("[def | Type] varName");
		exampleListTask2
		    .add("GetParameter(\"ExampleParameter\")");

		exampleListTask2
		    .add("RepastMathEMC.initAll()");
		
		exampleListTask2
				.add("String returnValue = CallMATLAB(\"\")");
		exampleListTask2
				.add("double returnValue = GetMATLABValue(\"\")");
		exampleListTask2
				.add("ResetMATLAB()");
	}

	protected static Map<String, ArrayList> exampleListsMap = new HashMap<String, ArrayList>();
	{
		AgentEditor.putExampleList("java.lang.Object exampleListIfWizard",
				exampleListIfWizard);
		AgentEditor.putExampleList("java.lang.Object exampleListTaskWizard",
				exampleListTaskWizard);
		AgentEditor.putExampleList("java.lang.Object exampleListLoopWizard",
				exampleListLoopWizard);
		AgentEditor.putExampleList("java.lang.Object exampleListIf",
				exampleListIf);
		AgentEditor.putExampleList("java.lang.Object exampleListLoop",
				exampleListLoop);
		AgentEditor.putExampleList("java.lang.Object exampleListTask1",
				exampleListTask1);
		AgentEditor.putExampleList("java.lang.Object exampleListTask2",
				exampleListTask2);
	}

	/**
	 * Creates a new flow editor and sets it's edit domain.
	 */
	public AgentEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithPalette#getPaletteRoot()
	 */
	@Override
	protected PaletteRoot getPaletteRoot() {
		if (root == null)
			root = PaletteHelper.createPalette();

		return root;
	}

	public void activateDefaultTool() {
		getEditDomain();
		getPaletteRoot().getDefaultEntry().createTool();
		// editDomain.setActiveTool(defaultTool);
		getPaletteViewer().setActiveTool(getPaletteRoot().getDefaultEntry());
	}

	/**
	 * Get the desktop's StatusLineManager
	 */
	public IStatusLineManager getStatusLineManager() {
		IEditorActionBarContributor contributor = getEditorSite()
				.getActionBarContributor();
		if (contributor instanceof EditorActionBarContributor) {
			return ((EditorActionBarContributor) contributor).getActionBars()
					.getStatusLineManager();
		}
		return null;
	}

	/**
	 * Returns the KeyHandler with common bindings for both the Outline and
	 * Graphical Views. For example, delete is a common action.
	 */
	protected KeyHandler getCommonKeyHandler() {
		if (sharedKeyHandler == null) {
			sharedKeyHandler = new KeyHandler();
			sharedKeyHandler
					.put(KeyStroke.getPressed(SWT.DEL, 127, 0),
							getActionRegistry().getAction(
									ActionFactory.DELETE.getId()));
			sharedKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0),
					getActionRegistry().getAction(
							GEFActionConstants.DIRECT_EDIT));
		}
		return sharedKeyHandler;
	}

	/**
	 * Returns the editor's flow diagram instance
	 * 
	 * @return the editor's flow diagram instance
	 */
	public AgentDiagramModelPart getFlowDiagram() {
		return flowDiagram;
	}

	/**
	 * 
	 * @param diagram
	 */
	public void setFlowDiagram(AgentDiagramModelPart diagram) {
		flowDiagram = diagram;
	}

	/**
	 * Returns the File which corresponds with this Editor
	 * 
	 * @return the File which corresponds with this Editor
	 */
	public IFile getFlowFile() {
		return ((FileEditorInput) getEditorInput()).getFile();
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	@Override
	protected void configureGraphicalViewer() {
		// super.configureGraphicalViewer();

		ScrollingGraphicalViewer viewer = (ScrollingGraphicalViewer) getGraphicalViewer();

		ScalableFreeformRootEditPart root = new ScalableFreeformRootEditPart();

		IAction zoomIn = new ZoomInAction(root.getZoomManager());
		IAction zoomOut = new ZoomOutAction(root.getZoomManager());
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);
		List zoomLevels = new ArrayList(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		root.getZoomManager().setZoomLevelContributions(zoomLevels);

		IHandlerService handlerService = (IHandlerService) PlatformUI
				.getWorkbench().getService(IHandlerService.class);
		handlerService.activateHandler(zoomIn.getActionDefinitionId(),
				new ActionHandler(zoomIn));

		handlerService = (IHandlerService) PlatformUI.getWorkbench()
				.getService(IHandlerService.class);
		handlerService.activateHandler(zoomOut.getActionDefinitionId(),
				new ActionHandler(zoomOut));

		IAction snapAction = new ToggleSnapToGeometryAction(viewer) {

			@Override
			public void run() {
				super.run();
				if (isChecked()) {
					currentGridResolution = AgentEditor.DEFAULT_GRID_RESOLUTION;
				} else {
					currentGridResolution = 1;
				}
			}

		};
		snapAction.setChecked(true);
		getActionRegistry().registerAction(snapAction);

		viewer.setRootEditPart(root);

		viewer
				.setEditPartFactory(new AgentBuilderEditPartFactory(
						getFlowFile()));
		ContextMenuProvider provider = new AgentBuilderContextMenuProvider(
				viewer, getActionRegistry());
		viewer.setContextMenu(provider);
		getSite().registerContextMenu(
				"repast.simphony.agents.designer.ui.editors.contextmenu",
				provider, viewer);
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer)
				.setParent(getCommonKeyHandler()));

	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	@Override
	protected void initializeGraphicalViewer() {
		getGraphicalViewer().setContents(getFlowDiagram());
	}

	/**
	 * Returns the KeyHandler with common bindings for both the Outline and
	 * Graphical Views. For example, delete is a common action.
	 */
	/*
	 * protected KeyHandler getCommonKeyHandler(){ if (sharedKeyHandler ==
	 * null){ sharedKeyHandler = new KeyHandler(); sharedKeyHandler.put(
	 * KeyStroke.getPressed(SWT.DEL, 127, 0),
	 * getActionRegistry().getAction(GEFActionConstants.DELETE));
	 * sharedKeyHandler.put( KeyStroke.getPressed(SWT.F2, 0),
	 * getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT)); } return
	 * sharedKeyHandler; }
	 */

	@Override
	public Object getAdapter(Class type) {
		/*
		 * if (type == IContentOutlinePage.class) return new OutlinePage(new
		 * TreeViewer());
		 */
		if (type == ZoomManager.class)
			return ((ScalableFreeformRootEditPart) getGraphicalViewer()
					.getRootEditPart()).getZoomManager();

		return super.getAdapter(type);
	}

	/**
	 * Returns the graphical viewer.
	 * 
	 * @return the graphical viewer
	 */
	public GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}

	/**
	 * @see org.eclipse.gef.commands.CommandStackListener#commandStackChanged(java.util.EventObject)
	 */
	@Override
	public void commandStackChanged(EventObject event) {
		if (isDirty()) {
			if (!savePreviouslyNeeded()) {
				setSavePreviouslyNeeded(true);
				firePropertyChange(IEditorPart.PROP_DIRTY);
			}
		} else {
			setSavePreviouslyNeeded(false);
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
		super.commandStackChanged(event);
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	private boolean savePreviouslyNeeded() {
		return savePreviouslyNeeded;
	}

	/**
	 * TODO
	 * 
	 * @param value
	 */
	private void setSavePreviouslyNeeded(boolean value) {
		savePreviouslyNeeded = value;
	}

	public void exportAsImage(IFile file) {

		try {

			IFile outputFile = file.getProject().getFile(
					file.getFullPath().removeFirstSegments(1)
							.removeFileExtension().addFileExtension(
									Consts.FILE_EXTENSION_HTML));

			IPath outputFilePath = outputFile.getFullPath();

			while ((outputFilePath.segmentCount() > 0)
					&& (!outputFilePath.segment(0).equals("src"))) {
				outputFilePath = outputFilePath.removeFirstSegments(1);
			}

			String baseFileName = file.getName();
			if (baseFileName.endsWith(".agent"))
				baseFileName = baseFileName.substring(0,
						baseFileName.length() - 6);
			String fullFileName = outputFilePath.removeFirstSegments(1)
					.removeLastSegments(1).removeFileExtension()
					.toPortableString().replace("/", ".")
					+ "." + baseFileName + ".png";
			IFolder docsFolder = file.getProject().getFolder("docs");
			IFolder imagesFolder = docsFolder.getFolder("images");
			if (!imagesFolder.exists()) {
				try {
					imagesFolder.create(true, true, new NullProgressMonitor());
				} catch (Exception e) {
				}
			}
			IFile imageFile = imagesFolder.getFile(fullFileName);
			file.getProject().refreshLocal(IResource.DEPTH_INFINITE,
					new NullProgressMonitor());

			Util.exportAsImage(this, imageFile.getLocation().toString(),
					SWT.IMAGE_PNG);
			file.getProject().refreshLocal(IResource.DEPTH_INFINITE,
					new NullProgressMonitor());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * saves the marshalled model. Afterwords the Fileresource is written. This
	 * is necessary to inform the workspace that the filecontent has changed.
	 * 
	 * @see org.eclipse.ui.ISaveablePart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		try {
			this.exportAsImage(file);
			AgentModelBind agentModelBind = new DesignerModelDigester(
					getFlowDiagram()).createFlowModelBind();
			ByteArrayInputStream bin = BindingHandler.getInstance()
					.saveFlowModel(agentModelBind);
			file.setContents(bin, true, true, monitor);
			bin.close();
			getCommandStack().markSaveLocation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IEditorPart#gotoMarker(org.eclipse.core.resources.IMarker)
	 */
	public void gotoMarker(IMarker marker) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.eclipse.ui.ISaveablePart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return isSaveOnCloseNeeded();
	}

	/**
	 * @see org.eclipse.ui.ISaveablePart#isSaveOnCloseNeeded()
	 */
	@Override
	public boolean isSaveOnCloseNeeded() {
		return getCommandStack().isDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#createActions()
	 */
	@Override
	protected void createActions() {
		super.createActions();
		ActionRegistry registry = getActionRegistry();
		IAction action;

		action = new ShowPropertiesAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new SelectionCountingAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		this.selectionCountingAction = (SelectionCountingAction) action;

		action = new CopyAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		this.copyAction = (CopyAction) action;

		action = new PasteAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new SaveToImageAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

	}

	/**
	 * TODO
	 * 
	 * @param input
	 */
	private void superSetInput(IEditorInput input) {
		// The workspace never changes for an editor. So, removing and re-adding
		// the
		// resourceListener is not necessary. But it is being done here for the
		// sake
		// of proper implementation. Plus, the resourceListener needs to be
		// added
		// to the workspace the first time around.
		/*
		 * if (getEditorInput() != null) { IFile file =
		 * ((FileEditorInput)getEditorInput()).getFile();
		 * file.getWorkspace().removeResourceChangeListener(resourceListener); }
		 */

		super.setInput(input);

		if (getEditorInput() != null) {
			IFile file = ((FileEditorInput) getEditorInput()).getFile();
			// file.getWorkspace().addResourceChangeListener(resourceListener);
			setPartName(file.getName());
			setContentDescription(file.getName());
		}
	}

	/**
	 * @see org.eclipse.ui.part.EditorPart#setInput(org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void setInput(IEditorInput input) {
		superSetInput(input);

		IFile file = ((IFileEditorInput) input).getFile();
		try {
			repast.simphony.agents.model.bind.AgentModelBind agentModelBind = BindingHandler
					.getInstance().loadFlowModel(file.getLocation().toFile());

			AgentDiagramModelPart flowDiagramModelPart = new AgentDiagramModelPart();
			// DesignerModelDigester digester = new
			// DesignerModelDigester(flowDiagramModelPart);
			DesignerModelDigester digester = new DesignerModelDigester(
					flowDiagramModelPart);
			digester.digest(agentModelBind);
			setFlowDiagram(flowDiagramModelPart);
			digester.getCreateCommand().execute();
		} catch (AgentBuilderRuntimeException e) {
			AgentBuilderPlugin.log(e);
			AgentBuilderPlugin.message("Problems while reading agent file: "
					+ e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			AgentBuilderPlugin.log(e);
			AgentBuilderPlugin.message("Problems while opening agent file: "
					+ e.getMessage());
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {

		SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		if (file != null) {
			saveAsDialog.setOriginalFile(file);
		} else {
			saveAsDialog.setOriginalName("Untitled");
		}
		saveAsDialog.create();
		saveAsDialog.setMessage("Please Provide a New Name...");
		saveAsDialog.open();
		IPath newPath = saveAsDialog.getResult();

		try {

			if (newPath != null) {

				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

				IFile iFile = root.getFile(newPath);
				this.exportAsImage(iFile);

				AgentModelBind agentModelBind = new DesignerModelDigester(
						getFlowDiagram()).createFlowModelBind();
				ByteArrayInputStream bin = BindingHandler.getInstance()
						.saveFlowModel(agentModelBind);
				iFile.create(bin, true, new NullProgressMonitor());
				bin.close();
				getCommandStack().markSaveLocation();
				this.setInput(new FileEditorInput(iFile));

			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public static Map<String, ArrayList> getExampleListsMap() {
		return exampleListsMap;
	}

	public static void setExampleListsMap(Map<String, ArrayList> exampleListsMap) {
		AgentEditor.exampleListsMap = exampleListsMap;
	}

	public static ArrayList getExampleList(String parentClassNamePlusBlockType) {
		return exampleListsMap.get(parentClassNamePlusBlockType);
	}

	public static void putExampleList(String parentClassNamePlusBlockType,
			ArrayList list) {
		exampleListsMap.put(parentClassNamePlusBlockType, list);
	}

	public boolean isSelectedItems() {
		if (this.selectionCountingAction == null) {
			return false;
		} else {
			return this.selectionCountingAction.getCount() > 0;
		}
	}

	public int selectedItems() {
		if (this.selectionCountingAction == null) {
			return 0;
		} else {
			return this.selectionCountingAction.getCount();
		}
	}

	public List getSelectedObjects() {
		if (this.selectionCountingAction == null) {
			return null;
		} else {
			return this.selectionCountingAction.getCurrentlySelectedObjects();
		}
	}

	public boolean isCopiedObjects() {
		if ((this.copyAction == null)
				|| (this.copyAction.getSelectedObjects() == null)) {
			return false;
		} else {
			return (this.copyAction.getSelectedObjects().size() > 0);
		}
	}

	public List getCopiedObjects() {
		if (this.copyAction == null) {
			return null;
		} else {
			return this.copyAction.getSelectedObjects();
		}
	}

	public boolean labelsCanMove() {
		if (this.selectionCountingAction == null) {
			return false;
		} else {
			return this.selectionCountingAction.labelsCanMove();
		}
	}

	public static int snap(int location) {
		AgentEditor agentEditor = AgentBuilderPlugin.getActiveFlowEditor();
		if (agentEditor == null) {
			return snap(location, 1);
		} else {
			return snap(location, agentEditor.currentGridResolution);
		}
	}

	public static int snap(int location, int resolution) {
		if (resolution <= 1) {
			return location;
		} else {
			return (resolution * (int) (location / resolution));
		}
	}

	public static org.eclipse.draw2d.geometry.Point snapPoint(Point location,
			Point size) {
		return new org.eclipse.draw2d.geometry.Point(snap(location.x) + size.x,
				snap(location.y) + size.y);
	}

	public static org.eclipse.draw2d.geometry.Point snapPoint(Point point) {
		return new org.eclipse.draw2d.geometry.Point(snap(point.x),
				snap(point.y));
	}

	public static org.eclipse.draw2d.geometry.Point snapPoint(Point point,
			int resolution) {
		return new org.eclipse.draw2d.geometry.Point(snap(point.x, resolution),
				snap(point.y, resolution));
	}

}
