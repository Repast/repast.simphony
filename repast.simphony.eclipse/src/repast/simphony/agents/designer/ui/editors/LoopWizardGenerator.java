package repast.simphony.agents.designer.ui.editors;

import java.util.ArrayList;

import repast.simphony.agents.designer.ui.wizards.NewCodeWizardEntry;
import repast.simphony.agents.designer.ui.wizards.NewCodeWizardFormEntry;
import repast.simphony.agents.designer.ui.wizards.NewCodeWizardRadioButtonEntry;

/**
 * Generate the contents of the Loop block wizard for the agent editor.
 * 
 * @author Michael J. North
 *
 */
public class LoopWizardGenerator {

	public static ArrayList<NewCodeWizardEntry> createLoopWizard(){
		ArrayList<NewCodeWizardEntry> exampleListLoopWizard = 
			new ArrayList<NewCodeWizardEntry>();

		exampleListLoopWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Loop Step",
				"Please Select the Loop Logic to Create",
				new String[] {
						"Scan a List",
						"Scan a Given Range of Values",
						"Scan While Comparing Values",
						"Scan a Projection (e.g., a Grid, Network or Continuous Space)",
						"Scan a Context",
						"Make a Random Draw and Scan While It Matches a Given Threshold or Thresholds",
				"Enter User Code" }, null));

		exampleListLoopWizard.add(new NewCodeWizardFormEntry("Scan a List",
				"Please Fill In the Scanning Options", new String[] {
				"Scanning Variable", "Variable Holding the List" },
				new String[] { "$1", "$2" }, new String[] { "i", "AgentList" },
		"$1 in $2"));

		exampleListLoopWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Scan a Given Range of Values",
				"Please Select the Scanning Logic to Create",
				new String[] {
						"Scan from a Numeric Lower Bound to a Numeric Upper Bound",
				"Scan a Given List of Values" }, null));

		exampleListLoopWizard.add(new NewCodeWizardRadioButtonEntry(
				"Scan from a Numeric Lower Bound to a Numeric Upper Bound",
				"Please Select the Scanning Logic to Create", new String[] {
						"Scan in Steps of One Each",
				"Scan in User Defined Steps" }, null));

		exampleListLoopWizard.add(new NewCodeWizardFormEntry(
				"Scan in Steps of One Each",
				"Please Fill In the Scanning Options", new String[] {
						"Scanning Variable", "Lower Bound", "Upper Bound" },
						new String[] { "$1", "$2", "$3" },
						new String[] { "i", "", "" }, "$1 in $2..$3"));

		exampleListLoopWizard.add(new NewCodeWizardFormEntry(
				"Scan in User Defined Steps",
				"Please Fill In the Scanning Options", new String[] {
						"Scanning Variable",
						"Integer or Floating Point Lower Bound",
						"Integer or Floating Point Upper Bound",
				"Integer or Floating Point Step Size" }, new String[] {
						"$1", "$2", "$3", "$4" }, new String[] { "i", "", "",
				"1" }, "def $1 = ($2); $1 < ($3); $1 = $1 + ($4)"));

		exampleListLoopWizard.add(new NewCodeWizardFormEntry(
				"Scan a Given List of Values",
				"Please Fill In the Scanning Options",
				new String[] { "Scanning Variable",
				"Comma-Separated List of Values" }, new String[] {
						"$1", "$2" }, new String[] { "i", "" }, "$1 in $3"));

		exampleListLoopWizard.add(new NewCodeWizardRadioButtonEntry(
				"Scan While Comparing Values",
				"Please Select the Comparision Logic to Create", new String[] {
						"Compare Two Values", "Combine Two Comparisions" },
						null));

		exampleListLoopWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Compare Two Values",
				"Please Select the Loop Logic to Create",
				new String[] {
						"Use Two Numeric Thresholds or Formulas",
						"Use an Attribute of the Current Agent and a Numeric Threshold or Formula",
						"Use Two Attributes of the Current Agent",
						"Use an Attribute of a Watched Agent and a Numeric Threshold or Formula",
				"Use Two Attributes of a Watched Agent" }, null));

		exampleListLoopWizard.add(new NewCodeWizardFormEntry(
				"Use Two Numeric Thresholds or Formulas",
				"Please Fill In the Comparison Options", new String[] {
						"First Number or Formula to Compare",
						"Comparision Operation (>, >=, ==, !=, <=, <)",
				"Second Number or Formula to Compare" }, new String[] {
						"$1", "$2", "$3" }, new String[] { "", "==", "" },
		"($1 $2 $3)"));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Use an Attribute of the Current Agent and a Numeric Threshold or Formula",
				"Please Fill In the Comparison Options", new String[] {
						"Attribute to Compare",
						"Comparision Operation (>, >=, ==, !=, <=, <)",
				"Numeric Threshold, or Formula to Compare" },
				new String[] { "$1", "$2", "$3" }, new String[] { "",
						"==", "" }, "($1 $2 $3)"));

		exampleListLoopWizard.add(new NewCodeWizardFormEntry(
				"Use Two Attributes of the Current Agent",
				"Please Fill In the Comparison Options", new String[] {
						"First Attribute to Compare",
						"Comparision Operation (>, >=, ==, !=, <=, <)",
				"Second Attribute to Compare" }, new String[] { "$1",
						"$2", "$3" }, new String[] { "", "==", "" },
		"($1 $2 $3)"));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Use an Attribute of a Watched Agent and a Numeric Threshold or Formula",
				"Please Fill In the Comparison Options", new String[] {
						"Attribute to Compare",
						"Comparision Operation (>, >=, ==, !=, <=, <)",
				"Numeric Threshold, or Formula to Compare" },
				new String[] { "$1", "$2", "$3" }, new String[] { "",
						"==", "" }, "(watchedAgent.$1 $2 $3)"));

		exampleListLoopWizard.add(new NewCodeWizardFormEntry(
				"Use Two Attributes of a Watched Agent",
				"Please Fill In the Comparison Options", new String[] {
						"First Attribute to Compare",
						"Comparision Operation (>, >=, ==, !=, <=, <)",
				"Second Attribute to Compare" }, new String[] { "$1",
						"$2", "$3" }, new String[] { "", "==", "" },
		"(watchedAgent.$1 $2 watchedAgent.$3)"));

		exampleListLoopWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Combine Two Comparisions",
				"Please Select the Loop Logic to Create",
				new String[] {
						"Combine Two Comparisions Such that Both Must Be True (i.e., \"and\")",
				"Combine Two Comparisions Such that One Must Be True (i.e., \"or\")" },
				null));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Combine Two Comparisions Such that Both Must Be True (i.e., \"and\")",
				"Please Fill In the Comparison Options", new String[] {
						"First Value to Compare",
						"Comparision Operation (>, >=, ==, <=, <)",
						"Second Value to Compare",
						"Third Value to Compare",
						"Comparision Operation (>, >=, ==, !=, <=, <)",
				"Fourth Value to Compare" }, new String[] {
						"$1", "$2", "$3", "$4", "$5", "$6" },
						new String[] { "", "==", "", "", "==", "" },
		"(($1 $2 $3) ($4 $5 $6))"));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Combine Two Comparisions Such that One Must Be True (i.e., \"or\")",
				"Please Fill In the Comparison Options", new String[] {
						"First Value to Compare",
						"Comparision Operation (>, >=, ==, !=, <=, <)",
						"Second Value to Compare",
						"Third Value to Compare",
						"Comparision Operation (>, >=, ==, !=, <=, <)",
				"Fourth Value to Compare" }, new String[] {
						"$1", "$2", "$3", "$4", "$5", "$6" },
						new String[] { "", "==", "", "", "==", "" },
		"(($1 $2 $3) ($4 $5 $6))"));

		exampleListLoopWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Scan a Projection (e.g., a Grid, Network or Continuous Space)",
				"Please Select the Projection Scaning Logic to Create",
				new String[] { "Scan a Network", "Scan a Grid",
				"Scan a Continuous Space" }, null));

		exampleListLoopWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Scan a Network",
				"Please Select the Network Scaning Logic to Create",
				new String[] {
						"Scan Across All Agents Within a Given Number of Links of the Current Agent",
						"Scan Across All Agents Within a Given Number of Links of a Watched Agent", },
						null));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Scan Across All Agents Within a Given Number of Links of the Current Agent",
				"Scan Across All Agents Within a Given Number of Links of the Current Agent While Respecting Link Directions, If Any, and Noting that \"Within\" Includes the Upper Limit",
				new String[] { "Scanning Variable", "Network Variable",
						"Distance", },
						new String[] { "$1", "$2", "$3" },
						new String[] { "neighbor", "network", "" },
		"$1 in (new NetPathWithin(($2), this, ($3)).query(optNestedQueryResult).iterator().hasNext())"));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Scan Across All Agents Within a Given Number of Links of a Watched Agent",
				"Scan Across All Agents Within a Given Number of Links of a Watched Agent While Respecting Link Directions, If Any, and Noting that \"Within\" Includes the Upper Limit",
				new String[] { "Scanning Variable", "Network Variable",
						"Distance", },
						new String[] { "$1", "$2", "$3" },
						new String[] { "neighbor", "network", "" },
		"$1 in (new NetPathWithin(($2), watchedAgent, ($3)).query(optNestedQueryResult).iterator().hasNext())"));

		exampleListLoopWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Scan a Grid",
				"Please Select the Grid Scaning Logic to Create",
				new String[] {
						"Scan Across All Agents Within a Moore Neighborhood of the Current Agent",
						"Scan Across All Agents Within a Moore Neighborhood of a Watched Agent",
						"Scan Across All Agents Within a Von Neumann Neighborhood of the Current Agent",
						"Scan Across All Agents Within a Von Neumann Neighborhood of a Watched Agent", },
						null));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Scan Across All Agents Within a Moore Neighborhood of the Current Agent",
				"This Code Will Scan the Given Grid",
				new String[] {
						"Scanning Variable",
						"Grid Variable",
						"Comma Separated List of Distances (e.g., \"1, 3, 7\")", },
						new String[] { "$1", "$2", "$3" }, new String[] {
						"neighbor", "grid", "" },
		"$1 in (new MooreQuery(($2), this, $3).query().iterator().hasNext())"));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Scan Across All Agents Within a Moore Neighborhood of a Watched Agent",
				"This Code Will Scan the Given Grid",
				new String[] {
						"Scanning Variable",
						"Grid Variable",
						"Comma Separated List of Distances (e.g., \"1, 3, 7\")", },
						new String[] { "$1", "$2", "$3" }, new String[] {
						"neighbor", "grid", "" },
		"$1 in (new MooreQuery(($2), watchedAgent, $3).query().iterator().hasNext())"));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Scan Across All Agents Within a Von Neumann Neighborhood of the Current Agent",
				"This Code Will Scan the Given Grid",
				new String[] {
						"Scanning Variable",
						"Grid Variable",
						"Comma Separated List of Distances (e.g., \"1, 3, 7\")", },
						new String[] { "$1", "$2", "$3" }, new String[] {
						"neighbor", "grid", "" },
		"$1 in (new VNQuery(($2), this, $3).query().iterator().hasNext())"));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Scan Across All Agents Within a Von Neumann Neighborhood of a Watched Agent",
				"This Code Will Scan the Given Grid",
				new String[] {
						"Scanning Variable",
						"Grid Variable",
						"Comma Separated List of Distances (e.g., \"1, 3, 7\")", },
						new String[] { "$1", "$2", "$3" }, new String[] {
						"neighbor", "grid", "" },
		"$1 in (new VNQuery(($2), watchedAgent, $3).query().iterator().hasNext())"));

		exampleListLoopWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Scan a Continuous Space",
				"Please Select the Continuous Space Scaning Logic to Create",
				new String[] {
						"Scan Across All Agents Within a Circular Neighborhood of the Current Agent",
						"Scan Across All Agents Within a Circular Neighborhood of a Watched Agent", },
						null));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Scan Across All Agents Within a Circular Neighborhood of the Current Agent",
				"This Code Will Scan the Given Continuous Space for an Agent in a Circular Neighborhood of the Current Agent",
				new String[] { "Scanning Variable", "Grid Variable",
						"Distance (e.g., 11.5)", }, new String[] {
						"$1", "$2", "$3" }, new String[] { "neighbor",
						"grid", "" },
		"$1 in (new ContinuousWithin(($2), this, $3).query().iterator().hasNext())"));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Scan Across All Agents Within a Circular Neighborhood of a Watched Agent",
				"This Code Will Scan the Given Continuous Space for an Agent in a Circular Neighborhood of a Watched Agent",
				new String[] { "Scanning Variable", "Grid Variable",
						"Distance (e.g., 11.5)", },
						new String[] { "$1", "$2", "$3" },
						new String[] { "neighbor", "grid", "" },
		"$1 in (new ContinuousWithin(($2), watchedAgent, $3).query().iterator().hasNext())"));

		exampleListLoopWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Scan a Context",
				"Please Select the Context Scaning Logic to Create",
				new String[] {
						"Scan for Agents with a Given Property Value",
						"Scan for Agents with a Numeric Property Value Strictly Less Than a Given Threshold",
				"Scan for Agents with a Numeric Property Value Strictly Greater Than a Given Threshold" },
				null));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Scan for Agents with a Given Property Value",
				"Please Fill In the Context Scaning Options (The Default Context is the Current Agent's Top Level Context)",
				new String[] { "Scanning Variable", "Context",
						"Attibute Name", "Target Value" },
						new String[] { "$1", "$2", "$3", "$4" },
						new String[] { "neighbor",
						"ContextUtils.getContext(this)", "" },
		"$1 in (new PropertyEquals(($2), \"$3\", \"$4\").query().iterator().hasNext())"));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Scan for Agents with a Numeric Property Value Strictly Less Than a Given Threshold",
				"Please Fill In the Context Scaning Options (The Default Context is the Current Agent's Top Level Context)",
				new String[] { "Scanning Variable", "Context",
						"Attibute Name", "Target Value", "" },
						new String[] { "$1", "$2", "$3", "$4" },
						new String[] { "neighbor",
						"ContextUtils.getContext(this)", "" },
		"$1 in (new PropertyGreaterThan(($2), \"$3\", \"$4\").query().iterator().hasNext())"));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Scan for Agents with a Numeric Property Value Strictly Greater Than a Given Threshold",
				"Please Fill In the Context Scaning Options (The Default Context is the Current Agent's Top Level Context)",
				new String[] { "Scanning Variable", "Context",
						"Attibute Name", "Target Value" },
						new String[] { "$1", "$2", "$3", "$4" },
						new String[] { "neighbor",
						"ContextUtils.getContext(this)", "" },
		"$1 in (new PropertyLessThan(($2), \"$3\", \"$4\").query().iterator().hasNext())"));

		exampleListLoopWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Make a Random Draw and Scan While It Matches a Given Threshold or Thresholds",
				"Please Select the Logic to Create",
				new String[] {
						"Make a Random Draw and Scan While It Matches a Given Threshold",
				"Make a Random Draw from a Range and Scan While It Matches a Given Threshold" },
				null));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Make a Random Draw and Scan While It Matches a Given Threshold",
				"This Code Will Make a Uniform Random Draw and Scan to See If It Is Greater Than the Given Threshold",
				new String[] { "Threshold (0.0 to 1.0)" },
				new String[] { "$1" }, new String[] { "" },
		"RandomDrawAgainstThreshold($1)"));

		exampleListLoopWizard
		.add(new NewCodeWizardFormEntry(
				"Make a Random Draw from a Range and Scan While It Matches a Given Threshold",
				"This Code Will Make a Uniform Random Draw and Scan to See If It Is Between Than the Given Thresholds",
				new String[] {
						"Inclusive Lower Bound for the Random Draw",
						"Inclusive Upper Bound for the Random Draw",
				"Inclusive Lower Bound for Comparision" },
				new String[] { "$1", "$2", "$3" }, new String[] { "",
						"", "" }, "RandomDraw(($1), ($2)) > ($3)"));

		exampleListLoopWizard.add(new NewCodeWizardFormEntry("Enter User Code",
				"Please Enter the Code to Create",
				new String[] { "Code to Create" }, new String[] { "$1" },
				new String[] { "" }, "$1"));


		return exampleListLoopWizard;
	}
}
