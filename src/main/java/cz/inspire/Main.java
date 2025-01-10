package cz.inspire;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Main is running...");

        // Initialize the CDI container
        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}