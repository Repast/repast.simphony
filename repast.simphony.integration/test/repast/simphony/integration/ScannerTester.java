package repast.simphony.integration;


import java.util.Scanner;

public class ScannerTester {
	public static void main(String[] args) {
		Scanner scanner = new Scanner("line1\nline2\n");
		
		scanner.useDelimiter("2");
		System.out.println(":" + scanner.next() + ":");
		System.out.println(scanner.hasNext());
		System.out.println(":" + scanner.next() + ":");
		System.out.println(scanner.hasNext());
		
		System.out.println("****************");
		scanner = new Scanner("line1\nline2\n");
		
//		System.out.println(":" + scanner.next("line2") + ":");
//		System.out.println(scanner.hasNext());
//		System.out.println(":" + scanner.next() + ":");
//		System.out.println(scanner.hasNext());
		
//		System.out.println("******************");
//		scanner = new Scanner("line1\nline2\n");
//		scanner.useDelimiter("\\z");
//		System.out.println(scanner.next("li"));
		
		
		System.out.println("******************");
		scanner = new Scanner("line1\nline2\n");
		scanner.useDelimiter("z");
		System.out.println(scanner.hasNext());
	}
}
