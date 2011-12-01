package repast.simphony.integration;


import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import repast.simphony.util.SystemConstants;

public class RegexTester {
//	private static final String FLOAT_REGEX2 = "(\\+|-)?" +
//		""
	
	private static final String FLOAT_REGEX = "(\\+|-)?" +
			"("
			+ "\\d+\\.\\d*([eE][+-]?\\d+)?|" + // ex: 34., 3.E2
			"\\d*\\.\\d+([eE][+-]?\\d+)?|" + // ex: .2, .994e+4
			"\\d+(?:[eE][+-]?\\d+|)?|" + // ex: 1e-15
			"\\d+?" + // ex: 22f
			")[fF]?";
	
	private static final String INT_REGEX = "(\\+|\\-)?\\d+";
	
	public static void main(String[] args) {
		System.out.println("Float trues");
		
//		System.out.println("\t" + "10".matches("[^(" + FLOAT_REGEX + ")]"));
		System.out.println("\t" + "-11".matches(FLOAT_REGEX));
		System.out.println("\t" + "+10".matches(FLOAT_REGEX));
		System.out.println("\t" + "-10.".matches(FLOAT_REGEX));
		System.out.println("\t" + "-10.6".matches(FLOAT_REGEX));
		System.out.println("\t" + "-10.6E+9".matches(FLOAT_REGEX));
		System.out.println("\t" + "-.6E9".matches(FLOAT_REGEX));
		System.out.println("\t" + "-.6E9f".matches(FLOAT_REGEX));
		

		System.out.println("Float falses");
		System.out.println("\t" + "10asdf".matches(FLOAT_REGEX));
		System.out.println("\t" + "-+1".matches(FLOAT_REGEX));
		System.out.println("\t" + "+".matches(FLOAT_REGEX));
		System.out.println("\t" + "".matches(FLOAT_REGEX));
		System.out.println("\t" + "-.6E9ff".matches(FLOAT_REGEX));
		
		
		System.out.println("Int trues");
		System.out.println("\t" + "-1".matches(INT_REGEX));
		System.out.println("\t" + "+1".matches(INT_REGEX));
		System.out.println("\t" + "1".matches(INT_REGEX));
		System.out.println("\t" + "100".matches(INT_REGEX));
		
		System.out.println("Int falses");
		System.out.println("\t" + "-+1".matches(INT_REGEX));
		System.out.println("\t" + "+".matches(INT_REGEX));
		System.out.println("\t" + "".matches(INT_REGEX));
		
		for (String s : "\n".split("\n", 2)) {
			System.out.println(":" + s + ":");
		}
		System.out.println("foo:bar".split("p", 2).length);
		
		System.out.println("\n".split("\n", 2).length);
		
		
		System.out.println("******************");
		System.out.println("newline test");
		;
		System.out.println("\t" + Pattern.compile(".", Pattern.DOTALL).matcher("\n").matches());

		System.out.println("******************");
		final String TEST_STRING = "blah\nfoo:bar\n"; 
		
		CharBuffer buf = CharBuffer.allocate(TEST_STRING.length());
		char[] dest = new char[TEST_STRING.length()];
		buf.put(TEST_STRING);
		buf.position(0);
		buf.limit(buf.capacity());
		
		Matcher matcher = Pattern.compile("\n").matcher(buf);
		System.out.println(matcher.find());
		buf.get(dest, 0, matcher.start());
		System.out.print(":");
		for (int i = 0; i < matcher.start(); i++) {
			System.out.print(dest[matcher.regionStart() + i]);
		}
		System.out.println(":");


		
		
		buf.position(matcher.end());
		matcher.region(buf.position(), buf.length());

		System.out.println(matcher.find());
		
		buf.get(dest, 0, matcher.start());
		
		System.out.print(":");
		for (int i = 0; i < matcher.start(); i++) {
			System.out.print(dest[i]);
		}
		System.out.println(":");
		
		System.out.println("*******************");
		System.out.println("Reset test");
		

		
		System.out.println("*******************");
		System.out.println("Replace newline test");
		String foo = "\\n";
		System.out.println(foo);
		System.out.println(foo.replaceAll("\\\\n", "\\\\r\\\\n"));
		System.out.println(foo.replaceAll("\n", "[as]"));
		foo = "\\r\\n";
		System.out.println(foo.replaceAll("\r\n", "[as]"));
		System.out.println(foo.replaceAll("$", "[as]"));
		
		System.out.println("*******************");
		System.out.println("Replace newline system test");
		foo = SystemConstants.LINE_SEPARATOR;
		System.out.println(foo);
		System.out.println(foo.replaceAll("\n", "[as]"));
		System.out.println(foo.replaceAll("\r", "[as]"));
		System.out.println(foo.replaceAll("\r", "[as]").replaceAll("\n", "[as]"));
		System.out.println(foo.replaceAll("\r", "[as]").replaceAll("\n", "[as]"));
	}
}


