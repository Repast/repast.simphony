package repast.simphony.gis.styleEditor;

import org.geotools.feature.AttributeType;
import org.geotools.feature.AttributeTypeFactory;
import org.geotools.feature.FeatureType;
import org.geotools.feature.FeatureTypeBuilder;
import org.geotools.filter.ExpressionBuilder;
import org.geotools.filter.Filter;
import org.geotools.filter.parser.ParseException;

public class ExpressionTest {
	public static void main(String[] args) throws Exception {
		AttributeType type = AttributeTypeFactory.newAttributeType("name",
				String.class);
		FeatureType ft = FeatureTypeBuilder.newFeatureType(
				new AttributeType[] { type }, "myfeature");
		ExpressionBuilder builder = new ExpressionBuilder();
		String exp = "age < 4";
		try {
			Filter expr = (Filter) builder.parser(ft, exp);
			System.out.println(expr + " = " + expr.contains(null));
		} catch (ParseException e) {
			System.out.println(e.getCause().getMessage());
		}
	}
}
