package cz.inspire.thesis;

import cz.inspire.common.service.HeaderService;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Main is running...");


        // Initialize the CDI container
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            // Obtain the HeaderService bean
            HeaderService headerService = container.select(HeaderService.class).get();

            System.out.println(headerService.findValidAttributes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}