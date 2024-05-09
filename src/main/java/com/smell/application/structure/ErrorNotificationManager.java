package com.smell.application.structure;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * A utility class to manage and display error notifications.
 * *
 *
 * @author FlynnDynamics
 * @version ${version}
 * @since 24/04/24
 */
public class ErrorNotificationManager {
    private static final List<String> messages = new ArrayList<>();

    /**
     * Adds an error message to the list of messages to be displayed.
     *
     * @param message The error message to add.
     */
    public static void addErrorMessage(String message) {

        if (message.isEmpty()) {
            messages.add("Errors have occurred which may affect the integrity and informative value of the data collected.");
            messages.add("The following errors have occurred:");
            messages.add("#################################");
        }

        messages.add(message);
    }

    /**
     * Displays all accumulated error messages as notifications and clears the list.
     */
    public static void showAndClearMessages() {
        if (!messages.isEmpty()) {
            messages.forEach(msg -> {
                Notification notification = new Notification(msg, 5000, Position.BOTTOM_START);
                notification.open();
            });
            messages.clear();
        }
    }
}
