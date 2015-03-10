package by.evgen.android.apiclient.twitter;

import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by evgen on 05.03.2015.
 */
public class TwitterClient {

    private static String encodeKeys(String consumerKey, String consumerSecret) {
        try {
            String encodedConsumerKey = URLEncoder.encode(consumerKey, "UTF-8");
            String encodedConsumerSecret = URLEncoder.encode(consumerSecret, "UTF-8");
            String fullKey = encodedConsumerKey + ":" + encodedConsumerSecret;
            byte[] encodedBytes = Base64.encode(fullKey.getBytes(), Base64.DEFAULT);
            return new String(encodedBytes);
        } catch (UnsupportedEncodingException e) {
            return new String();
        }
    }

    private static String requestBearerToken(String endPointUrl) throws IOException {
        HttpsURLConnection connection = null;
        String encodedCredentials = encodeKeys("<consumerkey>", "<consumersecret>");
        try {
            URL url = new URL(endPointUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", "api.twitter.com");
            connection.setRequestProperty("User-Agent", "Your Program Name");
            connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            connection.setRequestProperty("Content-Length", "29");
            connection.setUseCaches(false);
            writeRequest(connection, "grant_type=client_credentials");
            // Parse the JSON response into a JSON mapped object to fetch fields from.
            //          JSONObject obj = (JSONObject)JSONValue.parse(readResponse(connection));
//            if (obj != null) {
//                String tokenType = (String)obj.get("token_type");
//               String token = (String)obj.get("access_token");
//                 return ((tokenType.equals("bearer")) && (token != null)) ? token : "";
            //           }
            return new String();
        } catch (MalformedURLException e) {
            throw new IOException("Invalid endpoint URL specified.", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static String fetchTimelineTweet(String endPointUrl) throws IOException {
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(endPointUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Host", "api.twitter.com");
            connection.setRequestProperty("User-Agent", "Your Program Name");
//            connection.setRequestProperty("Authorization", "Bearer " + bearerToken);
//            connection.setUseCaches(false);
//            // Parse the JSON response into a JSON mapped object to fetch fields from.
//            JSONArray obj = (JSONArray) JSONValue.parse(readResponse(connection));
//            if (obj != null) {
//                String tweet = ((JSONObject)obj.get(0)).get("text").toString();
//                return (tweet != null) ? tweet : "";
//            }
            return new String();
        } catch (MalformedURLException e) {
            throw new IOException("Invalid endpoint URL specified.", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private static boolean writeRequest(HttpsURLConnection connection, String textBody) {
        try {
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            wr.write(textBody);
            wr.flush();
            wr.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Reads a response for a given connection and returns it as a string.
    private static String readResponse(HttpsURLConnection connection) {
        try {
            StringBuilder str = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while ((line = br.readLine()) != null) {
                str.append(line + System.getProperty("line.separator"));
            }
            return str.toString();
        } catch (IOException e) {
            return new String();
        }
    }


}
