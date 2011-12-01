package repast.simphony.filter;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class FilteredIterator<T> implements Iterator<T> {
	Iterator<T> iterator;

	Filter<T> filter;

	T current;

	public FilteredIterator(Filter<T> filter, Collection<T> collection) {
		super();
		this.filter = filter;
		this.iterator = collection.iterator();
	}

	public FilteredIterator(Filter<T> filter, Iterator<T> iterator) {
		this.filter = filter;
		this.iterator = iterator;
	}

	public boolean hasNext() {
		if (current != null) {
			return true;
		}
		if (iterator.hasNext()) {
			T tmp = iterator.next();
			if (filter.evaluate(tmp)) {
				current = tmp;
				return true;
			} else {
				return hasNext();
			}
		}
		return false;
	}

	public T next() {
		if (hasNext()) {
			T tmp = current;
			current = null;
			return tmp;
		}
		throw new NoSuchElementException();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
