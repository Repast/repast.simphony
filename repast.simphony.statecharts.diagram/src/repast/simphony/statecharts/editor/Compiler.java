/**
 * 
 */
package repast.simphony.statecharts.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.compiler.tool.EclipseCompiler;
import org.eclipse.jdt.internal.core.JavaModel;

import repast.simphony.statecharts.part.StatechartDiagramEditorPlugin;

/**
 * Compiles in memory java source.
 * 
 * @author Nick Collier
 */
public enum Compiler {

  INSTANCE;

  Map<IPath, CompilerTask> map = new HashMap<>();

  /**
   * Gets the CompilerTask for the specified project. If the task doesn't exist
   * it will be created.
   * 
   * @param project
   * @return the CompilerTask for the specified project.
   */
  public CompilerTask getCompilerTask(IJavaProject project) {
    CompilerTask task = map.get(project.getProject().getLocation());
    if (task == null) {
      task = new CompilerTask(project);
      map.put(project.getProject().getLocation(), task);
    }
    return task;
  }

  public static class CompilerTask {

    private JavaCompiler compiler;
    private Set<File> classpath = new HashSet<>();
    private List<String> options = new ArrayList<>();

    public CompilerTask(IJavaProject project) {
      if (compiler == null) {
        initClasspath(project);
        compiler = new EclipseCompiler();
      }
    }

    /**
     * Compiles the specified source with the specified name. Compiler
     * diagnostics will reported to the specified DiagnosticListener.
     * 
     * @param name
     * @param source
     * @param listener
     * @return true if the compilation succeeded, otherwise false.
     */
    public boolean compile(String name, String source, DiagnosticListener<JavaFileObject> listener) {
      StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
      SpecialJavaFileManager fileManager = new SpecialJavaFileManager(manager);
      try {
        fileManager.setLocation(StandardLocation.CLASS_PATH, classpath);
        // swallow the exceptoin because we don't throw in setLocation,
        // just has it as part of the API
      } catch (IOException e) {
      }

      List<MemorySource> compilationUnits = Arrays.asList(new MemorySource(name, source));
      JavaCompiler.CompilationTask compile = compiler.getTask(new StringWriter(), fileManager,
          listener, options, null, compilationUnits);
      return compile.call();
    }

    private void addCPESource(IJavaProject project, IClasspathEntry entry)
        throws JavaModelException {
      IPath classDirPath = entry.getOutputLocation();
      if (classDirPath == null) {
        classDirPath = project.getOutputLocation();
      }

      classpath.add(new File(project.getProject().getLocation().removeLastSegments(1)
          .toPortableString()
          + classDirPath.toPortableString()));
    }

    private void addLibrary(IJavaProject project, IClasspathEntry entry) {
      IPath path = entry.getPath();
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      if (root.exists(path)) {
        File file = new File(root.getLocation().toPortableString() + path.toPortableString());
        classpath.add(file);
      } else {
        classpath.add(new File(path.toPortableString()));
      }
    }

    private void addCPEContainer(IJavaProject project, IClasspathEntry entry)
        throws JavaModelException {
      IClasspathContainer container = JavaCore.getClasspathContainer(entry.getPath(), project);
      if (container != null) {
        for (IClasspathEntry child : container.getClasspathEntries()) {
          IPath path = child.getPath();
          Object target = JavaModel.getTarget(path, true);
          if (target instanceof File) {
            classpath.add(new File(path.toPortableString()));
          }
        }
      }
    }

    private void initClasspath(IJavaProject project) {

      try {
        for (IClasspathEntry entry : project.getRawClasspath()) {
          if (entry.getContentKind() == IPackageFragmentRoot.K_SOURCE) {
            if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
              addCPESource(project, entry);

            } else if (entry.getEntryKind() == IClasspathEntry.CPE_CONTAINER
                && !entry.toString().startsWith("org.eclipse.jdt.launching.JRE_CONTAINER")) {
              addCPEContainer(project, entry);

            } else if (entry.getEntryKind() == IClasspathEntry.CPE_PROJECT) {
              IResource resource = ResourcesPlugin.getWorkspace().getRoot()
                  .findMember(entry.getPath());
              if (resource != null && resource.getType() == IResource.PROJECT) {
                if (resource.exists()) {
                  initClasspath((IJavaProject) JavaCore.create(resource));
                }
              }
            }
          }

          if (entry.getEntryKind() == IClasspathEntry.CPE_LIBRARY
              && entry.getContentKind() == IPackageFragmentRoot.K_BINARY) {
            addLibrary(project, entry);
          }
        }

      } catch (JavaModelException ex) {
        StatechartDiagramEditorPlugin.getInstance().logError(
            "Error while creating compiler classpath", ex);
      }
    }

    /**
     * Creates a source JavaFileObject from an in memory String.
     * 
     * @author Nick Collier
     */
    static class MemorySource extends SimpleJavaFileObject {
      private String src;

      public MemorySource(String name, String src) {
        super(URI.create("file:///" + name + ".java"), Kind.SOURCE);
        this.src = src;
      }

      public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return src;
      }

      public OutputStream openOutputStream() {
        throw new IllegalStateException();
      }

      public InputStream openInputStream() {
        return new ByteArrayInputStream(src.getBytes());
      }
    }

    /**
     * Allows the compiled class to be written to memory rather than disk.
     * 
     * @author Nick Collier
     */
    static class MemoryByteCode extends SimpleJavaFileObject {
      private ByteArrayOutputStream baos;

      public MemoryByteCode(String name) {
        super(URI.create("byte:///" + name + ".class"), Kind.CLASS);
      }

      public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        throw new IllegalStateException();
      }

      public OutputStream openOutputStream() {
        baos = new ByteArrayOutputStream();
        return baos;
      }

      public InputStream openInputStream() {
        throw new IllegalStateException();
      }

      public byte[] getBytes() {
        return baos.toByteArray();
      }
    }

    // we have to implement StandardJavaFileManager because of a bug in the
    // Eclipse compiler.
    // It will ignored a ForwardingJavaFileManager.
    static class SpecialJavaFileManager extends ForwardingJavaFileManager<StandardJavaFileManager>
        implements StandardJavaFileManager {

      private Map<Location, Iterable<? extends File>> locations = new HashMap<>();

      public SpecialJavaFileManager(StandardJavaFileManager sjfm) {
        super(sjfm);
      }

      public JavaFileObject getJavaFileForOutput(Location location, String name,
          JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        MemoryByteCode mbc = new MemoryByteCode(name);
        return mbc;
      }

      @Override
      public Iterable<? extends JavaFileObject> getJavaFileObjectsFromFiles(
          Iterable<? extends File> files) {
        return null;
      }

      @Override
      public Iterable<? extends JavaFileObject> getJavaFileObjects(File... files) {
        return null;
      }

      @Override
      public Iterable<? extends JavaFileObject> getJavaFileObjectsFromStrings(Iterable<String> names) {
        return null;
      }

      @Override
      public Iterable<? extends JavaFileObject> getJavaFileObjects(String... names) {
        return null;
      }

      @Override
      public void setLocation(Location location, Iterable<? extends File> path) throws IOException {
        locations.put(location, path);
      }

      @Override
      public Iterable<? extends File> getLocation(Location location) {
        return locations.get(location);
      }
    }
  }
}
