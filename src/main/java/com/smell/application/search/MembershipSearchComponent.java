package com.smell.application.search;

import com.smell.application.structure.CharacterDataService;
import com.smell.application.structure.ErrorNotificationManager;
import com.smell.application.structure.EveService;
import com.smell.application.user.Character;
import com.smell.application.views.CharacterView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * MembershipSearchComponent is a VerticalLayout component that provides functionality
 * for searching and displaying EVE Online characters. It uses a TextField for URL input,
 * a Button to trigger the search, and an Image to display the character portrait.
 * <p>
 * The component integrates with EveService to fetch character data based on the provided
 * URL, and it displays detailed character information through a CharacterView component.
 *
 * @author FlynnDynamics
 * @version 0.x
 * @since 24/04/24
 */
public class MembershipSearchComponent extends VerticalLayout {

    private static final Logger LOGGER = Logger.getLogger(MembershipSearchComponent.class.getName());

    private TextField urlField;
    private Button searchButton;
    private Image characterImage;
    private EveService eveService;
    private CharacterView characterView;
    private SearchHelper searchHelper;

    /**
     * Constructor for MembershipSearchComponent, initializes the component with dependencies.
     * Sets up the layout and integration with EveService for data fetching.
     *
     * @param eveService The service used to fetch character data from the EVE Online API.
     */
    public MembershipSearchComponent(EveService eveService) {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        this.eveService = eveService;
        characterView = new CharacterView();
        characterView.setVisible(false);

        searchHelper = new SearchHelper();
        urlField = searchHelper.getUrlField();
        searchButton = searchHelper.getSearchButton();
        searchButton.addClickListener(event -> searchCharacter());

        createSearch();
        add(characterView);
    }

    /**
     * Initializes the search components including a text field for the URL, a search button, and an image display.
     * Configures layout properties and adds them to the component.
     */
    private void createSearch() {
        characterImage = new Image();
        characterImage.setMaxHeight("128px");
        characterImage.setSrc("https://images.evetech.net/characters/null/portrait");

        HorizontalLayout searchLayout = new HorizontalLayout(urlField, searchButton);
        searchLayout.setWidthFull();
        searchLayout.setDefaultVerticalComponentAlignment(Alignment.END);
        searchLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout imageLayout = new VerticalLayout(characterImage);
        imageLayout.setWidthFull();
        imageLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(searchLayout, imageLayout);
    }

    /**
     * Triggered by the search button click, this method processes the URL entered in the text field,
     * extracts the character ID, and fetches the character portrait and details.
     * Logs actions and displays notifications on errors or invalid inputs.
     */
    private void searchCharacter() {
        String url = urlField.getValue();
        try {
            Long characterId = SearchHelper.extractCharacterIdFromUrl(url);
            LOGGER.info("Search initiated for character ID: " + characterId);
            characterImage.setSrc("https://images.evetech.net/characters/" + characterId + "/portrait");
            characterImage.setVisible(true);
            processMembership(characterId);
        } catch (Exception e) {
            LOGGER.severe("Failed to extract character ID from URL: " + e.getMessage());
            Notification.show("Invalid URL or problem extracting the ID.");
        }
    }

    /**
     * Fetches detailed character data using the character ID and updates the CharacterView.
     * Displays the character information in the UI. Logs the process and handles exceptions.
     *
     * @param id The character ID used to fetch character data.
     * @throws IOException          if there is an error in the network or server communication.
     * @throws InterruptedException if the thread is interrupted while waiting for data.
     */
    private void processMembership(long id) {
        try {
            Character character = CharacterDataService.getCharacterData(id, eveService);
            characterView.updateList(character);
            characterView.setVisible(true);
            LOGGER.info("Membership processed for character ID: " + id);
        } catch (IOException | InterruptedException e) {
            LOGGER.severe("Error fetching character data: " + e.getMessage());
            Notification.show("Failed to load character data, please try again.");
        } finally {
            ErrorNotificationManager.showAndClearMessages();
        }
    }
}
