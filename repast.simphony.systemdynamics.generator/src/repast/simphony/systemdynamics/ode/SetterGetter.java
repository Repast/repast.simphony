package repast.simphony.systemdynamics.ode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang.WordUtils;

import repast.simphony.systemdynamics.sdmodel.VariableType;
import repast.simphony.systemdynamics.translator.Equation;

public class SetterGetter {
	
	private Map<String, Equation> equations;
	
	public SetterGetter(Map<String, Equation> equations) {
		this.equations = equations;
	}
	
	public static void generate(BufferedWriter code, ODEAnalyzer analyzer) {
		
	}

	
	public void generate(BufferedWriter code) {
		
		for (String var : equations.keySet()) {
			Equation eqn = equations.get(var);
			// Auxiliary vars that reference other vars will be set via inline code
			if (!eqn.isOneTime())
				continue;
			// only Auxiliary variables will have getters and setters
			if (!eqn.getVariableType().equals(VariableType.AUXILIARY))
				continue;
			
			generateGetter(code, var);
			generateSetter(code, var);
		}
		
	}
	
	public void generateGetter(BufferedWriter code, String var) {
		String captitalized = WordUtils.capitalize(var);
		try {
			code.append("public double get"+captitalized+"() {\n");
			code.append("\treturn "+var+";\n");
			code.append("}\n\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void generateSetter(BufferedWriter code, String var) {
		String captitalized = WordUtils.capitalize(var);
		try {
			code.append("public void set"+captitalized+"(double value) {\n");
			code.append("\t"+var+" = value;\n");
			code.append("}\n\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
