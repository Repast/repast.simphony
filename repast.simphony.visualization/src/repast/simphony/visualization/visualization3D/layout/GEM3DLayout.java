package repast.simphony.visualization.visualization3D.layout;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jogamp.vecmath.Point3f;
import org.jogamp.vecmath.Vector3f;

import repast.simphony.random.RandomHelper;
import repast.simphony.space.graph.Network;
import repast.simphony.visualization.Box;
import repast.simphony.visualization.Layout;
import repast.simphony.visualization.VisualizationProperties;

/**
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/06 22:35:19 $
 */
public class GEM3DLayout implements Layout<Object, Network> {

  private Network graph;
  private Point3f center = new Point3f();

  // optimal edge length
  private int optEdgeLength;
  // minimum edge length squared
  private int minEdgeLengthS;
  // optimal edgel length squared
  private int optEdgeLengthS;

  // max vertex temp
  private int tempMax;
  // initial vertex temp
  private int tempInit;
  // global temp
  private int temperature;
  // minimum temp -- layout ends with global temp hits this
  private int endTemp;

  // number of rounds
  private int rounds;
  // current round
  private int round = 0;

  // max attraction between verts
  private int maxAttraction;

  private int shake;
  private float gravity = 0.1f;

  // angle of rotation
  private double alphaRotationCos;
  // angle of oscillation
  private double alphaOscillationCos;

  // intensity of reaction to oscillations
  private double sigmaOscillation = 1.0f;
  // intensity of reaction to rotations
  private double sigmaRotation = .01f;

  private float[] location = new float[3];

  private Map<Object, VertexData> dataMap = new HashMap<Object, VertexData>();

  class VertexData {

    Vector3f impulse = new Vector3f();
    Vector3f pos = new Vector3f();
    float skewXY, skewYZ, skewZX;
    float heat;

    public VertexData(int scale) {
      pos.x = RandomHelper.getUniform().nextFloatFromTo(-(scale / 2), scale + 1);
      pos.y = RandomHelper.getUniform().nextFloatFromTo(-(scale / 2), scale + 1);
      pos.z = RandomHelper.getUniform().nextFloatFromTo(-(scale / 2), scale + 1);
      center.add(pos);
      heat = tempInit;
    }
  }

  public void setProjection(Network projection) {
    this.graph = projection;
  }

  /**
   * Sets the layout properties for this layout.
   * 
   * @param props
   *          the layout properties
   */
  public void setLayoutProperties(VisualizationProperties props) {
    // not used
  }

  /**
   * Gets the layout properties for this layout.
   * 
   * @return the layout properties for this layout.
   */
  public VisualizationProperties getLayoutProperties() {
    return null;
  }

  public float[] getLocation(Object o) {
    VertexData data = dataMap.get(o);
    data.pos.get(location);
    return location;
  }

  private void setParameters() {
    int maxVertexWidth = 150;
    int vertCount = graph.size();
    double factor = Math.sqrt((double) (graph.numEdges() * 2) / vertCount);
    factor = Math.max(1.0, factor);
    optEdgeLength = (int) (factor * maxVertexWidth);
    optEdgeLengthS = optEdgeLength * optEdgeLength;
    minEdgeLengthS = (int) (maxVertexWidth * maxVertexWidth);

    tempInit = optEdgeLength;
    tempMax = 2 * optEdgeLength;
    temperature = tempInit * vertCount;
    endTemp = (int) (temperature * .01f);

    shake = (int) (optEdgeLength / 2f);
    maxAttraction = 64 * optEdgeLengthS;

    double alphaOscillation = Math.PI / 3;
    alphaOscillationCos = Math.cos(alphaOscillation / 2);
    double alphaRotation = Math.PI / 3;
    alphaRotationCos = Math.abs(Math.cos((Math.PI + alphaRotation) / 2));

    rounds = 50 * vertCount;

  }

  private void init() {
    center.set(0, 0, 0);
    dataMap.clear();
    round = 0;

    int scale = (int) ((Math.sqrt(graph.size()) / 2) * optEdgeLength);
    for (Iterator iter = graph.getNodes().iterator(); iter.hasNext();) {
      VertexData data = new VertexData(scale);
      dataMap.put(iter.next(), data);
    }
  }

