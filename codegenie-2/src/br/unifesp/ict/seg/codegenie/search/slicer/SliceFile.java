package br.unifesp.ict.seg.codegenie.search.slicer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.preference.IPreferenceStore;

import br.unifesp.ict.seg.codegenie.Activator;
import br.unifesp.ict.seg.codegenie.pool.SearchResultMap;
import br.unifesp.ict.seg.codegenie.pool.SlicePool;
import br.unifesp.ict.seg.codegenie.pool.SolrPool;
import br.unifesp.ict.seg.codegenie.preferences.PreferenceConstants;
import br.unifesp.ict.seg.codegenie.search.CGMethodInterface;
import br.unifesp.ict.seg.codegenie.test.CodeGenieTestRunner;
import br.unifesp.ict.seg.codegenie.tmp.Debug;
import br.unifesp.ict.seg.codegenie.tmp.MySingleResult;
import br.unifesp.ict.seg.codegenie.util.Folders;
import br.unifesp.ict.seg.codegenie.util.Unzip;
import br.unifesp.ict.seg.codegenie.views.GRProgressMonitor;





public class SliceFile extends AbstractProjectEditor{

	protected byte[] bytes;
	protected Long eid;
	protected CGMethodInterface mi;
	protected String zipFileName;
	protected String projectFolder;
	//protected FailCase fc;
	protected String sliceSrcFolder;
	protected String sliceBkpFolder;
	protected Weaver weave;
	protected Unweaver unweave;
	protected Long queryID;


	/**Create a slice file ready to be saved, uncompressed, 
	 * Weave and removed if it is the case
	 * @param bytes the byte array returned by the server
	 * @param entityID the entity ID that these bytes represent*/
	public SliceFile(byte[] bytes, long entityID,Long queryID) {
		this.bytes=bytes;
		eid=entityID;
		this.queryID = queryID;
		//SlicePool.add(eid, this);
	}


	public void setMethodInterface(CGMethodInterface mi){
		this.mi=mi;

		//fc = FailCasePool.get(mi);
		javap = SearchResultMap.getProject(mi.getQueryID());
		weave = new Weaver(javap,eid);
		unweave = new Unweaver(javap,eid);
		unweave.setMethodInterface(mi);
	}

