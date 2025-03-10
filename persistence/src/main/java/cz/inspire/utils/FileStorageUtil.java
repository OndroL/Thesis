package cz.inspire.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class FileStorageUtil {

    private final String rootDirectory;

    public FileStorageUtil(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public FileAttributes saveFile(byte[] fileData, String originalFileName, String subDirectory) throws IOException {
        // Generate a unique file name
        String uniqueName = UUID.randomUUID().toString();
        String filePath = rootDirectory + "/" + subDirectory + "/" + uniqueName;

        // Ensure the directory exists
        Path directoryPath = Paths.get(rootDirectory + "/" + subDirectory);
        Files.createDirectories(directoryPath);

        // Write the file data
        Files.write(Paths.get(filePath), fileData);

        // Return a File object with metadata
        return new FileAttributes(originalFileName, filePath);
    }


    public byte[] readFile(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath)); // Read file as bytes
    }

    public void removeFile(String filePath) throws IOException {
        Files.deleteIfExists(Paths.get(filePath)); // Delete the file if it exists
    }
}
