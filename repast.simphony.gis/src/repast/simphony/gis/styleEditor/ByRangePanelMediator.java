package repast.simphony.gis.styleEditor;

import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.TableModel;

import org.geotools.brewer.color.BrewerPalette;
import org.geotools.brewer.color.ColorBrewer;
import org.geotools.brewer.color.StyleGenerator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.AndImpl;
import org.geotools.filter.function.Classifier;
import org.geotools.filter.function.RangedClassifier;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.Graphic;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.Mark;
import org.geotools.styling.PointSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.PropertyName;

import repast.simphony.gis.GeometryUtil;

/**
 * Mediates between the different components in ByRangePanel.
 *
 * @author Nick Collier
 * @author Eric Tatara
 */
public class ByRangePanelMediator {

	private ColorBrewer brewer = ColorBrewer.instance();

	private DefaultComboBoxModel paletteModel = new DefaultComboBoxModel();
	private DefaultComboBoxModel cTypeModel = new DefaultComboBoxModel();
	private DefaultComboBoxModel attributeModel = new DefaultComboBoxModel();
	private DefaultComboBoxModel markModel = new DefaultComboBoxModel(new String[]{
					"circle", "cross", "star", "square", "triangle"});
	private FeatureType featureType;
	 static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
	private FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();
	private int classesCount;
	private SampleStyleTableModel tableModel;
	private FeatureTypeStyle fts;
	private boolean ignorePaletteChange = false;
	private boolean ignoreAttributeChange = false;
	private Rule defaultRule;
	private GeometryUtil.GeometryType type;
  private double min = 0, max = 10;

  public ByRangePanelMediator(FeatureType featureType, Rule rule, PreviewLabel preview) {
  	this.featureType = featureType;
		
  	tableModel = new SampleStyleTableModel(preview);
  	
  	// TODO Geotools add other range types in new GT 8 API?
  	cTypeModel.addElement(new IntervalItemType());
		//cTypeModel.addElement(new QuantileItemType());
		
		type = GeometryUtil.findGeometryType(featureType.getGeometryDescriptor().getType().getBinding());
			
		DuplicatingStyleVisitor dsv = new DuplicatingStyleVisitor(
							CommonFactoryFinder.getStyleFactory(), CommonFactoryFinder.getFilterFactory2());
		dsv.visit(rule);
		defaultRule = (Rule) dsv.getCopy();
		defaultRule.setTitle("<Default>");
		defaultRule.setName("Default");
	}

  public double getMin() {
    return min;
  }

  public double getMax() {
    return max;
  }

  public Rule getDefaultRule() {
		return defaultRule;
	}

	public void setDefaultRule(Rule defaultRule) {
		this.defaultRule = defaultRule;
		recreateStyle();
	}

	public ComboBoxModel getPaletteModel() {
		return paletteModel;
	}

	public DefaultComboBoxModel getClassifcationTypeModel() {
		return cTypeModel;
	}

	public DefaultComboBoxModel getAttributeModel() {
		return attributeModel;
	}

	public TableModel getPreviewTableModel() {
		return tableModel;
	}

	public ComboBoxModel getMarkModel() {
		return markModel;
	}

	/**
	 * Called whenever the classes count changes
	 *
	 * @param classes the new number of classes
	 */
	public void classesChanged(int classes) {
		// TODO Geotools restrict to max 11 classes
		ignorePaletteChange = true;
		this.classesCount = classes;
		paletteModel.removeAllElements();
		BrewerPalette[] pals = brewer.getPalettes(ColorBrewer.ALL, classes + 1);
		for (BrewerPalette pal : pals) {
			Palette palette = new Palette(pal.getColors(classes + 1), pal.getDescription());
			paletteModel.addElement(palette);
		}
		recreateStyle();
		ignorePaletteChange = false;
	}

	public void paletteChanged() {
		if (!ignorePaletteChange) recreateStyle();
	}

  public void minChanged(double min) {
    if (min > max) {
      this.min = max;
      this.max = min;
    } else this.min = min;
    recreateStyle();
  }

  public void maxChanged(double max) {
    if (max < min) {
      this.max = min;
      this.min = max;
    } else this.max = max;
    recreateStyle();
  }

