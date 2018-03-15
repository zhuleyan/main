package seedu.address.commons.core;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import seedu.address.ui.BrowserWindow;


/**
 * Class that handles the OAuth2 call with LiknedIn
 * Acts as the client in the client-server scheme
 */
public class Oauth2Client {

    /**
     * Called when user types Linkedin_login
     * starts a webserver and opens a browser for Linkedin Authorization
     */
    public static void authenticateWithLinkedIn() throws IOException {
        try {
            startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Config config = new Config();
        String clientId = config.getAppId();

        String urlString = "https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id="
            + clientId + "&redirect_uri=http://127.0.0.1:13370&state=123";

        BrowserWindow bWindow = new BrowserWindow(urlString);
        bWindow.show();


    }

    /**
     * Starts a webserver
     */
    public static void startServer() throws IOException {
        int port = 13370;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null);
        server.start();
    }

    /**
     * Allows the server to host a webpage at the directory specified in createContext
     */
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "This is the response";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

}
