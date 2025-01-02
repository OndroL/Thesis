package cz.inspire.thesis.data.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Converter
public class MapToBinaryConverter implements AttributeConverter<Map<String, String>, byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(Map<String, String> attribute) {
        if (attribute == null) {
            return null;
        }
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(attribute);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("Error serializing map to binary", e);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(byte[] dbData) {
        if (dbData == null || dbData.length == 0) {
            return new HashMap<>();
        }
        try (ByteArrayInputStream bis = new ByteArrayInputStream(dbData);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (Map<String, String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalArgumentException("Error deserializing binary to map", e);
        }
    }
}
