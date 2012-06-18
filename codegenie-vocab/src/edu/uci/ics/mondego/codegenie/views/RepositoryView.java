package edu.uci.ics.mondego.codegenie.views;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import edu.uci.ics.mondego.codegenie.localrepository.IRepositoryStoreChangeListener;
import edu.uci.ics.mondego.codegenie.localrepository.RepositoryEntity;
import edu.uci.ics.mondego.codegenie.CodeGeniePlugin;
//import edu.uci.ics.mondego.eclipse.actions.OpenSourcererFileAction;
import edu.uci.ics.mondego.codegenie.localrepository.LocalRepositoryTreeLeaf;
import edu.uci.ics.mondego.codegenie.localrepository.LocalRepositoryTreeNode;
//import edu.uci.ics.mondego.eclipse.search.OperationsWSSearchResult;
import edu.uci.ics.mondego.codegenie.search.TreeObjectBase;
import edu.uci.ics.mondego.codegenie.search.results.Entity;
import edu.uci.ics.mondego.codegenie.search.Util;
import edu.uci.ics.mondego.search.model.SearchResultEntry;
import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;

public class RepositoryView extends ViewPart implements
		IRepositoryStoreChangeListener {

	public static String VIEWPART_ID = "codegenie.view.RepositoryView";
	
	private TreeViewer viewer;
	private DrillDownAdapter drillDownAdapter;
	private Action action1;
	private Action clearLocalRepositoryAction;
	//private OpenSourcererFileAction openSourcererFileAction;

	

	class ViewContentProvider implements IStructuredContentProvider, ITreeContentProvider 
	{
		private LocalRepositoryTreeNode invisibleRoot;

		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			if (parent.equals(getViewSite())) {
				if (invisibleRoot==null) initialize();
				return getChildren(invisibleRoot);
			}
			return getChildren(parent);
		}
		public Object getParent(Object child) {
			if (child instanceof TreeObjectBase) {
				return ((TreeObjectBase)child).getParent();
			}
			return null;
		}
		public Object [] getChildren(Object parent) {
			if (parent instanceof LocalRepositoryTreeNode) {
				return ((LocalRepositoryTreeNode)parent).getChildren();
			}
			return new Object[0];
		}
		public boolean hasChildren(Object parent) {
			if (parent instanceof LocalRepositoryTreeNode)
				return ((LocalRepositoryTreeNode)parent).hasChildren();
			return false;
		}

		public void reset ()
		{
			invisibleRoot = null;
		}
		
		private void initialize() 
		{
			populateLocalRepositoryTree ();
		}
		
		public void populateLocalRepositoryTree ()
		{
			invisibleRoot = new LocalRepositoryTreeNode();
			
			
			RepositoryEntity[] entities = 
				CodeGeniePlugin.getPlugin().getStore().getRepositoryStore().getEntities();
			
			for (int i = 0; i < entities.length; i++)
			{
				insertRepositoryEntryIntoLocalRepositoryTree(entities[i]);
			}
			
		}
		
		public LocalRepositoryTreeLeaf insertRepositoryEntryIntoLocalRepositoryTree (RepositoryEntity entity)
		{
			String[] path = Util.getPathListFromPathString(entity.searchResultEntry.getEntry().getFilePath());
			
			
			LocalRepositoryTreeNode parent = invisibleRoot;			
			
			for (int i = 0; i < path.length; i++)
			{
				TreeObjectBase child = parent.getChild(path[i]);

				if (child instanceof LocalRepositoryTreeLeaf) return null;
				
				if (child == null)
				{
					child = new LocalRepositoryTreeNode (path[i]);
					parent.addChild(child);
				}
				
				parent = (LocalRepositoryTreeNode) child;
			}

			parent.isFile = true;
			
			Object obj = parent.getChild(entity.searchResultEntry.getEntry().getEntityName());
			LocalRepositoryTreeLeaf child = null;
			
			if (obj == null || obj instanceof LocalRepositoryTreeNode)
			{
				child = new LocalRepositoryTreeLeaf(entity);
				parent.addChild(child);				
			}
			else
			{
				child = (LocalRepositoryTreeLeaf) obj;
			}
			
			return child;
		}
	}

	
	class ViewLabelProvider extends LabelProvider {

		public String getText(Object obj) {
			String text = "";
			if (obj instanceof LocalRepositoryTreeNode)
			{
				   text = ((LocalRepositoryTreeNode) obj).getName();
				   return text;
			}
			if (obj instanceof TreeObjectBase)
			{
				text = ((LocalRepositoryTreeLeaf) obj).getRepositoryEntity().searchResultEntry.getEntry().getEntityName();
				return text;
			}
			return text;
		}
		
		public Image getImage(Object obj) 
		{
			String imageKey = ISharedImages.IMG_OBJ_FOLDER;
			Image result = PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);

			if (obj instanceof LocalRepositoryTreeNode)
			{
				LocalRepositoryTreeNode node = (LocalRepositoryTreeNode) obj;

				if (node.isFile)
				{
					imageKey = org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CUNIT;
					result = JavaUI.getSharedImages().getImage(imageKey);
				}
			}
			else if (obj instanceof LocalRepositoryTreeLeaf)
			{
				LocalRepositoryTreeLeaf leaf = (LocalRepositoryTreeLeaf) obj;
				
				switch (leaf.getRepositoryEntity().searchResultEntry.getEntry().getEntityTypeId())
				{
					case Entity.TypeEnum.CLASS:
						imageKey = org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CLASS;
						break;
					case Entity.TypeEnum.METHOD:
						imageKey = org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_PUBLIC;
						break;
					case Entity.TypeEnum.CONSTRUCTOR:
						imageKey = org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_DEFAULT;
						break;
					case Entity.TypeEnum.FIELD:
						imageKey = org.eclipse.jdt.ui.ISharedImages.IMG_FIELD_PUBLIC;
						break;
					case Entity.TypeEnum.INTERFACE:
						imageKey = org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_INTERFACE;
						break;
					case Entity.TypeEnum.PACKAGE:
						imageKey = org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_PACKAGE;
						break;
					case Entity.TypeEnum.STATIC_INIT:
						imageKey = org.eclipse.jdt.ui.ISharedImages.IMG_FIELD_DEFAULT;
						break;
					default:
						imageKey = org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_DEFAULT;
						break;				
				}

				result = JavaUI.getSharedImages().getImage(imageKey);

			}
			return result;
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public RepositoryView() {
	}

	
	public void entityAddedToRepositoryStore (RepositoryEntity entity)
	{
		LocalRepositoryTreeLeaf leaf = ((ViewContentProvider)viewer.getContentProvider()).insertRepositoryEntryIntoLocalRepositoryTree (entity);
		
		//setViewerInput ();
		refreshViewerLeaf(leaf);
	}
	
	class RefreshViewerLeaf implements Runnable
	{
		public LocalRepositoryTreeLeaf leaf;
		
		public void run ()
		{
			viewer.refresh();
//			viewer.expandToLevel(leaf, 1);
		}
	}
	protected void refreshViewerLeaf (LocalRepositoryTreeLeaf leaf)
	{
		RefreshViewerLeaf r = new RefreshViewerLeaf ();
		
		r.leaf = leaf;
		
		Display.getDefault().syncExec(r);
	}
	
	protected void setViewerInput ()
	{
		Display.getDefault().syncExec(
				new Runnable () {

					
					public void run () 
					{
						((ViewContentProvider)viewer.getContentProvider()).reset();
						viewer.setInput(getViewSite());
						//viewer.expandAll();
					}
				});		
	}
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		drillDownAdapter = new DrillDownAdapter(viewer);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
		
		viewer.expandAll();
		
		CodeGeniePlugin.getPlugin().getStore().getRepositoryStore().setRepositoryListener(this, getSite().getPage());
	}
	
	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				RepositoryView.this.fillContextMenu(manager);
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
		manager.add(action1);
		manager.add(new Separator());
		manager.add(clearLocalRepositoryAction);
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(action1);
		//manager.add(openSourcererFileAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(clearLocalRepositoryAction);
		manager.add(new Separator());
		drillDownAdapter.addNavigationActions(manager);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage("Action 1 executed");
			}
		};
		action1.setText("Action 1");
		action1.setToolTipText("Action 1 tooltip");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		clearLocalRepositoryAction = new Action() {
			public void run() {
				CodeGeniePlugin.getPlugin().getStore().getRepositoryStore().clearStore();
				//OperationsWSSearchResult.clearAllLocalSourcererFiles ();
				setViewerInput();
			}
		};
		clearLocalRepositoryAction.setText("Clear Local Repository");
		clearLocalRepositoryAction.setToolTipText("Clear Local Repository");
		clearLocalRepositoryAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));

//		openSourcererFileAction = new OpenSourcererFileAction(this.getSite()) 
//		{
//			public void run() {
//				ISelection selection = viewer.getSelection();
//				
//				Object obj = ((IStructuredSelection)selection).getFirstElement();
//				if (obj instanceof LocalRepositoryTreeLeaf)
//				{
//					run(new WSSearchResultEntry[] {((LocalRepositoryTreeLeaf) obj).getRepositoryEntity().searchResultEntry});
//				}
//			}
//		};
		//openSourcererFileAction.setText ("Open Sourcerer Entity");	
	
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				//openSourcererFileAction.run();
			}
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Sample View",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
}
