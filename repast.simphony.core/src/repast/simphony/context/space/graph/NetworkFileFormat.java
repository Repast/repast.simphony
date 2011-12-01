package repast.simphony.context.space.graph;

import repast.simphony.context.Context;

import java.io.InputStream;

/**
 * Network format file types.
 *
 * @author Nick Collier
 */
public enum NetworkFileFormat {

  DL() {
    public NetworkGenerator createLoader(Context context, InputStream stream, NodeCreator nodeCreator, int matrixIndex) {
      return new DLNetworkLoader(context, stream, nodeCreator, matrixIndex);
    }},

  EXCEL() {
    public NetworkGenerator createLoader(Context context, InputStream stream, NodeCreator nodeCreator, int matrixIndex) {
      return new ExcelNetworkLoader(context, stream, nodeCreator, matrixIndex);
    }},

  /*
  ASCII() {
    public NetworkGenerator createLoader(Context context, InputStream stream, NodeCreator nodeCreator, int matrixIndex) {
      return null;
    }},

  PAJEK() {
    public NetworkGenerator createLoader(Context context, InputStream stream, NodeCreator nodeCreator, int matrixIndex) {
      return new PajekNetworkLoader(context, stream, nodeCreator, matrixIndex);
    }}*/;


  public abstract NetworkGenerator createLoader(Context context, InputStream stream, NodeCreator nodeCreator,
                                                int matrixIndex);
}
