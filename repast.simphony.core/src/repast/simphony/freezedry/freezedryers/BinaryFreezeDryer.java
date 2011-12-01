package repast.simphony.freezedry.freezedryers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import repast.simphony.freezedry.FreezeDryedObject;
import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.freezedry.FreezeDryingException;

public class BinaryFreezeDryer implements FreezeDryer {

	public FreezeDryedObject freezeDry(String id, Object o)
			throws FreezeDryingException {
		FreezeDryedObject fdo = new FreezeDryedObject(id, o.getClass());
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);

			oos.writeObject(o);
			byte[] serialized = baos.toByteArray();
			fdo.put("BYTESTREAM", serialized);
			return fdo;
		} catch (IOException ioe) {
			throw new FreezeDryingException(ioe);
		} finally {
			try {
				if (oos != null) {
					oos.flush();
					oos.close();
				}
				if (baos != null) {
					baos.close();
				}
			} catch (IOException ioe) {
				throw new FreezeDryingException(ioe);
			}
		}
	}

	public Object rehydrate(FreezeDryedObject fdo) throws FreezeDryingException {
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream((byte[]) fdo.get("BYTESTREAM"));
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (IOException ioe) {
			throw new FreezeDryingException(ioe);
		} catch (ClassNotFoundException cnfe) {
			throw new FreezeDryingException(cnfe);
		} finally {
			try {
				if (bais != null) {
					bais.close();
				}
				if (ois != null) {
					ois.close();
				}
			} catch (IOException ioe) {
				throw new FreezeDryingException(ioe);
			}
		}
	}

	public boolean handles(Class clazz) {
		return true;
	}

}
