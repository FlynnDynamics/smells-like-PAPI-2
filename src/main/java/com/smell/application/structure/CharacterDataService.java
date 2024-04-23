package com.smell.application.structure;

import com.smell.application.obj.*;
import com.smell.application.user.Alliance;
import com.smell.application.user.Character;
import com.smell.application.user.Corporation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CharacterDataService {
    private final EveService eveService;

    public CharacterDataService(EveService eveService) {
        this.eveService = eveService;
    }

    /**
     * Retrieves the complete data structure starting from a character ID.
     *
     * @param characterId The character ID to fetch and process data for
     * @return Character object filled with all relevant data
     * @throws IOException          If an error occurs during API communication
     * @throws InterruptedException If the operation is interrupted
     */
    public Character getCharacterData(long characterId) throws IOException, InterruptedException {
        EVE_Character eveCharacter = eveService.getCharacterDetails(characterId);
        List<CorporationHistory> corpHistories = List.of(eveService.getCorporationHistory(characterId));
        List<Corporation> corporations = new ArrayList<>();

        CorporationHistory previousCorpHistory = null;
        for (CorporationHistory corpHistory : corpHistories) {
            if (isNpcCorporation(corpHistory.getCorporation_id())) {
                continue; // Skip NPC corporations
            }

            String startDate = corpHistory.getStart_date();
            String endDate = previousCorpHistory != null ? previousCorpHistory.getStart_date() : "now"; // Use the start date of the next corporation as the end date

            EVE_Corporation eveCorporation = eveService.getCorporationDetails(corpHistory.getCorporation_id()); // Fetch corporation details to get the name
            List<AllianceHistory> allianceHistories = List.of(eveService.getAllianceHistory(corpHistory.getCorporation_id()));
            List<Alliance> alliances = new ArrayList<>();
            AllianceHistory previousAllianceHistory = null;
            for (AllianceHistory allianceHistory : allianceHistories) {
                String allianceStartDate = allianceHistory.getStart_date();
                String allianceEndDate = previousAllianceHistory != null ? previousAllianceHistory.getStart_date() : "now"; // Use the start date of the next alliance as the end date

                EVE_Alliance eveAlliance = eveService.getAllianceDetails(allianceHistory.getAlliance_id());
                alliances.add(new Alliance(allianceHistory.getAlliance_id(), eveAlliance.getName(), allianceStartDate, allianceEndDate));
                previousAllianceHistory = allianceHistory; // Update previous history for the next iteration
            }
            corporations.add(new Corporation(corpHistory.getCorporation_id(), eveCorporation.getName(), startDate, endDate, alliances));
            previousCorpHistory = corpHistory; // Update previous history for the next iteration
        }

        for (Corporation corp : corporations) {
            corp.setSpecial(specialIds.contains(corp.getId()));

            for (Alliance alliance : corp.getAlliances()) {
                alliance.setSpecial(specialIds.contains(alliance.getId()));
            }
        }

        return new Character(characterId, eveCharacter.getName(), corporations);
    }

    /**
     * Determines if a given corporation is an NPC corporation based on its ID.
     * NPC corporations are identified by their ID being between 1000000 and 2000000.
     *
     * @param corporationId The corporation ID to check
     * @return true if the corporation is an NPC corporation, otherwise false
     */
    private boolean isNpcCorporation(long corporationId) {
        return corporationId >= 1000000 && corporationId <= 2000000;
    }

}
