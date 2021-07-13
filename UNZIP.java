import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UNZIP
{
    public static void main(String[] args)
    {
        List<String> Zipfile_path = new ArrayList<>();
        Zipfile_path.add("Input\\ZipFilePath1.zip");
        Zipfile_path.add("Input\\ZipFilePath2.zip");
        //Add more ZIP folder paths

        int Path_Count=0;
        for(String each:Zipfile_path) {
            //Open the file
            try (ZipFile file = new ZipFile(each)) {
                Path_Count++;
                FileSystem fileSystem = FileSystems.getDefault();
                //Get file entries
                Enumeration<? extends ZipEntry> entries = file.entries();

                //We will unzip files in this folder so change the output path accordingly
                String uncompressedDirectory = "\\Output"+Path_Count+"\\";
                File directory = new File(uncompressedDirectory);
                if(!directory.exists())
                    Files.createDirectory(fileSystem.getPath(uncompressedDirectory));

                //Iterate over entries
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    if (entry.isDirectory()) {
                        System.out.println("Creating Directory:" + uncompressedDirectory + entry.getName() + "\\");
                        Files.createDirectories(fileSystem.getPath(uncompressedDirectory + entry.getName() + "\\"));
                    }
                    else {
                        InputStream is = file.getInputStream(entry);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        String uncompressedFileName = uncompressedDirectory + entry.getName();
                        Path uncompressedFilePath = fileSystem.getPath(uncompressedFileName);
                        Files.createFile(uncompressedFilePath);
                        FileOutputStream fileOutput = new FileOutputStream(uncompressedFileName);
                        while (bis.available() > 0) {
                            fileOutput.write(bis.read());
                        }
                        fileOutput.close();
                        System.out.println("Written :" + entry.getName());
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
