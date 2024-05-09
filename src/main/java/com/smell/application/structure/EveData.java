package com.smell.application.structure;

import java.util.HashMap;

public class EveData {

    /**
     * Provides a centralized storage for specific corporation data categorized by unique traits or alliances, referred to as "snowflakes".
     * The class includes methods for managing and accessing these special categories and for identifying NPC (non-player controlled) corporations.
     * The data is stored statically, allowing easy access from various parts of the application without needing to instantiate the class.
     *
     * @author FlynnDynamics
     * @version ${version}
     * @since 24/04/24
     */

    private static HashMap<Long, String> snowflakes;

    /**
     * Returns a map of corporation IDs to their special categories. This method ensures the map is initialized only once and is populated
     * with predefined data representing various major alliances and their associated tags.
     *
     * @return A HashMap containing the corporation IDs and their associated "snowflake" categories.
     */

    public static HashMap<Long, String> getSnowflakes() {
        if (snowflakes != null)
            return snowflakes;

        snowflakes = new HashMap<>();
        // Initialization of "snowflake" categories with predefined data
        // For example, "Legacy", "PanFam", "Winter", and "Fire" with respective corporation IDs
        populateSnowflakes(); // Assume a method populateSnowflakes that populates the HashMap
        return snowflakes;
    }


    private static void populateSnowflakes() {
        snowflakes.put(99003214l, "Legacy");  //Brave Collective
        snowflakes.put(498125261l, "Legacy"); //Test Alliance Please Ignore
        snowflakes.put(99004116l, "Legacy");  //Warped Intentions
        snowflakes.put(99007289l, "Legacy");  //Federation Uprising
        snowflakes.put(99009104l, "Legacy");  //VINDICTIVE
        snowflakes.put(99010428l, "Legacy");  //Eternal Requiem
        snowflakes.put(99002367l, "Legacy");  //Evictus.
        snowflakes.put(99008809l, "Legacy");  //Already Replaced.
        snowflakes.put(982284363l, "Legacy"); //Sev3rance
        snowflakes.put(99009082l, "Legacy");  //The Army of Mango Alliance
        snowflakes.put(99010079l, "Legacy");  //Brave United
        snowflakes.put(99007439l, "Legacy");  //Blue Sun Interstellar Technologies
        snowflakes.put(99003838l, "Legacy");  //Requiem Eternal
        snowflakes.put(99008098l, "Legacy");  //The Watchman International Alliance

        snowflakes.put(1727758877l, "PanFam"); //Northern Coalition.
        snowflakes.put(99005338l, "PanFam");   //Pandemic Horde
        snowflakes.put(386292982l, "PanFam");  //Pandemic Legion
        snowflakes.put(1042504553l, "PanFam"); //Solyaris Chtonium
        snowflakes.put(99000163l, "PanFam");   //Northern Associates.
        snowflakes.put(99003006l, "PanFam");   //Brothers of Tangra
        snowflakes.put(99009289l, "PanFam");   //Reckless Contingency.
        snowflakes.put(99002003l, "PanFam");   //No Value
        snowflakes.put(99007722l, "PanFam");   //Stellae Renascitur
        snowflakes.put(99006411l, "PanFam");   //NullSechnaya Sholupen
        snowflakes.put(99003557l, "PanFam");   //LowSechnaya Sholupen

        snowflakes.put(99003581l, "Winter");   //Fraternity.
        snowflakes.put(99009310l, "Winter");   //VENI VIDI VICI.
        snowflakes.put(99007498l, "Winter");   //STARCHASER Alliance
        snowflakes.put(99008278l, "Winter");   //Literally Triggered
        snowflakes.put(99005393l, "Winter");   //Blades of Grass
        snowflakes.put(99006343l, "Winter");   //Lord of Worlds Alliance
        snowflakes.put(99009275l, "Winter");   //The Stars of northern moon
        snowflakes.put(99001954l, "Winter");   //Caladrius Alliance
        snowflakes.put(99009764l, "Winter");   //Azure Citizen
        snowflakes.put(99009977l, "Winter");   //Sylvanas Super mercenary

        snowflakes.put(1411711376l, "Fire");   //Legion of xXDEATHXx
        snowflakes.put(741557221l, "Fire");    //Razor Alliance
        snowflakes.put(99008469l, "Fire");     //UNREAL Alliance
        snowflakes.put(99009168l, "Fire");     //Valkyrie Alliance
        snowflakes.put(99001648l, "Fire");     //P-A-T-R-I-O-T-S
        snowflakes.put(99002685l, "Fire");     //Synergy of Steel
        snowflakes.put(99002392l, "Fire");     //NEXT FORCE
    }

    /**
     * Determines if a given corporation ID represents an NPC corporation. NPC corporations are typically controlled by the game system
     * and not by players. This method identifies such corporations by checking if their IDs fall within a specific range.
     *
     * @param corporationId The ID of the corporation to check.
     * @return true if the corporation is an NPC corporation, otherwise false.
     */

    public static boolean isNpcCorporation(long corporationId) {
        return corporationId >= 1000000 && corporationId <= 2000000;
    }

}
