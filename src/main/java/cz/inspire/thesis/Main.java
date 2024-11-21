package cz.inspire.thesis;

import cz.inspire.thesis.data.service.HeaderService;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Main is running...");

        // Initialize the CDI container
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            // Obtain the HeaderService bean
            HeaderService headerService = container.select(HeaderService.class).get();

            // Use the HeaderService
            headerService.ejbCreate("1", 1,1);
            System.out.println(headerService.findValidAtributes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}