package repast.simphony.adaptation.neural;

import java.io.Serializable;

import org.joone.engine.DirectSynapse;
import org.joone.engine.InputPatternListener;
import org.joone.engine.Monitor;
import org.joone.engine.NeuralNetEvent;
import org.joone.engine.NeuralNetListener;
import org.joone.engine.Pattern;
import org.joone.engine.learning.ComparingElement;
import org.joone.net.NeuralNet;

/**
 * A wrapper around a {@link org.joone.net.NeuralNet}. This class adds training
 * and retrieval methods that wait until the training/retrieval is finished
 * before returning.
 * 
 * Note: if you want serialize the network that this wrapper wraps, use the
 * saveNetToFile and NeuralUtils.loadNetFromFile methods, otherwise you will
 * have multiple wrappers attached to the serialized networks (through the
 * listeners).
 * 
 * @author Jerry Vos
 * 
 */
public class RepastNeuralWrapper implements NeuralNetListener {
	private static final long serialVersionUID = 3257846575983114032L;

	/**
	 * The net being wrapped
	 */
	protected NeuralNet net;

	/**
	 * The number of times to apply the patterns to the network during the
	 * training
	 */
	protected int epochsPerIteration = 1;

	/**
	 * Whether or not the network is running (training/retrieving). This doesn't
	 * mean that the network's threads have all shut down, just that the network
	 * has signaled its start or stop.
	 */
	protected transient boolean netStopped = true;

	/** the monitor watching if the network is running or not **/
	private transient Object networkRunningMonitor = new Object();

	/**
	 * The default constructor for a network wrapper. This creates a basic
	 * network, however this cannot be used until {@link org.joone.engine.Layer}
	 * s, {@link org.joone.engine.Synapse}s, and so forth have been
	 * specified.<br/> Same as <c>new RepastNeuralWrapper(new NeuralNet());</c>
	 */
	public RepastNeuralWrapper() {
		this(new NeuralNet());
	}

	/**
	 * This creates a basic network wrapper, however this cannot be used until
	 * {@link org.joone.engine.Layer}s, {@link org.joone.engine.Synapse}s, and
	 * so forth have been specified.<br/>
	 */
	public RepastNeuralWrapper(NeuralNet net) {
		super();

		this.net = net;
		net.removeAllListeners();
		net.addNeuralNetListener(this);
	}

	/**
	 * This method will retrieve a result Pattern from a network based on the in
	 * parameter.<br/> This method will remove all outputs and inputs from the
	 * network, and then it will add a DirectSynapse as an output, and the in
	 * parameter as an input. THESE ARE NOT RESTORED BY THE CALL.<br/>
	 * 
	 * @param in
	 *            The input to the network (best as a {@link DirectSynapse})
	 * @return the Pattern resulting from running the network
	 * 
	 * @throws NeuralException
	 *             when there is an error querying the network
	 */
	public synchronized Pattern retrieve(InputPatternListener in)
			throws Exception {
		// logger.debug("Retrieving from the network");
		// the call to removeAllOutputs will clear the teacher
		ComparingElement teacher = net.getTeacher();

		DirectSynapse out = new DirectSynapse();

		net.removeAllInputs();
		net.removeAllOutputs();
		net.addInputSynapse(in);
		net.addOutputSynapse(out);

		// setup the monitor for the retrieval
		Monitor monitor = net.getMonitor();

		monitor.setTrainingPatterns(1);
		monitor.setTotCicles(1);
		monitor.setLearning(false);

		net.start();
		// have to start the monitor if this isn't a direct synapse
		if (!(in instanceof DirectSynapse))
			net.getMonitor().Go();
		// grab the pattern
		Pattern retPattern = out.fwdGet();

		net.stop();
		if (!(in instanceof DirectSynapse))
			net.getMonitor().Stop();

		try {
			// wait around until the network signals its stop
			synchronized (networkRunningMonitor) {
				while (!netStopped) {
					networkRunningMonitor.wait();
				}
			}
		} catch (InterruptedException ex) {
			throw new Exception("Error waiting for net to stop", ex);
		}

		waitTilNetTrulyStops();

		// restore the teacher
		net.setTeacher(teacher);

		return retPattern;
	}

