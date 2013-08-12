package br.unifesp.ict.seg.codegenie.search.slicer;


import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import br.unifesp.ict.seg.codegenie.tmp.Debug;
import br.unifesp.ict.seg.codegenie.util.Folders;
import br.unifesp.ict.seg.codegenie.views.GRProgressMonitor;


public class AbstractProjectEditor {
	protected IJavaProject javap;
	
	protected void excludeFromBuilding(long eid) throws CoreException {
		IProject myPrj = javap.getProject();
		IFolder mySlicedFolder = myPrj.getFolder(File.separator+Folders.SLICE+File.separator+eid);
		IClasspathEntry srcEntry = JavaCore.newSourceEntry(mySlicedFolder.getFullPath());
		IClasspathEntry[] cpe = new IClasspathEntry[javap.getRawClasspath().length-1];
		IClasspathEntry[] all = javap.getRawClasspath();
		int count = 0;
		for(int i=0;i<all.length;i++){
			if(!all[i].getPath().equals(srcEntry.getPath())){
				cpe[count]=all[i];
				count++;
			}
		}
		javap.setRawClasspath(cpe,null);
		mySlicedFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
		saveAndRebuild();
	}
	
	protected void includeInBuilding(long id) throws CoreException {
		IProject myPrj = javap.getProject();
		IFolder mySlicedFolder = myPrj.getFolder(File.separator+Folders.SLICE+File.separator+id);
		IClasspathEntry srcEntry = JavaCore.newSourceEntry(mySlicedFolder.getFullPath());
		IClasspathEntry[] cpe = new IClasspathEntry[javap.getRawClasspath().length+1];
		for(int i = 0; i < javap.getRawClasspath().length; i++)
			cpe[i] = javap.getRawClasspath()[i];
		cpe[javap.getRawClasspath().length] = srcEntry;
		javap.setRawClasspath(cpe, null);
		mySlicedFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
		saveAndRebuild();
	}
	
	public void saveAndRebuild() {
		try {
			GRProgressMonitor monitor = new GRProgressMonitor();
			javap.getProject().refreshLocal(IProject.DEPTH_INFINITE, null);
			javap.save(null, true);
			javap.getProject().refreshLocal(IProject.DEPTH_INFINITE, null);
			javap.getProject().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
			javap.getProject().refreshLocal(IProject.DEPTH_INFINITE, monitor);
			GRProgressMonitor.waitMonitor(monitor);
		} catch (Exception e) {
			Debug.debug(AbstractProjectEditor.class,e.getLocalizedMessage());
		}

	}

}
