package cz.inspire.thesis.data.utils;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CollectionsHelper {
    public static byte[] serializeCollection(Collection<String> collection) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        return String.join(",", collection).getBytes();
    }

    public static Collection<String> deserializeCollection(byte[] data) {
        if (data == null || data.length == 0) {
            return Collections.emptyList();
        }
        String csv = new String(data);
        return Arrays.asList(csv.split(","));
    }

    public static byte[] serializeAttributes(Map<String, Object> attributes) {
        if (attributes == null) {
            return null;
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(attributes);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize attributes", e);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> deserializeAttributes(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Map<String, Object>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to deserialize attributes", e);
        }
    }
}
