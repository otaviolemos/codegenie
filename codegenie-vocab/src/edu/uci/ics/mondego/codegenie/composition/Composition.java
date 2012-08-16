/**
 * @author Otavio Lemos
 */

package edu.uci.ics.mondego.codegenie.composition;
import org.eclipse.jdt.core.*;

import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jdt.core.IClasspathEntry;

public class Composition {

  private IJavaProject javaProject;
  private boolean insertedAnnotation = false;
  private boolean existingClass = false;
  private String toImport;
  private IType testType;

  public IType getTestType() {
    return testType;
  }

  public void setTestType(IType testType) {
    this.testType = testType;
  }
  
  public Composition(IJavaProject myJPrj, IType tType) {
    javaProject = myJPrj;
    testType = tType;
  }

  public Composition(IJavaProject myJPrj, IType tType, String toImp) {
    javaProject = myJPrj;
    testType = tType;
    toImport = toImp;
  }

  public void weave(String sourceSrcFolder, String targetSrcFolder) 
      throws JavaModelException {
    // get source and target source folders
    saveAndRebuild();
    IPackageFragmentRoot[] allPcks;
    try {
      allPcks = javaProject.getAllPackageFragmentRoots();		
    } catch(JavaModelException e) {
      System.out.println(e.getLocalizedMessage());
      throw e;
    }
    IPackageFragmentRoot sourceFR = null;
    IPackageFragmentRoot targetFR = null;
    for (int i = 0; i < allPcks.length && (sourceFR == null || targetFR == null); 
        i++) {
      if (allPcks[i].getElementName().equals(sourceSrcFolder)) 
        sourceFR = allPcks[i];
      else if (allPcks[i].getElementName().equals(targetSrcFolder))
        targetFR = allPcks[i]; 
    }
    if (sourceFR == null || targetFR == null) {
      //throw new Exception("Source or target folder not found.");
      return;
    }
    IJavaElement[] elements = sourceFR.getChildren();
    try {
      // create missing packages in the target source folder
      for(int i = 1; i < elements.length; i++) {
        String parts[] = elements[i].getResource().getFullPath().toPortableString().replace(IPath.SEPARATOR, ':').split(":");
        String pckName = parts[3]; 
        for(int l=4; l < parts.length; l++)
          pckName = pckName + "." + parts[l]; 

        //String pckName = elements[i].getResource().getName();

        if (!targetFR.getPackageFragment(pckName).exists())
          targetFR.createPackageFragment(pckName, true, null);
        IResource[] res = ((IFolder) elements[i].getResource()).members(); 
        for (int j = 0; j < res.length; j++) {
          try {

            if(res[j].getType() == IResource.FOLDER)
              continue;

            IPath theFolderPath = targetFR.getPackageFragment(pckName)
                .getResource().getFullPath().removeFirstSegments(1); 
            IFile myFile = javaProject.getProject()
                .getFile(theFolderPath + "/" + res[j].getName());

            // create missing files in the target source folder
            // but with the @FromSlice annotation
            
            if (!myFile.exists()) {
              insertAnnotation(targetFR, pckName);
              IBuffer originalContent = sourceFR.getPackageFragment(pckName).getCompilationUnit(myFile.getName()).getBuffer();
              int typeOffset = sourceFR.getPackageFragment(pckName).getCompilationUnit(myFile.getName()).findPrimaryType().getSourceRange().getOffset();
              int typeLength = sourceFR.getPackageFragment(pckName).getCompilationUnit(myFile.getName()).findPrimaryType().getSourceRange().getLength();
              originalContent.replace(typeOffset, typeLength, 
                  "@FromSlice(name=" + "\"" + sourceFR.getElementName() + "\"" + ") \r" + 
                      originalContent.getContents().substring(typeOffset, typeOffset + typeLength));
              targetFR.getPackageFragment(pckName).createCompilationUnit(myFile.getName(),
                  originalContent.getContents(), true, null);
              sourceFR.getPackageFragment(pckName).getCompilationUnit(myFile.getName()).delete(true, null);
            } else {
              IJavaElement[] javaElements = null;
              javaElements = targetFR.getPackageFragment(pckName).getChildren();
              int h = 0;
              IJavaElement mje;
              do {
                mje = javaElements[h];
                h++;					  
              } while(!mje.getResource().getName().equals(myFile.getName()));

              //adds the annotation type to the project if it is not already there
              insertAnnotation(targetFR, pckName);
              //merge the methods in a type
              //todo: check secondary types - those also need to be added
              mergeTypes(((ICompilationUnit)mje).findPrimaryType(),
                  sourceFR.getPackageFragment(pckName).getCompilationUnit(myFile.getName())
                  .findPrimaryType());

              sourceFR.getPackageFragment(pckName).getCompilationUnit(myFile.getName()).delete(true, null);
            }
          } catch (Exception e) {}
        }
        
      }
      
      //inserts import statement in the test type for the desired function
      
      if(!existingClass) {
        testType.getCompilationUnit().createImport(toImport, null, null);
        testType.getCompilationUnit().getBuffer().save(null, true);
      }
      
      

      // delete the slice folder that was woven and remove it from build path 
      sourceFR.getResource().delete(true, null);
      IClasspathEntry[] currentCP = javaProject.getRawClasspath();
      IClasspathEntry[] newCP = new IClasspathEntry[currentCP.length-1];
      for(int i = 0; i < currentCP.length; i++) {
        if (!currentCP[i].getPath().equals(sourceFR.getResource().getFullPath()))
          newCP[i] = javaProject.getRawClasspath()[i];
      }
      javaProject.setRawClasspath(newCP, null);
      targetFR.getResource().refreshLocal(IResource.DEPTH_INFINITE, null);
      saveAndRebuild();
    } catch(Exception e) {

    }

  }

