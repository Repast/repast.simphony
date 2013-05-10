package repast.simphony.systemdynamics.translator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class RepastSimphonyEnvironment {
    
    /*
     * This class generates all the scenario files in the .rs directory
     * as well as the context builder
     */
    
 public static void generateContextBuilder(BufferedWriter source, String objectName, Translator translator) {
	
	try {
	    
	    source.append("package "+translator.getPackageName()+";\n\n");
	    source.append("import repast.simphony.context.Context;\n");
	    source.append("import repast.simphony.dataLoader.ContextBuilder;\n");
	    source.append("import repast.simphony.engine.environment.RunEnvironment;\n");
	    
	    source.append("public class ContextBuilder"+objectName+" implements ContextBuilder<Object> {\n");
	    source.append("@Override\n");
	    source.append("public Context<Object> build(Context<Object> context) {\n");
	    source.append(objectName+" on = new "+objectName+"(\""+objectName+"\");\n");
	    source.append("on.oneTime();\n");
	    source.append("context.setId(\""+objectName+"\");\n");
	    source.append("context.add(on);\n");
	    source.append("context.add(on.getMemory());\n");
	    
	    source.append("RunEnvironment.getInstance().endAt((on.getMemory().getFINALTIME()- on.getMemory().getINITIALTIME()) / on.getMemory().getTIMESTEP());\n");
	    
	    source.append("return context;\n}\n}\n");
	    source.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
 
 public static void generateScenarioXml(BufferedWriter source, String objectName) {
     
//     <?xml version="1.0" encoding="UTF-8" ?>
//     <Scenario>
//     <repast.simphony.action.data_set context="EnergySecurity5_0" file="repast.simphony.action.data_set_0.xml" />
//     <repast.simphony.action.data_set context="EnergySecurity5_0" file="repast.simphony.action.data_set_1.xml" />
//     <repast.simphony.dataLoader.engine.ClassNameDataLoaderAction context="EnergySecurity5_0" file="repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_2.xml" />
//     </Scenario>

     try {
	 source.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
	 source.append("<Scenario>\n");
	 source.append("<repast.simphony.action.data_set context=\""+objectName+"\" file=\"repast.simphony.action.data_set_0.xml\" />\n");
	 source.append("<repast.simphony.dataLoader.engine.ClassNameDataLoaderAction context=\""+objectName+"\" " +
		 "file=\"repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_1.xml\" />\n");
	 source.append("</Scenario>\n");
	 source.close();
     } catch (IOException e) {
	 // TODO Auto-generated catch block
	 e.printStackTrace();
     }
 }
 
 public static void generateContextXml(BufferedWriter source, String objectName) {

     try {

	 source.append("<context id=\""+objectName+"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
	 		"xsi:noNamespaceSchemaLocation=\"http://repast.org/scenario/context\">\n");
	 source.append("</context>\n");


	 source.close();
     } catch (IOException e) {
	 // TODO Auto-generated catch block
	 e.printStackTrace();
     }
}
 public static void generateClassLoaderXml(BufferedWriter source, String objectName, Translator translator) {

     try {

	 source.append("<string>"+translator.getPackageName()+".ContextBuilder"+objectName+"</string>\n");

	 source.close();
     } catch (IOException e) {
	 // TODO Auto-generated catch block
	 e.printStackTrace();
     }
}
 
 public static void generateParametersXml(BufferedWriter source, String objectName, Translator translator, Map<String, String> intialValues) {

     try {

	 source.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
	 source.append("<parameters>\n");
	 source.append("<parameter name=\"randomSeed\" displayName=\"Default Random Seed\" type=\"int\"\n");
	 source.append("\tdefaultValue=\"__NULL__\"\n");
	 source.append("\tisReadOnly=\"false\" \n");
	 source.append("\tconverter=\"repast.simphony.parameter.StringConverterFactory$IntConverter\"\n");
	 source.append("/>\n");
	 
	 for (String var : intialValues.keySet()) {
	     String value = intialValues.get(var);
	     String legalVar = InformationManagers.getInstance().getNativeDataTypeManager().makeLegal(var.replace("memory.", ""));
	     
	     source.append("<parameter name=\""+legalVar+"\" displayName=\""+InformationManagers.getInstance().getNativeDataTypeManager().getOriginalName(var)+"\" type=\"double\" \n");
	     source.append("\tdefaultValue=\""+value+"\" \n");
	     source.append("\tisReadOnly=\"false\" \n");
	     source.append("\tconverter=\"repast.simphony.parameter.StringConverterFactory$DoubleConverter\"\n");
	     source.append("/>\n");
	 }

	 
	 source.append("</parameters>\n");
	 source.close();
     } catch (IOException e) {
	 // TODO Auto-generated catch block
	 e.printStackTrace();
     }
}
 
 public static void generateUserPathXml(BufferedWriter source, String objectName) {

     try {
	 source.append("<model name=\""+objectName+"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
	 "xsi:noNamespaceSchemaLocation=\"http://repast.org/scenario/user_path\">\n");
	 source.append("<classpath>\n");
	 source.append("<agents path=\"../bin\" />\n");
	 source.append("<entry path=\"../lib\" />\n");
	 source.append("</classpath>\n");
	 source.append("</model>\n");


	 source.close();
     } catch (IOException e) {
	 // TODO Auto-generated catch block
	 e.printStackTrace();
     }
}
}
