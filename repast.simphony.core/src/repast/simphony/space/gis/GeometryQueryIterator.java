package repast.simphony.space.gis;

import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import repast.simphony.space.GeometryTest;

import com.vividsolutions.jts.geom.Geometry;

public class GeometryQueryIterator<T> implements Iterator<T> {
	Iterator<T> sourceIterator;

	Iterator<T> inputIterator;

	Set<T> preChecked = new HashSet<T>();

	Geography<T> geography;

	GeometryTest test;

	T current;

	public GeometryQueryIterator(Iterator<T> sourceIterator, Iterator<T> inputIterator,
			Geography<T> geography, GeometryTest test) {
		this.sourceIterator = sourceIterator;
		this.inputIterator = inputIterator;
		this.geography = geography;
		this.test = test;
	}

	public boolean hasNext() {
		if (current != null) {
			return true;
		} else if (!sourceIterator.hasNext()) {
			return false;
		}
		T tmp = sourceIterator.next();
		Geometry geom = geography.getGeometry(tmp);
		if (!test.test(geom)) {
			return false;
		}
		if (checkInInput(tmp)) {
			current = tmp;
			return true;
		}
		return hasNext();
	}

	private boolean checkInInput(T object) {
		if (preChecked.contains(object)) {
			return true;
		}
		while (inputIterator.hasNext()) {
			T tmp = inputIterator.next();
			preChecked.add(tmp);
			if (object.equals(tmp)) {
				return true;
			}
		}
		return false;
	}

	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		T tmp = current;
		current = null;
		return tmp;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}