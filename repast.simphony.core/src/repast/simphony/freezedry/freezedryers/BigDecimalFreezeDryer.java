package repast.simphony.freezedry.freezedryers;

import repast.simphony.freezedry.FreezeDryedObject;
import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.freezedry.FreezeDryingException;

import java.math.BigDecimal;

public class BigDecimalFreezeDryer implements FreezeDryer<BigDecimal> {

  private static final String STR_REP = "STR_REP";


	public BigDecimal rehydrate(FreezeDryedObject fdo) throws FreezeDryingException {
		String text = (String) fdo.get(STR_REP);
    return new BigDecimal(text);
	}

	public FreezeDryedObject freezeDry(String id, BigDecimal bd)
			throws FreezeDryingException {
		FreezeDryedObject fdo = new FreezeDryedObject(id, BigDecimal.class);
		fdo.put(STR_REP, bd.toString());
		return fdo;
	}

	public boolean handles(Class<?> clazz) {
		return clazz.equals(BigDecimal.class);
	}
}