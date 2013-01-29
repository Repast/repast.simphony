/*
 * Copyright (c) 2003, Alexander Greif All rights reserved. (Adapted by Michael
 * J. North for Use in Repast Simphony from Alexander Greif’s Flow4J-Eclipse
 * (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks to the
 * Original Author) (Michael J. North’s Modifications are Copyright 2007 Under
 * the Repast Simphony License, All Rights Reserved)
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. * Redistributions in
 * binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other
 * materials provided with the distribution. * Neither the name of the
 * Flow4J-Eclipse project nor the names of its contributors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package repast.simphony.agents.flows.tasks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * With the help of the AgentBuilderManager you can register and execute flows.
 * 
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 */
@SuppressWarnings("unchecked")
public class AgentBuilderManager {

	private static Map taskFlowletSingletons = new HashMap();
	private static Map flowname2FlowMap = new HashMap();
	private static Map flowclass2FlowMap = new HashMap();

	/**
	 * Returns whether a flow with the given flow name is registered.
	 * 
	 * @param flowName
	 *            the name of the flow
	 * @return reue if a flow with the given flow name is registered.
	 */
	public static boolean isFlowNameRegistered(String flowName) {
		return getFlowname2FlowMap().containsKey(flowName);
	}

	/**
	 * Returns an instance for the class, that's classpath is supported, or
	 * throws an Exception
	 * 
	 * @param classPath
	 *            the class's path
	 * @return a new instance
	 * @throws AgentBuilderRuntimeException
	 */
	static protected Object getInstance(String classPath)
			throws AgentBuilderRuntimeException {
		Object obj = null;
		try {
			obj = getInstance(Class.forName(classPath));
		} catch (ClassNotFoundException e) {
			throw new AgentBuilderRuntimeException("Cannot find class: "
					+ classPath);
		}

		return obj;
	}

	/**
	 * TODO
	 * 
	 * @param clazz
	 * @return
	 */
	static private Object getInstance(Class clazz) {
		Object obj = null;
		try {
			obj = clazz.newInstance();
		} catch (InstantiationException e) {
			throw new AgentBuilderRuntimeException("Cannot instantiate class: "
					+ clazz.getName());
		} catch (IllegalAccessException e) {
			throw new AgentBuilderRuntimeException(
					"IllegalAccessException while instantiating class: "
							+ clazz.getName());
		}

		return obj;
	}

