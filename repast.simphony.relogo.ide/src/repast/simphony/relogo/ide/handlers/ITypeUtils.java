package repast.simphony.relogo.ide.handlers;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;

public class ITypeUtils {
	@SuppressWarnings("restriction")
	protected static boolean extendsClass(IType type, String clazzName, IProgressMonitor monitor)
			throws JavaModelException {
		IType[] types = JavaModelUtil.getAllSuperTypes(type, monitor);
		for (IType t : types) {
			if (t.getFullyQualifiedName().equals(clazzName))
				return true;
		}
		return false;
	}

}
