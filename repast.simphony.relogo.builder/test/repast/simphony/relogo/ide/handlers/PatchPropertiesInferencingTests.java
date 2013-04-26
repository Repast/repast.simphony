/**
 * 
 */

package repast.simphony.relogo.ide.handlers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;
import org.junit.Test;

/**
 * THIS HAS TO BE RUN AS A JUNIT PLUGIN-TEST AS IT USES WORKSPACE ETC.
 * 
 * @author Nick Collier
 * @author jozik
 */
public class PatchPropertiesInferencingTests {

	private static String PATH = "src/anl/relogo";

	public IJavaProject resetFolder() throws CoreException, IOException {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject project = workspace.getRoot().getProject("testy");
		if (!project.exists())
			project.create(null);
		project.open(null);

		// IJavaProject javaProj = project.

		IFolder folder = project.getFolder(PATH);
		if (folder.exists()) {
			folder.delete(true, null);
		}

		if (!folder.getParent().exists()) {
			project.getFolder("src").create(true, true, null);
			project.getFolder("src/anl").create(true, true, null);
		}
		folder.create(true, true, null);

		// From:
		// https://sdqweb.ipd.kit.edu/wiki/JDT_Tutorial:_Creating_Eclipse_Java_Projects_Programmatically
		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(description, null);

		IJavaProject javaProject = JavaCore.create(project);

		IFolder binFolder = project.getFolder("bin");
		binFolder.create(false, true, null);
		javaProject.setOutputLocation(binFolder.getFullPath(), null);

		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
		for (LibraryLocation element : locations) {
			entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
		}
		// add libs to project class path
		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);

		IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(project.getFolder("src"));
		IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
		IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
		System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
		newEntries[oldEntries.length] = JavaCore.newSourceEntry(root.getPath());
		javaProject.setRawClasspath(newEntries, null);