  private void mergeTypes(IType target, IType source) {

    try {
      IMethod[] sourceMethods = null;
      IField[] sourceFields = null;
      IType[] sourceTypes = null;
      IImportDeclaration[] sourceImports = null;
      sourceMethods = source.getMethods();
      sourceFields = source.getFields();
      sourceTypes = source.getTypes();
      sourceImports = source.getCompilationUnit().getImports();

      // create all methods that are missing in the target type
      for (int i = 0; i < sourceMethods.length; i++)
        if (target.findMethods(sourceMethods[i]) == null)
          target.createMethod("@FromSlice(name=" + "\"" + source.getPackageFragment().getParent().getElementName() + "\"" + ") \r" +
              sourceMethods[i].getSource(), null, true, null);

      // create all fields that are missing in the target type
      for (int i = 0; i < sourceFields.length; i++)
        if (!target.getField(sourceFields[i].getElementName()).exists())
          target.createField("@FromSlice(name=" + "\"" + source.getPackageFragment().getParent().getElementName() + "\"" + ") \r" +
              sourceFields[i].getSource(), null, true, null);

      // create all types that are missing in the target type
      for (int i = 0; i < sourceTypes.length; i++)
        if (!target.getType(sourceTypes[i].getElementName()).exists())
          target.createType("@FromSlice(name=" + "\"" + source.getPackageFragment().getParent().getElementName() + "\"" + ") \r" +
              sourceTypes[i].getSource(), null, true, null);

      // create all imports that are missing in the target type
      for (int i = 0; i < sourceImports.length; i++) {
        if (!target.getCompilationUnit().getImport(sourceImports[i].getElementName()).exists()) {
          target.getCompilationUnit().createImport(sourceImports[i].getElementName(),
              null, null);
        }
      }

      // save changes
      target.getCompilationUnit().getBuffer().save(null, true);

    } catch (Exception e) {}
  }

  private void insertAnnotation(IPackageFragmentRoot targetFR, String pckName) {
    if (!insertedAnnotation) {
      try {
        String contents = "package " + pckName + "; \r" +
            "public @interface FromSlice { \r String name(); \n}";
        targetFR.getPackageFragment(pckName).createCompilationUnit("FromSlice.java", 
            contents, true, null);
        insertedAnnotation = true;
      }
      catch (JavaModelException e) {
        System.out.println(e.getLocalizedMessage());
      }
    }
  }

  public void unweave(String targetSrcFolder, String sliceName, boolean deleteAnnotation)
      throws JavaModelException {
    //todo: delete empty packages
    IPackageFragmentRoot[] allPcks;
    try {
      allPcks = javaProject.getAllPackageFragmentRoots();		
    } catch(JavaModelException e) {
      System.out.println(e.getLocalizedMessage());
      throw e;
    }
    IPackageFragmentRoot targetFR = null;
    for (int i = 0; i < allPcks.length && (targetFR == null);i++) {
      if (allPcks[i].getElementName().equals(targetSrcFolder)) 
        targetFR = allPcks[i];
    }
    IJavaElement[] pcks = targetFR.getChildren();
    for (int i = 0; i < pcks.length; i++) {
      IResource[] resources = null;
      try {
        resources = ((IFolder) pcks[i].getResource()).members();
      } catch (Exception e) {}

      for(int j=0; j < resources.length; j++) {
        if (resources[j].getType() == IResource.FILE) {
          ICompilationUnit cpu = 
              targetFR.getPackageFragment(pcks[i].getElementName()).
              getCompilationUnit(resources[j].getName());

          // check whether type is annotaded - if it is, delete the whole resource
          // todo: change to APT
          if (cpu.findPrimaryType().getSource().indexOf("@FromSlice(name=\"" +
              sliceName + "\")") == 0 
              || (deleteAnnotation && cpu.findPrimaryType().getElementName().equals("FromSlice"))) {
            try {
              cpu.delete(true, null);
            } catch(Exception e) {
              System.out.println(e.getLocalizedMessage());
            }
          } else {

            // if type itself is not annotated search for annotaded 
            // methods, fields and inner types and delete them
            // TODO: change to APT
            IType myType = cpu.findPrimaryType();
            IMethod[] myMethods = myType.getMethods();
            IField[] myFields = myType.getFields();
            IType[] myTypes = myType.getTypes();
            for(int h=0; h < myMethods.length; h++) {
              if(myMethods[h].getSource().indexOf("@FromSlice(name=\"" +
                  sliceName + "\")") == 0) {
                myMethods[h].delete(true, null);
                cpu.getBuffer().save(null, true);
              }
            }					
            for(int h=0; h < myFields.length; h++) {
              if(myFields[h].getSource().indexOf("@FromSlice(name=\"" +
                  sliceName + "\")") == 0) {
                myFields[h].delete(true, null);
                cpu.getBuffer().save(null, true);
              }
            }
            for(int h=0; h < myTypes.length; h++) {
              if(myTypes[h].getSource().indexOf("@FromSlice(name=\"" +
                  sliceName + "\")") == 0) {
                myTypes[h].delete(true, null);
                cpu.getBuffer().save(null, true);
              }
            }

          }
        }
      }
    }
    
    if(!existingClass) {
      testType.getCompilationUnit().getImport(toImport).delete(true, null);
      testType.getCompilationUnit().getBuffer().save(null, true);
    }
    saveAndRebuild();
  }

  public void saveAndRebuild() {
    try {
      javaProject.save(null, true);
      javaProject.getProject().build(IncrementalProjectBuilder.INCREMENTAL_BUILD, null);
    } catch (Exception e) {
      System.out.println(e.getLocalizedMessage());
    }
  }
  
  public void setExistingClass(boolean ec) {
    existingClass = ec;
  }
}