  public void typeChanged() {
		recreateStyle();
	}

	public void attributeChanged() {
		if (!ignoreAttributeChange) recreateStyle();
	}

	private void recreateStyle() {
		if (attributeModel.getSelectedItem() != null) {
			String attribute = attributeModel.getSelectedItem().toString();
			PropertyName pn = filterFactory.property(attribute);

			Classifier classifier = ((ClassTypeItem) cTypeModel.getSelectedItem()).createFunction(classesCount,
					min, max, attribute);

			Palette palette = (Palette) paletteModel.getSelectedItem();

			fts = StyleGenerator.createFeatureTypeStyle(classifier, pn, 
					palette.getColors(), "Generated FeatureTypeStyle", 
					featureType.getGeometryDescriptor(),
					StyleGenerator.ELSEMODE_INCLUDEASMIN, 0.95, null);

			addSymbolizers(fts);

			tableModel.initStyle(fts, palette);
		}
	}

	public void replaceRule(Rule oldRule, Rule newRule) {
		for (Rule rule : fts.rules()){
			if (rule.equals(oldRule)){
				rule = newRule;
				break;
			}
		}
	}

	private void addSymbolizers(FeatureTypeStyle style) {
		StyleBuilder builder = new StyleBuilder();
		for (Rule rule : style.rules()) {
			if (type == GeometryUtil.GeometryType.POINT) {
				PointSymbolizer sym = (PointSymbolizer) rule.getSymbolizers()[0];
				Graphic ruleGraphic = ((PointSymbolizer)	defaultRule.getSymbolizers()[0]).getGraphic(); 
				Mark ruleMark = (Mark)ruleGraphic.graphicalSymbols().get(0);		
				Fill fill = ruleMark.getFill();
				Mark mark = (Mark)sym.getGraphic().graphicalSymbols().get(0);
				Fill newFill = builder.createFill(mark.getFill().getColor(), fill.getOpacity());
				
				Stroke stroke = ruleMark.getStroke();
				Stroke newStroke = builder.createStroke(stroke.getColor(), stroke.getWidth(), stroke.getOpacity());
				Mark newMark = builder.createMark(ruleMark.getWellKnownName(), newFill, newStroke);
				
			  Graphic gr = styleFactory.createDefaultGraphic();
			  gr.graphicalSymbols().clear();
        gr.graphicalSymbols().add(newMark);
        gr.setSize(filterFactory.literal(ruleGraphic.getSize()));								
				sym.setGraphic(gr);
			} 
			else if (type == GeometryUtil.GeometryType.LINE) {
				LineSymbolizer lineSym = (LineSymbolizer) rule.getSymbolizers()[0];
				Stroke ruleStroke = ((LineSymbolizer) defaultRule.getSymbolizers()[0]).getStroke();
				Stroke stroke = builder.createStroke(lineSym.getStroke().getColor(), ruleStroke.getWidth(),
								ruleStroke.getOpacity());
				lineSym.setStroke(stroke);
			} 
			else {
				PolygonSymbolizer polySym = (PolygonSymbolizer) rule.getSymbolizers()[0];
				Fill fill = ((PolygonSymbolizer) defaultRule.getSymbolizers()[0]).getFill();
				Fill newFill = builder.createFill(polySym.getFill().getColor(), fill.getOpacity());
				Stroke stroke = ((PolygonSymbolizer) defaultRule.getSymbolizers()[0]).getStroke();
				Stroke newStroke = builder.createStroke(stroke.getColor(), stroke.getWidth(), stroke.getOpacity());
				polySym.setFill(newFill);
				polySym.setStroke(newStroke);
			}
		}
	}

	public FeatureTypeStyle getFeatureTypeStyle() {
		return fts;
	}

