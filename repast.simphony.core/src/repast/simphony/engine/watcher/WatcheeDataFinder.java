/**
 * 
 */
package repast.simphony.engine.watcher;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import repast.simphony.filter.Filter;
import simphony.util.messages.MessageCenter;

/**
 * Finds the Watchee info for instrumentation by searching paths for classes
 * with @Watch annotations, as well as looking for specified watcher classes in
 * those paths.
 * 
 * @author Nick Collier
 */
public class WatcheeDataFinder {

  private static final MessageCenter msg = MessageCenter.getMessageCenter(WatcheeDataFinder.class);

  public class FinderResult {
    private boolean ok;
    private String message;

    public FinderResult(boolean result, String message) {
      this.ok = result;
      this.message = message;
    }

    public String getMessage() {
      return message;
    }

    public boolean failed() {
      return !ok;
    }
  }
  
  private class ClassData {
    String name, superName;
    URL path;
    
    public ClassData(String name, URL path) {
      this.name = name;
      this.path = path;
    }
    
    WatcheeData createWatcheeData() {
      WatcheeData data = new WatcheeData(name);
      data.path = this.path;
      return data;
    }
  }

  // key is class name,
  private Map<String, WatcheeData> watchees = new HashMap<String, WatcheeData>();

  // key classname
  private Map<String, ClassData> classMap = new HashMap<String, ClassData>();

  // list of paths to seach for classes that contains watchers.
  private List<String> watcherPaths = new ArrayList<String>();

  private Map<String, WatcheeDataNode> nodeMap = new HashMap<String, WatcheeDataNode>();

  private WatcheeInstrumentor instrumentor;

  public WatcheeDataFinder(WatcheeInstrumentor watcheeInstrumentor) {
    this.instrumentor = watcheeInstrumentor;
  }

  /**
   * Adds the named class as a Watchee to find and the specified field to watch.
   * 
   * @param className
   *          the field's class
   * @param fieldName
   *          the field to watch
   */
  public void addClassToFind(String className, String fieldName) {
    WatcheeData data = watchees.get(className);
    if (data == null) {
      data = new WatcheeData(className);
      watchees.put(className, data);
    }
    data.addField(fieldName);
  }

  /**
   * Adds fields and watchees to watch from any @Watch annotations on any class
   * that this finds in the specified path. The path can be a top level class
   * directory of classes or a jar file.
   * 
   * @param path
   */
  public void addPathToSearch(String path) {
    watcherPaths.add(path);
  }

  /**
   * Finds @Watch annotations and creates WatcheeData from them.
   * 
   * @return a successful FinderResult if the run was successful and all classes
   *         that were tagged as watchees were found, otherwise an unsuccessful
   *         finder result with an explanatory message.
   * 
   * @throws ClassNotFoundException
   * @throws IOException
   */
  public FinderResult run(String classpath, Filter<String> filter) throws IOException,
      ClassNotFoundException {

    // finds all the classes in the paths
    // where we expect to find @Watch annotations
    // or manually specified watchers.
    findClasses(watcherPaths);
    // this will add names of watchees to the classNameMap.
    findWatches(filter);

    // make the full path
    //String jcp = System.getProperty("java.class.path");
    //if (classpath.length() > 0) {
      //jcp = jcp + File.pathSeparator + classpath;
    //}

    String[] paths = classpath.split(File.pathSeparator);
    // get all the classes on the full path
    findClasses(Arrays.asList(paths));

    // watchees should have partial WatcheeData for the Watchees
    // and classNameMap has the location to name mapping for the
    // watchees. We need to get the location from the class data map
    // and fill in the location of the watchees.
    List<String> missingClasses = new ArrayList<String>();
    for (WatcheeData data : watchees.values()) {
      ClassData classData = classMap.get(data.className);
      if (classData == null) {
        missingClasses.add(data.className);
      } else
        data.path = classData.path;
    }

    if (!missingClasses.isEmpty())
      return new FinderResult(false, "Unable to find class(es) to watch: " + missingClasses);

    // create Nodes for the initial WatcheeData
    for (WatcheeData data : watchees.values()) {
      WatcheeDataNode node = new WatcheeDataNode(data);
      nodeMap.put(data.className, node);
    }
    //System.out.println(classMap.size());

    findSuperClasses();
    findSubClasses();
    
    classMap.clear();
    return new FinderResult(true, "");
  }

  private void showSuperSearchError(String cname, List<String> fields) {
    // if we get all the way up to java.lang.Object then probably the
    // the field name is a typo or doesn't exist.
    if (cname.equals("java.lang.Object")) {
      for (String field : fields) {
        msg.warn("Watched field '" + field + "' could not be found.");
      }
    } else {
      msg.warn("Cannot find super classes to instrument: " + cname);
    }
  }
  