		return javaProject;
	}

	private void copyFile(IProject project, String fromName, String toName) throws IOException,
			CoreException {
		URL relativeURL = FileLocator.find(Platform.getBundle("repast.simphony.relogo.builder"),
				new Path(""), null);
		URL absoluteURL = FileLocator.resolve(relativeURL);
		String tempURLString = URLDecoder.decode(absoluteURL.getFile(),
				System.getProperty("file.encoding"));
		String path = new File(tempURLString).getPath();
		IPath p = new Path(path + "/test_data/" + fromName);

		File from = p.toFile();
		IFile file = project.getFile(PATH + "/" + toName);
		file.create(new FileInputStream(from), true, null);
	}

	@Test
	public void testAll() throws IOException, CoreException {
		IJavaProject javaProj = resetFolder();
		// IJavaProject javaProject = JavaCore.create(proj);
		// javaProject.open(null);
		IProject proj = javaProj.getProject();
		IFolder folder = proj.getFolder(PATH);
		copyFile(proj, "UP.g", "UserPatch.groovy");
		assertTrue(folder.getFile("UserPatch.groovy").exists());
		IResource resource = folder.getFile("UserPatch.groovy");
		ICompilationUnit cu1 = JavaCore.createCompilationUnitFrom((IFile) resource);
		IType[] types = cu1.getAllTypes();
		assertEquals(1, types.length);
		IType type = types[0];

		ReLogoBuilder.FullBuildInstrumentationInformationVisitor fbiiv = new ReLogoBuilder.FullBuildInstrumentationInformationVisitor(
				proj, null);
		List<PatchTypeFieldNameFieldTypeInformation> individualPatchFieldTypes = fbiiv
				.getIndividualPatchFieldTypes(type);
		assertTrue(individualPatchFieldTypes != null);
		int counter = 0;
		PatchTypeFieldNameFieldTypeInformation ptf = individualPatchFieldTypes.get(counter);

		// Cases 1
		// oneAvar
		assertEquals("oneAvar", ptf.fieldName);
		assertEquals("double", ptf.fieldType);
		assertEquals("getOneAvar", ptf.patchGetter);
		assertEquals("setOneAvar", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);

		// oneBvar's (negative cases)
		ptf = individualPatchFieldTypes.get(++counter);

		assertTrue(!"oneBvar".equals(ptf.fieldName));
		assertTrue(!"oneBvar2".equals(ptf.fieldName));
		assertTrue(!"oneBvar3".equals(ptf.fieldName));

		// Cases 2
		// twoDef
		assertEquals("twoDef", ptf.fieldName);
		assertEquals("java.lang.Object", ptf.fieldType);
		assertEquals("", ptf.patchGetter);
		assertEquals("", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);

		ptf = individualPatchFieldTypes.get(++counter);

		// twoDef2 (comma separated from twoDef)
		assertEquals("twoDef2", ptf.fieldName);
		assertEquals("java.lang.Object", ptf.fieldType);
		assertEquals("", ptf.patchGetter);
		assertEquals("", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);

		ptf = individualPatchFieldTypes.get(++counter);

		// twoInt
		assertEquals("twoInt", ptf.fieldName);
		assertEquals("int", ptf.fieldType);
		assertEquals("", ptf.patchGetter);
		assertEquals("", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);
		
		ptf = individualPatchFieldTypes.get(++counter);

		// twoInt2
		assertEquals("twoInt2", ptf.fieldName);
		assertEquals("int", ptf.fieldType);
		assertEquals("", ptf.patchGetter);
		assertEquals("", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);
		
		ptf = individualPatchFieldTypes.get(++counter);

		// twoInt3
		assertEquals("twoInt3", ptf.fieldName);
		assertEquals("int", ptf.fieldType);
		assertEquals("", ptf.patchGetter);
		assertEquals("", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);
		
		ptf = individualPatchFieldTypes.get(++counter);

		// twoDouble
		assertEquals("twoDouble", ptf.fieldName);
		assertEquals("double", ptf.fieldType);
		assertEquals("", ptf.patchGetter);
		assertEquals("", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);
		
		ptf = individualPatchFieldTypes.get(++counter);

		// twoDouble2
		assertEquals("twoDouble2", ptf.fieldName);
		assertEquals("double", ptf.fieldType);
		assertEquals("", ptf.patchGetter);
		assertEquals("", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);
		
		ptf = individualPatchFieldTypes.get(++counter);

		// twoBoolean
		assertEquals("twoBoolean", ptf.fieldName);
		assertEquals("boolean", ptf.fieldType);
		assertEquals("", ptf.patchGetter);
		assertEquals("", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);

		ptf = individualPatchFieldTypes.get(++counter);

		// twoBoolean2
		assertEquals("twoBoolean2", ptf.fieldName);
		assertEquals("java.lang.Boolean", ptf.fieldType);
		assertEquals("", ptf.patchGetter);
		assertEquals("", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);
		
		// Extras
		ptf = individualPatchFieldTypes.get(++counter);

		// hi (1a)
		assertEquals("hi", ptf.fieldName);
		assertEquals("double", ptf.fieldType);
		assertEquals("getHi", ptf.patchGetter);
		assertEquals("setHi", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);
		
		// Cases 3 + 4 (public accessors)
		
		ptf = individualPatchFieldTypes.get(++counter);

		// threeDouble
		assertEquals("", ptf.fieldName);
		assertEquals("double", ptf.fieldType);
		assertEquals("getThreeDouble", ptf.patchGetter);
		assertEquals("setThreeDouble", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);

		ptf = individualPatchFieldTypes.get(++counter);

		// threeBoolean
		assertEquals("", ptf.fieldName);
		assertEquals("boolean", ptf.fieldType);
		assertEquals("isThreeBoolean", ptf.patchGetter);
		assertEquals("setThreeBoolean", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);

		ptf = individualPatchFieldTypes.get(++counter);

		// fourDouble
		assertEquals("", ptf.fieldName);
		assertEquals("double", ptf.fieldType);
		assertEquals("getFourDouble", ptf.patchGetter);
		assertEquals("setFourDouble", ptf.patchSetter);
		assertEquals("anl.relogo.UserPatch", ptf.patchType);
		

		
		assertEquals(counter,individualPatchFieldTypes.size() - 1);
		
	}

}
