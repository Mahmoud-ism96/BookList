package com.example.android.booklist;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static ArrayList<Book> extractFeatureFromJson(String bookJson) {

        if (TextUtils.isEmpty(bookJson)) {
            return null;
        }

        ArrayList<Book> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject baseJsonResponse = new JSONObject(bookJson);
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            for(int i=0;i<bookArray.length();i++){
                JSONObject currentBook = bookArray.getJSONObject(i);

                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");

                String  authorsNames="By ";
                if(volumeInfo.has("authors")) {
                    JSONArray authors=volumeInfo.getJSONArray("authors");
                    int authorLength = authors.length();
                    if(authorLength >=2)
                    for (int loop = 0; loop <= 1; loop++) {
                        authorsNames += authors.getString(loop)+",";
                    }else
                        authorsNames+= authors.getString(0);
                }else
                    authorsNames="Author Unknown";

                String thumbnail="nopic";
                if(volumeInfo.has("imageLinks")) {
                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    thumbnail = imageLinks.getString("thumbnail");
                }
                String previewLink =volumeInfo.getString("previewLink");

               Book book = new Book (title,authorsNames,thumbnail,previewLink);


                books.add(book);
            }


        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the Book JSON results", e);
        }

        return books;
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the Book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    public static List<Book> fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Book> book = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return book;
    }















}