package br.unifesp.ict.seg.codegenie.search.slicer;


import java.io.File;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import br.unifesp.ict.seg.codegenie.tmp.Debug;
import br.unifesp.ict.seg.codegenie.util.MapProcessor;
import br.unifesp.ict.seg.codegenie.util.Folders;


public class Weaver extends AbstractProjectEditor {

	private Long sliceID;
	private IPackageFragmentRoot source;
	private IPackageFragmentRoot sliceSrc;

	public Weaver(IJavaProject javap, Long sliceID){
		this.javap = javap;
		this.sliceID =sliceID;
		this.resolveSourceFolder();
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


	public boolean weave() throws CoreException{
		includeInBuilding(sliceID);
		this.resolveIncludedSlice();
		//create annotation
		SliceAddedAnn ann = new SliceAddedAnn(sliceID);
		//get packages
		IJavaElement[] sliceje = sliceSrc.getChildren();
		for(int i=0;i<sliceje.length;++i){
			String parts[] = sliceje[i].getResource().getFullPath().toPortableString().replace(IPath.SEPARATOR, ':').split(":");
			if(parts.length<5){
				continue;
			}
			//parts[0] is the project name
			//parts[1] is the slice projectFolder
			//parts[3] is the entityID projectFolder
			//parts[4] is the src projectFolder inside the entityID projectFolder
			//parts[5] and after are packages name
			String pckName = "";//parts[4];
			String fix = Folders.SRC_NAME+".";//the src projectFolder doesnot contain a subfoldar src
			for(int l=5; l < parts.length; l++)
				if(pckName.equals("")){
					pckName=parts[l];
				} else {
					pckName = pckName + "." + parts[l];
				}
			//create package if it does not exist yet
			Debug.debug(getClass(), "checking package = "+pckName);
			if(!source.getPackageFragment(pckName).exists()){
				source.createPackageFragment(pckName, true, null);
				Debug.debug(getClass(), "created package = "+pckName);
			}
			//go into the package and lookup if there is any class inside it
			//if class already exists, just add the methods that there arent in the class yet
			//otherwise add this class to the project
			IResource[] res = ((IFolder) sliceje[i].getResource()).members();
			String skip = sliceID+File.separator+Folders.BKP_NAME+File.separator;
			for(int j=0;j<res.length;++j){
				if(res[j].getType()==IResource.FOLDER || res[j].toString().contains(skip)){
					Debug.debug(getClass(), "Skiping resource = "+res[j]);
					continue;
				}
				Debug.debug(getClass(), "adding "+res[j]+" to the project");
				IPath theFolderPath = source.getPackageFragment(pckName)
						.getResource().getFullPath().removeFirstSegments(1); 
				IFile myFile = javap.getProject()
						.getFile(theFolderPath + File.separator + res[j].getName());
				//insert annotation into class and copy it to the source projectFolder
				if(!myFile.exists()){
					//get original content
					IBuffer originalContent = sliceSrc.getPackageFragment(fix+pckName).getCompilationUnit(myFile.getName()).getBuffer();
					//get range
					ISourceRange range =sliceSrc.getPackageFragment(fix+pckName).getCompilationUnit(myFile.getName()).findPrimaryType().getSourceRange();  
					int typeOffset = range.getOffset();
					int typeLength = range.getLength();
					//add annotation to the content
					originalContent.replace(typeOffset, typeLength,
							ann.getImport()+ann+originalContent.getContents().substring(typeOffset, typeOffset + typeLength));
					source.getPackageFragment(pckName).createCompilationUnit(myFile.getName(),
							originalContent.getContents(), true, null);
					//delete added class
					//sliceSrc.getPackageFragment(fix+pckName).getCompilationUnit(myFile.getName()).delete(true, null);
				} else {
					String name = res[j].getName();
					if(name.contains(".java")){
						name = name.replace(".java", "");
					}
					mergeTypes(pckName+"."+name,ann);
				}
			}

		}
		excludeFromBuilding(sliceID);
		return true;
	}
	

	private void mergeTypes(String relativePath,SliceAddedAnn ann) throws CoreException{
		String annotation = ann.toString();
		//get ITypes
		IType t = javap.findType(relativePath);									//get target Itype
		IType s = javap.findType(Folders.SRC_NAME+"."+relativePath);
		
		Debug.debug(getClass(),"Merging files:");
		Debug.debug(getClass(),"\ttarget => "+t+"\n\t\tfrom: "+relativePath);
		Debug.debug(getClass(),"\tsrc    => "+s+"\n\t\tfrom: "+Folders.SRC_NAME+"."+relativePath);
		//import annotation
		t.getCompilationUnit().createImport(ann.fqn(), null, null);

		IInitializer lastInit = null;

		try{//get,process and add Initializers
			IInitializer[] sinit = s.getInitializers();
			//add new initializers
			for(IInitializer o : sinit){
				//cannot put annotation on initializers
				lastInit = t.createInitializer("//"+annotation+o.getSource(), null, null);

			}

		} catch (JavaModelException e){
			Debug.debug(getClass(),e.getLocalizedMessage());
		}

		//get fields
		IField[] tfields = t.getFields();									
		IField[] sfields = s.getFields();
		//process fields
		MapProcessor<IField> mpf = new MapProcessor<IField>(sfields,tfields,MapProcessor.Action.KEEP);
		//add fields
		Map<String, IField> toaddF = mpf.getToAdd();
		for(IField m: toaddF.values()){
			String msource = annotation+m.getSource();
			Debug.debug(getClass(),"Adding field:\n"+msource);
			t.createField(msource, lastInit, false, null);
		}
		//get methods
		IMethod[] tmethods = t.getMethods();
		IMethod[] smethods = s.getMethods();
		//process methods
		MapProcessor<IMethod> mpm = new MapProcessor<IMethod>(smethods,tmethods,MapProcessor.Action.KEEP){
			public String getName(IMethod m){
				String ret=m.getElementName();
				try {
					ret+=" "+m.getSignature();
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
				return ret;
			}
		};
		//add methods
		Map<String, IMethod> toaddM = mpm.getToAdd();
		for(IMethod m: toaddM.values()){
			//m.getCompilationUnit().
			Debug.debug(getClass(), "adding method ="+m);
			String msource = annotation+m.getSource();
			int oidx = m.getSource().indexOf("{");
			String before = msource.substring(0, oidx);
			String after = msource.substring(oidx);
			if(before.contains("private"))
				before = before.replaceAll("private", "public");
			else if(before.contains("protected"))
				before = before.replaceAll("protected", "public");
			msource=before+after;
			Debug.debug(getClass(),"Adding method:\n"+msource);
			t.createMethod(msource, null, false, null);
		}

		//get types
		IType[] ttypes = t.getTypes();
		IType[] stypes = s.getTypes();
		//process types
		MapProcessor<IType> mpt = new MapProcessor<IType>(stypes,ttypes,MapProcessor.Action.KEEP);
		//add types
		Map<String, IType> toaddT = mpt.getToAdd();
		for(IType m: toaddT.values()){
			String msource = annotation+m.getSource();
			Debug.debug(getClass(),"Adding type:\n"+msource);
			t.createMethod(msource, null, false, null);
		}
	}


	/**resolves the included slice projectFolder*/
	private void resolveIncludedSlice() {
		try {
			for(IPackageFragmentRoot r :javap.getAllPackageFragmentRoots()){//for each package
				if(r.getKind()==IPackageFragmentRoot.K_SOURCE){
					if(r.getElementName().equals(sliceID.toString())){//verify if it is a source package
						this.sliceSrc = r;
						break;
					}
				}
			}
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}
	




}
