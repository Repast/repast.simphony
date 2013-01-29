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

package repast.simphony.agents.designer.ui.views;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.navigator.MainActionGroup;

import repast.simphony.agents.designer.core.AgentBuilderPlugin;

/**
 * @author agreif (Adapted by Michael J. North for Use in Repast Simphony from
 *         Alexander Greif’s Flow4J-Eclipse
 *         (http://flow4jeclipse.sourceforge.net/docs/index.html), with Thanks
 *         to the Original Author)
 * 
 * TODO
 * 
 * 
 * 
 */
public class AgentBuilderResourcesView extends ViewPart implements
		ISetSelectionTarget, IMenuListener {

	protected TreeViewer treeViewer;
	protected MainActionGroup mainActionGroup;

	/**
	 * TODO
	 * 
	 */
	public AgentBuilderResourcesView() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL);
		treeViewer.addFilter(new AgentBuilderResourcesViewerFilter());
		treeViewer.setContentProvider(new WorkbenchContentProvider());
		treeViewer.setLabelProvider(new WorkbenchLabelProvider());
		treeViewer.setInput(this.getInitialInput());
		treeViewer.addOpenListener(new AgentBuilderResourcesViewOpenListener(
				this));

		MenuManager menuMgr = new MenuManager(
				"repast.simphony.agents.ui.PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(this);

		Menu fContextMenu = menuMgr.createContextMenu(treeViewer.getTree());
		treeViewer.getTree().setMenu(fContextMenu);

		// Register viewer with site. This must be done before making the
		// actions.
		IWorkbenchPartSite site = this.getSite();
		site.registerContextMenu(menuMgr, treeViewer);
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	/**
	 * TODO
	 * 
	 * @return
	 */
	private Object getInitialInput() {
		IAdaptable input = this.getSite().getPage().getInput();
		IResource resource = null;
		if (input instanceof IResource) {
			resource = (IResource) input;
		} else {
			resource = (IResource) input.getAdapter(IResource.class);
		}

		if (resource != null) {
			int type = resource.getType();
			if (type == IResource.FILE)
				return resource.getParent();
			else if (type == IResource.FOLDER || type == IResource.PROJECT
					|| type == IResource.ROOT)
				return resource;
		}
		return AgentBuilderPlugin.getWorkspace().getRoot();
	}

	/**
	 * @see org.eclipse.ui.part.ISetSelectionTarget#selectReveal(org.eclipse.jface.viewers.ISelection)
	 */
	public void selectReveal(ISelection selection) {
		treeViewer.setSelection(selection, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IMenuListener#menuAboutToShow(org.eclipse.jface.action.IMenuManager)
	 */
	public void menuAboutToShow(IMenuManager manager) {
		// TODO Auto-generated method stub

	}

}
