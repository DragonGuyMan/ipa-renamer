package ajs.dragonguyman.iparename;

import com.dd.plist.NSDictionary;
import com.dd.plist.PropertyListFormatException;
import com.dd.plist.PropertyListParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * Intended to rename ipa files to more "No-Intro" like format
 */
public class IPARenamer {

    /**
     * @param args The command line arguments
     */
    public static void main(String[] args) {

        String ipaFilePath;         // Location of IPA file to rename
        String ipaFileName = "";    // Name of selected file
        boolean ipaUsable = false;  // For checking if given file is usable
        String appName;
        String appVersion;

        Scanner input = new Scanner(System.in);


        /* Input */
        System.out.println("Please enter the location of the IPA file.");
        ipaFilePath = input.nextLine();

        input.close();


        /* Testing if file can be used */
        File ipaFile = new File(ipaFilePath);

        if (ipaFile.exists()) {

            ipaFileName = ipaFile.getName();

            if (ipaFile.canRead() && ipaFile.canWrite()) {

                ipaUsable = true;
            }
            else {
                System.out.println(ipaFileName + " needs to be readable and writable.");
            }
        }
        else {
            System.out.println("The file location " + ipaFile.getAbsolutePath() + " doesn't exist or is invalid.");
        }


        /* Zip (ipa) file stuff */
        if (ipaUsable) {
            try (ZipFile zipFile = new ZipFile(ipaFilePath)) {

                String infoPath; // Path to Info.plist inside ipa file

                infoPath = ZipReader.findEntry(zipFile, "Info.plist");

                /* ipa Reading */
                if (infoPath == null) {
                    System.out.println("Error: The Info.plist file could not be found.");
                }
                else {
                    ZipEntry infoEntry = new ZipEntry(zipFile.getEntry(infoPath));
                    NSDictionary nsDictionary = (NSDictionary) PropertyListParser.parse(zipFile.getInputStream(infoEntry));

                    appVersion = nsDictionary.objectForKey("CFBundleVersion").toString();
                    appName = nsDictionary.objectForKey("CFBundleDisplayName").toString();

                    File rename = new File(appName + " (v" + appVersion + ").ipa");

                    // TODO Make it not delete original file
                    // TODO Figure out how to make it deposit in same folder as original file
                    if (ipaFile.renameTo(rename)) {
                        System.out.println(ipaFileName + " has been renamed to " + rename.getName());
                    }
                    else {
                        System.out.println("Error: Rename has failed");
                    }
                }
            }
            catch (ZipException e) {
                System.out.println("Error: " + ipaFile.getName() + " is not a valid zip/ipa file");
            }
            catch (IOException | SAXException | ParserConfigurationException | ParseException |
                   PropertyListFormatException e) {
                e.printStackTrace();
            }
        }
    }


}