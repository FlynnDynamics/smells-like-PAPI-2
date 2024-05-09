package com.smell.application.views;

import com.smell.application.structure.EveService;
import com.smell.application.search.MembershipSearchComponent;
import com.smell.application.search.OtherSearchComponent;
import com.smell.application.structure.res.StaticData;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

/**
 * Represents the main view of the web application, providing a user interface for navigating
 * between different search functionalities within tabs. This view is the primary entry point
 * for users interacting with the EVE Online character and entity search capabilities.
 *
 * ██     ██  █████  ██████  ███    ██ ██ ███    ██  ██████
 * ██     ██ ██   ██ ██   ██ ████   ██ ██ ████   ██ ██
 * ██  █  ██ ███████ ██████  ██ ██  ██ ██ ██ ██  ██ ██   ███
 * ██ ███ ██ ██   ██ ██   ██ ██  ██ ██ ██ ██  ██ ██ ██    ██
 * ███ ███  ██   ██ ██   ██ ██   ████ ██ ██   ████  ██████
 *
 * <p>
 * Note: This implementation of MainView serves as a placeholder and is expected to be replaced or significantly
 * modified in future versions. The current implementation should not be considered final and
 * may lack complete functionality.
 * </p>
 *
 * ██     ██  █████  ██████  ███    ██ ██ ███    ██  ██████
 * ██     ██ ██   ██ ██   ██ ████   ██ ██ ████   ██ ██
 * ██  █  ██ ███████ ██████  ██ ██  ██ ██ ██ ██  ██ ██   ███
 * ██ ███ ██ ██   ██ ██   ██ ██  ██ ██ ██ ██  ██ ██ ██    ██
 * ███ ███  ██   ██ ██   ██ ██   ████ ██ ██   ████  ██████
 *
 * @author FlynnDynamics
 * @version ${version} Temporary implementation for demonstration purposes.
 * @since 24/04/24 The date of the first deployment or usage in the project.
 */
@PageTitle("SLP2")
@Route(value = "")
@PreserveOnRefresh
@CssImport("./styles/shared-styles.css")
public class MainView extends VerticalLayout {

    private EveService eveService;

    /**
     * Constructs the main view, setting up the user interface components and integrating the EveService.
     * This method initializes the layout, adds ASCII banners, and configures the tabs for navigation.
     */
    public MainView() {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        eveService = new EveService();

        add(new AsciiBannerComponent(StaticData.getSpyWareBanner(), 6, AsciiBannerComponent.BannerAlignment.TOP_RIGHT));
        add(new AsciiBannerComponent(StaticData.getNameBanner(), 6, AsciiBannerComponent.BannerAlignment.TOP_LEFT));

        createLayout();
        createFooter();
    }

    private Tab currentTab;
    private Tabs tabs;
    private VerticalLayout contentArea;

    /**
     * Initializes the layout with tabs and content areas for each search functionality.
     * Currently, only the 'Membership Search' tab is active, while others serve as placeholders
     * and are disabled.
     */
    private void createLayout() {
        tabs = new Tabs();
        contentArea = new VerticalLayout();
        contentArea.setSizeFull();

        Tab mSSTab = new Tab("Membership Search");
        Tab cCSTab = new Tab("CC Search");
        Tab aCSTab = new Tab("ACS Search");

        cCSTab.setEnabled(false);
        aCSTab.setEnabled(false);

        currentTab = mSSTab;

        tabs.add(mSSTab, cCSTab, aCSTab);

        tabs.setFlexGrowForEnclosedTabs(1);
        tabs.addSelectedChangeListener(event -> handleTabChange(event.getSelectedTab()));

        add(tabs, contentArea);
        switchTabContent(currentTab);
    }

    /**
     * Handles tab changes, checking for unsaved changes and prompting the user to confirm
     * navigation away from the current tab.
     *
     * @param selectedTab The tab selected by the user.
     */
    private void handleTabChange(Tab selectedTab) {
        if (!contentArea.getChildren().findFirst().isEmpty() && !currentTab.equals(selectedTab)) {
            confirmTabChange(selectedTab);
        }
    }

    /**
     * Switches the content displayed in the main area based on the selected tab.
     * This method dynamically loads the appropriate content for the selected search functionality.
     *
     * @param selectedTab The tab to switch the content for.
     */
    private void switchTabContent(Tab selectedTab) {
        contentArea.removeAll();
        currentTab = selectedTab;
        tabs.setSelectedTab(selectedTab); // Update the tab selection visually
        if ("Membership Search".equals(selectedTab.getLabel())) {
            contentArea.add(new MembershipSearchComponent(eveService));
        } else if ("CC Search".equals(selectedTab.getLabel())) {
            contentArea.add(new OtherSearchComponent());
        } else if ("ACS Search".equals(selectedTab.getLabel())) {
            contentArea.add(new OtherSearchComponent());
        }
    }

    /**
     * Displays a confirmation dialog when attempting to switch tabs if there are potential unsaved changes.
     * This prevents accidental loss of data by informing the user and requiring confirmation.
     *
     * @param nextTab The tab that the user intends to switch to.
     */
    private void confirmTabChange(Tab nextTab) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setCloseOnEsc(false);
        confirmDialog.setCloseOnOutsideClick(false);

        Span message = new Span("Switching tabs will erase any existing search results. Do you want to continue?");
        Button confirmButton = new Button("Continue", event -> {
            switchTabContent(nextTab);
            confirmDialog.close();
        });
        Button cancelButton = new Button("Cancel", event -> {
            tabs.setSelectedTab(currentTab); // Revert the tab selection visually
            confirmDialog.close();
        });

        VerticalLayout dialogLayout = new VerticalLayout(message, confirmButton, cancelButton);
        confirmDialog.add(dialogLayout);
        confirmDialog.open();
    }

    private Anchor linkToCorporation;
    private Anchor linkToCreator;
    private Anchor linkToRecruitment;

    /**
     * Creates and configures footer links to external resources related to the EVE Online corporation and recruitment videos.
     * This adds interactive elements to the footer for user engagement.
     */
    private void createFooter() {
        createLinks();
        HorizontalLayout footerLayout = new HorizontalLayout(linkToCorporation, linkToCreator, linkToRecruitment);
        footerLayout.setWidthFull();
        footerLayout.setClassName("footer-layout");
        footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        add(footerLayout);
    }

    /**
     * Helper method to create and setup links in the footer. Each link is configured with a URL and target attribute
     * to open in a new tab, facilitating access to external resources without navigating away from the application.
     */
    private void createLinks() {
        linkToCorporation = new Anchor("https://zkillboard.com/corporation/679900455/", "Kriegsmarinewerft");
        linkToCorporation.setTarget("_blank");
        linkToCreator = new Anchor("https://zkillboard.com/character/2117078142/", "Creator: FlynnDynamics");
        linkToCreator.setTarget("_blank");
        linkToRecruitment = new Anchor("https://www.youtube.com/watch?v=-6Nf0G9n1to", "KMW Recruitment");
        linkToRecruitment.setTarget("_blank");
    }
}
