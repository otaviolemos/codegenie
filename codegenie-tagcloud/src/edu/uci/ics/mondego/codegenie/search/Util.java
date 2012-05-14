package edu.uci.ics.mondego.codegenie.search;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;

import edu.uci.ics.mondego.codegenie.CodeGeniePlugin;
import edu.uci.ics.mondego.search.model.SearchResultEntry;

public class Util {

	
	public static String[] getPathListFromPathString (String filePath)
	{
		if (filePath == null) return new String[0];
		
		String[] list = null;
		
		StringTokenizer tok = new StringTokenizer(filePath, "/");
		
		int size = tok.countTokens(); 
		
		if (size == 0) return new String[] {};
		
		list = new String[size];
		
		for (int i = 0; i < size; i++)
		{
			list[i] = tok.nextToken();
		}
		
		return list;
	}
	
	public static IFile getFileFromSearchResult(SearchResultEntryWrapper searchResultEntry, IProject project)
	{
		IFile file = null;
		
		if (searchResultEntry.getEntry().getFilePath() == null) return null;
		
		try
		{
			IFolder sourcererFolder = getFolder(".sourcerer", project);

			if (sourcererFolder == null) {
				ResourceAttributes ra = new ResourceAttributes();
				ra.setHidden(true);
				sourcererFolder.setResourceAttributes(ra);
				sourcererFolder.create(true, true, null);
			}

			file = getIFileFromFilePath(searchResultEntry.getEntry().getFilePath(), sourcererFolder);
			
			if (!file.exists())
			{
				try
				{
					String filepath = 
						CodeGeniePlugin.getPlugin().getSourcererURL() + "/repodata/resource?rp=" 
						+ searchResultEntry.getEntry().getFilePath();
					URL u = new URL ("http://" + filepath + "&client=codegenie");
					file.create(u.openStream(), IFile.FORCE, null);
					return file;
				}
				catch (MalformedURLException e)
				{
					return null;
				}
				catch (IOException e)
				{
					return null;
				}
			}			
		}		
		catch (CoreException e)
		{
			return null;
		}

		return file;
		
//		return new LocalRepositoryFile ();
	}
	
	public static IFile getSliceFromSearchResult(SearchResultEntryWrapper searchResultEntry,
			String[] query, IProject project)
	{
		boolean problems = false;
		IFile file = null;
		
		if (searchResultEntry.getEntry().getFilePath() == null) return null;
		
		try
		{
			IFolder sliceFolder = getFolder(CodeGeniePlugin.getSliceDirName(), project);
			if (sliceFolder == null) return null;
			if (query != null)
				file = sliceFolder.getFile(searchResultEntry.getEntry().getEntityId() + "." +
					query[0] + "." + query[1] + "." + query[2] + ".zip");
			else
				file = sliceFolder.getFile(searchResultEntry.getEntry().getEntityId() + ".zip");
			if (!file.exists())
			{
				try
				{
					String filepath = 
						CodeGeniePlugin.getPlugin().getSourcererURL() + "/slicer/slice?eid=" + searchResultEntry.getEntry().getEntityId();
					URL u = new URL ("http://" + filepath +  "&client=codegenie");
					file.create(u.openConnection().getInputStream(), IFile.FORCE, null);
					return file;
				}
				catch (MalformedURLException e)
				{
					problems = true;
				}
				catch (IOException e) {
					problems = true;
				}
			}			
		}		
		catch (CoreException e) {
			return null;
		}

		if(!problems) {
			searchResultEntry.setProblemRetrieving(false);
			return file;
		} else {
			searchResultEntry.setProblemRetrieving(true);
			return null;
		}
	}

	public static IFolder getFolder(String folderName, IProject project)
	{
		IFolder sourcererFolder = null;
		
		try
		{
			sourcererFolder = project.getFolder(folderName);
			if (!sourcererFolder.exists())
			{
				try
				{
					sourcererFolder.create(IFolder.NONE, true, null);
				}
				catch (ResourceException e)
				{
					sourcererFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
				}
				catch (Exception e)
				{
					return null;
				}
			}

			try
			{
//				IJavaProject javaProject = (IJavaProject) project.getAdapter(IJavaProject.class);
				
			}
			catch (ClassCastException e)
			{
				
			}
			
		
		}		
		catch (CoreException e)
		{
			return null;
		}
		
		
		return sourcererFolder;
	}
	
	public static IFile getIFileFromFilePath (String filePath, IFolder parentFolder)
	{
		IFile file = null;

		String[] path = getPathListFromPathString(filePath);
		
		IFolder fp = parentFolder;
				
		for (int i = 0; i < path.length - 1; i++)
		{
			IFolder f = fp.getFolder (path[i]);
			
			if (!f.exists())
			{
				try
				{
					f.create(IFolder.NONE, true, null);
				}
				catch (ResourceException e)
				{
					return null;
				}
				catch (Exception e)
				{
					return null;
				}
			}

			fp = f;
		}
		
		file = fp.getFile (path[path.length-1]);
		
		return file;
	}


	
}
