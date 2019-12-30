package zw.co.paynow.http;

import zw.co.paynow.parsers.UrlParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles sending HTTP requests in the application
 */
public class HttpClient {

    /**
     * Send an empty post request to the given URL
     *
     * @param url The url to send post request to
     * @return The response body from the request
     * @throws IOException Thrown if eception occurs during network communication
     */
    public String postAsync(String url) throws IOException {
        return postAsync(url, null);
    }

    /**
     * Send a post request to the given url with the given data
     *
     * @param url  The url to send post request to
     * @param data The data to send in the post request body
     * @return The response body from the request
     * @throws IOException Thrown if eception occurs during network communication
     */
    public String postAsync(String url, Map<String, String> data) throws IOException {

        // Define the server endpoint to send the HTTP request to
        URL serverUrl = new URL(url);

        HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();

        // Indicate that we want to write to the HTTP request body
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");

        // Writing the post data to the HTTP request body
        BufferedWriter httpRequestBodyWriter =
                new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
        httpRequestBodyWriter.write(data == null ? "" : UrlParser.parseQueryStringFromMap(data));
        httpRequestBodyWriter.close();

        // Reading from the HTTP response body
        StringBuilder sb = new StringBuilder();
        Scanner httpResponseScanner = new Scanner(urlConnection.getInputStream());
        while (httpResponseScanner.hasNextLine()) {
            sb.append(httpResponseScanner.nextLine());
        }

        httpResponseScanner.close();

        return sb.toString();
    }

}