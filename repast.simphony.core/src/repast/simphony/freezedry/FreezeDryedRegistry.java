package repast.simphony.freezedry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.ProjectionRegistry;
import repast.simphony.engine.environment.ProjectionRegistryData;
import repast.simphony.freezedry.freezedryers.AmountFreezeDryer;
import repast.simphony.freezedry.freezedryers.ArrayFreezeDryer;
import repast.simphony.freezedry.freezedryers.BigDecimalFreezeDryer;
import repast.simphony.freezedry.freezedryers.ClassFreezeDryer;
import repast.simphony.freezedry.freezedryers.CollectionFreezeDryer;
import repast.simphony.freezedry.freezedryers.ContextFreezeDryer;
import repast.simphony.freezedry.freezedryers.DefaultFreezeDryer;
import repast.simphony.freezedry.freezedryers.MapFreezeDryer;
import repast.simphony.freezedry.freezedryers.PrimitiveFreezeDryer;
import simphony.util.messages.MessageCenter;

public class FreezeDryedRegistry {
	private static final MessageCenter LOG = MessageCenter
			.getMessageCenter(FreezeDryedRegistry.class);

	private transient Map<String, Object> keyCache;

	private transient Map<Object, String> objectCache;

//	private List<FreezeDryedDataSource> writers;

	private FreezeDryer defaultDryer;

	private Map<Class<?>, FreezeDryer<?>> freezerMap;

	private LinkedList<FreezeDryer> driers;

	private FreezeDryedDataSource dataSource;
	
//	private FreezeDryedDataSource reader;
//
//	private FreezeDryedDataSource writer;

	public FreezeDryedRegistry() {
		keyCache = new HashMap<String, Object>();
		objectCache = new HashMap<Object, String>();

		defaultDryer = new DefaultFreezeDryer(this);
		freezerMap = new HashMap<Class<?>, FreezeDryer<?>>();

		driers = new LinkedList<FreezeDryer>();
		driers.add(new PrimitiveFreezeDryer());
		driers.add(new PrimitiveArrayFreezeDryer(this));
		driers.add(new ArrayFreezeDryer(this));
		driers.add(new ContextFreezeDryer(this));
		driers.add(new MapFreezeDryer(this));
		driers.add(new CollectionFreezeDryer(this));
		driers.add(new ClassFreezeDryer(this));
    driers.add(new AmountFreezeDryer());
    driers.add(new BigDecimalFreezeDryer());
    
    // Add additional driers from the projection registry.
    for (ProjectionRegistryData data : ProjectionRegistry.getRegistryData()){
    	driers.addAll(data.getFreezeDryers());
    }
  }

	public String getId(Object o) throws FreezeDryingException {
		String id = objectCache.get(o);
		if (id == null) {
			id = freezeDryObject(o);
		}
		return id;
	}

	@SuppressWarnings("unchecked")
	public <T> T getObject(String key, Class<T> type) throws FreezeDryingException {
		T object = (T) keyCache.get(key);
		if (object == null) {
			object = rehydrateObject(type, key);
			keyCache.put(key, object);
			return object;
		}
		return object;
	}

	// public void registerWriter(FreezeDryedDataSource writer) {
	// if (reader == null) {
	// reader = writer;
	// }
	// writers.add(writer);
	// }

//	/**
//	 * Sets the data source that will be used for writing data. Setting this will not change the
//	 * data source that will be used for reading.
//	 * 
//	 * @see #setDataSource(FreezeDryedDataSource)
//	 * @see #setReader(FreezeDryedDataSource)
//	 * 
//	 * @param writer
//	 *            the data source to use for writing
//	 */
//	public void setWriter(FreezeDryedDataSource writer) {
//		// TODO: potentially get rid of this method and require that if people want this
//		// behavior that they use a data source that will delegate to a reader and writer 
//		// separately
//		this.writer = writer;
//	}

	/**
	 * Sets this data source to use the specified data source for both reading and writing. 
	 * 
	 * @param dataSource
	 *            the data source to use for reading and writing
	 */
	public void setDataSource(FreezeDryedDataSource dataSource) {
		this.dataSource = dataSource;
//		this.writer = source;
//		this.reader = source;
	}

//	/**
//	 * Sets the data source that will be used for reading in data. This does not change the
//	 * data source that will be used for writing.
//	 * 
//	 * @see #setDataSource(FreezeDryedDataSource)
//	 * @see #setWriter(FreezeDryedDataSource)
//	 * 
//	 * @param reader
//	 *            the data source for reading data
//	 */
//	public void setReader(FreezeDryedDataSource reader) {
//		// TODO: get rid of this method and require that if people want this
//		// behavior that they use a data source that will delegate to a reader and writer 
//		// separately
//		this.reader = reader;
//	}

	/**
	 * Rehydates the specified object using this registry's data source.
	 * 
	 * @param <T>
	 * @param type
	 * @param key
	 * @return
	 * @throws FreezeDryingException
	 */
	@SuppressWarnings("unchecked")
	private <T> T rehydrateObject(Class<T> type, String key) throws FreezeDryingException {
		try {
			FreezeDryedObject fdo = dataSource.read(type, key);
			FreezeDryer<T> dryer = getFreezeDryer(type);
			return dryer.rehydrate(fdo);
		} catch (RuntimeException ex) {
			LOG.error("Error while attempting to rehydrate class '" + type + "' with key '" + key
					+ "'.", ex);
			throw ex;
		}
	}

  public <T> T rehydrate(Class<T> type, String key) throws FreezeDryingException {
    T result = rehydrateObject(type, key);
    dataSource.close();
    return result;
  }

  @SuppressWarnings("unchecked")
	public <T> Collection<T> rehydrate(Class<T> type) throws FreezeDryingException {
		List<T> objects = new ArrayList<T>();
		Iterable<FreezeDryedObject> fdos = dataSource.read(type);
		FreezeDryer<T> dryer = getFreezeDryer(type);
		for (FreezeDryedObject fdo : fdos) {
			objects.add(dryer.rehydrate(fdo));
		}
		return objects;
	}

  public String freezeDry(Object obj) throws FreezeDryingException {
    String retVal = freezeDryObject(obj);
    dataSource.close();
    return retVal;
  }

  @SuppressWarnings("unchecked")
	private String freezeDryObject(Object o) throws FreezeDryingException {
		String key = (o instanceof Context) ? ((Context)o).getId().toString() : generateObjectKey().toString();
		keyCache.put(key, o);
		objectCache.put(o, key);
		FreezeDryer dryer = getFreezeDryer(o.getClass());
		FreezeDryedObject fdo = dryer.freezeDry(key, o);
    dataSource.write(fdo);
//		for (FreezeDryedDataSource writer : writers) {
//			writer.write(fdo);
//		}
		return key;
	}

	@SuppressWarnings("unchecked")
	public <T> FreezeDryer<T> getFreezeDryer(Class<T> type) {
		FreezeDryer registeredDryer = freezerMap.get(type);
		// see if there is one registered
		if (registeredDryer != null) {
			return registeredDryer;
		} else {
			// try and see if one of the default ones handles it
			for (FreezeDryer dryer : driers) {
				if (dryer.handles(type)) {
					return dryer;
				}
			}
		}
		// return the default dryer
		return this.defaultDryer;
	}

	public void registerFreezeDryer(Class clazz, FreezeDryer dryer) {
		freezerMap.put(clazz, dryer);
	}

	public String generateObjectKey() {
		return UUID.randomUUID().toString();
	}

	public void reset() {
		objectCache.clear();
		keyCache.clear();
	}
}