  private Vector3f calcImpulse(Object vertex) {
    Vector3f impulse = new Vector3f();

    // random brownian "shake"
    impulse.x = RandomHelper.getUniform().nextFloatFromTo(-(shake / 2), shake + 1);
    impulse.y = RandomHelper.getUniform().nextFloatFromTo(-(shake / 2), shake + 1);
    impulse.z = RandomHelper.getUniform().nextFloatFromTo(-(shake / 2), shake + 1);

    VertexData data = dataMap.get(vertex);
    Vector3f pos = data.pos;
    int vertexCount = graph.size();

    // gravity pull towards center
    impulse.x += (center.x / vertexCount - pos.x) * gravity;
    impulse.y += (center.y / vertexCount - pos.y) * gravity;
    impulse.z += (center.z / vertexCount - pos.z) * gravity;

    // repulsion
    Vector3f delta = new Vector3f();
    for (Iterator iter = graph.getNodes().iterator(); iter.hasNext();) {
      Object other = iter.next();
      if (!other.equals(vertex)) {
        Vector3f otherPos = dataMap.get(other).pos;
        delta.sub(pos, otherPos);
        float lengthSq = delta.lengthSquared();
        if (lengthSq != 0) {
          delta.scale(optEdgeLengthS / lengthSq);
          impulse.add(delta);
          /*
           * impulse.x += delta.x * optEdgeLengthS / lengthSq; impulse.y +=
           * delta.y * optEdgeLengthS / lengthSq; impulse.z += delta.z *
           * optEdgeLengthS / lengthSq;
           */

          if (lengthSq < minEdgeLengthS) {
            impulse.add(delta);
            /*
             * impulse.x += delta.x * minEdgeLengthS / lengthSq; impulse.y +=
             * delta.y * minEdgeLengthS / lengthSq; impulse.z += delta.z *
             * minEdgeLengthS / lengthSq;
             */
          }
        }
      }
    }

    // attraction
    int weight = (int) (optEdgeLengthS * (1.0 + ((double) graph.getDegree(vertex)) / 2));
    for (Iterator iter = graph.getSuccessors(vertex).iterator(); iter.hasNext();) {
      Object other = iter.next();
      Vector3f otherPos = dataMap.get(other).pos;
      delta.sub(pos, otherPos);
      float lengthSq = delta.lengthSquared();
      lengthSq = Math.min(lengthSq, maxAttraction);
      if (lengthSq > minEdgeLengthS) {
        delta.scale(lengthSq / weight);
        impulse.sub(delta);
        /*
         * impulse.x -= delta.x * lengthSq / weightFunc; impulse.y -= delta.y *
         * lengthSq / weightFunc; impulse.z -= delta.z * lengthSq / weightFunc;
         */
      }
    }

    return impulse;
  }

  private void temperatureUpdate(Object vertex, Vector3f impulse) {
    VertexData data = dataMap.get(vertex);
    float temp = data.heat;
    float impulseLength = impulse.length();
    if (impulseLength != 0) {
      impulse.scale(temp / impulseLength);
      /*
       * impulse.x = impulse.x * temp / impulseLength; impulse.y = impulse.y *
       * temp / impulseLength; impulse.z = impulse.z * temp / impulseLength;
       */

      data.pos.add(impulse);

      /*
       * data.pos.x += impulse.x; data.pos.y += impulse.y; data.pos.z +=
       * impulse.z;
       */

      center.add(impulse);
      /*
       * center.x += impulse.x; center.y += impulse.y; center.z += impulse.z;
       */

      float dataImpLengthS = data.impulse.lengthSquared();
      float impulseLengthS = impulse.lengthSquared();
      if (dataImpLengthS != 0 && impulseLengthS != 0) {
        temperature -= data.heat;
        // return this.dot(vector) / Math.sqrt(this.lengthSquared() *
        // vector.lengthSquared());
        double cos = data.impulse.dot(impulse) / Math.sqrt(dataImpLengthS * impulseLengthS);// data.impulse.length()
                                                                                            // *
                                                                                            // impulse.length();
        double absCos = Math.abs(cos);
        if (absCos >= alphaOscillationCos) {
          temp += sigmaOscillation * cos;
        }

        if (absCos <= alphaRotationCos) {
          data.skewXY += sigmaRotation
              * ((data.impulse.x * impulse.y - data.impulse.y * impulse.x) < 0 ? -1 : 1);
          data.skewYZ += sigmaRotation
              * ((data.impulse.y * impulse.z - data.impulse.z * impulse.y) < 0 ? -1 : 1);
          data.skewZX += sigmaRotation
              * ((data.impulse.z * impulse.x - data.impulse.x * impulse.z) < 0 ? -1 : 1);
        }

        double d = (1 - Math.abs(data.skewXY)) * (1 - Math.abs(data.skewYZ))
            * (1 - Math.abs(data.skewZX));
        temp = (float) Math.min(tempMax, temp * Math.pow(d, (1.0 / 3.0)));
        data.heat = temp;
        temperature += data.heat;
      }

      data.impulse.set(impulse);
    }
  }

  private void iterate() {
    for (Iterator iter = graph.getNodes().iterator(); iter.hasNext();) {
      Object vertex = iter.next();
      Vector3f impulse = calcImpulse(vertex);
      temperatureUpdate(vertex, impulse);
    }
  }

  public void update() {
    setParameters();
    init();

    while (round++ < rounds && temperature > endTemp) {
      iterate();
    }

    Point3f barycenter = new Point3f(0, 0, 0);
    for (Iterator iter = graph.getNodes().iterator(); iter.hasNext();) {
      Object vertex = iter.next();
      Vector3f pos = dataMap.get(vertex).pos;
      barycenter.add(pos);
    }

    int size = graph.size();
    barycenter.x = barycenter.x / size;
    barycenter.y = barycenter.y / size;
    barycenter.z = barycenter.z / size;

    float maxLength = 0;
    for (Iterator iter = graph.getNodes().iterator(); iter.hasNext();) {
      Object vertex = iter.next();
      float length = new Point3f(dataMap.get(vertex).pos).distance(barycenter);
      if (length > maxLength) {
        maxLength = length;
      }
    }

    if (maxLength == 0)
      maxLength = 1;
    for (VertexData data : dataMap.values()) {
      data.pos.x = .8f * (data.pos.x - barycenter.x) / maxLength;
      data.pos.y = .8f * (data.pos.y - barycenter.y) / maxLength;
      data.pos.z = .8f * (data.pos.z - barycenter.z) / maxLength;
    }

  }

  public String getName() {
    return "Gem 3D";
  }

  /*
   * (non-Javadoc)
   * 
   * @see repast.simphony.visualization.Layout#getBoundingBox()
   */
  @Override
  public Box getBoundingBox() {
    // TODO Auto-generated method stub
    return null;
  }

}
