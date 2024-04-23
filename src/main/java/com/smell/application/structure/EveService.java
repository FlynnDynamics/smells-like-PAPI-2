package com.smell.application.structure;

import com.google.gson.Gson;
import com.smell.application.obj.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class EveService {

    private final HttpClient client;
    private final Gson gson;

    public EveService() {
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    /**
     * Retrieves character details from the EVE Online API and converts it into an EVE_Character object.
     * @param characterId The ID of the character
     * @return EVE_Character object containing the character's details
     * @throws IOException If an I/O error occurs
     * @throws InterruptedException If the thread is interrupted
     */
    public EVE_Character getCharacterDetails(long characterId) throws IOException, InterruptedException {
        String url = String.format("https://esi.evetech.net/latest/characters/%d/", characterId);
        return sendRequest(url, EVE_Character.class);
    }

    /**
     * Retrieves corporation details from the EVE Online API and converts it into an EVE_Corporation object.
     * @param corporationId The ID of the corporation
     * @return EVE_Corporation object containing the corporation's details
     * @throws IOException If an I/O error occurs
     * @throws InterruptedException If the thread is interrupted
     */
    public EVE_Corporation getCorporationDetails(long corporationId) throws IOException, InterruptedException {
        String url = String.format("https://esi.evetech.net/latest/corporations/%d/", corporationId);
        return sendRequest(url, EVE_Corporation.class);
    }

    /**
     * Retrieves alliance details from the EVE Online API and converts it into an EVE_Alliance object.
     * @param allianceId The ID of the alliance
     * @return EVE_Alliance object containing the alliance information
     * @throws IOException If an I/O error occurs
     * @throws InterruptedException If the thread is interrupted
     */
    public EVE_Alliance getAllianceDetails(long allianceId) throws IOException, InterruptedException {
        String url = String.format("https://esi.evetech.net/latest/alliances/%d/", allianceId);
        return sendRequest(url, EVE_Alliance.class);
    }

    /**
     * Retrieves character's corporation history from the EVE Online API and converts it into an array of CorporationHistory objects.
     * @param characterId The ID of the character
     * @return An array of CorporationHistory objects detailing the corporations the character has been part of
     * @throws IOException If an I/O error occurs
     * @throws InterruptedException If the thread is interrupted
     */
    public CorporationHistory[] getCorporationHistory(long characterId) throws IOException, InterruptedException {
        String url = String.format("https://esi.evetech.net/latest/characters/%d/corporationhistory/", characterId);
        return sendRequest(url, CorporationHistory[].class);
    }

    /**
     * Retrieves alliance history from the EVE Online API and converts it into an array of AllianceHistory objects.
     * @param corporationId The ID of the corporation
     * @return An array of AllianceHistory objects detailing the alliances the corporation has been part of
     * @throws IOException If an I/O error occurs
     * @throws InterruptedException If the thread is interrupted
     */
    public AllianceHistory[] getAllianceHistory(long corporationId) throws IOException, InterruptedException {
        String url = String.format("https://esi.evetech.net/latest/corporations/%d/alliancehistory/", corporationId);
        return sendRequest(url, AllianceHistory[].class);
    }

    /**
     * Sends an HTTP GET request to the specified URL and converts the JSON response into a Java object.
     * @param url The URL to send the request to
     * @param responseType The class of the Java type the response should be converted into
     * @return An object of type responseType
     * @throws IOException If an I/O error occurs
     * @throws InterruptedException If the thread is interrupted
     */
    private <T> T sendRequest(String url, Class<T> responseType) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return gson.fromJson(response.body(), responseType);
    }
}
