package com.smell.application.structure;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.smell.application.obj.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

/**
 * Provides access to the EVE Online API to retrieve data related to characters, corporations, and alliances.
 * This service class handles HTTP requests and JSON parsing to return Java objects representing the data.
 *
 * Each method in this class is designed to fetch specific types of data by making HTTP GET requests to the EVE Online API,
 * and parsing the resulting JSON into predefined data structures.
 *
 * @author FlynnDynamics
 * @version ${version}
 * @since 24/04/24
 */
public class EveService {
    private static final Logger LOGGER = Logger.getLogger(EveService.class.getName());

    private final HttpClient client;
    private final Gson gson;

    public EveService() {
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    /**
     * Fetches detailed information about a specific character from the EVE Online API.
     * The method constructs an API request, sends it, and processes the JSON response into an EVE_Character object.
     *
     * @param characterId The unique identifier for the character
     * @return EVE_Character object containing detailed information about the character
     * @throws IOException If an error occurs during the HTTP request or processing
     * @throws InterruptedException If the operation is interrupted during execution
     */
    public EVE_Character getCharacterDetails(long characterId) throws IOException, InterruptedException {
        String url = String.format("https://esi.evetech.net/latest/characters/%d/", characterId);
        return sendRequest(url, EVE_Character.class);
    }

    /**
     * Fetches detailed information about a specific corporation from the EVE Online API.
     * Similar to character details, this method returns an EVE_Corporation object representing the corporation.
     *
     * @param corporationId The unique identifier for the corporation
     * @return EVE_Corporation object containing detailed information about the corporation
     * @throws IOException If an error occurs during the HTTP request or processing
     * @throws InterruptedException If the operation is interrupted during execution
     */
    public EVE_Corporation getCorporationDetails(long corporationId) throws IOException, InterruptedException {
        String url = String.format("https://esi.evetech.net/latest/corporations/%d/", corporationId);
        return sendRequest(url, EVE_Corporation.class);
    }

    /**
     * Fetches detailed information about a specific alliance from the EVE Online API.
     * The data fetched includes the name, identifier, and other relevant details wrapped in an EVE_Alliance object.
     *
     * @param allianceId The unique identifier for the alliance
     * @return EVE_Alliance object containing detailed information about the alliance
     * @throws IOException If an error occurs during the HTTP request or processing
     * @throws InterruptedException If the operation is interrupted during execution
     */
    public EVE_Alliance getAllianceDetails(long allianceId) throws IOException, InterruptedException {
        String url = String.format("https://esi.evetech.net/latest/alliances/%d/", allianceId);
        return sendRequest(url, EVE_Alliance.class);
    }

    /**
     * Retrieves a history of all corporations that a specific character has been part of.
     * The method returns an array of CorporationHistory objects, each representing a period the character was associated with a corporation.
     *
     * @param characterId The unique identifier for the character
     * @return An array of CorporationHistory objects detailing historical corporation affiliations
     * @throws IOException If an error occurs during the HTTP request or processing
     * @throws InterruptedException If the operation is interrupted during execution
     */
    public CorporationHistory[] getCorporationHistory(long characterId) throws IOException, InterruptedException {
        String url = String.format("https://esi.evetech.net/latest/characters/%d/corporationhistory/", characterId);
        return sendRequest(url, CorporationHistory[].class);
    }

    /**
     * Retrieves a history of all alliances that a specific corporation has been part of.
     * The method returns an array of AllianceHistory objects, each representing a period the corporation was associated with an alliance.
     *
     * @param corporationId The unique identifier for the corporation
     * @return An array of AllianceHistory objects detailing historical alliance affiliations
     * @throws IOException If an error occurs during the HTTP request or processing
     * @throws InterruptedException If the operation is interrupted during execution
     */
    public AllianceHistory[] getAllianceHistory(long corporationId) throws IOException, InterruptedException {
        String url = String.format("https://esi.evetech.net/latest/corporations/%d/alliancehistory/", corporationId);
        return sendRequest(url, AllianceHistory[].class);
    }

    /**
     * Sends an HTTP GET request to the specified URL and converts the JSON response into a Java object of the specified type.
     * This generic method is used internally to handle all GET requests within the class.
     *
     * @param url The URL to send the request to
     * @param responseType The class type into which the JSON response should be converted
     * @return An object of type responseType, containing the parsed data
     * @throws IOException If an error occurs during the HTTP request or in processing the response
     * @throws InterruptedException If the operation is interrupted
     */
    private <T> T sendRequest(String url, Class<T> responseType) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                LOGGER.severe("HTTP request to " + url + " failed with status code: " + response.statusCode());
                throw new IOException("Unexpected HTTP response: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.severe("HTTP request failed for URL: " + url + ": " + e.getMessage());
            throw e;
        }

        try {
            T result = gson.fromJson(response.body(), responseType);
            if (result == null) {
                throw new IOException("Failed to parse JSON, resulting object is null");
            }
            return result;
        } catch (JsonParseException e) {
            LOGGER.severe("Failed to parse JSON response for URL: " + url + ": " + e.getMessage());
            throw new IOException("JSON parsing error", e);
        }
    }
}
