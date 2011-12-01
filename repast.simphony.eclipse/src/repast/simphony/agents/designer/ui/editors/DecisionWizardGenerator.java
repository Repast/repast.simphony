package repast.simphony.agents.designer.ui.editors;

import java.util.ArrayList;

import repast.simphony.agents.designer.ui.wizards.NewCodeWizardEntry;
import repast.simphony.agents.designer.ui.wizards.NewCodeWizardFormEntry;
import repast.simphony.agents.designer.ui.wizards.NewCodeWizardRadioButtonEntry;

/**
 * Generate the contents of the Decision block wizard for the agent editor.
 * 
 * @author Michael J. North
 *
 */
public class DecisionWizardGenerator {

	public static ArrayList<NewCodeWizardEntry> createDecisionWizard(){
		ArrayList<NewCodeWizardEntry> exampleListIfWizard = 
			new ArrayList<NewCodeWizardEntry>();

		exampleListIfWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Decision Step",
				"Please Select the Decision Logic to Create",
				new String[] {
						"Compare Values",
						"Check a Projection (e.g., a Grid, Network or Continuous Space)",
						"Check a Context",
						"Make a Random Draw and Check It Against a Threshold or Thresholds",
				"Enter User Code" }, null));

		exampleListIfWizard.add(new NewCodeWizardRadioButtonEntry(
				"Compare Values",
				"Please Select the Comparision Logic to Create", new String[] {
						"Compare Two Values", "Combine Two Comparisions" },
						null));

		exampleListIfWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Compare Two Values",
				"Please Select the Decision Logic to Create",
				new String[] {
						"Use Two Numeric Thresholds or Formulas",
						"Use an Attribute of the Current Agent and a Numeric Threshold or Formula",
						"Use Two Attributes of the Current Agent",
						"Use an Attribute of a Watched Agent and a Numeric Threshold or Formula",
				"Use Two Attributes of a Watched Agent" }, null));

		exampleListIfWizard.add(new NewCodeWizardFormEntry(
				"Use Two Numeric Thresholds or Formulas",
				"Please Fill In the Comparison Options", new String[] {
						"First Number or Formula to Compare",
						"Comparision Operation (>, >=, ==, !=, <=, <)",
				"Second Number or Formula to Compare" }, new String[] {
						"$1", "$2", "$3" }, new String[] { "", "==", "" },
		"($1 $2 $3)"));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Use an Attribute of the Current Agent and a Numeric Threshold or Formula",
				"Please Fill In the Comparison Options", new String[] {
						"Attribute to Compare",
						"Comparision Operation (>, >=, ==, !=, <=, <)",
				"Numeric Threshold, or Formula to Compare" },
				new String[] { "$1", "$2", "$3" }, new String[] { "",
						"==", "" }, "($1 $2 $3)"));

		exampleListIfWizard.add(new NewCodeWizardFormEntry(
				"Use Two Attributes of the Current Agent",
				"Please Fill In the Comparison Options", new String[] {
						"First Attribute to Compare",
						"Comparision Operation (>, >=, ==, !=, <=, <)",
				"Second Attribute to Compare" }, new String[] { "$1",
						"$2", "$3" }, new String[] { "", "==", "" },
		"($1 $2 $3)"));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Use an Attribute of a Watched Agent and a Numeric Threshold or Formula",
				"Please Fill In the Comparison Options", new String[] {
						"Attribute to Compare",
						"Comparision Operation (>, >=, ==, !=, <=, <)",
				"Numeric Threshold, or Formula to Compare" },
				new String[] { "$1", "$2", "$3" }, new String[] { "",
						"==", "" }, "(watchedAgent.$1 $2 $3)"));

		exampleListIfWizard.add(new NewCodeWizardFormEntry(
				"Use Two Attributes of a Watched Agent",
				"Please Fill In the Comparison Options", new String[] {
						"First Attribute to Compare",
						"Comparision Operation (>, >=, ==, !=, <=, <)",
				"Second Attribute to Compare" }, new String[] { "$1",
						"$2", "$3" }, new String[] { "", "==", "" },
		"(watchedAgent.$1 $2 watchedAgent.$3)"));

		exampleListIfWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Combine Two Comparisions",
				"Please Select the Decision Logic to Create",
				new String[] {
						"Combine Two Comparisions Such that Both Must Be True (i.e., \"and\")",
				"Combine Two Comparisions Such that One Must Be True (i.e., \"or\")" },
				null));

		exampleListIfWizard
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

		exampleListIfWizard
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

		exampleListIfWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Check a Projection (e.g., a Grid, Network or Continuous Space)",
				"Please Select the Projection Checking Logic to Create",
				new String[] { "Check a Network", "Check a Grid",
				"Check a Continuous Space" }, null));

		exampleListIfWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Check a Network",
				"Please Select the Network Checking Logic to Create",
				new String[] {
						"Check to See If There is Another Agent Within a Given Number of Links of the Current Agent",
						"Check to See If There is Another Agent Within a Given Number of Links of a Watched Agent", },
						null));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Check to See If There is Another Agent Within a Given Number of Links of the Current Agent",
				"Check to See If There is Another Agent Within a Given Number of Links of the Current Agent While Respecting Link Directions, If Any, and Noting that \"Within\" Includes the Upper Limit",
				new String[] { "Network Variable", "Distance", },
				new String[] { "$1", "$2" },
				new String[] { "network", "" },
		"new NetPathWithin(($1), this, ($2)).query(optNestedQueryResult).iterator().hasNext()"));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Check to See If There is Another Agent Within a Given Number of Links of a Watched Agent",
				"Check to See If There is Another Agent Within a Given Number of Links of a Watched Agent While Respecting Link Directions, If Any, and Noting that \"Within\" Includes the Upper Limit",
				new String[] { "Network Variable", "Distance", },
				new String[] { "$1", "$2" },
				new String[] { "network", "" },
		"new NetPathWithin(($1), watchedAgent, ($2)).query(optNestedQueryResult).iterator().hasNext()"));

		exampleListIfWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Check a Grid",
				"Please Select the Grid Checking Logic to Create",
				new String[] {
						"Check to See If There is Another Agent Within a Moore Neighborhood of the Current Agent",
						"Check to See If There is Another Agent Within a Moore Neighborhood of a Watched Agent",
						"Check to See If There is Another Agent Within a Von Neumann Neighborhood of the Current Agent",
						"Check to See If There is Another Agent Within a Von Neumann Neighborhood of a Watched Agent", },
						null));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Check to See If There is Another Agent Within a Moore Neighborhood of the Current Agent",
				"This Code Will Check the Given Grid",
				new String[] {
						"Grid Variable",
						"Comma Separated List of Distances (e.g., \"1, 3, 7\")", },
						new String[] { "$1", "$2" },
						new String[] { "grid", "" },
		"new MooreQuery(($1), this, $2).query().iterator().hasNext()"));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Check to See If There is Another Agent Within a Moore Neighborhood of a Watched Agent",
				"This Code Will Check the Given Grid",
				new String[] {
						"Grid Variable",
						"Comma Separated List of Distances (e.g., \"1, 3, 7\")", },
						new String[] { "$1", "$2" },
						new String[] { "grid", "" },
		"new MooreQuery(($1), watchedAgent, $2).query().iterator().hasNext()"));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Check to See If There is Another Agent Within a Von Neumann Neighborhood of the Current Agent",
				"This Code Will Check the Given Grid",
				new String[] {
						"Grid Variable",
						"Comma Separated List of Distances (e.g., \"1, 3, 7\")", },
						new String[] { "$1", "$2" },
						new String[] { "grid", "" },
		"new VNQuery(($1), this, $2).query().iterator().hasNext()"));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Check to See If There is Another Agent Within a Von Neumann Neighborhood of a Watched Agent",
				"This Code Will Check the Given Grid",
				new String[] {
						"Grid Variable",
						"Comma Separated List of Distances (e.g., \"1, 3, 7\")", },
						new String[] { "$1", "$2" },
						new String[] { "grid", "" },
		"new VNQuery(($1), watchedAgent, $2).query().iterator().hasNext()"));

		exampleListIfWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Check a Continuous Space",
				"Please Select the Continuous Space Checking Logic to Create",
				new String[] {
						"Check to See If There is Another Agent Within a Circular Neighborhood of the Current Agent",
						"Check to See If There is Another Agent Within a Circular Neighborhood of a Watched Agent", },
						null));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Check to See If There is Another Agent Within a Circular Neighborhood of the Current Agent",
				"This Code Will Check the Given Continuous Space for an Agent in a Circular Neighborhood of the Current Agent",
				new String[] { "Grid Variable",
						"Distance (e.g., 11.5)", }, new String[] {
						"$1", "$2" }, new String[] { "grid", "" },
		"new ContinuousWithin(($1), this, $2).query().iterator().hasNext()"));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Check to See If There is Another Agent Within a Circular Neighborhood of a Watched Agent",
				"This Code Will Check the Given Continuous Space for an Agent in a Circular Neighborhood of a Watched Agent",
				new String[] { "Grid Variable",
						"Distance (e.g., 11.5)", }, new String[] {
						"$1", "$2" }, new String[] { "grid", "" },
		"new ContinuousWithin(($1), watchedAgent, $2).query().iterator().hasNext()"));

		exampleListIfWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Check a Context",
				"Please Select the Context Checking Logic to Create",
				new String[] {
						"Check for Agents with a Given Property Value",
						"Check for Agents with a Numeric Property Value Strictly Less Than a Given Threshold",
				"Check for Agents with a Numeric Property Value Strictly Greater Than a Given Threshold" },
				null));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Check for Agents with a Given Property Value",
				"Please Fill In the Context Checking Options (The Default Context is the Current Agent's Top Level Context)",
				new String[] { "Context", "Attibute Name",
				"Target Value" }, new String[] { "$1", "$2",
				"$3" }, new String[] {
						"ContextUtils.getContext(this)", "" },
		"new PropertyEquals(($1), \"$2\", \"$3\").query().iterator().hasNext()"));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Check for Agents with a Numeric Property Value Strictly Less Than a Given Threshold",
				"Please Fill In the Context Checking Options (The Default Context is the Current Agent's Top Level Context)",
				new String[] { "Context", "Attibute Name",
				"Target Value" }, new String[] { "$1", "$2",
				"$3" }, new String[] {
						"ContextUtils.getContext(this)", "" },
		"new PropertyGreaterThan(($1), \"$2\", \"$3\").query().iterator().hasNext()"));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Check for Agents with a Numeric Property Value Strictly Greater Than a Given Threshold",
				"Please Fill In the Context Checking Options (The Default Context is the Current Agent's Top Level Context)",
				new String[] { "Context", "Attibute Name",
				"Target Value" }, new String[] { "$1", "$2",
				"$3" }, new String[] {
						"ContextUtils.getContext(this)", "" },
		"new PropertyLessThan(($1), \"$2\", \"$3\").query().iterator().hasNext()"));

		exampleListIfWizard
		.add(new NewCodeWizardRadioButtonEntry(
				"Make a Random Draw and Check It Against a Threshold or Thresholds",
				"Please Select the Logic to Create",
				new String[] {
						"Make a Random Draw and Check It Against a Threshold",
				"Make a Random Draw from a Range and Check It Against a Threshold" },
				null));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Make a Random Draw and Check It Against a Threshold",
				"This Code Will Make a Uniform Random Draw and Check to See If It Is Greater Than the Given Threshold",
				new String[] { "Threshold (0.0 to 1.0)" },
				new String[] { "$1" }, new String[] { "" },
		"RandomDrawAgainstThreshold($1)"));

		exampleListIfWizard
		.add(new NewCodeWizardFormEntry(
				"Make a Random Draw from a Range and Check It Against a Threshold",
				"This Code Will Make a Uniform Random Draw and Check to See If It Is Between Than the Given Thresholds",
				new String[] {
						"Inclusive Lower Bound for the Random Draw",
						"Inclusive Upper Bound for the Random Draw",
				"Inclusive Lower Bound for Comparision" },
				new String[] { "$1", "$2", "$3" }, new String[] { "",
						"", "" }, "RandomDraw(($1), ($2)) > ($3)"));

		exampleListIfWizard.add(new NewCodeWizardFormEntry("Enter User Code",
				"Please Enter the Code to Create",
				new String[] { "Code to Create" }, new String[] { "$1" },
				new String[] { "" }, "$1"));

		return exampleListIfWizard;
	}

}
