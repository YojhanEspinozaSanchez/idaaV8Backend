package com.idaav8.app.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.idaav8.app.dto.ProfileResponseDTO;

@Service
public class ProfileService {

    @Value("${PROFILE_API_KEY}")
    private String API_KEY;

    public ProfileResponseDTO getProfile(String id) throws Exception {

        HttpURLConnection connection = null;

        try {

            URI uri = URI.create(
                    "https://w3-unified-profile-api.ibm.com/v3/profiles/"
                            + id
                            + "/profile");

            URL url = uri.toURL();

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-UP-API-KEY", API_KEY);
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                StringBuilder response = new StringBuilder();

                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()))) {

                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                }

                JSONObject jsonResponse =
                        new JSONObject(response.toString());

                JSONObject content =
                        jsonResponse.getJSONObject("content");

                JSONObject nameObject =
                        content.getJSONObject("name");

                JSONObject areaObject =
                        content.getJSONObject("org");

                String name = nameObject.getString("first");
                String lastName = nameObject.getString("last");

                String email = content.getString("preferredIdentity");

                String area = areaObject.optString("supOrgLevel6", "");

                String userName = content.getString("notesShortName");


                return new ProfileResponseDTO(
                        name,
                        lastName,
                        email,
                        area,
                        userName
                );
            }

            StringBuilder errorResponse = new StringBuilder();

            if (connection.getErrorStream() != null) {
                try (BufferedReader errorReader = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream()))) {

                    String line;

                    while ((line = errorReader.readLine()) != null) {
                        errorResponse.append(line);
                    }
                }
            }

            throw new RuntimeException(
                    "Error HTTP "
                            + responseCode
                            + ". Response: "
                            + errorResponse);

        } catch (Exception e) {

            System.err.println("Failed employeeId=" + id);

            e.printStackTrace();

            throw e;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}