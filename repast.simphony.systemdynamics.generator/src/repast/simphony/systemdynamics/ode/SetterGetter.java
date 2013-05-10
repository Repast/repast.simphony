package repast.simphony.systemdynamics.ode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.WordUtils;

import repast.simphony.systemdynamics.sdmodel.VariableType;
import repast.simphony.systemdynamics.translator.Equation;
import repast.simphony.systemdynamics.translator.InformationManagers;
import repast.simphony.systemdynamics.translator.NativeDataTypeManager;

public class SetterGetter {

	public static void generate(ODECodeGenerator odeCG, BufferedWriter code,
			ODEAnalyzer analyzer) {
		
		NativeDataTypeManager ndtm = InformationManagers.getInstance().getNativeDataTypeManager();
		for (Equation eqn : analyzer.getAuxiliariesForConstructor()) {
			System.out.println("SetterGetter "+eqn.getLhs()+" "+eqn.isOneTime()+" "+eqn.getVariableType().toString());
//			if (!eqn.isOneTime())
//				continue;
//			// only Auxiliary variables will have getters and setters
//			if (!eqn.getVariableType().equals(VariableType.AUXILIARY))
//				continue;

			generateGetter(code, ndtm.makeLegal(eqn.getLhs()));
			generateSetter(code, ndtm.makeLegal(eqn.getLhs()));
		}
	}

	public static void generateGetter(BufferedWriter code, String var) {
		String captitalized = WordUtils.capitalize(var);
		try {
			code.append("\tpublic double get" + captitalized + "() {\n");
			code.append("\t\treturn " + var + ";\n");
			code.append("\t}\n\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateSetter(BufferedWriter code, String var) {
		String captitalized = WordUtils.capitalize(var);
		try {
			code.append("\tpublic void set" + captitalized + "(double value) {\n");
			code.append("\t\t" + var + " = value;\n");
			code.append("\t}\n\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
