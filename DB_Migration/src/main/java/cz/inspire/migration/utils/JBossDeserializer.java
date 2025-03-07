package cz.inspire.migration.utils;

import org.jboss.invocation.MarshalledValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class JBossDeserializer {
    private static final Logger logger = LoggerFactory.getLogger(JBossDeserializer.class);

    public static Object deserialize(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        // Check for Java serialization header (0xACED)
        if (data.length < 2 || (data[0] & 0xFF) != 0xAC || (data[1] & 0xFF) != 0xED) {
            // Data does not appear to be a Java-serialized object.
            return data;
        }

        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream in = new ObjectInputStream(bis)) {

            Object obj = in.readObject();

            // If the object is an instance of MarshalledValue, unwrap it
            if (obj instanceof MarshalledValue) {
                return ((MarshalledValue) obj).get();
            }

            return obj;
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Deserialization failed for data: {}", bytesToHex(data), e);
            System.err.println("Deserialization failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Converts a byte array to a hex string for logging purposes.
     *
     * @param bytes The byte array.
     * @return Hexadecimal representation.
     */
    private static String bytesToHex(byte[] bytes) {
        if (bytes == null) return "";
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}
