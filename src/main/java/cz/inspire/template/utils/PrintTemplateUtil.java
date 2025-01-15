package cz.inspire.template.utils;


public class PrintTemplateUtil {

    /**
     * Helper method to generate GUID for reason of mimicking the original implementation of Beans
     * (placeholder for actual implementation).
     */
    public static <E> String generateGUID(E entity) {
        return java.util.UUID.randomUUID().toString();
    }
}
