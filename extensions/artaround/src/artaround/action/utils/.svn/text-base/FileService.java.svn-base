/*
	Copyright © Rolf Sint, Michael Schneider, Christian Osterrieder, 2010
 */

package artaround.action.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * @author coster, www.utilo.eu 
 * Helper class to handle files
 */
public class FileService {
	
	public static void main (String args[]){
		File file = new File("c:\\daten\\datenSnapnews\\innotv\\data\\programme\\publish\\albus_1bild__001.swf");
		String f = FileService.generateMD5Base64Hash(file);
	}
	
	/**
	 * generates a md5 hash string encoded to Base64 <br/>
	 * @param password the string to convert to md5 
	 * @return the converted md5 string
	 */
	public static String generateMD5Base64Hash(File file) {
		
		if (!file.exists()){
			return null;
		}

		try {
						
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update( FileService.getBytesFromFile(file) );
			byte[] encrypted = algorithm.digest();
			
			return Base64.encode(encrypted);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return null;
	}	
	/**
	 * Returns the contents of the file in a byte array.
	 * @param file
	 * @return the contents of the file in a byte array.
	 * @throws IOException
	 */
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
    
        // Get the size of the file
        long length = file.length();
    
        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
    
        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];
    
        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
    
        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
    
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }
	/**
	 * copy a file
	 * 
	 * @param in
	 *            the file to copy
	 * @param the
	 *            new file
	 * */
	public static void copyFile(File in, File out) throws Exception {

		FileInputStream fis = new FileInputStream(in);
		FileOutputStream fos = new FileOutputStream(out);
		byte[] buf = new byte[1024];
		int i = 0;

		while ((i = fis.read(buf)) != -1) {
			fos.write(buf, 0, i);
		}

		fis.close();
		fos.close();

	}

	/**
	 * copy a file
	 * 
	 * @param in
	 *            the file to copy
	 * @param the
	 *            new file
	 * */
	public static void copyFile(String in, String out) throws Exception {
		FileService.copyFile(new File(in), new File(out));
	}

	/**
	 * Deletes all files under dir. Returns true if all deletions were
	 * successful. If a deletion fails, the method stops attempting to delete
	 * and returns false.
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteFilesInDir(File dir) {

		dir.setWritable(true);

		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteFilesInDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		if (dir.isFile()) {
			dir.delete();
		}

		return true;

	}
	/**
     * Deletes all files and subdirectories under dir.
     * Returns true if all deletions were successful.
     * If a deletion fails, the method stops attempting to delete and returns false.
     * @param dir the dir to delete
     * @return true if successful
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
    
        // The directory is now empty so delete it
        return dir.delete();
    }	
	/**
	 * reads the file date from a file in the programme folder publish or data
	 * @param filename the name of the file to check
	 * @return the date of the file
	 */
	public static Date getFileDate(String file){
		
		File f = new File(file);
		if (f.exists()){
			return new Date( f.lastModified() );
		}
		return null;
		
	}
	/**
	 * renames a file 
	 * 
	 * @param uri
	 *            the path to the file
	 * @param newName the new filename 
	 */
	public static void renameFile(String uri, String newName) throws Exception {

		if (uri == null || !(new File(uri)).exists() || newName == null){
			return;
		}
		
		File file = new File(uri);
		String filename = file.getName();
		String newUri  = uri.replace(filename, newName);
		file.renameTo( new File( newUri ) );

	}
	/**
	 * returns the file extension of the file eg. jpg
	 * 
	 * @param filename
	 *            the filename include the extension
	 * @return the file extension of the file
	 */
	public static String getFileExtension(String filename) throws Exception {

		String result = null;

		if (filename != null) {
			int point = filename.lastIndexOf(".");
			result = filename.substring(point, filename.length());
		}

		return result;

	}

	/**
	 * stores a file from a stream
	 * 
	 * @param uri
	 *            the URI of the file to store
	 * @param stream
	 *            an input stream of the file
	 * @return true if successfully
	 */
	public static Boolean storeFileFromStream(String uri, FileInputStream stream) throws Exception {

		if (uri == null || uri.equals("")) {
			return false;
		}

		FileOutputStream fos;

		fos = new FileOutputStream(uri);
		byte[] buf = new byte[1024];
		int read = 0;
		while ((read = stream.read(buf)) > 0) {
			fos.write(buf, 0, read);
		}

		fos.close();
		stream.close();

		return true;

	}

}
