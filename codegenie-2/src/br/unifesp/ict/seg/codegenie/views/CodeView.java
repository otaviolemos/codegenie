package br.unifesp.ict.seg.codegenie.views;


import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;

import br.unifesp.ict.seg.codegenie.CodeGenieImages;
import br.unifesp.ict.seg.codegenie.pool.MethodInterfacePool;
import br.unifesp.ict.seg.codegenie.pool.SlicePool;
import br.unifesp.ict.seg.codegenie.search.fileserver.FileServerConnector;
import br.unifesp.ict.seg.codegenie.search.slicer.SliceFile;
import br.unifesp.ict.seg.codegenie.search.slicer.SlicerConnector;
import br.unifesp.ict.seg.codegenie.search.solr.MySingleResult;


public class CodeView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "br.unifesp.ict.seg.codegenie.views.CodeView";

	private Text text;
	private Action remove;
	private Action addNtest;
	private MySingleResult obj;
	private Long qid;
	

	
	public CodeView() {
		
	}
	
	public void setText(MySingleResult obj, Long qid){
		this.obj =obj;
		this.qid = qid;
		Long eid = obj.getEntityID();
		FileServerConnector fsc = new FileServerConnector(eid,FileServerConnector.ENTITY);
		String text = fsc.getSourceCode();
		this.text.setText(text);
	}

	public void createPartControl(Composite parent) {
		text = new Text(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		makeActions();
		hookContextMenu();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				CodeView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(text);
		text.setMenu(menu);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(addNtest);
		manager.add(new Separator());
		manager.add(remove);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(addNtest);
		manager.add(remove);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(addNtest);
		manager.add(remove);
	}

	private void makeActions() {
		addNtest = new Action(){
			public void run(){
				Long eid = obj.getEntityID();
				SlicerConnector sc = new SlicerConnector(eid,qid);
				SliceFile slice = sc.getSlice();
				SlicePool.add(slice, eid, qid);
				slice.setTestClass(obj.getTestClass());
				slice.setMethodInterface(MethodInterfacePool.get(qid));
				try {
					slice.unzip();
					slice.merge();
					slice.saveAndRebuild();
					slice.createMethod();
					slice.saveAndRebuild();
					obj.setWeaven();
					slice.runTests(obj);
					showMessage("Slice "+eid+" added");
					//ResultsView.this.waitAndRefresh();
				} catch (IOException | CoreException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

				ResultsView.getCurrent().refresh();
			}
		};
		addNtest.setText("Test");
		addNtest.setToolTipText("Include and test slice");
		addNtest.setImageDescriptor(CodeGenieImages.getImageDescriptor(
				CodeGenieImages.ADD_N_TEST));
		remove = new Action() {
			public void run() {
				Long eid = obj.getEntityID();

				if(SliceFile.removeSlice(eid,obj)){
					showMessage("Slice "+eid+" removed");
				} else {
					showMessage("Could not remove slice "+eid);
				}
				SlicePool.remove(eid);
				ResultsView.getCurrent().refresh();
			}
		};
		remove.setText("Remove");
		remove.setToolTipText("Remove selected slice");
		remove.setImageDescriptor(CodeGenieImages.getImageDescriptor(
				CodeGenieImages.REMOVE));
		
	}

	protected void showMessage(String message) {
		MessageDialog.openInformation(
				text.getShell(),
				"CodeGenie view",
				message);
		
	}

	public void setFocus() {
		text.setFocus();
	}
}