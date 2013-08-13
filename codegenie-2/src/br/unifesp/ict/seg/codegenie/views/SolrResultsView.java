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

public class SolrResultsView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "br.unifesp.ict.seg.codegenie.views.SolrResultsView";

	private TableViewer viewer;
	private Action refresh;
	private Action add;
	private Action remove;
	private Action doubleClickAction;

	private boolean canRefresh;


	private static SolrResultsView current = null;
	public static SolrResultsView getCurrent(){return current;}


	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */

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
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public SolrResultsView() {
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
				SolrResultsView.this.fillContextMenu(manager);
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
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(refresh);
		manager.add(add);
		manager.add(remove);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(refresh);
		manager.add(add);
		manager.add(remove);
	}


	private void makeActions() {
		refresh = new Action() {
			public void run() {
				viewer.refresh();
				//showMessage("Action 1 executed");
			}
		};
		refresh.setText("Action 1");
		refresh.setToolTipText("Action 1 tooltip");
		refresh.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

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
					slice.runTests(obj);
					//SolrResultsView.this.waitAndRefresh();
				} catch (IOException | CoreException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
				viewer.refresh();
				//showMessage("Action 2 executed ("+obj.getClass()+")");
			}

		};
		add.setText("include and test slice");
		add.setToolTipText("Action 2 tooltip");
		add.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
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
		remove.setText("Action 3");
		remove.setToolTipText("Action 3 tooltip");
		remove.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

		
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				String packageName = showInput("Message",obj.toString());
				Debug.debug(SolrResultsView.this.getClass(), packageName);
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
	private void showMessage(String message) {
		MessageDialog.openInformation(
				viewer.getControl().getShell(),
				"CodeGenie view",
				message);
	}

	private String showInput(String message, String defaultValue){
		InputDialog input = new InputDialog(viewer.getControl().getShell(),
				"CodeGenie View",
				message,
				defaultValue,
				/*new IInputValidator(){
					@Override
					public String isValid(String arg0) {
						String[] tokens = arg0.split(".");
						for(String str : tokens){
							Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
							Matcher m = p.matcher(str);
							boolean b = m.find();
							if(b) return "contains special char: "+str;
						}
						return null;
					}
				}*/null);
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