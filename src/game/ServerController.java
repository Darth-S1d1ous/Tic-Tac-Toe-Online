package game;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * The ServerController class is responsible for initializing and starting the game server.
 * 
 * It sets up a ServerSocket to listen on a specific port and creates a GameServer instance
 * to handle client connections and game logic. It also adds a shutdown hook to log when
 * the server is stopped.
 */
public class ServerController {
    private static final Logger logger = Logger.getLogger(ServerController.class.getName());    

    /**
     * Initializes and starts the game server.
     * 
     * This method creates a new ServerSocket listening on port 58901 and initializes
     * a GameServer instance with it. The server is then started. If any exception
     * occurs during this process, it is logged and printed to the stack trace.
     * 
     * @throws IOException if an I/O error occurs when opening the socket.
     */
    public static void main(String[] args) throws IOException {
        logger.info("Server initializing");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                logger.info("Server stopped");
            }
        }));

        try (var listener = new ServerSocket(58901)) {
            GameServer server = new GameServer(listener);
            logger.info("Server initialized");
            server.start();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            e.printStackTrace();
        }
    }
}