	/**
	 * TODO
	 * 
	 * @param className
	 * @return
	 */
	static private Class getClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new AgentBuilderRuntimeException("Cannot find class: "
					+ className);
		}
	}

	/**
	 * Registers the task. Puts the task's singleton in the taskFlowlet registry
	 * map.
	 * 
	 * @param className
	 *            the task class's fully qualified class name
	 * @return the registeren task flowlet instance
	 * @throws AgentBuilderRuntimeException
	 *             if the class does not implement the ITaskStep interface or if
	 *             the classname is incorrect or if the task instance cannot be
	 *             registered (taskname already registered but with different
	 *             class)
	 */
	static public ITaskStep registerTaskFlowlet(String className)
			throws AgentBuilderRuntimeException {
		return registerTaskFlowlet(getClass(className));
	}

	/**
	 * Registers the task.
	 * 
	 * @param taskFlowletClass
	 * @return the registeren task flowlet instance
	 */
	static public ITaskStep registerTaskFlowlet(Class taskFlowletClass) {
		Object instance = getInstance(taskFlowletClass);
		if (!(instance instanceof ITaskStep))
			throw new AgentBuilderRuntimeException("class \""
					+ taskFlowletClass.getName()
					+ "\" does not implement the ITaskStep interface.");

		ITaskStep taskFlowletInstance = (ITaskStep) instance;
		String taskName = taskFlowletInstance.getName();
		if (taskName == null || taskName.trim().length() == 0)
			throw new AgentBuilderRuntimeException("The name of the task \""
					+ taskFlowletClass.getName()
					+ "\" is either empty or not set");

		// check for already registered Task with same name
		ITaskStep tempTaskFlowletInstance = (ITaskStep) getTaskFlowletSingletons()
				.get(taskName);
		if (tempTaskFlowletInstance != null) {
			if (!taskFlowletClass.getName().equals(
					tempTaskFlowletInstance.getClass().getName()))
				throw new AgentBuilderRuntimeException(
						"Exception while registering taskFlowlet class \""
								+ taskFlowletClass.getName()
								+ "\". Task with name \"" + taskName
								+ "\" is already registered for class \""
								+ tempTaskFlowletInstance.getClass().getName()
								+ "\".");
			else
				return taskFlowletInstance;
		}
		getTaskFlowletSingletons().put(taskName, taskFlowletInstance);

		return taskFlowletInstance;
	}

	/**
	 * Registers the flow. Puts the flow's singleton in the flow registry map.
	 * 
	 * @param className
	 *            the flow class's fully qualified class name
	 * @return the registered flow instance
	 * @throws AgentBuilderRuntimeException
	 *             if the class does not implement the IAgent interface or if
	 *             the classname is incorrect or if the flow instance cannot be
	 *             registered (already exists)
	 */
	static public IAgent registerFlow(String className)
			throws AgentBuilderRuntimeException {
		return registerFlow(getClass(className));
	}

	/**
	 * Registers the flow.
	 * 
	 * @param flowClass
	 * @return the registered flow instance
	 */
	static public IAgent registerFlow(Class flowClass) {
		Object instance = getInstance(flowClass);
		if (!(instance instanceof IAgent))
			throw new AgentBuilderRuntimeException("class \""
					+ flowClass.getName()
					+ "\" does not implement the IAgent interface.");

		IAgent flowInstance = (IAgent) instance;
		String flowName = flowInstance.getName();
		if (flowName == null || flowName.trim().length() == 0)
			throw new AgentBuilderRuntimeException("The name of flow \""
					+ flowClass.getName() + "\" is either empty or not set");

		// check for already registered flow with same name
		IAgent tempFlowInstance = getFlow(flowName);
		if (tempFlowInstance != null) {
			// throw new AgentBuilderRuntimeException
			System.err.println("Warning while registering flow class \""
					+ flowInstance.getClass().getName()
					+ "\". Flow with name \"" + flowName
					+ "\" is already registered for class \""
					+ tempFlowInstance.getClass().getName() + "\".");
		}

		getFlowname2FlowMap().put(flowName, flowInstance);
		getFlowclass2FlowMap().put(flowInstance.getClass(), flowInstance);

		return flowInstance;
	}

	/**
	 * Registers all flows in the collection.
	 * 
	 * @param flowClasses
	 */
	static public void registerFlows(Collection flowClasses) {
		if (flowClasses == null || flowClasses.isEmpty())
			return;
		for (Iterator iter = flowClasses.iterator(); iter.hasNext();)
			registerFlow((Class) iter.next());
	}

	/**
	 * Registers all flows which are in the flowrepository
	 * 
	 * @param agentRepository
	 */
	static public void registerFlows(IAgentRepository agentRepository) {
		if (agentRepository == null)
			return;
		registerFlows(agentRepository.getFlowClasses());
	}

	/**
	 * Executes the given flow with the given start node and an empty
	 * dictionary. Returns the automatically generated AgentBuilderDictionary
	 * which might have been manipulated/filled by flowlets.
	 * 
	 * @param flowName
	 *            the name of the flow
	 * @param startFlowletName
	 *            name of the start node
	 * @throws AgentBuilderRuntimeException
	 *             if something goes wrong
	 * @return the automatically generated AgentBuilderDictionary.
	 */
	static public AgentBuilderDictionary executeFlow(String flowName,
			String startFlowletName) throws AgentBuilderRuntimeException {
		AgentBuilderDictionary dict = new AgentBuilderDictionary();
		executeFlow(flowName, startFlowletName, dict);
		return dict;
	}

	/**
	 * Executes the flow with a newly created dictionary. The syntax for the
	 * flow and its start node is: <code>flowname-startflowletname</code>
	 * Returns the automatically generated AgentBuilderDictionary which might
	 * have been manipulated/filled by flowlets.
	 * 
	 * @param flowStart
	 *            the flow and it's start node
	 * @throws AgentBuilderRuntimeException
	 *             if something goes wrong
	 * @return the automatically generated AgentBuilderDictionary.
	 */
	static public AgentBuilderDictionary executeFlow(String flowStart)
			throws AgentBuilderRuntimeException {
		AgentBuilderDictionary dict = new AgentBuilderDictionary();
		executeFlow(flowStart, dict);
		return dict;
	}

	/**
	 * Executes the flow. The flow has to be registered prior calling this
	 * method. The syntax for the flow and it's start node is:<br/>
	 * <code>flowname-startflowletname</code>
	 * 
	 * @param flowStart
	 *            the flow and it's start node
	 * @param dictionary
	 *            the dictionary containing the runtime data
	 * @throws AgentBuilderRuntimeException
	 *             if something goes wrong
	 */
	static public void executeFlow(String flowStart,
			AgentBuilderDictionary dictionary)
			throws AgentBuilderRuntimeException {
		StringTokenizer tokenizer = new StringTokenizer(flowStart,
				AgentBuilderRuntimeConsts.AGENT_BUILDER_START_DELIMITER);
		if (tokenizer.countTokens() != 2)
			throw new AgentBuilderRuntimeException(
					"Invalid flow-start pair: \""
							+ flowStart
							+ "\". Valid syntax is {flowname}-{startflowletname}");

		String flowName = tokenizer.nextToken();
		String startFlowletName = tokenizer.nextToken();

		executeFlow(flowName, startFlowletName, dictionary);
	}

	/**
	 * Executes the start node of the given flow that uses the supported
	 * dictionary. The flow has to be registered prior calling this method. The
	 * syntax for the flow and it's start flowlet is:<br/>
	 * <code>flowname-startflowletname</code>
	 * 
	 * @param flowName
	 *            the name of the flow
	 * @param startFlowletName
	 *            name of the start flowlet
	 * @param dictionary
	 *            the map that contains runtime data
	 * @throws AgentBuilderRuntimeException
	 *             if something goes wrong
	 */
	static public void executeFlow(String flowName, String startFlowletName,
			AgentBuilderDictionary dictionary)
			throws AgentBuilderRuntimeException {
		IAgent flowSingleton = getFlow(flowName);
		if (flowSingleton == null)
			throw new AgentBuilderRuntimeException("Flow \"" + flowName
					+ "\" not found.");

		flowSingleton.execute(startFlowletName, dictionary);
	}

	/**
	 * Executes the start node of the given flow. The flow does not need to be
	 * registered when using this method, because the flow class is defined
	 * directly.<br/> You can use it like: <br/>
	 * <code>AgentBuilderManager.executeFlow(MyFlow.class, "Step");</code>
	 * Returns the automatically generated AgentBuilderDictionary which might
	 * have been manipulated/filled by flowlets.
	 * 
	 * @param flowClass
	 *            the flow's class
	 * @param startFlowletName
	 *            name of the start flowlet
	 * @return the automatically generated AgentBuilderDictionary.
	 */
	static public AgentBuilderDictionary executeFlow(Class flowClass,
			String startFlowletName) {
		AgentBuilderDictionary dict = new AgentBuilderDictionary();
		executeFlow(flowClass, startFlowletName, dict);
		return dict;
	}

	/**
	 * Executes the start node of the given flow and a valid flow dictionary.
	 * The flow does not need to be registered when using this method, because
	 * the flow class is defined directly.<br/> You can use it like: <br/>
	 * <code>AgentBuilderManager.executeFlow(MyFlow.class, "Step");</code>
	 * 
	 * @param flowClass
	 *            the flow's class
	 * @param startFlowletName
	 *            name of the start flowlet
	 * @param dictionary
	 *            the map that contains runtime data
	 */
	static public void executeFlow(Class flowClass, String startFlowletName,
			AgentBuilderDictionary dictionary) {
		IAgent flowSingleton = (IAgent) getFlowclass2FlowMap().get(flowClass);
		if (flowSingleton == null)
			flowSingleton = registerFlow(flowClass);

		flowSingleton.execute(startFlowletName, dictionary);
	}

	/**
	 * Returns the flow singleton instance associated to the given flow name
	 * 
	 * @param flowName
	 * @return the flow singleton instance
	 */
	static public IAgent getFlow(String flowName) {
		return (IAgent) getFlowname2FlowMap().get(flowName);
	}

	/**
	 * @return
	 */
	protected static Map getFlowclass2FlowMap() {
		return flowclass2FlowMap;
	}

	/**
	 * @return
	 */
	protected static Map getFlowname2FlowMap() {
		return flowname2FlowMap;
	}

	/**
	 * @return
	 */
	protected static Map getTaskFlowletSingletons() {
		return taskFlowletSingletons;
	}

	/**
	 * @param map
	 */
	protected static void setFlowclass2FlowMap(Map map) {
		flowclass2FlowMap = map;
	}

	/**
	 * @param map
	 */
	protected static void setFlowname2FlowMap(Map map) {
		flowname2FlowMap = map;
	}

	/**
	 * @param map
	 */
	protected static void setTaskFlowletSingletons(Map map) {
		taskFlowletSingletons = map;
	}

}
