package repast.simphony.gis.styleEditor;

import org.geotools.brewer.color.BrewerPalette;
import org.geotools.brewer.color.ColorBrewer;
import org.geotools.brewer.color.StyleGenerator;
import org.geotools.data.FeatureSource;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureCollection;
import org.geotools.filter.AttributeExpression;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.function.ClassificationFunction;
import org.geotools.filter.function.CustomClassifierFunction;
import org.geotools.styling.*;
import org.geotools.styling.visitor.DuplicatorStyleVisitor;
import repast.simphony.gis.GeometryUtil;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mediates between the different components in ByRangePanel.
 *
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2007/04/18 19:25:53 $
 */
public class ByRangePanelMediator {

	private ColorBrewer brewer = ColorBrewer.instance();

	private DefaultComboBoxModel paletteModel = new DefaultComboBoxModel();
	private DefaultComboBoxModel cTypeModel = new DefaultComboBoxModel();
	private DefaultComboBoxModel attributeModel = new DefaultComboBoxModel();
	private DefaultComboBoxModel markModel = new DefaultComboBoxModel(new String[]{
					"circle", "cross", "star", "square", "triangle"
	});
	private FeatureSource source;
	private FilterFactory filterFactory = FilterFactoryFinder.createFilterFactory();
	private int classesCount;
	private SampleStyleTableModel tableModel = new SampleStyleTableModel();
	private FeatureTypeStyle fts;
	private boolean ignorePaletteChange = false;
	private boolean ignoreAttributeChange = false;
	private Rule defaultRule;
	private GeometryUtil.GeometryType type;
  private double min = 0, max = 10;

