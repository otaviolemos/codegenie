package edu.uci.ics.mondego.codegenie.search.results;

import java.util.HashMap;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import edu.uci.ics.mondego.codegenie.search.results.Entity;
import edu.uci.ics.mondego.codegenie.search.results.EntryResultTreeNode;
import edu.uci.ics.mondego.codegenie.search.results.FolderTreeNode;
import edu.uci.ics.mondego.codegenie.search.results.GroupTreeNode;
import edu.uci.ics.mondego.codegenie.search.results.TDSearchResultPage;

import edu.uci.ics.mondego.codegenie.search.SearchResultEntryWrapper;
import edu.uci.ics.mondego.codegenie.*;

public class TDSearchResultLabelProvider extends LabelProvider {

	TDSearchResultPage page;

	public HashMap<Integer,String> imageKeyMap;
	
	public static String[] imageKeys = {
		org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_PACKAGE,
		org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CLASS,
		org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_INTERFACE,
		org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_PUBLIC,
		org.eclipse.jdt.ui.ISharedImages.IMG_FIELD_PUBLIC,
		org.eclipse.jdt.ui.ISharedImages.IMG_FIELD_DEFAULT,
		org.eclipse.jdt.ui.ISharedImages.IMG_FIELD_DEFAULT
	};
	
	public TDSearchResultLabelProvider (TDSearchResultPage page)
	{
		this.page = page;
		
		imageKeyMap = new HashMap<Integer,String>();
		
		for (int i = 0; i < Entity.TypeEnum.typeArray.length; i++) {
			imageKeyMap.put(new Integer (Entity.TypeEnum.typeArray[i]), 
					imageKeys[i]);
		}
	}

	
	public String getText(Object element) 
	{		
		if (element instanceof EntryResultTreeNode) {
			element = ((EntryResultTreeNode)element).entry;
		}
			
		if (element instanceof SearchResultEntryWrapper) {
			SearchResultEntryWrapper entry = (SearchResultEntryWrapper) element;
			
			String label = "";
			if (entry.getEntry().getReturnTypeFQN() != null)
				label = entry.getEntry().getReturnTypeFQN().replaceAll("java.lang.Void", "void") + " ";
			
			label += entry.getEntry().getEntityName();
			
			if(entry.getTestResult() != null)
				label += " - Test runs: " + entry.getTestResult().getRunCount() +
				  " Successes: "+ (entry.getTestResult().getRunCount() - entry.getTestResult().getFailureCount()) +
				  " Failures: " + entry.getTestResult().getFailureCount();
//			+
//				  " Elapsed time: " + entry.getTestResult().getRunTime();
			
			if(entry.isWoven()) {
				label += " - [currently woven]";
			}
			
			if(entry.hadProblemsRetrieving()) 
				label += " - [could not retrieve slice]";
			
			return label; //+ " (R" + String.valueOf(entry.getRank()) + ")";
		}
		
		if (element instanceof GroupTreeNode) {
			return ((GroupTreeNode)element).getName();
		}

		if (element instanceof FolderTreeNode) {
			return ((FolderTreeNode) element).getName();
		}
		
		
		return "";	
	}

	
	public Image getImage(Object element) 
	{
		String imageKey = org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_DEFAULT;
		Image result = JavaUI.getSharedImages().getImage(imageKey);

		if (element instanceof EntryResultTreeNode)
		{
			element = ((EntryResultTreeNode)element).entry;
		}
		
		if (element instanceof SearchResultEntryWrapper) {
			SearchResultEntryWrapper entry = (SearchResultEntryWrapper) element;

			// the default image is the grey one: no tests executed
			result = CodeGenieImages.getImage(CodeGenieImages.IMG_YELLOW);
			
			if(entry.getTestResult() != null) {
				if (entry.getTestResult().getFailureCount() != 0) {
					result = CodeGenieImages.getImage(CodeGenieImages.IMG_RED);
				} else {
					result = CodeGenieImages.getImage(CodeGenieImages.IMG_GREEN);
				}
			}
		}
		
		if (element instanceof GroupTreeNode)
		{
			imageKey = ISharedImages.IMG_OBJ_FOLDER;
			result = PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);

			/*	
			imageKey = (String) imageKeyMap.get(new Integer (((GroupTreeNode)element).type));
			
			if (imageKey == null) imageKey = org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_DEFAULT;	

			result = JavaUI.getSharedImages().getImage(imageKey);
			*/
		}
		
		if (element instanceof FolderTreeNode)
		{
			if (((FolderTreeNode) element).isFile)
			{
				imageKey = org.eclipse.jdt.ui.ISharedImages.IMG_OBJS_CUNIT;
				result = JavaUI.getSharedImages().getImage(imageKey);
			}
			else
			{			
				imageKey = ISharedImages.IMG_OBJ_FOLDER;
				result = PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
			}
		}

		
		return result;
	}

	
/*
	public String getColumnText(Object obj, int index) {

		if (obj instanceof WSSearchResultEntry)
		{
			WSSearchResultEntry entry = (WSSearchResultEntry) obj;

			switch (index)
			{
			case RANK:
				return String.valueOf(entry.getRank());
			case TYPE:
				return Type[entry.getType()];
			case NAME:
				return entry.getEntityName();
			}
		}
		return "";
	}
	
	public Image getColumnImage(Object obj, int index) {
		return null;
	}
	
*/	
}
