package br.unifesp.ict.seg.codegenie.search.slicer;

import java.util.HashSet;
import java.util.Set;



import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import br.unifesp.ict.seg.codegenie.search.CGMethodInterface;
import br.unifesp.ict.seg.codegenie.tmp.Debug;
import br.unifesp.ict.seg.codegenie.views.GRProgressMonitor;



public class Unweaver {

	IJavaProject javap;
	Long sliceID;
	IPackageFragmentRoot source;
	CGMethodInterface mi;

	public Unweaver(IJavaProject javap, Long sliceID){
		this.javap = javap;
		this.sliceID =sliceID;
		this.resolveSourceFolder();
	}

	public void setMethodInterface(CGMethodInterface mi){
		this.mi=mi;
	}

	private void resolveSourceFolder(){
		try {
			for(IPackageFragmentRoot r :javap.getAllPackageFragmentRoots()){//for each package
				if(r.getKind()==IPackageFragmentRoot.K_SOURCE){//verify if it is a source package
					this.source = r;
					break;
				}
			}
		} catch (JavaModelException e) {
			Debug.errDebug(getClass(),e.getLocalizedMessage());
		}
	}

	public boolean unweave(CGMethodInterface mi) throws CoreException{
		this.setMethodInterface(mi);
		SliceAddedAnn ann = new SliceAddedAnn(sliceID);
		IJavaElement[] srcElements = source.getChildren();
		for(int i=0;i<srcElements.length;++i){
			String parts[] = srcElements[i].getResource().getFullPath().toPortableString().replace(IPath.SEPARATOR, ':').split(":");
			int idx = parts[0].trim().equalsIgnoreCase("")?3:2;
			//parts[0] is unknown
			//parts[1] is project name
			//parts[2] is src projectFolder
			String pckName = "";
			for(int j=idx;j<parts.length;++j){
				//get package name
				if(pckName.equals("")){
					pckName=parts[j];
				} else {
					pckName+="."+parts[j];
				}
				//check package
				Debug.debug(getClass(), "checking package = "+pckName);
				IResource[] res = ((IFolder) srcElements[i].getResource()).members();
				IType[] types = new IType[res.length];
				//check for classes in this package
				for(int k=0;k<res.length;++k){
					String relativeName = pckName+(pckName.equals("")?"":".")+res[k].getName();
					if(relativeName.endsWith(".java")){
						relativeName = relativeName.substring(0,relativeName.lastIndexOf(".java"));
					}
					//get class type
					types[k]=javap.findType(relativeName);
					Debug.debug(getClass(), "type #"+k+": "+types[k]);
					if(types[k]==null || !(types[k].isClass() || types[k].isInterface() || types[k].isEnum() )){
						continue;
					}
					ICompilationUnit cpu = types[k].getCompilationUnit();
					String primaryCode = cpu.findPrimaryType().getSource();
					//look up for annotations
					if(primaryCode.startsWith(ann.toString())){//if this class starts with the annotation, delete it
						GRProgressMonitor gr = new GRProgressMonitor();
						cpu.delete(true, gr);
						GRProgressMonitor.waitMonitor(gr);
					} else {//check for methods starting with annotations
						Set<IMember> members = new HashSet<IMember>();
						for(IMember im : types[k].getMethods())
							members.add(im);
						for(IMember im :  types[k].getFields())
							members.add(im);
						for(IMember im :  types[k].getInitializers())
							members.add(im);
						for(IMember im :  types[k].getTypes())
							members.add(im);
						for(IMember im : members){
							String sourceCode = im.getSource();
							if(sourceCode.startsWith(ann.toString())){
								GRProgressMonitor monitor = new GRProgressMonitor();
								im.delete(true, monitor);
								GRProgressMonitor.waitMonitor(monitor);
							}
						}
					}
				}
			}
		}
		return true;
	}
}