  public ByRangePanelMediator(FeatureSource source, Rule rule) {
		this.source = source;
		cTypeModel.addElement(new IntervalItemType());
		//cTypeModel.addElement(new QuantileItemType());

		try {
			Feature feature = (Feature) source.getFeatures().iterator().next();
			type = GeometryUtil.findGeometryType(feature);
			DuplicatorStyleVisitor dsv = new DuplicatorStyleVisitor(
							StyleFactoryFinder.createStyleFactory(), FilterFactoryFinder
							.createFilterFactory());
			dsv.visit(rule);
			defaultRule = (Rule) dsv.getCopy();
			defaultRule.setTitle("<Default>");
			defaultRule.setName("Default");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
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
		try {
			if (attributeModel.getSelectedItem() != null) {
				String attribute = attributeModel.getSelectedItem().toString();
				AttributeExpression att = filterFactory.createAttributeExpression(attribute);
				ClassificationFunction function = ((ClassTypeItem) cTypeModel.getSelectedItem()).createFunction(classesCount,
								min, max, attribute);
				function.setCollection(source.getFeatures());
				function.setNumberOfClasses(classesCount);
				function.setExpression(att);
				Palette palette = (Palette) paletteModel.getSelectedItem();
				StyleGenerator gen = new StyleGenerator(palette.getColors(), function, "fts");
				gen.setElseMode(StyleGenerator.ELSEMODE_INCLUDEASMIN);
				fts = gen.createFeatureTypeStyle(source.getSchema().getDefaultGeometry());
				addSymbolizers(fts);
				tableModel.initStyle(fts, (Feature) source.getFeatures().iterator().next());
			}

		} catch (IOException ex) {

		}
	}

	public void replaceRule(Rule oldRule, Rule newRule) {
		Rule[] rules = fts.getRules();
		for (int i = 0; i < rules.length; i++) {
			if (rules[i].equals(oldRule)) {
				rules[i] = newRule;
				break;
			}
		}
	}

	private void addSymbolizers(FeatureTypeStyle style) {
		//builder.createPointSymbolizer(builder.createGraphic(null,
		//					builder.createMark("square", Color.RED)
		StyleBuilder builder = new StyleBuilder();
		for (Rule rule : style.getRules()) {
			if (type == GeometryUtil.GeometryType.POINT) {
				PointSymbolizer sym = (PointSymbolizer) rule.getSymbolizers()[0];
				Mark ruleMark = ((PointSymbolizer) defaultRule.getSymbolizers()[0]).getGraphic().getMarks()[0];
				Fill fill = ruleMark.getFill();
				Fill newFill = builder.createFill(sym.getGraphic().getMarks()[0].getFill().getColor(), fill.getOpacity());
				Stroke stroke = ruleMark.getStroke();
				Stroke newStroke = builder.createStroke(stroke.getColor(), stroke.getWidth(), stroke.getOpacity());
				Mark newMark = builder.createMark(ruleMark.getWellKnownName(), newFill, newStroke);
				newMark.setSize(ruleMark.getSize());
				sym.getGraphic().setSize(ruleMark.getSize());
				sym.getGraphic().setMarks(new Mark[]{newMark});
			} else if (type == GeometryUtil.GeometryType.LINE) {
				LineSymbolizer lineSym = (LineSymbolizer) rule.getSymbolizers()[0];
				Stroke ruleStroke = ((LineSymbolizer) defaultRule.getSymbolizers()[0]).getStroke();
				Stroke stroke = builder.createStroke(lineSym.getStroke().getColor(), ruleStroke.getWidth(),
								ruleStroke.getOpacity());
				lineSym.setStroke(stroke);
			} else {
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
		if (rules.size() > 1) {
      tableModel.init(rules.subList(1, rules.size()));
      String title = rules.get(0).getTitle();
      try {
        min = Double.valueOf(title.substring(0, title.indexOf(" ")));
        title = rules.get(rules.size() - 2).getTitle();
        max = Double.valueOf(title.substring(title.lastIndexOf(" ") + 1, title.length()));

      } catch (NumberFormatException ex) {}
    }
		ignoreAttributeChange = false;
	}

	private abstract class ClassTypeItem {
		private String label;

		public ClassTypeItem(String label) {
			this.label = label;
		}

		public abstract ClassificationFunction createFunction(int numClasses, FeatureCollection collection, String attribute);
		public abstract ClassificationFunction createFunction(int numClasses, double min, double max, String attribute);

		public String toString() {
			return label;
		}
	}


  // Quantile can't be used when we don't have
  // anything in the feature collection to style
  // to begin with.
  private class QuantileItemType extends ClassTypeItem {

		public QuantileItemType() {
			super("Quantile");
		}

		public ClassificationFunction createFunction(int numClasses, FeatureCollection collection, String attribute) {
			List<Number> vals = new ArrayList<Number>();
			for (Feature feature : (Iterable<Feature>) collection) {
				Number num = (Number) feature.getAttribute(attribute);
				if (num != null) {
					vals.add(num);
				}
			}

			int interval = (int) Math.ceil(((float) vals.size()) / numClasses);


			CustomClassifierFunction func = new CustomClassifierFunction();
			for (int i = 0; i < numClasses; i++) {
				int start = interval * i;
				int end = interval * (i + 1);
				if (end > vals.size() - 1) end = vals.size() - 1;
				func.setRangedValues(i, vals.get(start), vals.get(end));
			}

			return func;
		}

    public ClassificationFunction createFunction(int numClasses, double min, double max, String attribute) {
      return null;
    }
  }



	private class IntervalItemType extends ClassTypeItem {

		public IntervalItemType() {
			super("Equal Interval");
		}

    public ClassificationFunction createFunction(int numClasses, double min, double max, String attribute) {
			CustomClassifierFunction func = new CustomClassifierFunction();
			double interval = (max - min) / numClasses;
			for (int i = 0; i < numClasses; i++) {
				func.setRangedValues(i, min + (interval * i), min + (interval * (i + 1)));
			}

			return func;
		}




    public ClassificationFunction createFunction(int numClasses, FeatureCollection collection, String attribute) {
			double min = Double.POSITIVE_INFINITY;
			double max = Double.NEGATIVE_INFINITY;

			for (Feature feature : (Iterable<Feature>) collection) {
				Number num = (Number) feature.getAttribute(attribute);
				if (num != null) {
					double val = num.doubleValue();
					min = Math.min(min, val);
					max = Math.max(max, val);
				}
			}

			CustomClassifierFunction func = new CustomClassifierFunction();
			double interval = (max - min) / numClasses;
			for (int i = 0; i < numClasses; i++) {
				func.setRangedValues(i, min + (interval * i), min + (interval * (i + 1)));
			}

			return func;
		}
	}
}