	/**unzip Slice into the projects /slices/src/ projectFolder
	 * @throws IOException if there is no zip file to unzip
	 * or if the projects projectFolder could not be reached
	 * */
	public void unzip() throws IOException{
		//get project projectFolder
		this.projectFolder = Folders.getProjectsFolder(javap.getProject());
		File f = new File(projectFolder);
		if(!f.exists())
			throw new IOException("[SliceFile]: Given path ("+projectFolder+") does not exist.");
		if(!f.isDirectory())
			throw new IOException("[SliceFile]: Given path ("+projectFolder+") is not a projectFolder.");

		//make folders
		Folders.createSliceFolders(f);
		//clear folders
		Folders.clearSliceFolders(new File(f.getAbsolutePath()),Folders.SRC_CODE,eid);
		//save zip
		try {
			saveZipFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//unzip
		this.sliceSrcFolder = projectFolder+File.separator+Folders.SLICE+File.separator+this.eid+File.separator+Folders.SRC_NAME;
		this.sliceBkpFolder = projectFolder+File.separator+Folders.SLICE+File.separator+this.eid+File.separator+Folders.BKP_NAME;
		Unzip uzip = new Unzip(zipFileName,sliceSrcFolder);
		uzip.unzip();
	}


	/**@throws Exception if there is no zip file to save*/
	private void saveZipFile() throws Exception{
		if(eid==null){
			throw new Exception("There is no zip file to save");
		}
		//give the zipFile a name
		zipFileName = projectFolder+File.separator+Folders.SLICE+File.separator+Folders.ZIP_NAME+File.separator+eid+".zip";
		//write on disk
		FileOutputStream fos;
		Debug.debug(getClass(),"Saving file \""+zipFileName+"\" with length "+bytes.length);
		try {
			fos = new FileOutputStream(new File(zipFileName));
			fos.write(bytes,0,bytes.length);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**merge this slice with the project
	 * @throws IOException 
	 * @throws CoreException */
	public void merge() throws IOException, CoreException {
		createAnnotation();
		weave.weave();
		cleanFolder();
	}


	private void cleanFolder() {
		try {
			Folders.delete(new File(projectFolder+File.separator
					+Folders.SLICE+File.separator+eid));
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	/**creates the missing method
	 * @param packageName an optional param that represents the packageName
	 * @throws JavaModelException */
	public void createMethod() throws JavaModelException{
		//create package
		String packageName = mi.getParentFqn();
		if(packageName.contains(".")){
			packageName = packageName.substring(0,packageName.lastIndexOf("."));
		}
		Debug.debug(getClass(), "creating missing method => "+mi);
		IPackageFragmentRoot pack = null;
		IJavaProject javap = SearchResultMap.getProject(queryID);


		IType clazz = javap.findType(mi.getParentFqn());

		if(clazz == null){//class does not exist

			//create packages if needed
			for(IPackageFragmentRoot pfr : javap.getAllPackageFragmentRoots()){
				if(pfr.getKind()==IPackageFragmentRoot.K_SOURCE){
					pack = pfr;
					break;
				}
			}
			if(packageName==null || packageName.equals("")){
				packageName = javap.getElementName();
			}
			IPackageFragment frag = pack.getPackageFragment(packageName);
			if(!frag.exists()){
				Debug.debug(getClass(), "creating missing package => "+packageName);
				frag = pack.createPackageFragment(packageName, true, null);
				Debug.debug(getClass(), "package created => "+frag);
			}
			
			//create class for sure
			String className = mi.getParentFqn();
			if(className.contains(".")){
				className = className.substring(className.lastIndexOf(".")+1);
			}
			GRProgressMonitor monitor = new GRProgressMonitor();
			SliceAddedAnn ann = new SliceAddedAnn(eid);
			String contents = "package "+packageName+";"+System.lineSeparator()+System.lineSeparator();
			contents+=ann.getImport()+System.lineSeparator();
			contents+=ann;
			contents+="public class "+className+"{"+System.lineSeparator()+"}";
			frag.createCompilationUnit(className+".java", contents, true, monitor);
			GRProgressMonitor.waitMonitor(monitor);
			this.saveAndRebuild();
			clazz = javap.findType(mi.getParentFqn());
		}
		Debug.debug(getClass(), "clazz is =>"+clazz);
		//TODO create method calling the inserted method
		MySingleResult sr = SolrPool.get(eid);
		String fqn = sr.getFqn();
		String parentsFqn = fqn.substring(0,fqn.lastIndexOf("."));
		IType mergedType = javap.findType(parentsFqn);
		for(IMethod m : mergedType.getMethods()){
			String sourceCode = m.getSource();
			
		}
		
	}


	/**change failing method contents to call the new method
	 * @throws Exception*/
	public void changeMethodContents(long eid) throws Exception{
		SliceAddedAnn ann = new SliceAddedAnn(eid);
		String annotation = ann.toString();
		//find mi in the java project
		IType t = javap.findType(mi.getParentFqn());

		t.getCompilationUnit().createImport(ann.fqn(), null, null);
		Set<IMethod> ms = new HashSet<IMethod>();
		for(IMethod m : t.getMethods()){
			if(m.getElementName().equals(mi.getMethodname())){
				ms.add(m);
			}
		}
		IMethod aux = null;
		IMethod method = null;
		for(Iterator<IMethod> it = ms.iterator();it.hasNext();){
			aux=it.next();
			if(mi.equalsParams(aux.getParameterTypes())){
				Debug.debug(getClass(),"Method found by comparing params...");
				method = aux;
				break;
			}
		}
		if(method==null){
			method = (IMethod) ms.toArray()[0];	
		}
		//mi found
		String originalSource = method.getSource();			//save original source
		//rename method and refresh project
		mi.setParent(t);
		method.rename("replaced"+method.getElementName(), false, null);	
		mi.setMethod(method);
		javap.getProject().refreshLocal(IProject.DEPTH_INFINITE, null);

		//create new method with same signature...
		int openBraceidx = originalSource.indexOf('{');
		int closeBraceidx = originalSource.lastIndexOf('}');
		if(!(openBraceidx<closeBraceidx && openBraceidx>0)){
			throw new Exception("Could not change source code from method: "+mi.toString());
		}
		String sourceCode = originalSource.substring(0,openBraceidx+1)
				+System.lineSeparator();//signature

		//get the woven method
		Debug.debug(getClass(),"looking for the entity result method...");
		MySingleResult r = SolrPool.get(eid);
		//get its type in the project
		String fqn = r.getFqn();
		String methodName = fqn.substring(fqn.lastIndexOf(".")+1);
		fqn = fqn.substring(0,fqn.lastIndexOf("."));
		Debug.debug(getClass(),"Looking for type: "+fqn);
		IType target = javap.findType(fqn);


		Debug.debug(getClass(),"looking for the method to be called... "+target.getElementName()+"."+methodName);

		//find the method that will be replaced
		Debug.debug(getClass(),"trying to find the method that will be replaced...("+methodName+")");
		Debug.debug(getClass(),"target is: "+target);		
		IMethod[] allmethods = target.getMethods();
		Set<IMethod> sameNameMethods = new HashSet<IMethod>();
		for(IMethod m: allmethods){
			Debug.debug(getClass(),m.getElementName()+"=="+methodName);
			if(m.getElementName().equals(methodName)){
				sameNameMethods.add(m);
			}
		}
		IMethod wanted = (IMethod) sameNameMethods.toArray()[0];

		Debug.debug(getClass(),"Done!");
		int flags = wanted.getFlags();
		boolean isstatic = Flags.isStatic(flags);


		Debug.debug(getClass(),"is static?: "+isstatic);
		String toAdd = "";
		boolean isvoid = sourceCode.contains(" void ");
		if(!Flags.isPublic(wanted.getFlags())){
			wanted = Folders.changeToPublic(wanted, target);
		}
		if(isstatic){//just call it
			if(!isvoid){
				toAdd+="return ";
			}
			toAdd+=target.getFullyQualifiedName()+"."+wanted.getElementName()+"(";
		} else {	//create new instance and then call it
			toAdd+=target.getFullyQualifiedName()+" "+"slicedObject = new "
					+target.getFullyQualifiedName()+"();\n"+(isvoid?"":"return ")+"slicedObject."
					+wanted.getElementName()+"(";
		}

		//adjust params
		Debug.debug(getClass(),"adding params...");
		int op = sourceCode.lastIndexOf('(')+1;
		int cp = sourceCode.lastIndexOf(')');
		String param = "";
		if(op<cp){
			param=sourceCode.substring(op,cp);
		}
		StringTokenizer tok = new StringTokenizer(param,",");//separate at ','
		while(tok.hasMoreTokens()){
			String p = tok.nextToken();
			p=p.trim();										//remove spaces beetwen comas
			toAdd+=p.split(" ")[1];							//first is the type, and second the name
			if(tok.hasMoreElements()){
				toAdd+=", ";
			}
		}
		toAdd+=");\n}";
		sourceCode+=toAdd;
		sourceCode = annotation+sourceCode;
		Debug.debug(getClass(),"Done!\n"+sourceCode);
		t.createMethod(sourceCode, null, true, null);
		Debug.debug(getClass(),"Method Created!");
	}

	public void runTests() {
		Debug.debug(getClass(),"Running tests...");
		CodeGenieTestRunner testRunner = new CodeGenieTestRunner(queryID);
		//try {
		testRunner.runTest();
		//} catch (JavaModelException e) {
		//	Debug.errDebug(getClass(),e.getMessage());
		//}
	}

	public static boolean removeSlice(long eid){
		SliceFile slice = SlicePool.get(eid);
		try {
			slice.remove();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean remove() throws IOException {
		try {
			Debug.debug(getClass(),"unweaving...");
			unweave.unweave(mi);
			saveAndRebuild();
			Debug.debug(getClass(),"restoring previous code...");
			mi.restore();
		} catch (CoreException e) {
			e.printStackTrace();
			return false;
		}
		saveAndRebuild();
		runTests();
		return true;
	}

	protected void createAnnotation() throws IOException{
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String pkgName = store.getString(PreferenceConstants.ANNOTATIONPACKAGE);
		String annName = store.getString(PreferenceConstants.ANNOTATIONCLASS);
		Debug.debug(getClass(), "creating annotation => "+pkgName+"."+annName);
		String sourceCode = "package "+pkgName+";\n";
		sourceCode+="public @interface "+annName+" {\n\t";
		sourceCode+="long entityID();\n}\n";
		File src = Folders.getSourceFolder(javap);
		File pack = null;
		if(pkgName.contains(".")){
			pack = src;
			StringTokenizer tok = new StringTokenizer(pkgName,".");
			while(tok.hasMoreTokens()){
				pack = new File(pack,tok.nextToken());
				if(!pack.exists()){
					pack.mkdir();
				}
			}
		} else {
			pack = new File(src,pkgName);
		}

		if(!pack.exists()){
			pack.mkdir();
		}
		File javaFile = new File(pack,annName+".java");
		if(!javaFile.exists()){
			javaFile.createNewFile();
		}
		OpenOption options= StandardOpenOption.WRITE;
		Files.write(javaFile.toPath(), sourceCode.getBytes(), options);
	}
}
