package networking;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import config.Settings;
import gamelogic.Spectre;
import logging.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.UUID;

public class HTTPServer {

    private static HttpServer server;

    private static UUID connectionUUID;

    public static void init() {
        try {
            server = HttpServer.create(new InetSocketAddress(Settings.httpPortNumber), 0);
            server.setExecutor(null);
            server.createContext("/level", new HttpHandlerInstance());
            server.start();
        } catch (Exception e) {
            Logger.log("There was a fatal error initialising the http server for s2c level serving. The error:");
            Logger.log(e.toString());
            System.exit(-1);
        }
    }

    public static void shutdown() {
        try {
            server.stop(0);
        } catch (Exception ignored) {}
    }

    static class HttpHandlerInstance implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            byte[] response = Spectre.level;
            t.sendResponseHeaders(200, response.length);
            OutputStream os = t.getResponseBody();
            os.write(response);
            os.close();
        }
    }

}
