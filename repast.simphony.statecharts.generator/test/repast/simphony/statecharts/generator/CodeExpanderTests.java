/**
 * 
 */
package repast.simphony.statecharts.generator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import repast.simphony.statecharts.generator.CodeExpander;

/**
 * @author Nick Collier
 */
public class CodeExpanderTests {

  @Test
  public void simpleTest() {
    CodeExpander expander = new CodeExpander();

    String in = "agent.getX()";
    String out = expander.expand(in, false);
    assertEquals(in + ";", out.trim());

    out = expander.expand(in, true);
    assertEquals("return " + in + ";", out.trim());
  }

  @Test
  public void semiColonTest() {
    CodeExpander expander = new CodeExpander();

    String in = "int x = 3; params.get(\"foo;bar\"); agent.getX()";
    String out = expander.expand(in, true);
    assertEquals("int x = 3;\nparams.get(\"foo;bar\");\nreturn agent.getX();", out.trim());
  }
  
  @Test
  public void bracketTest() {
    CodeExpander expander = new CodeExpander();

    String in = "if (agent.getX() == 3) {\nreturn 3\n}\n5";
    String out = expander.expand(in, true);
    assertEquals("if (agent.getX() == 3) {\nreturn 3;\n}\nreturn 5;", out.trim());
  }

}
