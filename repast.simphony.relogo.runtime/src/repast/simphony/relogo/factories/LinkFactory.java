package repast.simphony.relogo.factories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.relogo.BaseLink;
import repast.simphony.relogo.Directed;
import repast.simphony.relogo.Link;
import repast.simphony.relogo.Observer;
import repast.simphony.relogo.Plural;
import repast.simphony.relogo.TrackingEdgeCreator;
import repast.simphony.relogo.Undirected;
import repast.simphony.ui.RSApplication;
import simphony.util.messages.MessageCenter;

public class LinkFactory {
	@SuppressWarnings("unused")
	private static MessageCenter msgCenter = MessageCenter
			.getMessageCenter(LinkFactory.class);

	private Observer observer = null;

	private Class<? extends BaseLink> defaultDirectedLinkClass;
	private Class<? extends BaseLink> defaultUndirectedLinkClass;
	private Collection<Class<? extends BaseLink>> additionalDirectedLinkClasses;
	private Collection<Class<? extends BaseLink>> additionalUndirectedLinkClasses;

	Map<String, Class<? extends BaseLink>> linkTypesMap = new ConcurrentHashMap<String,Class<? extends BaseLink>>();
	
	public Class<?> getLinkTypeClass(String linkType){
		String standardizedLinkType = linkType.toLowerCase();
		return (linkTypesMap.get(standardizedLinkType));
	}
	//
	/*
	 * Constructors: 1. One link class (used for both directed and undirected
	 * default networks) 2. Two link classes (first used for directed, second
	 * for undirected) 3. One link class and one list of additional link classes
	 * 4. Two link classes and one list of additional link classes
	 */
	public LinkFactory(Class<? extends BaseLink> linkClass) {
		this(linkClass, linkClass);
	}

	public LinkFactory(Class<? extends BaseLink> dirLinkClass,
			Class<? extends BaseLink> undirLinkClass) {
		this(dirLinkClass, undirLinkClass,
				ReLogoImplementingClassesFinder.find(BaseLink.class));
	}

	public LinkFactory(Class<? extends BaseLink> linkClass,
			Collection<Class<? extends BaseLink>> additionalLinkList) {
		this(linkClass, linkClass, additionalLinkList);
	}

	public LinkFactory(Class<? extends BaseLink> dirLinkClass,
			Class<? extends BaseLink> undirLinkClass,
			Collection<Class<? extends BaseLink>> additionalLinkClasses) {
		if (!BaseLink.class.isAssignableFrom(dirLinkClass)) {
			throw new RuntimeException("Argument " + dirLinkClass
					+ " to LinkFactory's constructor needs to extend BaseLink.");
		}
		if (!BaseLink.class.isAssignableFrom(undirLinkClass)) {
			throw new RuntimeException("Argument " + undirLinkClass
					+ " to LinkFactory's constructor needs to extend BaseLink.");
		}
		for (Class<?> clazz : additionalLinkClasses) {
			if (!BaseLink.class.isAssignableFrom(clazz)) {
				throw new RuntimeException(
						"Argument "
								+ clazz
								+ " to LinkFactory's constructor needs to extend BaseLink.");
			}
		}
		
		this.defaultDirectedLinkClass = dirLinkClass;
		String singularName = dirLinkClass.getSimpleName();
		String pluralName;
		if (dirLinkClass.isAnnotationPresent(Plural.class)){
			pluralName = dirLinkClass.getAnnotation(Plural.class).value();
		}
		else {
			pluralName = singularName + "s";
		}
		this.linkTypesMap.put(singularName.toLowerCase(), dirLinkClass);
		this.linkTypesMap.put(pluralName.toLowerCase(), dirLinkClass);
		
		this.defaultUndirectedLinkClass = undirLinkClass;
		if (!dirLinkClass.equals(undirLinkClass)){
			singularName = undirLinkClass.getSimpleName();
			if (undirLinkClass.isAnnotationPresent(Plural.class)){
				pluralName = undirLinkClass.getAnnotation(Plural.class).value();
			}
			else {
				pluralName = singularName + "s";
			}
			this.linkTypesMap.put(singularName.toLowerCase(), undirLinkClass);
			this.linkTypesMap.put(pluralName.toLowerCase(), undirLinkClass);
		}

		additionalDirectedLinkClasses = new ArrayList<Class<? extends BaseLink>>();
		additionalUndirectedLinkClasses = new ArrayList<Class<? extends BaseLink>>();
		for (Class<? extends BaseLink> clazz : additionalLinkClasses) {
			if (clazz.isAnnotationPresent(Directed.class)){
				additionalDirectedLinkClasses.add(clazz);
			}
			else if (clazz.isAnnotationPresent(Undirected.class)){
				additionalUndirectedLinkClasses.add(clazz);
			}
			else { // if neither annotation is present, assume directed
				additionalDirectedLinkClasses.add(clazz);
			}
			singularName = clazz.getSimpleName();
			if (clazz.isAnnotationPresent(Plural.class)){
				pluralName = clazz.getAnnotation(Plural.class).value();
			}
			else {
				pluralName = singularName + "s";
			}
			this.linkTypesMap.put(singularName.toLowerCase(), clazz);
			this.linkTypesMap.put(pluralName.toLowerCase(), clazz);
		}
	}

