package com.smell.application.views.main;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AsciiBannerComponent extends VerticalLayout {

    public AsciiBannerComponent(String asciiArt, int fontSize) {
        Span asciiBanner = new Span(asciiArt);
        asciiBanner.addClassNames("ascii-banner");

        getStyle().set("pointer-events", "none");
        getStyle().set("user-select", "none");
        getStyle().set("position", "absolute");
        getStyle().set("top", "0");
        getStyle().set("right", "25px");
        getStyle().set("font-size", fontSize + "px");
        getStyle().set("z-index", "1000");

        setDefaultHorizontalComponentAlignment(Alignment.END);

        add(asciiBanner);
    }
}