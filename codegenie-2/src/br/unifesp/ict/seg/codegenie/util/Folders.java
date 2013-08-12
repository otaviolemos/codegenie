package br.unifesp.ict.seg.codegenie.util;


import java.io.File;
import java.io.IOException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import br.unifesp.ict.seg.codegenie.tmp.Debug;
import br.unifesp.ict.seg.codegenie.views.GRProgressMonitor;

//import javax.swing.JOptionPane;

import static java.io.File.separator;


public class Folders {

	/**the name of the subfolder that contains the src code*/
	public static final String SRC_NAME = new String("src");
	/**the name of the subfolder that contains the zip file downloaded from slice server*/
	public static final String ZIP_NAME = new String("zip");
	/**the name of the subfolder that contains the backpup of the original code*/
	public static final String BKP_NAME = new String("backup");
	/**the main projectFolder of the slices*/
	public static final String SLICE = new String(".sourcerer");


	public static final int SRC_CODE = 1;
	public static final int ZIP_CODE = 2;
	public static final int BKP_CODE = 4;

	/**@param proj the project which it you get the projectFolder location on disk
	 * @return the String containing the full path to the project*/
	public static String getProjectsFolder(IProject proj){
		IResource res = proj.getWorkspace().getRoot().findMember(separator+proj.getName());
		return res.getLocation().toFile().getAbsolutePath();

	}

	/**@param the IJavaProject which it will get the source projectFolder
	 * @return the source projectFolder of this java project*/
	public static File getSourceFolder(IJavaProject javap){
		try {
			for(IPackageFragmentRoot r :javap.getAllPackageFragmentRoots()){//for each package
				if(r.getKind()==IPackageFragmentRoot.K_SOURCE){//verify if it is a source package
					File projF = new File(getProjectsFolder(javap.getProject()));//create a projects projectFolder
					String src = r.getElementName();//get package name
					return new File(projF,src);//create a new projectFolder
				}
			}
		} catch (JavaModelException e) {
			Debug.errDebug(Folders.class,e.getLocalizedMessage());
		}
		return null;
	}


	/**creates the slice projectFolder struct inside the given projectFolder
	 * @param projectFolder must be the project projectFolder*/
	public static void createSliceFolders(File folder) {
		File slice = new File(folder.getAbsolutePath()+separator+SLICE);
		if(!slice.exists()) slice.mkdir();
		File zip = new File(slice.getAbsolutePath()+separator+ZIP_NAME);
		if(!zip.exists()) zip.mkdir();
	}

	/**clear the slice projectFolder struct inside the given projectFolder
	 * @param projFolder must be the project projectFolder*/
	public static void clearSliceFolders(File projFolder,int folder,long id) {
		boolean srcf = (folder & SRC_CODE)>0;
		boolean bkpf = (folder & BKP_CODE)>0;
		boolean zipf = (folder & ZIP_CODE)>0;
		if(srcf){
			File src = new File(projFolder.getAbsolutePath()+separator+SLICE+separator+id+separator+SRC_NAME);
			if(src.exists())
				for(File f : src.listFiles()){
					delete_r(f);
				}
		}
		if(zipf){
			File zip = new File(projFolder.getAbsolutePath()+separator+SLICE+separator+ZIP_NAME);
			if(zip.exists())
				for(File f : zip.listFiles()){
					delete_r(f);
				}
		}
		if(bkpf){
			File bkp = new File(projFolder.getAbsolutePath()+separator+SLICE+separator+id+separator+BKP_NAME);
			if(bkp.exists())
				for(File f : bkp.listFiles()){
					delete_r(f);
				}
		}
	}

	/**delete files recursively
	 * @param f the File that will be deleted*/
	private static void delete_r(File f){
		if(f.isDirectory()){
			for(File f2 : f.listFiles()){
				delete_r(f2);
			}
		}
		f.delete();
	}

	/**@return a relative path for @param fullpath inside the java project @param javap
	 * @throws IOException if the @param fullpath doesnt belong to this java project*/
	public static String removeSourcePath(String fullpath, IJavaProject javap) throws IOException{
		String sourceFolder = getSourceFolder(javap).getCanonicalPath();
		if(!fullpath.startsWith(sourceFolder)){
			throw new IOException("Given path doesnt seem to be in the same projectFolder as the project\n"
					+"source path: "+fullpath
					+"\nproject: "+sourceFolder);
		}
		int idx = sourceFolder.length();
		String packageAndClass = fullpath.substring(idx);
		return packageAndClass;
	}

	public static void delete(File f) throws IOException {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				delete(c);
		}
		if (!f.delete())
			throw new IOException("Failed to delete file: " + f);
	}

	public static IMethod changeToPublic(IMethod m,IType t)throws JavaModelException{
		int flags=Flags.AccPublic;
		flags = m.getFlags();
		boolean isPublic = Flags.isPublic(flags);
		boolean isPrivate = Flags.isPrivate(flags);
		boolean isProtected = Flags.isProtected(flags);	
		if(!isPublic){
			String msource = m.getSource();
			GRProgressMonitor pm = new GRProgressMonitor();
			pm=null;
			String javadoc = m.getAttachedJavadoc(pm);
			if(javadoc==null)javadoc="";
			Debug.debug(Folders.class,"waiting monitor1");

			//GRProgressMonitor.waitMonitor(pm);
			String before;
			String after;
			int idx = javadoc.length();
			idx = msource.indexOf("{", idx);
			Debug.debug(Folders.class,"waiting monitor1.1");
			before = msource.substring(javadoc.length(),idx);
			Debug.debug(Folders.class,"waiting monitor1.2");
			after = msource.substring(idx);
			Debug.debug(Folders.class,"waiting monitor1.3");
			if(isPrivate){
				before = before.replace("private", "public");
			} else if(isProtected){
				before = before.replace("protected", "public");
			} else {
				before+="public ";
			}
			msource=javadoc+before+after;
			pm = new GRProgressMonitor();
			Debug.debug(Folders.class,"waiting monitor1.9");
			m.rename("private_"+m.getElementName(), true, pm);
			Debug.debug(Folders.class,"waiting monitor2");
			GRProgressMonitor.waitMonitor(pm);
			pm = new GRProgressMonitor();
			m = t.createMethod(msource, null, true, pm);
			Debug.debug(Folders.class,"waiting monitor3");
			GRProgressMonitor.waitMonitor(pm);
		}
		return m;
	}

}
