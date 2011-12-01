package repast.simphony.adaptation.neural;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.joone.engine.Layer;
import org.joone.engine.Synapse;
import org.joone.engine.learning.TeachingSynapse;
import org.joone.net.NeuralNet;

/**
 * Some utility functions for creating neural networks.
 * 
 * @see RepastNeuralWrapper
 * 
 * @author Jerry Vos
 */
public class NeuralUtils {
	private NeuralUtils() {
	}

	/**
	 * This builds a neural network with nodesPerLayer.length layers, each
	 * layer being an instance of layerType, and each layer connected by a 
	 * synapse of type synapseType. <br/>
	 * 
	 * @param nodesPerLayer	the number of nodes in the layers
	 * @param layerType		the type to make the layers
	 * @param synapseType	the type to make the synapses
	 * 
	 * @return the created network
	 * 
	 * @throws NeuralException when there is an error creating the network
	 * 
	 * @see #buildNetwork(int[], Class[], Class[])
	 */
	public static RepastNeuralWrapper buildNetwork(
			int[] nodesPerLayer,
			Class layerType, 
			Class synapseType) throws Exception 
			{
		Class[] layerTypes = new Class[nodesPerLayer.length];
		for (int i = 0; i < nodesPerLayer.length; i++)
			layerTypes[i] = layerType;

		Class[] synapseTypes = new Class[nodesPerLayer.length - 1];
		for (int i = 0; i < nodesPerLayer.length - 1; i++)
			synapseTypes[i] = synapseType;

		return buildNetwork(nodesPerLayer, layerTypes, synapseTypes);
			}

	/**
	 * This builds a neural network using the specified parameters.  This network
	 * will have all the layers connected using the synapse types passed in. No
	 * input or output synapses will be attached to the layer, so you still
	 * must do this yourself.  <br/>
	 * This network will also have a teacher added to it, however, this teacher
	 * will NOT be attached as an output layer; it only will be added to the 
	 * network.
	 * 
	 * @param nodesPerLayer	the number of nodes in each layer
	 * @param layerTypes	the types to make the layers
	 * @param synapseTypes	the types of the synapses connecting the layers
	 * 
	 * @return the created network
	 * 
	 * @throws NeuralException when there is an error instantiating the layers/synapses 
	 * @throws IllegalArgumentException when (nodesPerLayer.length != layerTypes.length || layerTypes.length -1 != synapseTypes.length) 
	 */
	public static RepastNeuralWrapper buildNetwork(
			int[] nodesPerLayer,
			Class[] layerTypes, 
			Class[] synapseTypes) 
	throws Exception, IllegalArgumentException
	{
		if (nodesPerLayer.length != layerTypes.length || layerTypes.length -1 != synapseTypes.length)
			throw new IllegalArgumentException(
					"In NeuralUtils.buildNetwork, the length" +
					"of the passed in arrays must be nodesPerLayer.length == layerTypes.length and" +
			"layerTypes.length - 1 == synapseTypes.length");

		try {
			NeuralNet net = new NeuralNet();
			RepastNeuralWrapper netWrapper = new RepastNeuralWrapper(net);

			if (nodesPerLayer.length == 0)
				return netWrapper;

			Layer prevLayer = null;

			for (int i = 0; i < nodesPerLayer.length; i++) {
				Layer layer = (Layer) layerTypes[i].newInstance();
				layer.setRows(nodesPerLayer[i]);

				// connect this layer to the previous if this isn't the
				if (prevLayer != null) {
					linkLayers(prevLayer, layer, synapseTypes[i - 1]);
				}

				// add the layer to the net according to its type
				if (i == 0) {
					net.addLayer(layer, NeuralNet.INPUT_LAYER);
					layer.setLayerName("Input Layer");
				} else if (i < nodesPerLayer.length - 1) {
					net.addLayer(layer, NeuralNet.HIDDEN_LAYER);
					layer.setLayerName("Hidden Layer (" + (i - 1) + ")");
				} else {
					net.addLayer(layer, NeuralNet.OUTPUT_LAYER);
					layer.setLayerName("Output Layer");
				}

				prevLayer = layer;
			}

			net.setTeacher(new TeachingSynapse());

			return netWrapper;
		} catch (InstantiationException ex) {
			throw new Exception(
					"NeuralUtils.linkLayers: Couldn't create an instance of the specified layer/synapse type\nDo both classes supply default constructors?",
					ex);
		} catch (IllegalAccessException ex) {
			throw new Exception(
					"NeuralUtils.linkLayers: Couldn't create an instance of the specified layer/synapse type\nDo both classes supply default constructors?",
					ex);
		}
	}

	/**
	 * Links two layers using the specified synapse type.
	 * 
	 * @param outLayer		the layer the synapse is coming out of
	 * @param inLayer		the layer the synapse is going into
	 * @param synapseType	the type of the synapse
	 * 
	 * @return the created synapse instance
	 * 	
	 * @throws NeuralException when there is an error instantiating the synapse
	 */
	public static Synapse linkLayers(Layer outLayer, 
			Layer inLayer,
			Class synapseType) throws Exception 
			{
		try {
			// instantiate the synapse type
			Synapse synapse = (Synapse) synapseType.newInstance();

			// connect the layers
			inLayer.addInputSynapse(synapse);
			outLayer.addOutputSynapse(synapse);

			return synapse;

		} catch (InstantiationException ex) {
			throw new Exception(
					"NeuralUtils.linkLayers: Couldn't create an instance of the specified synapse type",
					ex);
		} catch (IllegalAccessException ex) {
			throw new Exception(
					"NeuralUtils.linkLayers: Couldn't create an instance of the specified synapse type",
					ex);
		}
			}

	/**
	 * Saves a {@link NeuralNet} to the specified file.  
	 * 
	 * @param net		the neural network to save
	 * @param fileName	the name of the file to save the network to
	 * 
	 * @throws NeuralException when there is an error saving the network
	 * @see RepastNeuralWrapper#saveNetToFile(String)
	 * @see RepastNeuralWrapper#loadNetFromFile(String)
	 */
	public static void saveNetToFile(NeuralNet net, String fileName) throws Exception {
		try {
			FileOutputStream stream	= new FileOutputStream(fileName);
			ObjectOutputStream out	= new ObjectOutputStream(stream);
			out.writeObject(net);
			out.close();
		}
		catch (Exception ex) {
			throw new Exception("Error saving net to file: " + ex.getMessage(), ex);
		}
	}

	/**
	 * This method creates a RepastNeuralWrapper that is wrapping a serialized
	 * Joone {@link NeuralNet}.  In other words, this creates a wrapper around a 
	 * predesigned {@link NeuralNet}. 
	 * 
	 * @param fileName	the name of the serialized {@link NeuralNet}
	 * @return the created wrapper
	 * 
	 * @throws NeuralException	when there is an error loading the specified
	 * 							network
	 */
	public static RepastNeuralWrapper loadNetFromFile(String fileName) 
	throws Exception
	{
		// this could use the NeuralNetLoader, but that catches all exceptions
		// with no notification, so this way we can handle errors
		NeuralNet nnet = null;
		try {
			FileInputStream stream	= new FileInputStream(fileName);
			ObjectInput input		= new ObjectInputStream(stream);
			nnet = (NeuralNet) input.readObject();
		} catch (Exception ex) {
			throw new Exception("Error loading network from file", ex);
		}

		nnet.start();
		nnet.getMonitor().Go();
//		nnet.getMonitor().Stop();
		nnet.stop();


		// return a new wrapper wrapping the loaded network 
		return new RepastNeuralWrapper(nnet);
	}
}