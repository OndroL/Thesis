package cz.inspire.thesis.data.utils;

import cz.inspire.thesis.data.model.PrintTemplateEntity;

public class PrintTemplateUtil {
    /**
     * Helper method to generate GUID for reason of mimicking the original implementation of Bean  (placeholder for actual implementation).
     */
    public static String generateGUID(PrintTemplateEntity entity) {
        return java.util.UUID.randomUUID().toString();
    }
}
