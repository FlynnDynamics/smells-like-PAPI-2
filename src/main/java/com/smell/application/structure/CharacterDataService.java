package com.smell.application.structure;

import com.smell.application.obj.*;
import com.smell.application.user.Alliance;
import com.smell.application.user.Character;
import com.smell.application.user.Corporation;
import com.vaadin.flow.component.notification.Notification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author FlynnDynamics
 * @version ${version}
 * @since 24/04/24
 */

public class CharacterDataService {
    private static final Logger LOGGER = Logger.getLogger(CharacterDataService.class.getName());

    /**
     * Retrieves the full hierarchical data for a character based on the provided character ID.
     * This method processes and combines various pieces of data such as character details,
     * corporation history, and alliance history to construct a comprehensive Character object.
     * It uses an external EveService instance for all data fetch operations, making the method
     * flexible in different contexts where different implementations or instances of EveService
     * might be used.
     *
     * @param characterId The unique identifier for the character whose data is to be fetched.
     *                    This ID is used to retrieve character-specific details and histories.
     * @param eveService  The EveService instance through which all API requests are made.
     *                    This dependency is passed in to facilitate easier testing and
     *                    modification of service behavior.
     * @return A fully populated Character object containing all relevant nested data structures
     *         (corporations and alliances included). The character data includes comprehensive details
     *         about the character's corporations and their respective alliances, with specific
     *         attention to special cases flagged in the 'snowflakes' dataset.
     * @throws IOException If there is a problem in network communication or interaction with
     *         the API endpoints provided by the EveService.
     * @throws InterruptedException If the thread running the operation is interrupted,
     *         typically during asynchronous operations or waiting periods.
     *
     * The method iterates through each corporation and alliance history record, fetching additional
     * details as required and constructing a rich object graph that reflects the character's history
     * in the EVE Online universe. Special care is taken to handle non-player corporations and empty
     * alliance records gracefully, skipping these to focus on significant historical records.
     *
     * Each corporation and alliance is checked against a special 'snowflakes' list to determine if
     * it should be marked as special, which can affect how these entities are displayed or processed
     * in the consuming application.
     */

    public static Character getCharacterData(long characterId, EveService eveService) throws IOException, InterruptedException {
        EVE_Character eveCharacter;
        List<CorporationHistory> corpHistories;
        List<Corporation> corporations = new ArrayList<>();
        CorporationHistory previousCorpHistory = null;

        try {
            eveCharacter = eveService.getCharacterDetails(characterId);
            corpHistories = List.of(eveService.getCorporationHistory(characterId));
        } catch (IOException | InterruptedException e) {
            LOGGER.severe("Failed to fetch character or corporation history data: " + e.getMessage());
            ErrorNotificationManager.addErrorMessage("Failed to fetch character or corporation history data: " + e.getMessage());
            throw e;
        }

        for (CorporationHistory corpHistory : corpHistories) {
            if (EveData.isNpcCorporation(corpHistory.getCorporation_id())) {
                continue;
            }

            String startDate = corpHistory.getStart_date();
            String endDate = previousCorpHistory != null ? previousCorpHistory.getStart_date() : "now";

            EVE_Corporation eveCorporation;
            try {
                eveCorporation = eveService.getCorporationDetails(corpHistory.getCorporation_id());
            } catch (IOException | InterruptedException e) {
                LOGGER.warning("Failed to fetch corporation details: " + e.getMessage());
                ErrorNotificationManager.addErrorMessage("Failed to fetch corporation details: " + e.getMessage());
                continue; // Skip this corporation and proceed with the next
            }

            List<AllianceHistory> allianceHistories;
            try {
                allianceHistories = List.of(eveService.getAllianceHistory(corpHistory.getCorporation_id()));
            } catch (IOException | InterruptedException e) {
                LOGGER.warning("Failed to fetch alliance history: " + e.getMessage());
                ErrorNotificationManager.addErrorMessage("Failed to fetch alliance history: " + e.getMessage());
                allianceHistories = new ArrayList<>(); // Continue with empty list if failed to fetch
            }

            List<Alliance> alliances = new ArrayList<>();
            AllianceHistory previousAllianceHistory = null;
            for (AllianceHistory allianceHistory : allianceHistories) {
                if (allianceHistory.getAlliance_id() == 0) {
                    continue;
                }

                String allianceStartDate = allianceHistory.getStart_date();
                String allianceEndDate = previousAllianceHistory != null ? previousAllianceHistory.getStart_date() : "now";

                EVE_Alliance eveAlliance;
                try {
                    eveAlliance = eveService.getAllianceDetails(allianceHistory.getAlliance_id());
                } catch (IOException | InterruptedException e) {
                    LOGGER.warning("Failed to fetch alliance details: " + e.getMessage());
                    ErrorNotificationManager.addErrorMessage("Failed to fetch alliance details: " + e.getMessage());
                    continue; // Skip this alliance and proceed with the next
                }

                Alliance alliance = new Alliance(allianceHistory.getAlliance_id(), eveAlliance.getName(), allianceStartDate, allianceEndDate);
                if (EveData.getSnowflakes().containsKey(alliance.getId())) {
                    alliance.setSpecial(true);
                }
                alliances.add(alliance);
                previousAllianceHistory = allianceHistory;
            }
            Corporation corporation = new Corporation(corpHistory.getCorporation_id(), eveCorporation.getName(), startDate, endDate, alliances);
            if (EveData.getSnowflakes().containsKey(corporation.getId())) {
                corporation.setSpecial(true);
            }
            corporations.add(corporation);
            previousCorpHistory = corpHistory;
        }

        return new Character(characterId, eveCharacter.getName(), corporations);
    }

}
