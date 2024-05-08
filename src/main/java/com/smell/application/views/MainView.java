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

@PageTitle("SLP2")
@Route(value = "")
@PreserveOnRefresh
@CssImport("./styles/shared-styles.css")


public class MainView extends VerticalLayout {

    private EveService eveService;

    public MainView() {
        setSizeFull();
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        eveService = new EveService();

        AsciiBannerComponent kmwBanner = new AsciiBannerComponent(StaticData.getSPYBanner(), 6, AsciiBannerComponent.BannerAlignment.TOP_RIGHT);
        add(kmwBanner);

        AsciiBannerComponent flynnBanner = new AsciiBannerComponent(StaticData.getFlynnBanner(), 6, AsciiBannerComponent.BannerAlignment.TOP_LEFT);
        add(flynnBanner);

        createLayout();
        createFooter();
    }

    private Tab currentTab;
    private Tabs tabs;
    private VerticalLayout contentArea;

    private void createLayout() {
        tabs = new Tabs();
        contentArea = new VerticalLayout();
        contentArea.setSizeFull();

        Tab membershipSearchTab = new Tab("Membership Search");
        Tab otherSearchTab = new Tab("Alt Search");
        currentTab = membershipSearchTab;

        tabs.add(membershipSearchTab, otherSearchTab);
        tabs.setFlexGrowForEnclosedTabs(1);
        tabs.addSelectedChangeListener(event -> handleTabChange(event.getSelectedTab()));

        add(tabs, contentArea);
        switchTabContent(currentTab);
    }

    private void handleTabChange(Tab selectedTab) {
        if (!contentArea.getChildren().findFirst().isEmpty() && !currentTab.equals(selectedTab)) {
            confirmTabChange(selectedTab);
        }
    }

    private void switchTabContent(Tab selectedTab) {
        contentArea.removeAll();
        currentTab = selectedTab;
        tabs.setSelectedTab(selectedTab); // Update the tab selection visually
        if ("Membership Search".equals(selectedTab.getLabel())) {
            contentArea.add(new MembershipSearchComponent(eveService));
        } else if ("Alt Search".equals(selectedTab.getLabel())) {
            contentArea.add(new OtherSearchComponent());
        }
    }

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

    private void createFooter() {
        createLinks();
        HorizontalLayout footerLayout = new HorizontalLayout(linkToCorporation, linkToCreator, linkToRecruitment);
        footerLayout.setWidthFull();
        footerLayout.setClassName("footer-layout");
        footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        add(footerLayout);
    }

    private void createLinks() {
        linkToCorporation = new Anchor("https://zkillboard.com/corporation/679900455/", "Kriegsmarinewerft");
        linkToCorporation.setTarget("_blank");
        linkToCreator = new Anchor("https://zkillboard.com/character/2117078142/", "Creator: FlynnDynamics");
        linkToCreator.setTarget("_blank");
        linkToRecruitment = new Anchor("https://www.youtube.com/watch?v=-6Nf0G9n1to", "KMW Recruitment");
        linkToRecruitment.setTarget("_blank");
    }
}
