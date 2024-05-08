package com.smell.application.views;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * AsciiBannerComponent is a custom component in Vaadin that displays ASCII art
 * in a specified alignment within the application. This component uses absolute
 * positioning to place the ASCII art in one of the four corners of the container.
 * The ASCII art is rendered using a {@link Span} element styled to reflect
 * the desired font size and alignment.
 *
 * Usage of this component allows for easy embedding of ASCII art into Vaadin
 * applications with flexible positioning.
 *
 * @author FlynnDynamics
 * @version 0.x
 * @since 24/04/24
 */
public class AsciiBannerComponent extends VerticalLayout {

    /**
     * Enum describing possible alignments for the ASCII banner within its container.
     */
    public enum BannerAlignment {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
    }

    /**
     * Constructs an AsciiBannerComponent with the specified ASCII art, font size, and alignment.
     * The component is absolutely positioned within its parent container based on the provided alignment.
     *
     * @param asciiArt   The ASCII art to display as a string.
     * @param fontSize   The font size to be applied to the ASCII art.
     * @param alignment  The desired alignment of the ASCII banner within its container.
     */
    public AsciiBannerComponent(String asciiArt, int fontSize, BannerAlignment alignment) {
        Span asciiBanner = new Span(asciiArt);
        asciiBanner.addClassNames("ascii-banner");

        add(asciiBanner);

        getStyle()
                .set("font-size", fontSize + "px")
                .set("position", "absolute");

        configureAlignment(alignment);
    }

    /**
     * Configures the alignment of the ASCII banner within the container.
     * This method applies CSS styles to position the banner at the specified
     * location of the container.
     *
     * @param alignment  The alignment setting that specifies where to place the banner.
     */
    private void configureAlignment(BannerAlignment alignment) {
        switch (alignment) {
            case TOP_RIGHT:
                setDefaultHorizontalComponentAlignment(Alignment.END);
                getStyle()
                        .set("top", "10px")
                        .set("right", "10px");
                break;
            case TOP_LEFT:
                setDefaultHorizontalComponentAlignment(Alignment.START);
                getStyle()
                        .set("top", "10px")
                        .set("left", "10px");
                break;
            case BOTTOM_LEFT:
                setDefaultHorizontalComponentAlignment(Alignment.START);
                getStyle()
                        .set("bottom", "10px")
                        .set("left", "10px");
                break;
            case BOTTOM_RIGHT:
                setDefaultHorizontalComponentAlignment(Alignment.END);
                getStyle()
                        .set("bottom", "10px")
                        .set("right", "10px");
                break;
        }
    }
}