	public void init(List<Rule> rules, String attributeName) {
		ignoreAttributeChange = true;
		attributeModel.setSelectedItem(attributeName);
		defaultRule = rules.get(rules.size() - 1);
		
		// Get the min and max range values by looking at the filter rhs values for
		// the first and last rules.  Currently this is done by parsing the rule
		// title which is either of the form "a - b" or ""a..b" where a is the rule
		// min and b is the rule max.  A more proper way to check these values
		// might be to get the rule->filter->children and check the value of the rhs
		// LiteralExpression.
		if (rules.size() > 1) {
      tableModel.init(rules.subList(1, rules.size()));
      String title = rules.get(0).getDescription().getTitle().toString();
      int idx = -1;
      try {
      	idx = title.indexOf(".");
      	if (idx == -1)
      		idx = title.indexOf(" ");
      	if (idx != -1)
          min = Double.valueOf(title.substring(0, idx));
        
      	idx = -1;
      	// rules.size() - 2 because we want the last range, but the very last
      	// rule is "ELSE" - not a range. 
      	title = rules.get(rules.size() - 2).getDescription().getTitle().toString();
        
      	idx = title.lastIndexOf(".");
      	if (idx == -1)
      		idx = title.lastIndexOf(" ");
      	if (idx != -1)
          max = Double.valueOf(title.substring(idx + 1, title.length()));

      } catch (NumberFormatException ex) {}
    }
		ignoreAttributeChange = false;
	}

	private abstract class ClassTypeItem {
		private String label;

		public ClassTypeItem(String label) {
			this.label = label;
		}

		public abstract Classifier createFunction(int numClasses, FeatureCollection collection, String attribute);
		public abstract Classifier createFunction(int numClasses, double min, double max, String attribute);

		public String toString() {
			return label;
		}
	}


  // Quantile can't be used when we don't have
  // anything in the feature collection to style
  // to begin with.
//  private class QuantileItemType extends ClassTypeItem {
//
//		public QuantileItemType() {
//			super("Quantile");
//		}
//
//		public Classifier createFunction(int numClasses, FeatureCollection collection, String attribute) {
//			List<Number> vals = new ArrayList<Number>();
//			for (SimpleFeature feature : (Iterable<SimpleFeature>) collection) {
//				Number num = (Number) feature.getAttribute(attribute);
//				if (num != null) {
//					vals.add(num);
//				}
//			}
//
//			int interval = (int) Math.ceil(((float) vals.size()) / numClasses);
//
//
//			CustomClassifierFunction func = new CustomClassifierFunction();
//			for (int i = 0; i < numClasses; i++) {
//				int start = interval * i;
//				int end = interval * (i + 1);
//				if (end > vals.size() - 1) end = vals.size() - 1;
//				func.setRangedValues(i, vals.get(start), vals.get(end));
//			}
//
//			return func;
//		}
//
//    public Classifier createFunction(int numClasses, double min, double max, String attribute) {
//      return null;
//    }
//  }



	private class IntervalItemType extends ClassTypeItem {

		public IntervalItemType() {
			super("Equal Interval");
		}

    public Classifier createFunction(int numClasses, double min, double max, String attribute) {
			double interval = (max - min) / numClasses;
			Double [] mins = new Double[numClasses];
			Double [] maxs = new Double[numClasses];
			for (int i = 0; i < numClasses; i++) {
				mins[i] = min + (interval * i);
				maxs[i] = min + (interval * (i + 1));
			}
			
			// requires Comparable Double.class, not double primive
			RangedClassifier func = new RangedClassifier(mins,maxs);

			return func;
		}

    public Classifier createFunction(int numClasses, FeatureCollection collection, String attribute) {
			double min = Double.POSITIVE_INFINITY;
			double max = Double.NEGATIVE_INFINITY;

			for (SimpleFeature feature : (Iterable<SimpleFeature>) collection) {
				Number num = (Number) feature.getAttribute(attribute);
				if (num != null) {
					double val = num.doubleValue();
					min = Math.min(min, val);
					max = Math.max(max, val);
				}
			}

 			Double [] mins = new Double[numClasses];
			Double [] maxs = new Double[numClasses];
			
			double interval = (max - min) / numClasses;
			for (int i = 0; i < numClasses; i++) {
				mins[i] = min + (interval * i);
				maxs[i] = min + (interval * (i + 1));
			}

			// requires Comparable Double.class, not double primive
			RangedClassifier func = new RangedClassifier(mins,maxs);
			
			return func;
		}
	}
}