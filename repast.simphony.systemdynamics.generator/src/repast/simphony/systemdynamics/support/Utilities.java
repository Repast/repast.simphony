package repast.simphony.systemdynamics.support;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class Utilities {
	public static  BufferedWriter openFileForWriting(String filename) {
		BufferedWriter report = null;
        try {
            File aFile = new File(filename);
            report = new BufferedWriter(new OutputStreamWriter(
                    (new FileOutputStream(aFile, false))));     
        } catch (FileNotFoundException e) {
            System.out.println(filename+" FileNotFoundException");
            e.printStackTrace();
        } 
		
		return report;
	}
	
	public static  int getRowFromCellAddress(String cellAddress) {
		int row = 1;
		String rc = cellAddress;
		if (cellAddress.contains("!")) {
			rc = cellAddress.split("!")[1];
		}
		int rowPos = getFirstNumberPosition(rc);
		row = Integer.parseInt(rc.substring(rowPos));
		return row;
	}
	
	public static  int getColumnFromCellAddress(String cellAddress) {
		int column =  1;
		String rc = cellAddress;
		if (cellAddress.contains("!")) {
			rc = cellAddress.split("!")[1];
		}
		int rowPos = getFirstNumberPosition(rc);
		column = convertColumnToNumber(rc.substring(0, rowPos));
		
		return column;
	}
	
	public static  int convertColumnToNumber(String column) {
		int num = 0;
		int pos = 0;
		
		char[] chars = column.toCharArray();
		for (int i = chars.length-1; i >=0; i--) {
			num += (Character.getNumericValue(chars[i]) - 9) * Math.pow(26, pos++);
		}
		return num;
	}
	
	private static int getFirstNumberPosition(String address) {
		MutableInteger pos = new MutableInteger(0);
		while (inRange(address, pos) && Character.isLetter(characterAt(address, pos).toCharArray()[0]))
			pos.add(1);
		return pos.value();
	}
	
	private static boolean inRange(String equation, MutableInteger position) {
	    if (position.value() <= equation.length()-1)
		return true;
	    else
		return false;
	}
	
	private static String characterAt(String equation, MutableInteger position) {
	    return equation.substring(position.value(), position.value()+1);
	}
}