	/**
	 * Trains a network. The network is expected to be fully built before this
	 * method is called. <br/> This method will remove all outputs and inputs
	 * from the network, and then it will add the network's teacher as an
	 * output, and the in parameter as an input. THESE ARE NOT RESTORED BY THE
	 * CALL.<br/>
	 * 
	 * To use this method the network's teacher should have its desired output
	 * specified through a getTeacher().setDesired(..) call. Also the in should
	 * be setup to deliver at least one pattern to the network (this training
	 * only feeds one pattern through).<br/>
	 * 
	 * Make sure before calling this method you specify the learning rate and
	 * momentum of the network's {@link Monitor}.
	 * 
	 * @param in
	 *            the input for the network.
	 * @throws NeuralException
	 *             when there is an error training the network
	 * 
	 * @see NeuralNet#check()
	 * @see #retrieve(InputPatternListener)
	 */
	public synchronized void train(InputPatternListener in) throws Exception {
		// the .removeAllOutputs call will set the teacher to null
		ComparingElement teacher = net.getTeacher();

		net.removeAllInputs();
		net.removeAllOutputs();

		net.setTeacher(teacher);
		net.addInputSynapse(in);
		net.addOutputSynapse(teacher);

		net.getMonitor().setLearning(true);

		net.start();
		net.getMonitor().Go();

		try {
			// wait around until the network signals its stop
			synchronized (networkRunningMonitor) {
				while (!netStopped) {
					networkRunningMonitor.wait();
				}
			}
		} catch (InterruptedException ex) {
			throw new Exception("Error waiting for net to stop", ex);
		}

		waitTilNetTrulyStops();
	}

	/**
	 * This waits around until the network really has stopped. If it isn't
	 * shutting down, this forcefully stops the network
	 * 
	 * @throws NeuralException
	 *             when there is an error stopping the network (only happens
	 */
	private synchronized void waitTilNetTrulyStops() throws Exception {
		for (int i = 0; net.isRunning() && i < 100; i++) {
			try {
				Thread.sleep(3);
				if (i == 50) {
					//System.out.println("WARNING****** Forcefully stopping net"
					// );
					net.stop();
				}
			} catch (Exception ex) {
			}
		}
		if (net.isRunning())
			throw new Exception("Couldn't stop network");
	}

	/**
	 * @return the number of times to feed a pattern through the network
	 */
	public synchronized int getEpochsPerIteration() {
		return epochsPerIteration;
	}

	/**
	 * @param epochsPerIteration
	 *            the number of times to feed a pattern through the network
	 */
	public synchronized void setEpochsPerIteration(int epochsPerIteration) {
		// logger.debug("Setting epochs to: " + epochsPerIteration);
		this.epochsPerIteration = epochsPerIteration;
	}

	public void netStarted(NeuralNetEvent e) {
		netStopped = false;
	}

	public void netStopped(NeuralNetEvent e) {
		// logger.debug("Net stopped");
		// System.out.println("\t*Entering stop");
		if (!netStopped) {
			netStopped = true;
			synchronized (networkRunningMonitor) {
				networkRunningMonitor.notify();
			}
		}
		// System.out.println("\t*Exiting stop");
	}

	public void netStoppedError(NeuralNetEvent e, String error) {
		// logger.error("Net stopped on error");
		System.out.println("STOPPED ON ERROR");
		if (!netStopped) {
			netStopped = true;
			synchronized (networkRunningMonitor) {
				networkRunningMonitor.notify();
			}
		}
	}

	public void cicleTerminated(NeuralNetEvent e) {
	}

	public void errorChanged(NeuralNetEvent e) {
	}

	/**
	 * @return the net this wrapper is wrapping
	 */
	public synchronized NeuralNet getNet() {
		return net;
	}

	/**
	 * @param net
	 *            the new net to wrap
	 */
	public synchronized void setNet(NeuralNet net) {
		this.net = net;
	}

	/**
	 * Saves the neural network this is wrapping to the specified file. This can
	 * be loaded through {@link #loadNetFromFile(String)}.
	 * 
	 * @param fileName
	 *            the name of the file to save the network to
	 * 
	 * @throws NeuralException
	 *             when there is an error saving the network
	 * @see #loadNetFromFile(String)
	 * @see NeuralUtils#saveNetToFile(NeuralNet, String)
	 */
	public void saveNetToFile(String fileName) throws Exception {
		this.net.removeNeuralNetListener(this);
		NeuralUtils.saveNetToFile(this.net, fileName);
		this.net.addNeuralNetListener(this);
	}
}