	public Link createLink(Class<? extends BaseLink> linkClass, Object source,
			Object target, boolean directed) {
		return this.createLink(linkClass, source, target, directed, 1.0);
	}

	public Link createLink(Class<? extends BaseLink> linkClass, Object source,
			Object target, boolean directed, double weight) {
		if (observer == null) {
			throw (new RuntimeException(
					"The LinkFactory init method needs to be called before instatiating Links."));
		}
		if (!(linkClass == defaultDirectedLinkClass
				|| linkClass == defaultUndirectedLinkClass
				|| additionalDirectedLinkClasses.contains(linkClass) || additionalUndirectedLinkClasses
				.contains(linkClass))) {
			throw new RuntimeException(
					"First argument "
							+ linkClass
							+ " to LinkFactory.createLink was not used in the LinkFactory constructor.");
		}
		BaseLink link = null;
		try {
			link = linkClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		link.setBaseLinkProperties(observer, this, source, target, directed,
				weight);
		return link;
	}

	@SuppressWarnings("unchecked")
	public void init(Observer observer) {
		this.observer = observer;
		Context<?> context = this.observer.getContext();

		// Add default directed network projection to observer's world context
		NetworkBuilder nbD = new NetworkBuilder("DirectedLinks", context, true);
		nbD.setEdgeCreator(
				new ReLogoEdgeCreator(this, defaultDirectedLinkClass))
				.buildNetwork().addProjectionListener(observer);

		// Add default undirected network projection to observer's world context
		nbD = new NetworkBuilder("UndirectedLinks", context, false);
		nbD.setEdgeCreator(
				new ReLogoEdgeCreator(this, defaultUndirectedLinkClass))
				.buildNetwork().addProjectionListener(observer);

		// Add tracking network projection to observer's world context
		nbD = new NetworkBuilder("Tracking Network", context, false);
		nbD.setEdgeCreator(new TrackingEdgeCreator()).buildNetwork();

		// Add additional directed network projections to observer's world
		// context
		for (Class<? extends BaseLink> clazz : additionalDirectedLinkClasses) {
			createNetwork(observer, clazz, true);
		}
		// Add additional undirected network projections to observer's world
		// context
		for (Class<? extends BaseLink> clazz : additionalUndirectedLinkClasses) {
			createNetwork(observer, clazz, false);
		}

	}

	@SuppressWarnings("unchecked")
	private void createNetwork(Observer observer,
			Class<? extends BaseLink> linkClass, boolean directed) {
		String networkName = linkClass.getSimpleName();
		NetworkBuilder nbD = new NetworkBuilder(networkName, observer
				.getContext(), directed);
		nbD.setEdgeCreator(new ReLogoEdgeCreator(this, linkClass))
				.buildNetwork().addProjectionListener(observer);
	}

}
