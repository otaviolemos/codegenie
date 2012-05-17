/**
 * @author ricardo
 */

package edu.uci.ics.mondego.codegenie;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import edu.uci.ics.mondego.codegenie.localrepository.RepositoryStore;

public class CodeGeniePluginStore implements ISaveParticipant {

	protected RepositoryStore repositoryStore;
	
	protected static int queryID = 0;
	
	
	public CodeGeniePluginStore ()
	{
		repositoryStore = new RepositoryStore ();
	}

	public RepositoryStore getRepositoryStore ()
	{
		return repositoryStore;
	}
	
	protected void writeState (File f)
	{
		try
		{
			FileOutputStream fos = new FileOutputStream (f);
			
			ObjectOutputStream dos = new ObjectOutputStream(fos);
			
			repositoryStore.writeState (dos);
			
			dos.writeInt(queryID);
			
			// add writeState for other stores 

			fos.close();
		}
		catch (IOException e)
		{
			
		}
	}

	protected void readState (File f)
	{
		try
		{
			FileInputStream fis = new FileInputStream (f);
			
			ObjectInputStream dis = new ObjectInputStream(fis);

			repositoryStore.readState (dis);
			
			queryID = dis.readInt();
			
			// add readState for other stores 
			
			fis.close ();
		}
		catch (IOException e)
		{
			
		}
	}
	
	public int getNewQueryID ()
	{
		queryID++;
		return queryID;
	}
	
	
	
	
	
    public void prepareToSave(ISaveContext context) throws CoreException {
    }
    
    public void saving(ISaveContext context) throws CoreException {
        switch (context.getKind()) {
           case ISaveContext.FULL_SAVE:
              CodeGeniePlugin myPluginInstance = CodeGeniePlugin.getPlugin();
              // save the plug-in state
              int saveNumber = context.getSaveNumber();
              String saveFileName = "save-" + Integer.toString(saveNumber);
              File f = myPluginInstance.getStateLocation().append(saveFileName).toFile();
              // if we fail to write, an exception is thrown and we do not update the path
              
              writeState(f);
              
              context.map(new Path("save"), new Path(saveFileName));
              context.needSaveNumber();
              break;
           case ISaveContext.PROJECT_SAVE:
              break;
           case ISaveContext.SNAPSHOT:
              break;
        }
     }
    
    public void doneSaving(ISaveContext context) {
    	CodeGeniePlugin myPluginInstance = CodeGeniePlugin.getPlugin();

        // delete the old saved state since it is not necessary anymore
        int previousSaveNumber = context.getPreviousSaveNumber();
        String oldFileName = "save-" + Integer.toString(previousSaveNumber);
        File f = myPluginInstance.getStateLocation().append(oldFileName).toFile();
        f.delete();
    }
    
    public void rollback(ISaveContext context) {
    	CodeGeniePlugin myPluginInstance = CodeGeniePlugin.getPlugin();

        // since the save operation has failed, delete the saved state we have just written
        int saveNumber = context.getSaveNumber();
        String saveFileName = "save-" + Integer.toString(saveNumber);
        File f = myPluginInstance.getStateLocation().append(saveFileName).toFile();
        f.delete();
     }    
}
