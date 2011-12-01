/**
 * 
 */
package repast.simphony.engine.watcher;

import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import repast.simphony.context.Context;
import repast.simphony.context.ContextEvent;
import repast.simphony.context.ContextListener;
import repast.simphony.engine.schedule.CallBackAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.watcher.query.ASTStart;
import repast.simphony.engine.watcher.query.ParseException;
import repast.simphony.engine.watcher.query.QueryParser;
import repast.simphony.engine.watcher.query.TokenMgrError;
import repast.simphony.util.collections.Pair;
import simphony.util.messages.MessageCenter;

/**
 * ContextListener that examines objects added to a Context to see if they have
 * watches and if so setup the notification mechanism on them.
 * 
 * @author Nick Collier
 */
public class WatchContextListener implements ContextListener<Object> {
  
  private static class CacheEntries {
    List<CacheEntry> entries = new ArrayList<CacheEntry>();
    
    public void addEntry(CacheEntry entry) {
      entries.add(entry);
    }
    
    public boolean addWPS(Watch watch, CallBackAction action, List<WatchParameters> wps) {
      boolean watchFound = false;
      for (CacheEntry entry : entries) {
        if (entry.watch.equals(watch)) {
          watchFound = true;
          wps.add(new WatchParameters(entry.params, action));
          break;
        }
      }
      return watchFound;
    }
  }
  
  private static class CacheEntry {
    
    SharedWatchParameters params;
    Watch watch;
    
    public CacheEntry(Watch watch, SharedWatchParameters params) {
      this.watch = watch;
      this.params = params;
    }
  }

  private static MessageCenter msg = MessageCenter.getMessageCenter(WatchContextListener.class);
  private ISchedule schedule;

  // key is the class name
  private Map<Class<?>, List<Pair<Method, Watch>>> watchMap;
  private Map<Method, CacheEntries> parameterCache = new HashMap<Method, CacheEntries>();

  public WatchContextListener(ISchedule schedule, Map<Class<?>, List<Pair<Method, Watch>>> watchMap) {
    this.schedule = schedule;
    this.watchMap = watchMap;
  }
  
  private ArgMatcher createArgMatcher(Method method, Watch watch) {
    int numParams = method.getParameterTypes().length;
    ArgMatcher matcher = new ArgMatcher(new AnnotatedWatchData(watch));

    if (numParams > 0) {
      try {
        StringTokenizer tok = new StringTokenizer(watch.watcheeFieldNames(), ",");
        boolean matched = false;
        while (tok.hasMoreTokens()) {
          matched = matcher.match(method, watch.watcheeClassName(), tok.nextToken().trim());
          if (matched)
            break;
        }
        if (!matched) {
          RuntimeException ex = new RuntimeException("Error in watcher method parameters");
          msg.error(ex.getMessage(), ex);
          throw ex;
        }
      } catch (ClassNotFoundException e) {
        // todo better error handling
        msg.error("Error while setting up watcher", e);
      } catch (NoSuchFieldException e) {
        msg.error("Error while setting up watcher", e);
      }
    }
    
    return matcher;
  }

  private List<WatchParameters> createWatchParameters(Object obj, Pair<Method, Watch> data,
      Context<? extends Object> context) {
    
    List<WatchParameters> wps = new ArrayList<WatchParameters>();

    // check the cache to see if we have setup this object
    // type before
    Method method = data.getFirst();
    Watch watch = data.getSecond();
   
    CacheEntries entries = parameterCache.get(method);
    boolean createCacheEntry = entries == null;
    if (entries != null) {
      CallBackAction action = new CallBackAction(obj, method);
      // idea is that we can add per watch because if we don't
      // find it then we add all the fields for the watch, so 
      // next time, they are all there.
      createCacheEntry = !entries.addWPS(watch, action, wps);
    } 
    
    if (createCacheEntry) {
      //System.out.println(watch + ": " + watchFound);
      ArgMatcher matcher = createArgMatcher(method, watch);
      CallBackAction action = new CallBackAction(obj, method);
      String fieldNames = watch.watcheeFieldNames();
      StringTokenizer tok = new StringTokenizer(fieldNames, ",");
      while (tok.hasMoreTokens()) {
        String fieldName = tok.nextToken().trim();
        SharedWatchParameters proto = new SharedWatchParameters(watch.watcheeClassName(), fieldName);
        WatchParameters wp = new WatchParameters(proto, action);
        wp.setWatcheeCondition(watch.triggerCondition());
        wp.setTriggerSchedule(watch.whenToTrigger(), watch.scheduleTriggerDelta(),
            watch.scheduleTriggerPriority());
        wp.setWatcherCount(watch.pick());
        wp.setShuffleWatchers(watch.shuffle());

        String query = watch.query();
        if (query.trim().length() > 0)
          processQuery(query, wp, context);
        wp.setArgMatcher(matcher);
        wps.add(wp);
        
        // add it to the cache.
        if (entries == null) {
          entries = new CacheEntries();
          parameterCache.put(method, entries);
        }
  
        entries.addEntry(new CacheEntry(watch, proto));
      }
    }

    return wps;
  }
  

  private void processQuery(String query, WatchParameters wp, Context<? extends Object> context) {
    QueryParser parser = new QueryParser(new StringReader(query + ";"));
    try {
      ASTStart start = parser.Start();
      // get the context that actually contains the watcher
      wp.setQueryCondition(start.buildExpression(context.findParent(wp.getWatcher())));
    } catch (TokenMgrError ex) {
      handleQueryException(query, ex);
    } catch (ParseException ex) {
      handleQueryException(query, ex);
    }
  }

  private void handleQueryException(String query, Throwable ex) {
    MessageCenter msgCenter = MessageCenter.getMessageCenter(getClass());
    String message = "Invalid query annotation '" + query + "'";
    msgCenter.error(message, ex);
    // todo better error handling
    throw new RuntimeException(ex);
  }

  public void eventOccured(ContextEvent<Object> event) {
    ContextEvent.EventType type = event.getType();
    Object obj = event.getTarget();
    if (type == ContextEvent.EventType.AGENT_ADDED) {
      processObject(obj, event.getContext());
    } else if (type == ContextEvent.EventType.AGENT_REMOVED) {
      // remove the notifier for that object
      WatcherTrigger.getInstance().removeNotifier(obj);
    }
  }

  void processObject(Object obj, Context<? extends Object> context) {
    List<Pair<Method, Watch>> watchData = findWatchData(obj.getClass());
    if (watchData != null) {
      for (Pair<Method, Watch> data : watchData) {
        for (WatchParameters wp : createWatchParameters(obj, data, context)) {
          WatcherTrigger.getInstance().addFieldSetWatch(wp, schedule);
        }
      }
    }
  }

  List<Pair<Method, Watch>> findWatchData(Class<?> clazz) {
    // try to find direct match first
    List<Pair<Method, Watch>> watchData = watchMap.get(clazz);

    // if direct search fails, look for watches defined in
    // superclasses
    if (watchData == null) {
      for (Class<?> key : watchMap.keySet()) {
        if (key.isAssignableFrom(clazz)) {
          watchData = watchMap.get(key);
          // put data in for clazz for next lookup
          watchMap.put(clazz, watchData);
          break;
        }
      }
    }

    return watchData;
  }
}
