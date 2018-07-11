package repast.simphony.relogo.ide.wizards;

import groovy.io.FileType 
import java.io.File
import java.util.List
import java.util.ArrayList
import java.lang.String
import java.lang.Character

public class WizardUtilities {
	
	static public String capitalize(String input){
		if (input.size() == 0){
			return ""
		}
		else if (input.size() == 1){
			return (input.substring(0,1).toUpperCase())
			}
		else {
			return (input.substring(0,1).toUpperCase() + input.substring(1));
		}
	}
	
	static public String getStringFromFile(File file){
		return file.text
	}
	
	static public String getJavaName(String name){
		String newName = name.trim()
		StringBuffer buf = new StringBuffer();
		if (newName == null || newName.isEmpty()){
			return "anonymous";
		}
		else {
			newName = newName.replaceAll(/[()]/, ' ');
			newName = newName.replaceAll(/\?/, 'Q');
			newName = newName.replaceAll(/%/, 'p');
			newName = newName.replaceAll(/!/, 'X');
			newName = newName.replaceAll(/[ ]+/, ' ');
			newName = newName.trim()
			buf.append(newName);
			
			for (int i = 0; i < buf.length(); i++) {
//				if (i == 0 && Character.isUpperCase(buf.charAt(i))){
//					buf.setCharAt(i, Character.toLowerCase(buf.charAt(i)));
//				}
				if (Character.isLetterOrDigit(buf.charAt(i))) {
					continue;
				} else if (Character.isWhitespace(buf.charAt(i)) || buf.charAt(i) == '-'){
					buf.deleteCharAt(i);
					if (i < buf.length()){
						buf.setCharAt(i, Character.toUpperCase(buf.charAt(i)));
					}
				}
			}
			return buf.toString()
		}
	}
	
	static public String stripNewLines(String name){
		return (name.replaceAll(/\\n/, ' '))
	}
	
	static public String replaceStopWithReturn(String old){
		return (old.replaceAll(/stop\(\)/, 'return '))
	}
	
	static public List<String> getFileNamesInDirectory(File file){
		List<String> list = new ArrayList<String>();
		if (file.exists()){
			file.eachFile(FileType.FILES){
				list.add(it.getName())
			}
		}
		return list;
	}
	
}