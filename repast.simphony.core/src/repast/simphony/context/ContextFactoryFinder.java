package repast.simphony.context;

import java.util.Map;

public class ContextFactoryFinder {

	public static <T> ContextFactory<T> createContextFactory(Class<T> type,
			Map<String, Object> hints) {
		return new DefaultContextFactory<T>();
	}
}
