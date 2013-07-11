package repast.simphony.relogo.ide;

import org.eclipse.jdt.internal.ui.filters.LibraryFilter;
import org.eclipse.jdt.internal.ui.filters.NamePatternFilter;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ReLogoFilter extends ViewerFilter {
	
	public static boolean isInReLogoPerspective = false;

	String[] filterStrings = {"*.rs","*.log","**.context","**.style","**.factories","Repast.settings","MessageCenter.log4j.properties","license.txt","model_description.txt","launchers","batch", "builders", "docs", "freezedried_data", "icons", "installer", "integration", "launchers", "lib", "misc", "repast-licenses", "output", "transferFiles","Groovy DSL Support","src-gen"};

	NamePatternFilter npf = new NamePatternFilter();
	LibraryFilter lf = new LibraryFilter();
	public ReLogoFilter() {
		super();
		npf.setPatterns(filterStrings);
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return (isInReLogoPerspective ? npf.select(viewer, parentElement, element) && lf.select(viewer, parentElement, element) : true);
	}

}
