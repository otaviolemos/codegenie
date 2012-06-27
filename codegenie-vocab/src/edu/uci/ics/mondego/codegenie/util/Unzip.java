/**
 * @author Otavio Lemos
 */

package edu.uci.ics.mondego.codegenie.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * UnZip -- print or unzip a JAR or PKZIP file using java.util.zip. Command-line
 * version: extracts files.
 * 
 * @author Ian Darwin, Ian@DarwinSys.com $Id: UnZip.java,v 1.7 2004/03/07
 *         17:40:35 ian Exp $
 */
public class Unzip {
  /** Constants for mode listing or mode extracting. */
  public static final int LIST = 0, EXTRACT = 1;
  protected int mode = LIST;

  /** The ZipFile that is used to read an archive */
  protected ZipFile zippy;

  /** The buffer for reading/writing the ZipFile data */
  protected byte[] b;

  /** Construct an UnZip object. Just allocate the buffer */
  public Unzip() {
    b = new byte[8092];
  }

  /** Cache of paths we've mkdir()ed. */
  protected SortedSet dirsMade;
  
  /** The root directory where files will be unzipped */
  protected String rootDir;

  /** For a given Zip file, process each entry. */
  public void unZip(String rootDir, String fileName) {	  
	//rootDir = fileName.substring(0, 
	//		fileName.substring(0, fileName.lastIndexOf("/")).lastIndexOf("/")+1);
	this.rootDir = rootDir;//.substring(0, fileName.substring(0, fileName.lastIndexOf("/")).lastIndexOf("/")+1);
    dirsMade = new TreeSet();
    try {
      zippy = new ZipFile(fileName);
      Enumeration all = zippy.entries();
      while (all.hasMoreElements()) {
        getFile((ZipEntry) all.nextElement());
      }
    } catch (IOException err) {
      System.err.println("IO Error: " + err);
      return;
    }
  }

  protected boolean warnedMkDir = false;

  /**
   * Process one file from the zip, given its name. Either print the name, or
   * create the file on disk.
   */
  protected void getFile(ZipEntry e) throws IOException {
	  String zipName = e.getName();
	  if (zipName.startsWith("/")) {
		  if (!warnedMkDir)
			  System.out.println("Ignoring absolute paths");
		  warnedMkDir = true;
		  zipName = zipName.substring(1);
	  }
	  
	  // if a directory, just return. We mkdir for every file,
	  // since some widely-used Zip creators don't put out
	  // any directory entries, or put them in the wrong place.
	  if (zipName.endsWith("/")) {
		  return;
	  }
	  
	  // Else must be a file; open the file for output
	  // Get the directory part.
	  int ix = zipName.lastIndexOf('/');
	  if (ix > 0) {
		  String dirName = zipName.substring(0, ix);
		  if (!dirsMade.contains(dirName)) {
			  File d = new File(rootDir + dirName);
			  // If it already exists as a dir, don't do anything
			  if (!(d.exists() && d.isDirectory())) {
				  // Try to create the directory, warn if it fails
				  System.out.println("Creating Directory: " + rootDir + dirName);
				  if (!d.mkdirs()) {
					  System.err.println("Warning: unable to mkdir "
							  + rootDir + dirName);
				  }
				  dirsMade.add(dirName);
			  }
		  }
	  }
	  System.err.println("Creating " + rootDir + zipName);
	  FileOutputStream os = new FileOutputStream(rootDir+zipName);
	  InputStream is = zippy.getInputStream(e);
	  int n = 0;
	  while ((n = is.read(b)) > 0)
		  os.write(b, 0, n);
	  is.close();
	  os.close();
  }
}
