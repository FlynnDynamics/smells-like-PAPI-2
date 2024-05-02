package com.smell.application.structure;

import com.smell.application.obj.*;
import com.smell.application.user.Alliance;
import com.smell.application.user.Character;
import com.smell.application.user.Corporation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author FlynnDynamics
 * @version 0.x
 * @since 24/04/24
 */

public class CharacterDataService {
    /**
     * Retrieves the full hierarchical data for a character based on the provided character ID. This method processes and combines various pieces of data such as character details, corporation history, and alliance history to construct a comprehensive Character object. It uses an external EveService instance for all data fetch operations, making the method flexible in different contexts where different implementations or instances of EveService might be used.
     *
     * @param characterId The unique identifier for the character whose data is to be fetched. This ID is used to retrieve character-specific details and histories.
     * @param eveService  The EveService instance through which all API requests are made. This dependency is passed in to facilitate easier testing and modification of service behavior.
     * @return A fully populated Character object containing all relevant nested data structures (corporations and alliances included).
     * @throws IOException          If there is a problem in network communication or interaction with the API endpoints provided by the EveService.
     * @throws InterruptedException If the thread running the operation is interrupted, typically during asynchronous operations or waiting periods.
     */

    public static Character getCharacterData(long characterId, EveService eveService) throws IOException, InterruptedException {
        EVE_Character eveCharacter = eveService.getCharacterDetails(characterId);
        List<CorporationHistory> corpHistories = List.of(eveService.getCorporationHistory(characterId));
        List<Corporation> corporations = new ArrayList<>();

        CorporationHistory previousCorpHistory = null;
        for (CorporationHistory corpHistory : corpHistories) {
            if (EveData.isNpcCorporation(corpHistory.getCorporation_id())) {
                continue; // Skip NPC corporations
            }

            String startDate = corpHistory.getStart_date();
            String endDate = previousCorpHistory != null ? previousCorpHistory.getStart_date() : "now"; // Use the start date of the next corporation as the end date

            EVE_Corporation eveCorporation = eveService.getCorporationDetails(corpHistory.getCorporation_id()); // Fetch corporation details to get the name

            List<AllianceHistory> allianceHistories = List.of(eveService.getAllianceHistory(corpHistory.getCorporation_id()));

            List<Alliance> alliances = new ArrayList<>();
            AllianceHistory previousAllianceHistory = null;
            for (AllianceHistory allianceHistory : allianceHistories) {
                if (allianceHistory.getAlliance_id() == 0)
                    continue;

                String allianceStartDate = allianceHistory.getStart_date();
                String allianceEndDate = previousAllianceHistory != null ? previousAllianceHistory.getStart_date() : "now"; // Use the start date of the next alliance as the end date

                EVE_Alliance eveAlliance = eveService.getAllianceDetails(allianceHistory.getAlliance_id());
                alliances.add(new Alliance(allianceHistory.getAlliance_id(), eveAlliance.getName(), allianceStartDate, allianceEndDate));
                if (EveData.getSnowflakes().containsKey(alliances.getLast().getId()))
                    alliances.getLast().setSpecial(true);
                previousAllianceHistory = allianceHistory; // Update previous history for the next iteration
            }
            corporations.add(new Corporation(corpHistory.getCorporation_id(), eveCorporation.getName(), startDate, endDate, alliances));
            if (EveData.getSnowflakes().containsKey(corporations.getLast().getId()))
                corporations.getLast().setSpecial(true);
            previousCorpHistory = corpHistory; // Update previous history for the next iteration
        }

        return new Character(characterId, eveCharacter.getName(), corporations);
    }

}
