package com.example.janit.waesche;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;

/**
 * Created by janit on 25.11.2017.
 */
public class DataFetch {
    public static int value1;
    private static final String OPEN_AIRTABLE_URL =
            "https://api.airtable.com/v0/appS3P0MHWE8rtZFz/Table%201?maxRecords=7&view=Grid%20view";
            //"https://api.airtable.com/v0/appS3P0MHWE8rtZFz/Table%201/rec6MH9ycPnbXinss";

    //"https://api.airtable.com/v0/appS3P0MHWE8rtZFz/Table%201?maxRecords=3&view=Grid%20view";

    public static JSONObject getJSON(Context context, String apikey) {


        try {

            URL url = new URL(OPEN_AIRTABLE_URL);
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            connection.addRequestProperty("authorization", "Bearer " +
                    apikey);
            connection.setUseCaches(false);
            Log.d("Geht", apikey);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();
            JSONObject data = new JSONObject(json.toString());
            Log.d("DatenFetch", data.toString());

            // This value will be 404 if the request was not
            // successful
            Log.d("Response Code", "" + connection.getResponseCode());
            if (200 != connection.getResponseCode()) {
                return null;
            }

            return data;
        } catch (Exception e) {
            Log.d("Daten geht", "nicht");
            Log.d("exeption", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
