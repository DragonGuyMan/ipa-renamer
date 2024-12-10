package ajs.dragonguyman.iparename;

import java.io.File;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Methods for reading zip files and their entries.
 */
public class ZipReader {

    /**
     * @param zipFile The zip file to search for entry in
     * @param entryName Name of the file to search for
     * @return Path to entry relative to zip file, or null if not found
     */
    public static String findEntry(ZipFile zipFile, String entryName) {

        boolean entryNotFound = true; // Should be true as long as location of entry hasn't been found
        String entryPath = null;      // Path to entry

        Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();

        while (zipEntries.hasMoreElements() && entryNotFound) {

            ZipEntry zipEntry = new ZipEntry(zipEntries.nextElement()); // Current ZipEntry from enumeration
            File entry = new File(zipEntry.getName());                  // For methods returning path as string

            if (entry.getName().equals(entryName)) {
                entryNotFound = false;
                entryPath = entry.getPath();

                System.out.println("Entry found at " + entryPath);
            }
        }
        return entryPath;
    }
}
