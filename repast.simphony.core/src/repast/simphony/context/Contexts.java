package repast.simphony.context;

public class Contexts {

	public static <T> Context<T> createContext(Class<T> type, Object id) {
		return ContextFactoryFinder.createContextFactory(type, null)
				.createContext(id);
	}

}
