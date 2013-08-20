package br.unifesp.ict.seg.codegenie.views;

import java.io.IOException;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

import br.unifesp.ict.seg.codegenie.CodeGenieImages;
import br.unifesp.ict.seg.codegenie.pool.MethodInterfacePool;
import br.unifesp.ict.seg.codegenie.pool.SlicePool;
import br.unifesp.ict.seg.codegenie.pool.SolrPool;
import br.unifesp.ict.seg.codegenie.popup.actions.SearchAction;
import br.unifesp.ict.seg.codegenie.search.slicer.SliceFile;
import br.unifesp.ict.seg.codegenie.search.slicer.SlicerConnector;
import br.unifesp.ict.seg.codegenie.search.solr.MySingleResult;
import br.unifesp.ict.seg.codegenie.tmp.Debug;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class ResultsView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "br.unifesp.ict.seg.codegenie.views.ResultsView";

	protected TableViewer viewer;
	protected Action refresh;
	protected Action add;
	protected Action remove;
	protected Action viewSourceCode;
	protected Action editQuery;
	protected Action doubleClickAction;
	
	protected boolean canRefresh;

	private static ResultsView current = null;
	public static ResultsView getCurrent(){return current;}
	public Action getTestAction(){return add;}



	class ViewContentProvider implements IStructuredContentProvider {

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return SolrPool.all();
		}
	}

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			if(obj instanceof MySingleResult){
				MySingleResult sr = (MySingleResult) obj;
				if(sr.getSuccess()==-1){
					return CodeGenieImages.getImageDescriptor(
							CodeGenieImages.YELLOW_IMG).createImage();
				} else if(sr.getSuccess()==sr.getTotal()){
					return CodeGenieImages.getImageDescriptor(
							CodeGenieImages.GREEN_IMG).createImage();
				} else {
					return CodeGenieImages.getImageDescriptor(
							CodeGenieImages.RED_IMG).createImage();
				}
			}
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	class NameSorter extends ViewerSorter {

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ViewerComparator#compare(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			if(e1 instanceof MySingleResult && e2 instanceof MySingleResult){
				MySingleResult sr1 = (MySingleResult) e1;
				MySingleResult sr2 = (MySingleResult) e2;
				int s1 = sr1.getSuccess();
				int s2 = sr2.getSuccess();
				if(s1==-1 || s2==-1){//one of them is not tested
					if(s1==-1 && s2==-1){
						return 0;
					} else if(s1==-1){//s1 is not tested
						if(s2==sr2.getTotal()){
							return 1;
						}
						return -1;
					} else { //s2 is not tested
						if(s1==sr1.getTotal()){
							return -1;
						}
						return 1;
					}
				}
				return sr2.getSuccess()-sr1.getSuccess();
			}
			return super.compare(viewer, e1, e2);
		}
		
	}

	/**
	 * The constructor.
	 */
	public ResultsView() {
		current=this;
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				ResultsView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);

	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(refresh);
		manager.add(new Separator());
		manager.add(add);
		manager.add(new Separator());
		manager.add(remove);
		manager.add(new Separator());
		manager.add(viewSourceCode);
		manager.add(new Separator());
		manager.add(editQuery);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(refresh);
		manager.add(add);
		manager.add(remove);
		manager.add(viewSourceCode);
		manager.add(editQuery);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(refresh);
		manager.add(add);
		manager.add(remove);
		manager.add(viewSourceCode);
		manager.add(editQuery);
	}


	private void makeActions() {
		refresh = new Action() {
			public void run() {
				viewer.refresh();
			}
		};
		refresh.setText("Refresh");
		refresh.setToolTipText("Refresh view");
		refresh.setImageDescriptor(
				CodeGenieImages.getImageDescriptor(CodeGenieImages.REFRESH));

		add = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				MySingleResult obj = (MySingleResult) ((IStructuredSelection)selection).getFirstElement();
				Long eid = obj.getEntityID();
				Long qid = SearchAction.getCurrentQueryID();
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
					obj.setWoven();
					slice.runTests(obj);
					showMessage("Slice "+eid+" added");
					//ResultsView.this.waitAndRefresh();
				} catch (IOException | CoreException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				viewer.refresh();
				//showMessage("Action 2 executed ("+obj.getClass()+")");
			}

		};
		add.setText("Test");
		add.setToolTipText("Include and test slice");
		add.setImageDescriptor(CodeGenieImages.getImageDescriptor(
				CodeGenieImages.ADD_N_TEST));
		//add.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
		//		getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		remove = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				MySingleResult obj = (MySingleResult) ((IStructuredSelection)selection).getFirstElement();
				Long eid = obj.getEntityID();

				if(SliceFile.removeSlice(eid,obj)){
					showMessage("Slice "+eid+" removed");
				} else {
					showMessage("Could not remove slice "+eid);
				}
				SlicePool.remove(eid);
				viewer.refresh();
			}
		};
		remove.setText("Remove");
		remove.setToolTipText("Remove selected slice");
		remove.setImageDescriptor(CodeGenieImages.getImageDescriptor(
				CodeGenieImages.REMOVE));

		viewSourceCode = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				MySingleResult obj = (MySingleResult) ((IStructuredSelection)selection).getFirstElement();
				//force showing the cpde genie view
				IWorkbench work = PlatformUI.getWorkbench();
				//bring view to the front
				try {
					work.getActiveWorkbenchWindow()
					.getActivePage()
					.showView(CodeView.ID);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
				//get ResultsView
				CodeView view = (CodeView) work.getActiveWorkbenchWindow()
						.getActivePage().findView(CodeView.ID);
				Long qid = SearchAction.getCurrentQueryID();
				view.setText(obj,qid);
			}
		};
		viewSourceCode.setText("Source Code");
		viewSourceCode.setToolTipText("View source code from selected slice");
		viewSourceCode.setImageDescriptor(
				CodeGenieImages.getImageDescriptor(CodeGenieImages.VIEW_CODE));

		editQuery = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				MySingleResult obj = (MySingleResult) ((IStructuredSelection)selection).getFirstElement();
				ResultsViewUpdater rvu = obj.getResultsViewUpdater();
				rvu.updateQuery();
				rvu.makeQueryAndUpdateView();			}
		};
		editQuery.setText("Edit query");
		editQuery.setToolTipText("Change Solr query that will be send to server");
		editQuery.setImageDescriptor(CodeGenieImages.getImageDescriptor(
				CodeGenieImages.EDIT_QUERY));
		

		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				String packageName = showInput("Double click action\nMessage: ",obj.toString());
				Debug.debug(ResultsView.this.getClass(), packageName);
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	protected void showMessage(String message) {
		MessageDialog.openInformation(
				viewer.getControl().getShell(),
				"CodeGenie view",
				message);
	}

	protected String showInput(String message, String defaultValue){
		InputDialog input = new InputDialog(viewer.getControl().getShell(),
				"CodeGenie View",
				message,
				defaultValue,
				null);
		input.create();
		input.open();
		if(input.getReturnCode()==InputDialog.OK){
			String value = input.getValue();
			Debug.debug(getClass(), "typed value is: "+value);
			return value;
		}
		return null;
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public void refresh() {
		Debug.debug(getClass(),"refreshing view");
		this.viewer.refresh();
	}

	public  void cantRefresh() {
		Debug.debug(getClass(), "setting canRefresh as false");
		this.canRefresh=false;
	}

	public void canRefresh() {
		Debug.debug(getClass(), "setting canRefresh as true");
		this.canRefresh=true;
	}

	public boolean ready(){
		return canRefresh;
	}
}