  private Map<String, WatcheeData> findSubClasses() {
    Map<String, WatcheeData> watcheeMap = new HashMap<String, WatcheeData>();
    for (ClassData data : classMap.values()) {
      if (!watcheeMap.containsKey(data.name)) {
        findParents(data, watcheeMap);
      }
    }
    
    return watcheeMap;
  }
  
  private void findParents(ClassData data, Map<String, WatcheeData> watcheeMap) {
    List<ClassData> hList = new ArrayList<ClassData>();
    WatcheeData watchee = null;
    while (data != null && watchee == null) {
      watchee = watchees.get(data.name);
      if (watchee == null) {
        hList.add(data);
        data = classMap.get(data.superName);
      }
    }
    
    if (watchee != null) {
      WatcheeDataNode parentNode = nodeMap.get(watchee.className);
      for (int i = hList.size() - 1; i >= 0; i--) {
        ClassData child = hList.get(i);
        WatcheeDataNode childNode = nodeMap.get(child.name);
        if (childNode == null) {
          WatcheeData childWatchee = child.createWatcheeData();
          watcheeMap.put(child.name, childWatchee);
          childNode = new WatcheeDataNode(childWatchee);
          parentNode.addChild(childNode);
        }
        childNode.data.addFields(parentNode.data.fields);
        parentNode = childNode;
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void addSuperClassData(WatcheeData data, Map<String, WatcheeData> watcheeMap)
      throws IOException {
    DataInputStream stream = new DataInputStream(data.path.openStream());
    ClassFile cf = new ClassFile(stream);
    stream.close();
    List<FieldInfo> fields = cf.getFields();
    List<String> missingFields = new ArrayList<String>();

    for (String field : data.fields) {
      if (!containsField(fields, field)) {
        missingFields.add(field);
      }
    }

    if (missingFields.size() > 0) {
      String superName = cf.getSuperclass();
      WatcheeData parentData = watcheeMap.get(superName);
      if (parentData == null) {
        // make new parent data
        ClassData proto = classMap.get(superName);
        if (proto == null) {
          showSuperSearchError(superName, missingFields);
        } else {
          parentData = proto.createWatcheeData();
          watcheeMap.put(parentData.className, parentData);
        }
      }
      parentData.addFields(missingFields);
      WatcheeDataNode parent = nodeMap.get(superName);
      if (parent == null) {
        parent = new WatcheeDataNode(parentData);
        nodeMap.put(superName, parent);
      }
      parent.addChild(nodeMap.get(data.className));
      addSuperClassData(parentData, watcheeMap);
    }
  }

  // for each watchee, create WatcheeData for their superclasses
  private Map<String, WatcheeData> findSuperClasses() throws IOException {
    Map<String, WatcheeData> watcheeMap = new HashMap<String, WatcheeData>();
    for (WatcheeData data : watchees.values()) {
      if (!watcheeMap.containsKey(data.className))
        watcheeMap.put(data.className, data);
      addSuperClassData(data, watcheeMap);
    }
    return watcheeMap;
  }
  
  private void addClassToMap(String name, URL path) {
    ClassData data = new ClassData(name, path);
    try {
      DataInputStream stream = new DataInputStream(data.path.openStream());
      ClassFile cf = new ClassFile(stream);
      stream.close();
      data.superName = cf.getSuperclass();
    } catch (Exception ex) {}
    
    classMap.put(name, data);
  }

  private void findClasses(List<String> paths) throws IOException {
    for (String path : paths) {
      File file = new File(path);
      if (file.getName().endsWith(".jar") && file.exists()) {
        // gather the jar classes
        JarFile jar = new JarFile(path);
        for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
          JarEntry entry = entries.nextElement();
          String name = entry.getName();
          // name will look something like
          // org/apache/commons/cli/PosixParser.class
          if (name.endsWith(".class")) {
            String clazz = name.substring(0, name.length() - 6);
            clazz = clazz.replace("/", ".");
            

            // jar url looks like:
            // jar:file:/c:/almanac/my.jar!/com/mycompany/MyClass.class
            StringBuilder builder = new StringBuilder("jar:file:");
            String cPath = file.getCanonicalPath();
            if (!cPath.startsWith(File.separator)) {
              builder.append("/");
            }
            builder.append(cPath);
            builder.append("!/");
            builder.append(name);
            addClassToMap(clazz, new URL(builder.toString()));
          }
        }
      } else if (file.exists() && file.isDirectory()) {
        // recurse into the directory looking for any .class files.
        int index = file.getCanonicalPath().length() + 1;
        findClasses(file, index);
      }
    }
  }

  // start is the index in the complete directory path where the package
  // directories etc.
  // begin.
  private void findClasses(File directory, int start) throws IOException {
    for (File file : directory.listFiles()) {
      // System.out.println("file = " + file);
      if (file.isDirectory())
        findClasses(file, start);
      else if (file.getName().endsWith(".class")) {
        String clazz = file.getCanonicalPath().substring(start);
        clazz = clazz.substring(0, clazz.length() - 6);
        clazz = clazz.replace(File.separator, ".");
        addClassToMap(clazz, file.toURI().toURL());
      }
    }
  }

  private boolean containsField(List<FieldInfo> fields, String name) {
    for (FieldInfo field : fields) {
      if (field.getName().equals(name))
        return true;
    }
    return false;
  }

  /**
   * Gets an iteratable over the WatcheeData found and created by this
   * WatcheeDataFinder. WatcheeData is ordered according to inheritence
   * hierarchy such that parent classes will appear before their children.
   * 
   * @return an iteratable over the WatcheeData found and created by this
   *         WatcheeDataFinder.
   */
  public Iterable<WatcheeData> data() {
    NodeIterator iter = new NodeIterator(nodeMap.values());
    return iter;
  }

  // look for @Watch annotations on all the classes
  // in the classMap
  private void findWatches(Filter<String> filter) throws IOException, ClassNotFoundException {

    // we have our pool of potential watchers and locations, so
    // examine them for @Watch and use addClassToFind to add them
    // to the watchees to find.
    ClassPool pool = ClassPool.getDefault();
    for (ClassData data : classMap.values()) {
      if (data.path != null && !instrumentor.isInstrumented(data.name)
          && (filter.evaluate(data.name) || filter.evaluate(data.path.toExternalForm()))) {
        BufferedInputStream stream = new BufferedInputStream(data.path.openStream());
        CtClass ctClass = pool.makeClass(stream);
        stream.close();

        CtMethod[] methods = ctClass.getMethods();
        for (CtMethod method : methods) {
          MethodInfo info = method.getMethodInfo();
          AnnotationsAttribute attr = (AnnotationsAttribute) info
              .getAttribute(AnnotationsAttribute.visibleTag);
          if (attr != null) {
            Annotation an = attr.getAnnotation("repast.simphony.engine.watcher.Watch");
            if (an != null) {
              String watcheeName = ((StringMemberValue) an.getMemberValue("watcheeClassName"))
                  .getValue();
              String fieldNames = ((StringMemberValue) an.getMemberValue("watcheeFieldNames"))
                  .getValue();
              StringTokenizer tok = new StringTokenizer(fieldNames, ",");

              while (tok.hasMoreTokens()) {
                addClassToFind(watcheeName, tok.nextToken().trim());
              }

            } else {
              an = attr.getAnnotation("repast.simphony.engine.watcher.WatchItems");
              if (an != null) {
                MemberValue[] watches = ((ArrayMemberValue) an.getMemberValue("watches"))
                    .getValue();
                for (MemberValue value : watches) {
                  Annotation watch = ((AnnotationMemberValue) value).getValue();
                  String watcheeName = ((StringMemberValue) watch
                      .getMemberValue("watcheeClassName")).getValue();
                  String fieldNames = ((StringMemberValue) watch
                      .getMemberValue("watcheeFieldNames")).getValue();
                  StringTokenizer tok = new StringTokenizer(fieldNames, ",");
                  while (tok.hasMoreTokens()) {
                    addClassToFind(watcheeName, tok.nextToken().trim());
                  }
                }
              }
            }
          }
        }
      }
    } // ugh so many brackets, this offends.
  }


  private static class NodeIterator implements Iterator<WatcheeData>, Iterable<WatcheeData> {

    private List<WatcheeDataNode> roots = new ArrayList<WatcheeDataNode>();
    private Iterator<WatcheeDataNode> rootIter;
    private Queue<WatcheeDataNode> queue = new LinkedList<WatcheeDataNode>();
    private WatcheeDataNode next;

    public NodeIterator(Collection<WatcheeDataNode> nodes) {
      for (WatcheeDataNode node : nodes) {
        if (node.isRoot())
          roots.add(node);
      }

      rootIter = roots.iterator();
      if (rootIter.hasNext())
        next = rootIter.next();
    }

    public boolean hasNext() {
      return next != null;
    }

    private void updateNext() {
      queue.addAll(next.children());
      if (queue.size() > 0)
        next = queue.poll();
      else if (rootIter.hasNext())
        next = rootIter.next();
      else
        next = null;
    }

    public WatcheeData next() {
      if (next == null)
        throw new NoSuchElementException();
      WatcheeData tmp = next.data;
      updateNext();
      return tmp;
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

    public Iterator<WatcheeData> iterator() {
      return this;
    }
  }
}
