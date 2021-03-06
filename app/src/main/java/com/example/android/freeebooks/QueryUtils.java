package com.example.android.freeebooks;

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

class QueryUtils {

    public QueryUtils() {
    }

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    static ArrayList<BookInformation> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        return extractFeatureFromJson(jsonResponse);
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

    /**
     * Return a list of {@link BookInformation} objects that has been built up from
     * parsing the given JSON response.
     */
    private static ArrayList<BookInformation> extractFeatureFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        ArrayList<BookInformation> books = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(bookJSON);
            if (baseJsonResponse.has("items")) {
                JSONArray bookArray = baseJsonResponse.getJSONArray("items");
                for (int i = 0; i < bookArray.length(); i++) {

                    JSONObject currentBook = bookArray.getJSONObject(i);
                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                    JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                    String thumbnail = imageLinks.getString("smallThumbnail").trim();

                    String title = volumeInfo.getString("title");

                    JSONArray authorsArray;
                    ArrayList<String> authors = new ArrayList<>();
                    if (volumeInfo.has("authors")) {
                        authorsArray = volumeInfo.getJSONArray("authors");
                        for (int j = 0; j < authorsArray.length(); j++) {
                            authors.add(authorsArray.getString(j));
                        }
                    } else {
                        authors.add("Unknown Author");
                    }

                    double rating;
                    if (volumeInfo.has("averageRating")) {
                        rating = volumeInfo.getDouble("averageRating");
                    } else {
                        rating = 0;

                    }

                    JSONObject accessInfo = currentBook.getJSONObject("accessInfo");
                    String reader = accessInfo.getString("webReaderLink");

                    BookInformation book = new BookInformation(thumbnail, title, authors,
                            rating, reader);
                    books.add(book);
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }
        return books;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
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
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
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
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
