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

package repast.simphony.agents.designer.figures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.agents.AgentBuilderConsts;
import repast.simphony.agents.base.FigureConsts;
import repast.simphony.agents.base.Point;

/**
 * @author greifa (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * TODO
 * 
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class AnchorRegistry {

	static private Map entries = new HashMap();

	/**
	 * TODO
	 * 
	 * @param type
	 * @return
	 */
	static private Entry getEntry(String type) {
		Entry entry = (Entry) entries.get(type);
		if (entry == null) {
			entry = new AnchorRegistry().new Entry();
			Point offset;

			if (AgentBuilderConsts.BEHAVIOR.equals(type)) {
				entry
						.getSourceAnchorOffsets()
						.add(
								new Point(
										FigureConsts.DIMENSION_FIGURE_START.width / 2,
										FigureConsts.DIMENSION_FIGURE_START.height));
				entry.setMaxSourceTransitions(1);
			} else if (AgentBuilderConsts.TASK.equals(type)) {
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_JAVATASK.width / 2, 0);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(0,
						FigureConsts.DIMENSION_FIGURE_JAVATASK.height / 2);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_JAVATASK.width / 2,
						FigureConsts.DIMENSION_FIGURE_JAVATASK.height);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_JAVATASK.width,
						FigureConsts.DIMENSION_FIGURE_JAVATASK.height / 2);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				entry.setMaxSourceTransitions(1);
				entry.setMaxTargetTransitions(1);
			} else if (AgentBuilderConsts.EXTRA_ITEM.equals(type)) {
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_JAVATASK.width / 2, 0);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(0,
						FigureConsts.DIMENSION_FIGURE_JAVATASK.height / 2);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_JAVATASK.width / 2,
						FigureConsts.DIMENSION_FIGURE_JAVATASK.height);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_JAVATASK.width,
						FigureConsts.DIMENSION_FIGURE_JAVATASK.height / 2);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				entry.setMaxSourceTransitions(1);
				entry.setMaxTargetTransitions(1);
			} else if (AgentBuilderConsts.DECISION.equals(type)) {
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_DECISION.width / 2, 0);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(0,
						FigureConsts.DIMENSION_FIGURE_DECISION.height / 2);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_DECISION.width / 2,
						FigureConsts.DIMENSION_FIGURE_DECISION.height);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_DECISION.width,
						FigureConsts.DIMENSION_FIGURE_DECISION.height / 2);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				entry.setMaxSourceTransitions(2);
				entry.setMaxTargetTransitions(1);
			} else if (AgentBuilderConsts.JOIN.equals(type)) {
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_JOIN.width / 2, 0);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(0,
						FigureConsts.DIMENSION_FIGURE_JOIN.height / 2);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_JOIN.width / 2,
						FigureConsts.DIMENSION_FIGURE_JOIN.height);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(FigureConsts.DIMENSION_FIGURE_JOIN.width,
						FigureConsts.DIMENSION_FIGURE_JOIN.height / 2);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				entry.setMaxSourceTransitions(1);
				entry.setMaxTargetTransitions(3);
			} else if (AgentBuilderConsts.LOOP.equals(type)) {
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_CALL.width / 2, 0);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_CALL.width / 2,
						FigureConsts.DIMENSION_FIGURE_CALL.height - 9);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				entry.setMaxSourceTransitions(1);
				entry.setMaxTargetTransitions(1);
			} else if (AgentBuilderConsts.SCAN.equals(type)) {
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_JUMP.width / 2,
						FigureConsts.DIMENSION_FIGURE_JUMP.height / 2 - 7);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(0,
						FigureConsts.DIMENSION_FIGURE_JUMP.height / 2);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_JUMP.width / 2,
						FigureConsts.DIMENSION_FIGURE_JUMP.height / 2 + 7);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				entry.setMaxSourceTransitions(0);
				entry.setMaxTargetTransitions(3);
			} else if (AgentBuilderConsts.PROPERTY.equals(type)) {
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_TEMPLATE.width / 2, 0);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(AgentPropertyFigure.h,
						FigureConsts.DIMENSION_FIGURE_TEMPLATE.height / 2);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(
						FigureConsts.DIMENSION_FIGURE_TEMPLATE.width / 2,
						FigureConsts.DIMENSION_FIGURE_TEMPLATE.height);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				offset = new Point(FigureConsts.DIMENSION_FIGURE_TEMPLATE.width
						- AgentPropertyFigure.h,
						FigureConsts.DIMENSION_FIGURE_TEMPLATE.height / 2);
				entry.getTargetAnchorOffsets().add(offset);
				entry.getSourceAnchorOffsets().add(offset);
				entry.setMaxSourceTransitions(0);
				entry.setMaxTargetTransitions(1);
			} else if (AgentBuilderConsts.END.equals(type)) {
				entry.getTargetAnchorOffsets().add(
						new Point(FigureConsts.DIMENSION_FIGURE_END.width / 2,
								0));
				entry.setMaxTargetTransitions(1);
			} else
				System.out
						.println("AnchorRegistry.getEntry() unknown anchor entry type: "
								+ type);
			// store in Map
			entries.put(type, entry);
		}

		return entry;
	}

	/**
	 * TODO
	 * 
	 * @param type
	 * @param index
	 * @return
	 */
	static public Point getSourceAnchorOffset(String type, int index) {
		return (Point) getEntry(type).getSourceAnchorOffsets().get(index);
	}

	/**
	 * TODO
	 * 
	 * @param type
	 * @param index
	 * @return
	 */
	static public Point getTargetAnchorOffset(String type, int index) {
		return (Point) getEntry(type).getTargetAnchorOffsets().get(index);
	}

	/**
	 * TODO
	 * 
	 * @param type
	 * @return
	 */
	static public Point getSourceAnchorOffset(String type) {
		return getSourceAnchorOffset(type, 0);
	}

	/**
	 * TODO
	 * 
	 * @param type
	 * @return
	 */
	static public Point gettargetAnchorOffset(String type) {
		return getTargetAnchorOffset(type, 0);
	}

	/**
	 * TODO
	 * 
	 * @param type
	 * @return
	 */
	static public List getSourceAnchorOffsets(String type) {
		return getEntry(type).getSourceAnchorOffsets();
	}

	/**
	 * TODO
	 * 
	 * @param type
	 * @return
	 */
	static public List getTargetAnchorOffsets(String type) {
		return getEntry(type).getTargetAnchorOffsets();
	}

	/**
	 * TODO
	 * 
	 * @param type
	 * @return
	 */
	static public int getMaxSourceTransitions(String type) {
		return getEntry(type).getMaxSourceTransitions();
	}

	/**
	 * TODO
	 * 
	 * @param type
	 * @return
	 */
	static public int getMaxTargetTransitions(String type) {
		return getEntry(type).getMaxTargetTransitions();
	}

	/**
	 * Holds all inforation on a flowlet's anchors.
	 * 
	 */
	@SuppressWarnings("unchecked")
	private class Entry {
		private List sourceAnchorOffsets = new ArrayList();
		private List targetAnchorOffsets = new ArrayList();
		private int maxSourceTransitions;
		private int maxTargetTransitions;

		/**
		 * TODO
		 * 
		 * @return
		 */
		public int getMaxSourceTransitions() {
			return maxSourceTransitions;
		}

		/**
		 * TODO
		 * 
		 * @return
		 */
		public int getMaxTargetTransitions() {
			return maxTargetTransitions;
		}

		/**
		 * TODO
		 * 
		 * @param i
		 */
		public void setMaxSourceTransitions(int i) {
			maxSourceTransitions = i;
		}

		/**
		 * TODO
		 * 
		 * @param i
		 */
		public void setMaxTargetTransitions(int i) {
			maxTargetTransitions = i;
		}

		/**
		 * TODO
		 * 
		 * @return
		 */
		public List getSourceAnchorOffsets() {
			return sourceAnchorOffsets;
		}

		/**
		 * TODO
		 * 
		 * @return
		 */
		public List getTargetAnchorOffsets() {
			return targetAnchorOffsets;
		}

	}

}
