package repast.simphony.visualization.editedStyle;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XStream11XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.jscience.physics.amount.Amount;
import repast.simphony.scenario.ScenarioUtils;
import simphony.util.messages.MessageCenter;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;

/**
 * @author Eric tatara
 */
public class EditedStyleUtils {

	/**
	 * @return
	 */
	public static String getStyleDirName() {
		return ScenarioUtils.getScenarioDir() + File.separator
		+ "styles";
	}

	public static EditedStyleData<Object> getStyle(String userStyleFile) {
		EditedStyleData<Object> style = null;

		XStream stream = new XStream(new XppDriver(new XStream11XmlFriendlyReplacer())) {
			protected boolean useXStream11XmlFriendlyMapper() {
				return true;
			}
		};
		stream.setClassLoader(EditedStyleData.class.getClassLoader());

		if (userStyleFile == null)
			return null;

		try {
			FileReader file = new FileReader(new File(getStyleDirName(), userStyleFile));
			style = (EditedStyleData<Object>) stream.fromXML(file);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return style;
	}

	public static EditedValueLayerStyleData getValueLayerStyle(String userStyleFile) {
		EditedValueLayerStyleData style = null;

		XStream stream = new XStream(new XppDriver(new XStream11XmlFriendlyReplacer())) {
			protected boolean useXStream11XmlFriendlyMapper() {
				return true;
			}
		};
		stream.setClassLoader(EditedValueLayerStyleData.class.getClassLoader());

		if (userStyleFile == null)
			return null;

		try {
			FileReader file = new FileReader(new File(getStyleDirName(), userStyleFile));
			style = (EditedValueLayerStyleData) stream.fromXML(file);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return style;
	}

	public static EditedEdgeStyleData<Object> getEdgeStyle(String userStyleFile) {
		EditedEdgeStyleData<Object> style = null;

		XStream stream = new XStream(new XppDriver(new XStream11XmlFriendlyReplacer())) {
			protected boolean useXStream11XmlFriendlyMapper() {
				return true;
			}
		};
		stream.setClassLoader(EditedStyleData.class.getClassLoader());

		if (userStyleFile == null)
			return null;

		try {
			FileReader file = new FileReader(new File(getStyleDirName(), userStyleFile));
			style = (EditedEdgeStyleData<Object>) stream.fromXML(file);
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return style;
	}

	public static float getSize(SizeableStyleData style, Object object) {
		// Size is based on either user-specified constant value or method
		float size = style.getSize();

		if (style.getSizeMethodName() == null)
			return size;

		size = getMethodFloat(style.getSizeMethodName(), object);

		// Size scaling
		float sizeMax = style.getSizeMax();
		float sizeMin = style.getSizeMin();
		float sizeScale = style.getSizeScale();

		float div = sizeMax - sizeMin;

		if (div <= 0)
			div = 1;

		size = ((size - sizeMin) / div) * sizeScale;

		return size;
	}

	public static String getLabel(EditedStyleData style, Object object) {
		String label = style.getLabel();

		if (style.getLabelMethod() == null)
			return label;

		String methodName = style.getLabelMethod();
		try {
			Method method = object.getClass().getMethod(methodName);

			if (method.getReturnType().equals(String.class))
				return (String) method.invoke(object);

			else if (method.getReturnType().equals(Amount.class))
				return ((Amount) method.invoke(object)).toString();

			else {
				label = ((Number) method.invoke(object)).toString();

				int precision = style.getLabelPrecision();

				int labelLength = label.indexOf('.') + precision + 1;

				if (labelLength < label.length())
					label = label.substring(0, labelLength);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return label;
	}

	public static float getMethodFloat(String methodName, Object object) {
		try {
			Method method = object.getClass().getMethod(methodName);

			if (method.getReturnType().equals(Amount.class)){
				Amount amnt = (Amount) method.invoke(object);
				if (amnt != null){
					return (float) amnt.getEstimatedValue();
				}
				else {
					return 0;
				}
			}

			Number nmbr = (Number) method.invoke(object);
			if (nmbr != null){
				return nmbr.floatValue();
			}
			else {
				return 0;
			}
		} catch (ClassCastException e){
			Method method;
			try {
				method = object.getClass().getMethod(methodName);
				MessageCenter.getMessageCenter(EditedStyleUtils.class).warn("Edited style tried to call " + object + "." + methodName + " and was expecting a numerical value but received a " + method.getReturnType(), (Object [])null);
			} catch (SecurityException e1) {
				e1.printStackTrace();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static Color getColor(PaintableStyleData style, Object object) {

		float red = 0;
		float green = 0;
		float blue = 0;

		if (style.getRedMethod() == null)
			red = style.getColor()[0];
		else {
			red = EditedStyleUtils.getMethodFloat(style.getRedMethod(), object);
			red = Math.max(Math.min(((red - style.getColorMin()[0]) / (style.getColorMax()[0] -
					style.getColorMin()[0])) * style.getColorScale()[0], 1), 0);
		}
		if (style.getBlueMethod() == null)
			blue = style.getColor()[2];
		else {
			blue = EditedStyleUtils.getMethodFloat(style.getBlueMethod(), object);
			blue = Math.max(Math.min(((blue - style.getColorMin()[2]) / (style.getColorMax()[2] -
					style.getColorMin()[2])) * style.getColorScale()[2], 1), 0);
		}
		if (style.getGreenMethod() == null)
			green = style.getColor()[1];
		else {
			green = EditedStyleUtils.getMethodFloat(style.getGreenMethod(), object);
			green = Math.max(Math.min(((green - style.getColorMin()[1]) / (style.getColorMax()[1] -
					style.getColorMin()[1])) * style.getColorScale()[1], 1), 0);
		}

		return new Color(red, green, blue);
	}

	public static Color getValueLayerColor(EditedValueLayerStyleData style, double val) {
		float[] rgb = new float[3];

		for (int i=0; i<3; i++){
			if (style.getColorValue()[i]){
				rgb[i] = (float)val;;
				rgb[i] = Math.max(Math.min(((rgb[i] - style.getColorMin()[i]) / (style.getColorMax()[i] -
						style.getColorMin()[i])) * style.getColorScale()[i], 1), 0);
			}
			else
				rgb[i] = style.getColor()[i];
		}

		return new Color(rgb[0],rgb[1],rgb[2]);
	}
}