package repast.simphony.ws;

import repast.simphony.visualization.gui.styleBuilder.IconFactory2D;

import java.awt.Shape;
import java.awt.geom.PathIterator;

public class ShapePaths {
    
  public static void main(String[] args) throws Exception {
    Shape s = IconFactory2D.getShape("arrow");

    PathIterator pi = s.getPathIterator(null,2);

    while (pi.isDone() == false) {
      describeCurrentSegment(pi);
      pi.next();
    }

  }

  public static void describeCurrentSegment(PathIterator pi) {
    double[] coordinates = new double[6];
    int type = pi.currentSegment(coordinates);
    switch (type) {
    case PathIterator.SEG_MOVETO:
      //System.out.println("shape.moveTo( " + coordinates[0] + " * size, " + coordinates[1] + " * size);");
        System.out.print("," + coordinates[0] + "," + coordinates[1]);
      break;
    case PathIterator.SEG_LINETO:
      //System.out.println("shape.lineTo(" + coordinates[0] + " * size, " + coordinates[1] + " * size);");
        System.out.print("," + coordinates[0] + "," + coordinates[1]);
      break;
    case PathIterator.SEG_QUADTO:
      System.out.println("quadratic to " + coordinates[0] + ", " + coordinates[1] + ", "
          + coordinates[2] + ", " + coordinates[3]);
      break;
    case PathIterator.SEG_CUBICTO:
      System.out.println("cubic to " + coordinates[0] + ", " + coordinates[1] + ", "
          + coordinates[2] + ", " + coordinates[3] + ", " + coordinates[4] + ", " + coordinates[5]);
      break;
    case PathIterator.SEG_CLOSE:
      System.out.println("close");
      break;
    default:
      break;
    }
  }
}
