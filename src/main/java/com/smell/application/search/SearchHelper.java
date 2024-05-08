package com.smell.application.search;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import java.util.logging.Logger;

/**
 * SearchHelper provides utilities for extracting a character ID from a URL and initializing
 * search-related components within a Vaadin application. This class encapsulates the functionality
 * needed to handle the input of URLs, initiate searches, and log outcomes of such operations.
 * The helper is designed to be reusable across different parts of the application that require
 * similar functionality.
 *
 * Usage:
 * <pre>
 * SearchHelper helper = new SearchHelper();
 * TextField urlField = helper.getUrlField();
 * Button searchButton = helper.getSearchButton();
 * searchButton.addClickListener(e -> {
 *     Long characterId = SearchHelper.extractCharacterIdFromUrl(urlField.getValue());
 *     if(characterId != null) {
 *         // proceed with characterId
 *     }
 * });
 * </pre>
 *
 * @author FlynnDynamics
 * @version 0.x
 * @since 24/04/24
 */
public class SearchHelper {
    private static final Logger LOGGER = Logger.getLogger(SearchHelper.class.getName());
    private TextField urlField;
    private Button searchButton;

    /**
     * Constructs a SearchHelper instance and initializes its components.
     */
    public SearchHelper() {
        createSearchComponents();
    }

    /**
     * Initializes the search components, specifically the text field and search button.
     * The text field is pre-configured with a placeholder and a fixed width.
     */
    private void createSearchComponents() {
        urlField = new TextField("Character URL");
        urlField.setPlaceholder("https://zkillboard.com/character/454518485/");
        urlField.setWidth("300px");

        searchButton = new Button("Search");
    }

    /**
     * Returns the text field used for URL input.
     * @return the text field for URL input
     */
    public TextField getUrlField() {
        return urlField;
    }

    /**
     * Returns the search button.
     * @return the search button
     */
    public Button getSearchButton() {
        return searchButton;
    }

    /**
     * Extracts and returns the character ID from the given URL.
     * Logs the outcome of the operation, including successful extraction and errors.
     *
     * @param url the URL from which to extract the character ID
     * @return the extracted character ID, or null if extraction fails
     */
    public static Long extractCharacterIdFromUrl(String url) {
        try {
            String[] pathSegments = url.split("/");
            Long characterId = Long.parseLong(pathSegments[pathSegments.length - 1]);
            LOGGER.info("Character ID extracted successfully: " + characterId);
            return characterId;
        } catch (NumberFormatException e) {
            LOGGER.severe("The URL must contain a valid character ID. Error: " + e.getMessage());
            Notification.show("The URL must contain a valid character ID.", 3000, Notification.Position.MIDDLE);
            return null;
        }
    }
}
