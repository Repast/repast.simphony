package repast.simphony.util;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Utility methods for dealing with files and directories.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class FileUtils {

	/**
	 * Backups the specified directory into a new directory in the same parent directory.
	 * The backed up directory name will be a form of the name of the dirToBack + .bak + an optional number.
	 * The number will be increased until a unique name is found.
	 *
	 * @param dirToBackup
	 * @return the backup directory
	 */
	public static File backupDir(File dirToBackup) throws IOException {
		return FileUtils.backupDir(dirToBackup, dirToBackup.getParentFile());
	}

	/**
	 * Backs up the specified directory into a new directory in the specified parent directory.
	 * The backed up directory name will be a form of the name of the dirToBack + .bak + an optional number.
	 * The number will be increased until a unique name is found.
	 *
	 * @param dirToBackup     the directory to backup
	 * @param backupParentDir the parent directory of the new backup directory
	 * @return the backup directory
	 */
	public static File backupDir(File dirToBackup, File backupParentDir) throws IOException {
		File bakDir = new File(backupParentDir, dirToBackup.getName() + ".bak");
		int count = 0;
		while (bakDir.exists()) {
			bakDir = new File(backupParentDir, dirToBackup.getName() + ".bak" + count++);
		}

		FileUtils.copyDirs(dirToBackup, bakDir);
		return bakDir;
	}

	/**
	 * Copies the specified source file to the destination file.
	 *
	 * @param source the source to copy
	 * @param dest   the destination file
	 */
	public static void copyFile(File source, File dest) throws IOException {
		if (source.equals(dest)) throw new IOException("Source and destination cannot be the same file path");
		FileChannel srcChannel = new FileInputStream(source).getChannel();

		// Create channel on the destination
		if (!dest.exists()) dest.createNewFile();
		FileChannel dstChannel = new FileOutputStream(dest).getChannel();

		// Copy file contents from source to destination
		dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

		// Close the channels
		srcChannel.close();
		dstChannel.close();
	}

	/**
	 * Recursively copy all the files from the source directory to the destination directory. If the
	 * destination directory does not exist it will be created.
	 *
	 * @param sourceDir the source directory
	 * @param destDir   the destination directory
	 * @throws IOException
	 */
	public static void copyDirs(File sourceDir, File destDir) throws IOException {
		if (!destDir.exists()) destDir.mkdirs();

		for (File file : sourceDir.listFiles()) {
			if (file.isDirectory()) {
				copyDirs(file, new File(destDir, file.getName()));
			} else {
				FileChannel srcChannel = new FileInputStream(file).getChannel();

				// Create channel on the destination
				File out = new File(destDir, file.getName());
				out.createNewFile();
				FileChannel dstChannel = new FileOutputStream(out).getChannel();

				// Copy file contents from source to destination
				dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

				// Close the channels
				srcChannel.close();
				dstChannel.close();
			}
		}
	}

	/**
	 * Deletes the specified file. If file is a directory, then recursively delete the contents of that
	 * directory and the directory itself, unless the directory is a version control dir -- .svn or .cvs.
	 *
	 * @param file the file or directory to delete.
	 */
	public static void deleteIgnoreVC(File file, final String... filesToIgnore) {
		if (file.isDirectory()) {
			for (File child : file.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					String name = pathname.getName();
					for (String fileName : filesToIgnore) {
						if (name.matches(fileName)) {
							return false;
						}
					}
					
					return !(name.endsWith(".cvs") || name.endsWith(".svn"));
				}
			})) {
				FileUtils.deleteIgnoreVC(child);
			}
		}
		file.delete();
	}

	/**
	 * Deletes the specified file. If file is a directory, then recursively delete the contents of that
	 * directory and the directory itself.
	 *
	 * @param file the file or directory to delete.
	 */
	public static void delete(File file) {
		if (file.isDirectory()) {
			for (File child : file.listFiles()) {
				FileUtils.delete(child);
			}
		}
		file.delete();
	}

	/**
	 * Gets a file based on a certain name. The way this works is it checks the current directory
	 * for the file, then data/, demos/data/, repast/demos/data/, demos/, for the file. This is
	 * useful especially for demos that will be in the demos/ directory and ran through the Repast
	 * GUI.
	 * 
	 * @param filename
	 *            the name of the file to get
	 * 
	 * @return a file object corresponding to the filename or null if the file wasn't found
	 */
	public static File getDataFile(String filename) {
		File file = new File(filename);
		if (file.exists())
			return file;

		file = new File("./data/" + filename);
		if (file.exists())
			return file;

		file = new File("./demos/data/" + filename);
		if (file.exists())
			return file;

		file = new File("./repast/demos/data/" + filename);
		if (file.exists())
			return file;

		file = new File("./demos/" + filename);
		if (file.exists())
			return file;

		return null;
	}

	/**
	 * Gets a filename based on a certain name. The way this works is it checks the current
	 * directory for the file, then data/, demos/data/, repast/demos/data/, demos/, for the file.
	 * This is useful especially for demos that will be in the demos/ directory and ran through the
	 * Repast GUI.
	 * 
	 * @param filename
	 *            the name of the file to get
	 * 
	 * @return a String corresponding to the absolute path of the file or null if the file wasn't
	 *         found
	 */
	public static String getDataFileName(String filename) {
		File file = getDataFile(filename);

		if (file == null)
			return null;

		return file.getAbsolutePath();
	}
	
	public static void main(String args[]) {
		try {
			FileUtils.backupDir(new File("c:/tmp/info"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
