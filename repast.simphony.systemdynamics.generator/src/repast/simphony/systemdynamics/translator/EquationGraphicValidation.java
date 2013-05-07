
package repast.simphony.systemdynamics.translator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EquationGraphicValidation {

	private SystemDynamicsObjectManager systemDynamicsObjectManager;
	private SystemDynamicsObject systemDynamicsObjectLHS;
	private Map<String, Equation> equations;
	private List<String> addedScreenNames;
	private List<Arrow> addedArrows;

	public EquationGraphicValidation(
			SystemDynamicsObjectManager systemDynamicsObjectManager,
			SystemDynamicsObject systemDynamicsObject,
			Map<String, Equation> equations) {
		this.systemDynamicsObjectManager = systemDynamicsObjectManager;
		this.systemDynamicsObjectLHS = systemDynamicsObject;
		this.equations = equations;
		addedScreenNames = new ArrayList<String>();
		addedArrows = new ArrayList<Arrow>();
	}

	public String getScreenName() {
		return systemDynamicsObjectLHS.getScreenName();
	}

	public void validate() {

		List<Arrow> incomingArrows = systemDynamicsObjectLHS
				.getIncomingArrows();

		List<Equation> eqns = systemDynamicsObjectLHS.getEquations();

		for (Equation eqn : eqns) {
			// if not an assignment statement, or if get external data
			// we do not need to have arrows
			if (!eqn.isAssignment() || eqn.isGetExternalData())
				continue;

			// need to check each rhs variable to see if there is an associated
			// arrow as input
			Set<String> rhsVars = eqn.getRHSVariables();
			Iterator<String> iter = rhsVars.iterator();

			while (iter.hasNext()) {
				String rhsVar = iter.next();
				String rhsVarScreenName = SystemDynamicsObjectManager
						.getScreenName(rhsVar);

				// if there is no incoming arrow, create one
				if (!Arrow.listContainsArrowWithOtherEnd(incomingArrows,
						rhsVarScreenName)) {
					// there may be an "out" flow to this since we plays some
					// games on FLOW distections
					
					Arrow flow = Arrow.getArrowWithOtherEnd(systemDynamicsObjectLHS.getOutgoingArrows(),
							rhsVarScreenName);
					
					if (flow == null || !flow.isFlow()) {
						Arrow arrow = new Arrow(rhsVarScreenName, Arrow.IN,
								Arrow.INFLUENCE);
						addedArrows.add(arrow);
						addedScreenNames.add(rhsVarScreenName);
					}
				}
			}
		} // end eqn loop
	}

	public boolean isValid() {
		return addedArrows.size() == 0;
	}

	public void apply() {

		for (String addedScreenName : addedScreenNames) {

			System.out.println("EquationGraphicValidation add");
			// create a SDO for this variable
			systemDynamicsObjectManager
					.addSystemDynamicsObject(addedScreenName);

			// get the equations that implement this variable (can be array)
			List<Equation> eqns = systemDynamicsObjectManager
					.findEquationsWithScreenName(addedScreenName, equations);

			// this sdo is associated with the new added sreen name (i.e. a
			// rhsVar)
			// the class sdo is the sdo for the lhs
			SystemDynamicsObject systemDynamicsObjectRHS = systemDynamicsObjectManager
					.getObjectWithName(addedScreenName);

			for (Equation eqn : eqns) {
				// add these equations to the new RHS SDO
				systemDynamicsObjectManager.addEquation(
						systemDynamicsObjectRHS.getScreenName(), eqn);
			}
		}

		// now add incoming and outgoing arrows for the two sdos
		for (Arrow arrow : addedArrows) {

			// this is the incoming arrow
			systemDynamicsObjectManager.addIncomingArrow(
					systemDynamicsObjectLHS.getScreenName(), arrow);

			// this is the outgoing arrow for the rhs sdo
			systemDynamicsObjectManager.addOutgoingArrow(arrow.getOtherEnd(),
					new Arrow(systemDynamicsObjectLHS.getScreenName(),
							Arrow.OUT, Arrow.INFLUENCE));
		}

	}

	public List<String> getAddedScreenNames() {
		return addedScreenNames;
	}

	public List<Arrow> getAddedArrows() {
		return addedArrows;
	}

}
