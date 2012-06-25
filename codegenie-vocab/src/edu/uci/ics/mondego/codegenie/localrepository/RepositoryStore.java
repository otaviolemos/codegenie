/**
 * @author ricardo
 */

package edu.uci.ics.mondego.codegenie.localrepository;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.part.IPage;

//import edu.uci.ics.mondego.codegenie.views.RepositoryView;
import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;


public class RepositoryStore implements IPartListener {

	protected HashMap<Long, RepositoryEntity> entityMap = new HashMap<Long, RepositoryEntity>( 149
            /* capacity */,
            0.75f
            /* loadfactor */ );
	protected IRepositoryStoreChangeListener changeListener = null;
	protected boolean listenerOk = false; 
	
	
	public void readState (ObjectInputStream ois)
	{
		try
		{
			entityMap = (HashMap) ois.readObject();
		}
		catch (IOException e)
		{
			
		}
		catch (ClassNotFoundException e)
		{
			
		}
	}
	
	public void writeState (ObjectOutputStream oos)
	{
		try
		{
			oos.writeObject(entityMap);
		}
		catch (IOException e)
		{
			
		}

	}

	public void addEntity (SearchResultEntryWrapper entry)
	{
		Object o = entityMap.get(new Long(entry.getEntry().getEntityID()));
		
		if (o != null) 
		{
			return;
		}
		else
		{
			RepositoryEntity entity = new RepositoryEntity();

			entity.downloadedIntoLocalRepository = false;
			entity.localRepositoryFile = null;
			entity.searchResultEntry = entry;
			
			entityMap.put(new Long (entry.getEntry().getEntityID()), entity);

			triggerEntityAddedToRepositoryStoreEvent(entity);
		}
	}
	
	public RepositoryEntity[] getEntities() {
		return (RepositoryEntity [])entityMap.values().toArray(new RepositoryEntity[entityMap.size()]);
	}
	
	public void changeEntry(long id, SearchResultEntryWrapper entry) {
		RepositoryEntity entity = entityMap.get(id);
		entity.setSearchResultEntry(entry);
		entityMap.put(id, entity);
	}
	
	public SearchResultEntryWrapper getEntry(long id) {
		return entityMap.get(id).getSearchResultEntry();
	}
	
	public void clearStore ()
	{
		entityMap.clear();
	}
	
	public void triggerEntityAddedToRepositoryStoreEvent (RepositoryEntity entity)
	{
		if (listenerOk &&  changeListener != null)
		{
			changeListener.entityAddedToRepositoryStore(entity);
		}
	}
	
	public void setRepositoryListener (IRepositoryStoreChangeListener listener, IWorkbenchPage page)
	{
		changeListener = listener;
		
		page.addPartListener(this);
	}

	public void removeRepositoryListener ()
	{
		changeListener = null;
	}
	
	
	 public void partActivated(IWorkbenchPart part)
	 {
	 }
	 
	 public void partDeactivated(IWorkbenchPart part)
	 {
	 }
	 
	 public void partBroughtToTop(IWorkbenchPart part)
	 {
	 }
	 
	 public void partClosed(IWorkbenchPart part)
	 {
		 //if (part.getSite().getId().equals(RepositoryView.VIEWPART_ID))
		 //{
			 listenerOk = false;
		 //}		 
	 }
	 
	 public void partOpened(IWorkbenchPart part)
	 {
		 //if (part.getSite().getId().equals(RepositoryView.VIEWPART_ID))
		 //{
			 listenerOk = true;
		 //}
	 }
}
