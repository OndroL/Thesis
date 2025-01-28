package cz.inspire.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

public class FileStorageUtil {

    private final String rootDirectory;

    public FileStorageUtil(String rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    public Map<String, String> saveFile(byte[] fileData, String originalFileName, String subDirectory) throws IOException {
        String uniqueName = UUID.randomUUID().toString();
        String filePath = rootDirectory + "/" + subDirectory + "/" + uniqueName;
        Path directoryPath = Paths.get(rootDirectory + "/" + subDirectory);
        Files.createDirectories(directoryPath); // Ensure the directory exists

        Files.write(Paths.get(filePath), fileData); // Save the file

        return Map.of("FilePath", filePath, "FileName", originalFileName); // Return file metadata
    }

    public byte[] readFile(String filePath) throws IOException {
        return Files.readAllBytes(Paths.get(filePath)); // Read file as bytes
    }
}
