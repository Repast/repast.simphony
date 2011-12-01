package repast.simphony.engine.watcher.query;

public class ASTNumber extends SimpleNode {

	private double number;

  public ASTNumber(int id) {
    super(id);
  }

  public ASTNumber(QueryParser p, int id) {
    super(p, id);
  }

	public double getNumber() {
		return number;
	}

	public void setNumber(String name) {
		number = Double.parseDouble(name);
	}
}
