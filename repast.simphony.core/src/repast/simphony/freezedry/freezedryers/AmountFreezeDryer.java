package repast.simphony.freezedry.freezedryers;

import org.jscience.physics.amount.Amount;

import repast.simphony.freezedry.FreezeDryedObject;
import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.freezedry.FreezeDryingException;

public class AmountFreezeDryer implements FreezeDryer<Amount> {

  private static final String STR_REP = "STR_REP";


	public Amount rehydrate(FreezeDryedObject fdo) throws FreezeDryingException {
		String text = (String) fdo.get(STR_REP);
    return Amount.valueOf(text);
	}

	public FreezeDryedObject freezeDry(String id, Amount amt)
			throws FreezeDryingException {
		FreezeDryedObject fdo = new FreezeDryedObject(id, Amount.class);
		fdo.put(STR_REP, amt.toString());
		return fdo;
	}

	public boolean handles(Class<?> clazz) {
		return clazz.equals(Amount.class);
	}
}