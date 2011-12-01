package repast.simphony.terracotta.datagrid;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class to store work.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */
public class SimpleWorkCache implements WorkCache {

	/**A map cache to keep stored work*/
	private final Map<String, RepastWork> cache	= 
		new ConcurrentHashMap<String, RepastWork>();

	public Map<String, RepastWork> getCache() {
		return cache;
	}

	public RepastWork getWork(String location) {
		return cache.get(location);
	}

	public boolean hasWork(String location) {
		return cache.containsKey(location);
	}

	public void setWork(String location, RepastWork item) {
		cache.put(location, item);
	}
}