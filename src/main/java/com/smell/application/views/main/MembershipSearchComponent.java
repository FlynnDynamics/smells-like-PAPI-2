package com.smell.application.views.main;

import com.smell.application.structure.CharacterDataService;
import com.smell.application.structure.EveService;
import com.smell.application.user.Character;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.io.IOException;

public class MembershipSearchComponent extends VerticalLayout {

    private TextField urlField;
    private Button searchButton;
    private Image characterImage;
    private EveService eveService; // Using EveService passed from MainView
    private CharacterView characterView;

    public MembershipSearchComponent(EveService eveService) {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        this.eveService = eveService; // Use the passed EveService
        characterView = new CharacterView();
        characterView.setVisible(false);

        createSearch();
        add(characterView);  // Add the character view after the search components
    }

    private void createSearch() {
        urlField = new TextField("Character URL");
        urlField.setPlaceholder("https://zkillboard.com/character/454518485/");
        urlField.setWidth("300px");

        searchButton = new Button("Search", event -> searchCharacter());

        characterImage = new Image();
        characterImage.setMaxHeight("128px");
        characterImage.setSrc("https://images.evetech.net/characters/null/portrait");
        characterImage.setVisible(true);

        HorizontalLayout searchLayout = new HorizontalLayout(urlField, searchButton);
        searchLayout.setWidthFull();
        searchLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        add(searchLayout, characterImage); // Ensure search components are added first
    }

    private void searchCharacter() {
        String url = urlField.getValue();
        try {
            Long characterId = extractCharacterIdFromUrl(url);
            System.out.println(characterId);
            characterImage.setSrc("https://images.evetech.net/characters/" + characterId + "/portrait");
            characterImage.setVisible(true);
            processMembership(characterId);
        } catch (Exception e) {
            Notification.show("Invalid URL or problem extracting the ID.");
        }
    }

    private Long extractCharacterIdFromUrl(String url) {
        try {
            String[] pathSegments = url.split("/");
            return Long.parseLong(pathSegments[pathSegments.length - 1]);
        } catch (NumberFormatException e) {
            Notification.show("The URL must contain a valid character ID.", 3000, Notification.Position.MIDDLE);
            characterImage.setVisible(false);
            characterView.setVisible(false);
            return null;
        }
    }

    private void processMembership(long id) throws IOException, InterruptedException {
        Character character = CharacterDataService.getCharacterData(id, eveService);
        characterView.updateList(character);
        characterView.setVisible(true);
    }
}
