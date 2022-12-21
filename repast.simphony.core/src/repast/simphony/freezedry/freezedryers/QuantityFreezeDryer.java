package repast.simphony.freezedry.freezedryers;

import javax.measure.Quantity;

import repast.simphony.freezedry.FreezeDryedObject;
import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.freezedry.FreezeDryingException;
import tech.units.indriya.AbstractQuantity;

public class QuantityFreezeDryer implements FreezeDryer<Quantity<?>> {

  private static final String STR_REP = "STR_REP";


	public Quantity<?> rehydrate(FreezeDryedObject fdo) throws FreezeDryingException {
		String text = (String) fdo.get(STR_REP);
    return AbstractQuantity.parse(text);
	}

	public FreezeDryedObject freezeDry(String id, Quantity<?> amt) throws FreezeDryingException {
		FreezeDryedObject fdo = new FreezeDryedObject(id, Quantity.class);
		fdo.put(STR_REP, amt.toString());
		return fdo;
	}

	public boolean handles(Class<?> clazz) {
		return clazz.equals(Quantity.class);
	}
}