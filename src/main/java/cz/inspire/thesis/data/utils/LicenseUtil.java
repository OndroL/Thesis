package cz.inspire.thesis.data.utils;

import cz.inspire.thesis.data.model.LicenseEntity;

public class LicenseUtil {
    /**
     * Helper method to generate GUID for reason of mimicking the original implementation of Bean  (placeholder for actual implementation).
     */
    public static String generateGUID(LicenseEntity entity) {
        return java.util.UUID.randomUUID().toString();
    }
}
