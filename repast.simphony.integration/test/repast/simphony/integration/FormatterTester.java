package repast.simphony.integration;

import java.util.Formatter;

public class FormatterTester {
	public static void main(String[] args) {
		System.out.println("***************");
		Formatter formatter = new Formatter();
		formatter.format("% f               ", 200.0);
		System.out.println(200.0f);
		System.out.println(formatter.toString());
		
		System.out.println("***************");
		formatter = new Formatter();
		formatter.format("% 4d               !number of meas. sites\n", 25);
		System.out.println(formatter.toString());
	}
}
