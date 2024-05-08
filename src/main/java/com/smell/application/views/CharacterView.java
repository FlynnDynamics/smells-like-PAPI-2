package com.smell.application.views;

import com.smell.application.structure.ComparisonService;
import com.smell.application.user.Alliance;
import com.smell.application.user.Character;
import com.smell.application.user.Corporation;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.grid.GridVariant;

import java.util.logging.Logger;

/**
 * CharacterView is a Vaadin view component that displays information about characters
 * along with their associated corporations and alliances in a structured grid format.
 * The class is responsible for rendering corporation details and dynamically displaying
 * their alliances when selected.
 * <p>
 * The grid is configured to be non-clickable by default, and expands to show detailed
 * information about each corporation's alliances when appropriate conditions are met
 * within the date range specified.
 * <p>
 * This view is accessed via the "/character-view" route and includes specific styling
 * defined in "shared-styles.css".
 * <p>
 * Usage:
 * - The view is initialized with a full-height setting and immediately sets up the grid
 * upon construction.
 * - Corporations are listed with details such as names and IDs, and can be expanded to
 * show alliances that fall within specific temporal ranges.
 * - Special styling is applied to alliances marked as 'special' to highlight them in the UI.
 *
 * @author FlynnDynamics
 * @version 0.x
 * @since 24/04/24
 */

@Route("character-view")
@CssImport("./styles/shared-styles.css")
public class CharacterView extends VerticalLayout {
    private static final Logger LOGGER = Logger.getLogger(CharacterView.class.getName());

    private Grid<Corporation> grid;

    /**
     * Constructor for CharacterView. Initializes the grid and adds it to the layout.
     * The grid is set to fill the entire view.
     */
    public CharacterView() {
        setHeightFull();
        setupGrid();
        add(grid);
    }

    /**
     * Configures the grid to display corporation data and their alliances. The grid columns are set up to show
     * corporation names, IDs, and active dates. It also includes a detailed renderer for displaying
     * alliances associated with each corporation based on overlapping date ranges.
     */
    private void setupGrid() {
        try {
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
        } catch (Exception e) {
            LOGGER.severe("Error setting up the grid: " + e.getMessage());
            Notification.show("Failed to set up the grid.", 5000, Notification.Position.BOTTOM_START);
        }
    }

    /**
     * Updates the grid items based on the provided character object. This is typically called
     * when there is new data to display or when an update to the existing data set is needed.
     *
     * @param character The character whose corporation and alliance data should be displayed in the grid.
     */
    public void updateList(Character character) {
        try {
            grid.setItems(character.getCorporations());
            grid.getColumns().forEach(col -> {
                grid.getDataProvider().fetch(new Query<>()).forEach(item -> grid.setDetailsVisible(item, true));

                if ("isSpecial".equals(col.getKey())) {
                    col.setClassNameGenerator(corpo -> {

                        if (corpo instanceof Corporation) {
                            return corpo.isSpecial() ? "special" : "";
                        }
                        return "";
                    });
                }
            });
        } catch (Exception e) {
            LOGGER.severe("Error updating the grid list: " + e.getMessage());
            Notification.show("Failed to update the grid data.", 5000, Notification.Position.BOTTOM_START);
        }
    }
}
