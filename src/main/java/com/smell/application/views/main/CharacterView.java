package com.smell.application.views.main;

import com.smell.application.structure.ComparisonService;
import com.smell.application.user.Alliance;
import com.smell.application.user.Character;
import com.smell.application.user.Corporation;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.grid.GridVariant;

/**
 * A Vaadin view for displaying details about characters and their associated corporations and alliances.
 * It uses a grid to show corporations and expands to show alliances when a corporation is selected.
 */
@Route("character-view")
@CssImport("./styles/shared-styles.css")
public class CharacterView extends VerticalLayout {

    private Grid<Corporation> grid;

    /**
     * Constructs the character view and initializes the UI components.
     */
    public CharacterView() {
        setHeightFull();
        setupGrid();
        add(grid);
    }


    /**
     * Sets up the grid used to display corporations and their alliances.
     * Defines columns for corporation details and a detailed renderer for showing alliances.
     */
    private void setupGrid() {
        grid = new Grid<>(Corporation.class, false);

        grid.addColumn(Corporation::getName).setHeader("Corporation Name").setKey("name");
        grid.addColumn(Corporation::getId).setHeader("Corporation ID").setKey("id");
        grid.addColumn(Corporation::getPlayerStartDate).setHeader("Beginn").setKey("playerStartDate");
        grid.setDetailsVisibleOnClick(false);
        grid.addColumn(Corporation::getPlayerEndDate).setHeader("Ende").setKey("playerEndDate");
        grid.setItemDetailsRenderer(new ComponentRenderer<>(corporation -> {

            VerticalLayout layout = new VerticalLayout();
            layout.addClassName("details-layout");

            for (Alliance alliance : corporation.getAlliances()) {
                if (alliance.getId() == 0) continue;
                if (ComparisonService.isTimeWithinRange(corporation.getPlayerStartDate(), corporation.getPlayerEndDate(), alliance.getStartDate(), alliance.getEndDate())) {
                    Span allianceDetails = new Span(String.format("%s; %d; %s; %s",
                            alliance.getName(), alliance.getId(), alliance.getStartDate(), alliance.getEndDate()));
                    allianceDetails.getStyle().set("padding-left", "30px");

                    if (alliance.isSpecial()) {
                        allianceDetails.addClassNames("special-alliance");
                    }

                    layout.add(allianceDetails);
                }
            }

            return layout;
        }));

        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    }

    /**
     * Updates the grid items based on the provided character object.
     * This method can be called to refresh the grid contents.
     *
     * @param character The character whose corporation data will be displayed.
     */
    public void updateList(Character character) {
        grid.setItems(character.getCorporations());
        grid.getColumns().forEach(col -> {
            grid.getDataProvider().fetch(new Query<>()).forEach(item -> grid.setDetailsVisible(item, true));

            if ("isSpecial".equals(col.getKey())) {
                col.setClassNameGenerator(corpo -> {

                    if (corpo instanceof Corporation) {
                        return ((Corporation) corpo).isSpecial() ? "special" : "";
                    }
                    return "";
                });
            }
        });
    }
}
