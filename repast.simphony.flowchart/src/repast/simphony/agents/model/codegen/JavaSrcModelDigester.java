/*
 * Copyright (c) 2003-2004, Alexander Greif. All rights reserved. (Adapted by
 * Michael J. North for Use in Repast Simphony from Alexander Greif’s
 * Flow4J-Eclipse (http://flow4jeclipse.sourceforge.net/docs/index.html), with
 * Thanks to the Original Author) (Michael J. North’s Modifications are
 * Copyright 2007 Under the Repast Simphony License, All Rights Reserved)
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

package repast.simphony.agents.model.codegen;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.rmi.MarshalException;
import java.util.Iterator;
import java.util.Stack;

import repast.simphony.agents.base.IOUtils;
import repast.simphony.agents.model.AgentStructureModelDigester;
import repast.simphony.agents.model.IAgentVisitor;
import repast.simphony.agents.model.bind.AgentModelBind;
import repast.simphony.agents.model.bind.AgentPropertyBind;
import repast.simphony.agents.model.bind.BehaviorStepBind;
import repast.simphony.agents.model.bind.BindingHandler;
import repast.simphony.agents.model.bind.DecisionStepBind;
import repast.simphony.agents.model.bind.EndStepBind;
import repast.simphony.agents.model.bind.IPropertyOrStepBind;
import repast.simphony.agents.model.bind.JoinStepBind;
import repast.simphony.agents.model.bind.TaskStepBind;

/**
 * @author greifa (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class JavaSrcModelDigester extends AgentStructureModelDigester {

	private static final String AGENT_BUILDER_XDOCLET_PREFIX = "agentbuilder.";

	protected File scriptRootFolder;
	private JavaFile javaFile;
	// private String flowName;
	protected boolean bsfNeeded;
	// private Set scriptLanguageNames;
	protected Stack joinStack = new Stack();

	/**
	 * Crates this Digester and also a JavaClass instance
	 * 
	 * @param className
	 *            the name of the class
	 * @param packageName
	 *            the package name or null if the default package
	 */
	private JavaSrcModelDigester(String className, String packageName,
			File scriptRootFolder, String comment, String imports,
			String interfaces, String parentClassName) {
		super();
		javaFile = new JavaFile();
		javaFile.getJavaClass().setClassName(className);
		javaFile.getJavaClass().setComment(comment);
		javaFile.getJavaClass().setSuperClassName(parentClassName);

		if (DepthCounter.isSet(imports)) {
			String[] importList = imports.split(",");
			for (int i = 0; i < importList.length; i++) {
				javaFile.addImport(importList[i].trim());
			}
		}

		if (DepthCounter.isSet(interfaces)) {
			String[] interfaceList = interfaces.split(",");
			for (int i = 0; i < interfaceList.length; i++) {
				javaFile.getJavaClass().addInterface(interfaceList[i].trim());
			}
		}

		javaFile.setPackageName(packageName);
		javaFile.getJavaClass().setJavadoc(new JavadocBlock());
		this.scriptRootFolder = scriptRootFolder;
	}

	/**
	 * Generates the flow's java source code and stores it in the output file
	 * 
	 * @param flowFile
	 * @param packagePath
	 * @param destDir
	 * @param scriptRootFolder
	 * @throws IOException
	 * @throws MappingException
	 * @throws MarshalException
	 * @throws ValidationException
	 */
	public static void generateCode(File flowFile, String packagePath,
			File destDir, File scriptRootFolder) throws IOException {

		// generate java source code
		InputStream javaSrcIn = generateCode(flowFile, packagePath,
				scriptRootFolder);
		File outputFolder = new File(destDir, packagePath.replace('.',
				File.separatorChar));
		String fileName = flowFile.getName();
		fileName = fileName.substring(0, fileName.lastIndexOf(".") + 1)
				+ Consts.FILE_EXTENSION_GROOVY;
		File outputFile = new File(outputFolder, fileName);
		OutputStream javaSrcOut = new FileOutputStream(outputFile);
		IOUtils.copyBufferedStream(javaSrcIn, javaSrcOut);
		javaSrcIn.close();
		javaSrcOut.close();
	}

	/**
	 * Returns a stream on the generated java source code. Creates an instancce
	 * of this class that builds the JavaClass object. Then the JavaClass object
	 * is serialized and a stream to the source code is returned
	 * 
	 * @param flowFile
	 * @param packagePath
	 * @return a stream on the generated java source code.
	 * @throws IOException
	 * @throws MappingException
	 * @throws MarshalException
	 * @throws ValidationException
	 */
	public static InputStream generateCode(File flowFile, String packagePath,
			File scriptRootFolder) throws IOException {

		String fileName = flowFile.getName();
		String className = fileName.substring(0, fileName.lastIndexOf("."));
		AgentModelBind agentModelBind = BindingHandler.getInstance()
				.loadFlowModel(flowFile,
						new String().getClass().getClassLoader());
		JavaSrcModelDigester digester = new JavaSrcModelDigester(className,
				packagePath, scriptRootFolder, agentModelBind.getComment(),
				agentModelBind.getImports(), agentModelBind.getInterfaces(),
				agentModelBind.getParentClassName());

		// digest all parts of the flow model
		digester.digest(agentModelBind);
		// generate the java class
		digester.generateClassSrc(agentModelBind);
		// serialize sources
		StringWriter stringWriter = new StringWriter();
		BufferedWriter writer = new BufferedWriter(stringWriter);
		digester.serialize(writer);
		writer.close();
		String source = stringWriter.toString();
		// pretty print sources
		try {
			// if (prettySettings != null)
			// source = PrettyPrinterUtils.prettyPrintSource(source);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return new ByteArrayInputStream(source.getBytes());
	}

	/**
	 * Generates the class's source, all fields and methods.
	 */
	public void generateClassSrc(AgentModelBind agentModelBind) {
		javaFile.getJavaClass().addModifier(Consts.MODIFIER_PUBLIC);

		// generate execute() method
		generateExecuteMethodSrc(agentModelBind);
		// generate the class's javadoc
		// generateClassJavadoc();
		// generate BSF Manager
		// generateBSFComponents();
	}

	/**
	 * Generates the source code of the flow's <code>execute()</code> method
	 */
	private void generateExecuteMethodSrc(AgentModelBind agentModelBind) {

		// visit all agent properties
		for (Iterator iter = getPropertyFlowletNodes().iterator(); iter
				.hasNext();) {
			IFlowNode propertyFlowletNode = (IFlowNode) iter.next();
			propertyFlowletNode.visit(null);
		}

		// visit all agent behaviors
		for (Iterator iter = getStartFlowletNodes().iterator(); iter.hasNext();) {
			IFlowNode startFlowletNode = (IFlowNode) iter.next();
			startFlowletNode.visit(null);
		}

	}

	/**
	 * Serializes the class's java source code to the given writer.
	 * 
	 * @param writer
	 *            the writer were the sources of the java file should be written
	 */
	public void serialize(BufferedWriter writer) throws IOException {
		javaFile.serialize(writer);
	}

	/**
	 * This visitor is passed around between the flow nodes. Flowlets
	 * individually adjust the code block where the following flowlet should put
	 * it's code.
	 */
	public class JavaSrcVisitor implements IAgentVisitor {
		CodeBlock codeBlock;

		JavaSrcVisitor(CodeBlock codeBlock) {
			this.codeBlock = codeBlock;
		}

		public CodeBlock getCodeBlock() {
			return codeBlock;
		}

		public void setCodeBlock(CodeBlock block) {
			codeBlock = block;
		}

	}

	private class BehaviorStepNode extends
			AgentStructureModelDigester.StartFlowletNode {

		BehaviorStepNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}

		@Override
		public void visit(IAgentVisitor visitor) {
			if (isVisited())
				return;
			BehaviorStepBind flowletBind = (BehaviorStepBind) getFlowletBind();

			String methodName = flowletBind.getCompiledName();

			// method for startFlowlet
			Method method = new Method(methodName);

			method.setReturnType(flowletBind.getReturnType());
			method.setComment(flowletBind.getComment());
			if (flowletBind.getLabel() != null) {
				method.setDisplayName(flowletBind.getLabel().getText());
			}
			
			String parametersString = flowletBind.getParameters();
			if (DepthCounter.isSet(parametersString)) {
				String[] parametersList = parametersString.split(",");
				for (String parameterName : parametersList) {
					method
							.addMethodParameter(new MethodParameter(
									parameterName));
				}
			}

			String exceptionsString = flowletBind.getExceptions();
			if (DepthCounter.isSet(exceptionsString)) {
				String[] exceptionsList = exceptionsString.split(",");
				for (String exceptionName : exceptionsList) {
					method.addThrowsExceptionType(exceptionName);
				}
			}

			if (flowletBind.getVisibility().equals("0")) {
				method.addModifier(Consts.MODIFIER_PUBLIC);
			} else if (flowletBind.getVisibility().equals("1")) {
				method.addModifier(Consts.MODIFIER_PRIVATE);
			} else if (flowletBind.getVisibility().equals("2")) {
				method.addModifier(Consts.MODIFIER_PROTECTED);
			} else if (flowletBind.getVisibility().equals("3")) {
			} else if (flowletBind.getVisibility().equals("4")) {
				method.addModifier(Consts.MODIFIER_PUBLIC);
				method.addModifier(Consts.MODIFIER_STATIC);
			} else if (flowletBind.getVisibility().equals("5")) {
				method.addModifier(Consts.MODIFIER_PRIVATE);
				method.addModifier(Consts.MODIFIER_STATIC);
			} else if (flowletBind.getVisibility().equals("6")) {
				method.addModifier(Consts.MODIFIER_PROTECTED);
				method.addModifier(Consts.MODIFIER_STATIC);
			} else if (flowletBind.getVisibility().equals("7")) {
				method.addModifier(Consts.MODIFIER_STATIC);
			}

			method.setScheduleAnnotationStart(flowletBind
					.getScheduleAnnotationStart());
			method.setScheduleAnnotationPick(flowletBind
					.getScheduleAnnotationPick());
			method.setScheduleAnnotationInterval(flowletBind
					.getScheduleAnnotationInterval());
			method.setScheduleAnnotationPriority(flowletBind
					.getScheduleAnnotationPriority());
			method.setScheduleAnnotationDuration(flowletBind
					.getScheduleAnnotationDuration());
			method.setScheduleAnnotationShuffle(flowletBind
					.getScheduleAnnotationShuffle());

			method.setWatchAnnotationId(flowletBind.getWatchAnnotationId());
			method.setWatchAnnotationQuery(flowletBind
					.getWatchAnnotationQuery());
			method.setWatchAnnotationTargetClassName(flowletBind
					.getWatchAnnotationTargetClassName());
			method.setWatchAnnotationTargetFieldNames(flowletBind
					.getWatchAnnotationTargetFieldNames());
			method.setWatchAnnotationTriggerCondition(flowletBind
					.getWatchAnnotationTriggerCondition());
			method.setWatchAnnotationTriggerSchedule(flowletBind
					.getWatchAnnotationTriggerSchedule());
			method.setWatchAnnotationTriggerDelta(flowletBind
					.getWatchAnnotationTriggerDelta());
			method.setWatchAnnotationTriggerPriority(flowletBind
					.getWatchAnnotationTriggerPriority());
			method.setWatchAnnotationPick(flowletBind.getWatchAnnotationPick());

			javaFile.getJavaClass().addMethod(method);

			createJavadoc(flowletBind);

			setVisited(true);
			if (visitor == null) {
				visitor = new JavaSrcVisitor(method.getCodeBlock());
			} else {
				((JavaSrcVisitor) visitor).setCodeBlock(method.getCodeBlock());
			}
			super.visit(visitor);
		}

		private void createJavadoc(BehaviorStepBind flowletBind) {
			if (flowletBind.getProperties() == null)
				return;
			// JavadocBlock javadoc = javaFile.getJavaClass().getJavadoc();
			// JavadocBlock.XDocletTag xdTag = javadoc.new
			// XDocletTag(AGENT_BUILDER_XDOCLET_PREFIX + "start-flowlet");
			// javadoc.addTag(xdTag);
			// String propNames = "";
			// for (Iterator iter = flowletBind.getProperties().iterator();
			// iter.hasNext();) {
			// PropertyBind propBind = (PropertyBind) iter.next();
			// xdTag.addParam("value-" + propBind.getName(),
			// propBind.getValue());
			// propNames += propBind.getName();
			// if (iter.hasNext())
			// propNames += ",";
			// }
			// xdTag.addParam("propertyNames", propNames);
		}
	}

	private class TaskStepNode extends
			AgentStructureModelDigester.TaskFlowletNode {
		public TaskStepNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}

		@Override
		public void visit(IAgentVisitor visitor) {
			if (isVisited())
				return;

			TaskStepBind flowletBind = (TaskStepBind) getFlowletBind();

			if (DepthCounter.isSet(flowletBind.getComment())) {
				((JavaSrcVisitor) visitor).getCodeBlock().addFragment(
						new CodeLine("// " + flowletBind.getComment()));
			}

			if (DepthCounter.isSet(flowletBind.getCommand1())) {
				((JavaSrcVisitor) visitor).getCodeBlock()
						.addFragment(
								new CodeLine(fixSetters(flowletBind
										.getCommand1())
										+ ""));
			}

			if (DepthCounter.isSet(flowletBind.getCommand2())) {
				((JavaSrcVisitor) visitor).getCodeBlock()
						.addFragment(
								new CodeLine(fixSetters(flowletBind
										.getCommand2())
										+ ""));
			}

			if (DepthCounter.isSet(flowletBind.getCommand3())) {
				((JavaSrcVisitor) visitor).getCodeBlock()
						.addFragment(
								new CodeLine(fixSetters(flowletBind
										.getCommand3())
										+ ""));
			}

			if (DepthCounter.isSet(flowletBind.getCommand4())) {
				((JavaSrcVisitor) visitor).getCodeBlock()
						.addFragment(
								new CodeLine(fixSetters(flowletBind
										.getCommand4())
										+ ""));
			}

			if (DepthCounter.isSet(flowletBind.getCommand5())) {
				((JavaSrcVisitor) visitor).getCodeBlock()
						.addFragment(
								new CodeLine(fixSetters(flowletBind
										.getCommand5())
										+ ""));
			}

			setVisited(true);
			super.visit(visitor);
		}

		private String fixSetters(String originalCodeLine) {
			String results = originalCodeLine;
			int inIndex = originalCodeLine.indexOf("=");
			if (inIndex >= 0) {
				if ((originalCodeLine.substring(0, inIndex).indexOf("\"") < 0)
						&& (originalCodeLine.substring(0, inIndex).indexOf(";") < 0)
						&& (originalCodeLine.substring(0, inIndex).indexOf("{") < 0)) {
					String variable = originalCodeLine.substring(0, inIndex)
							.trim();
					boolean foundVariable = false;
					Iterator it = this.agentModelBind.getPropertyFlowlets()
							.iterator();
					while (it.hasNext() && !foundVariable) {
						Object object = it.next();
						AgentPropertyBind propertyBind = (AgentPropertyBind) object;
						if (DepthCounter.isSet(propertyBind
								.getPropertyCompiledName())) {
							foundVariable = propertyBind
									.getPropertyCompiledName().equals(variable);
						}
					}
					if (foundVariable) {
						if (variable.length() == 1) {
							variable = variable.toUpperCase();
						} else {
							variable = variable.substring(0, 1).toUpperCase()
									+ variable.substring(1);
						}
						results = "set"
								+ variable
								+ "("
								+ originalCodeLine.substring(inIndex + 1)
										.trim() + ")";
					}
				}
			}
			return results;
		}

	}

	private class DecisionStepNode extends
			AgentStructureModelDigester.DecisionFlowletNode {
		public DecisionStepNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}

		@Override
		public void visit(IAgentVisitor visitor) {
			if (isVisited())
				return;
			DecisionStepBind flowletBind = (DecisionStepBind) getFlowletBind();
			IfThenElse ifThenElse = new IfThenElse();

			ifThenElse.setBooleanStatement(flowletBind.getBooleanStatement());
			ifThenElse.setComment(flowletBind.getComment());
			ifThenElse.setBranchType(flowletBind.getBranchType());

			CodeBlock parentCodeBlock = ((JavaSrcVisitor) visitor)
					.getCodeBlock();
			((JavaSrcVisitor) visitor).getCodeBlock().addFragment(ifThenElse);

			createJavadoc(flowletBind);

			setVisited(true);
			// true block
			((JavaSrcVisitor) visitor).setCodeBlock(ifThenElse.getTrueBlock());
			if (getFollowingTrueFlowletNode() != null) {
				getFollowingTrueFlowletNode().visit(visitor);
			}
			// false block
			((JavaSrcVisitor) visitor).setCodeBlock(ifThenElse.getFalseBlock());
			if (getFollowingFalseFlowletNode() != null) {
				getFollowingFalseFlowletNode().visit(visitor);
			}

			((JavaSrcVisitor) visitor).setCodeBlock(parentCodeBlock);
			if (joinStack.size() > 0)
				((IFlowNode) joinStack.pop()).visit(visitor);
		}

		private void createJavadoc(DecisionStepBind flowletBind) {
			if (flowletBind.getDescription() == null)
				return;
			JavadocBlock javadoc = javaFile.getJavaClass().getJavadoc();
			JavadocBlock.XDocletTag xdTag = javadoc.new XDocletTag(
					AGENT_BUILDER_XDOCLET_PREFIX + "decision-flowlet");
			javadoc.addTag(xdTag);
			xdTag.addParam("name", flowletBind.getDescription());
			StringBuffer buf = new StringBuffer(flowletBind.getLabel()
					.getText());
			int i = buf.toString().indexOf("\"");
			while (i > -1) {
				buf.replace(i, i + 1, "&quot;");
				i = buf.toString().indexOf("\"");
			}
			xdTag.addParam("statement", buf.toString());
		}
	}

	private class JoinStepNode extends
			AgentStructureModelDigester.JoinFlowletNode {
		Method method;

		JoinStepNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}

		@Override
		public void visit(IAgentVisitor visitor) {
			if (isVisited())
				return;

			// CodeBlock codeBlock;
			if (getPreviousFlowletNodes().size() != 1) {
				if (!isVisited()) {
				}
			}

			setVisited(true);
			if (super.followingFlowletNode != null)
				joinStack.push(super.followingFlowletNode);
		}
	}

	private class AgentPropertyNode extends
			AgentStructureModelDigester.PropertyFlowletNode {
		public AgentPropertyNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}

		@Override
		public void visit(IAgentVisitor visitor) {
			if (isVisited())
				return;
			AgentPropertyBind flowletBind = (AgentPropertyBind) getFlowletBind();

			Field localField = new Field(flowletBind.getPropertyCompiledName());
			localField.setComment(flowletBind.getPropertyComment());
			if (flowletBind.getLabel() != null) {
				localField.setDisplayName(flowletBind.getLabel().getText());
			}
			localField.setType(flowletBind.getPropertyType());
			localField.setDefaultValue(flowletBind.getPropertyDefaultValue());
			String vis = flowletBind.getVisibility().toString();
			if (vis.equals("0")) {
				localField.addModifier(Consts.MODIFIER_PUBLIC);
			} else if (vis.equals("1")) {
				localField.addModifier(Consts.MODIFIER_PRIVATE);
			} else if (vis.equals("2")) {
				localField.addModifier(Consts.MODIFIER_PROTECTED);
			} else if (vis.equals("3")) {
			} else if (vis.equals("4")) {
				localField.addModifier(Consts.MODIFIER_PUBLIC);
				localField.addModifier(Consts.MODIFIER_STATIC);
			} else if (vis.equals("5")) {
				localField.addModifier(Consts.MODIFIER_PRIVATE);
				localField.addModifier(Consts.MODIFIER_STATIC);
			} else if (vis.equals("6")) {
				localField.addModifier(Consts.MODIFIER_PROTECTED);
				localField.addModifier(Consts.MODIFIER_STATIC);
			} else if (vis.equals("7")) {
				localField.addModifier(Consts.MODIFIER_STATIC);
			}

			javaFile.getJavaClass().addField(localField);

			setVisited(true);
			super.visit(visitor);
		}

	}

	private class EndStepNode extends
			AgentStructureModelDigester.EndFlowletNode {
		public EndStepNode(IPropertyOrStepBind propertyOrStepBind,
				AgentModelBind agentModelBind) {
			super(propertyOrStepBind, agentModelBind);
		}

		@Override
		public void visit(IAgentVisitor visitor) {
			if (isVisited())
				return;

			setVisited(true);
			super.visit(visitor);
		}
	}

	/**
	 * @see repast.simphony.agents.model.IModelDigester#digest(repast.simphony.agents.model.bind.AgentModelBind)
	 */
	@Override
	public void digest(AgentModelBind agentModelBind) {
		super.digest(agentModelBind);
	}

	@Override
	public void digestBehaviorStepFlowlet(BehaviorStepBind flowletBind,
			AgentModelBind agentModelBind) {
		addFlowletNode(new BehaviorStepNode(flowletBind, agentModelBind));
	}

	@Override
	public void digestTaskFlowlet(TaskStepBind flowletBind,
			AgentModelBind agentModelBind) {
		addFlowletNode(new TaskStepNode(flowletBind, agentModelBind));
	}

	@Override
	public void digestDecisionFlowlet(DecisionStepBind flowletBind,
			AgentModelBind agentModelBind) {
		addFlowletNode(new DecisionStepNode(flowletBind, agentModelBind));
	}

	@Override
	public void digestJoinFlowlet(JoinStepBind flowletBind,
			AgentModelBind agentModelBind) {
		addFlowletNode(new JoinStepNode(flowletBind, agentModelBind));
	}

	@Override
	public void digestPropertyFlowlet(AgentPropertyBind flowletBind,
			AgentModelBind agentModelBind) {
		addFlowletNode(new AgentPropertyNode(flowletBind, agentModelBind));
	}

	@Override
	public void digestEndFlowlet(EndStepBind flowletBind,
			AgentModelBind agentModelBind) {
		addFlowletNode(new EndStepNode(flowletBind, agentModelBind));
	}
}
