package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.*;

/**
 * The Client class represents a client that connects to the game server.
 * It handles the connection, communication, and user interactions.
 */
public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    private MainWindow mainWindow;
    
    private Socket socket;
    private Scanner in;
    private PrintWriter out;

    private int id = 0;

    /**
     * Constructs a Client with the specified MainWindow.
     * 
     * @param mainWindow the main window of the client application
     */
    public Client(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * Starts the client and connects to the server.
     * Sets up the input and output streams, and initializes the event listeners.
     */
    public void start() {
        try {
            this.socket = new Socket("localhost", 58901);
            this.in = new Scanner(socket.getInputStream());
            this.out = new PrintWriter(socket.getOutputStream(), true);
            logger.info("Connected to server " + socket.getInetAddress());
        } catch (UnknownHostException e) {
            logger.log(Level.SEVERE, "Error in client socket", e);
            e.printStackTrace();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error in client socket", e);
            e.printStackTrace();
        }

        MiddlePanel middlePanel = mainWindow.getMiddlePanel();
        middlePanel.getBoardPanel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mainWindow.getName() == null) {
                    logger.warning("Player name is not set.");
                    return;
                }
                if (id == 0) {
                    logger.warning("Client is not registered at server.");
                    return;
                }
                int row = e.getY() / middlePanel.getCellSize();
                int col = e.getX() / middlePanel.getCellSize();
                if (row < 0 || row >= 3 || col < 0 || col >= 3) {
                    middlePanel.setInfoLabel("Click within the board boundaries.");
                    logger.warning("Invalid move: " + row + " " + col);
                } else {
                    out.println("MOVE " + row + " " + col);
                    logger.info("Player " + id + " asking move: " + row + " " + col);
                }
            }
        });

        mainWindow.getMenuPanel().getExitButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("QUIT");
            }
        });

        Thread handler = new ClientHandler(socket);
        handler.start();
    }

    /**
     * The ClientHandler class handles communication with the server.
     */
    class ClientHandler extends Thread {
        private Socket socket;

        /**
         * Constructs a ClientHandler with the specified socket.
         * 
         * @param socket the socket for communication with the server
         */
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Runs the handler to process server commands and update the client state.
         */
        @Override
        public void run() {
            try {
                readFromServer();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in client socket", e);
                e.printStackTrace();
            }
        }

        /**
         * Reads commands from the server and processes them.
         * 
         * @throws Exception if an error occurs while reading from the server
         */
        public void readFromServer() throws Exception {
            try {
                while(in.hasNextLine()) {
                    var command = in.nextLine();
                    out.flush();
                    if(command.startsWith("ID")) {
                        id = Integer.parseInt(command.split(" ")[1]);
                        logger.info("Client ID: " + id);
                    } else if(command.startsWith("MOVE")) {
                        int currentPlayer = Integer.parseInt(command.split(" ")[1]);
                        int row = Integer.parseInt(command.split(" ")[2]);
                        int col = Integer.parseInt(command.split(" ")[3]);
                        if (id == currentPlayer) {
                            mainWindow.getMiddlePanel().setInfoLabel("Valid move, wait for your opponent.");
                        } else {
                            mainWindow.getMiddlePanel().setInfoLabel("Your opponent has moved, now is your turn.");
                        }
                        mainWindow.getMiddlePanel().updateBoard(row, col, currentPlayer);
                    } else if(command.startsWith("WIN")) {
                        int winner = Integer.parseInt(command.split(" ")[1]);
                        int response = mainWindow.showEndGameDialog("WIN", id == winner);
                        postProcess(response, winner);
                    } else if(command.startsWith("DRAW")) {
                        int response = mainWindow.showEndGameDialog("DRAW");
                        postProcess(response, 0);
                    } else if(command.startsWith("QUIT")) {
                        mainWindow.showEndGameDialog("QUIT");
                        postProcess(JOptionPane.NO_OPTION, 0);
                    } else if(command.startsWith("WAIT")) {
                        mainWindow.getMiddlePanel().setInfoLabel("Wait for your turn.");
                    }
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error in client socket", e);
                e.printStackTrace();
            } finally {
                socket.close();
            }
        }
    }

    /**
     * Processes the response after the game ends.
     * 
     * @param response the response from the end game dialog
     * @param winner the winner of the game
     */
    private void postProcess(int response, int winner) {
        switch(response) {
            case JOptionPane.YES_OPTION:
                out.println("RESTART");
                mainWindow.getMiddlePanel().resetBoard();
                mainWindow.getMiddlePanel().updateStats(winner);
                if(id == 2) {
                    mainWindow.getMiddlePanel().setInfoLabel("Game started. Wait for your opponent.");
                }
                break;
            case JOptionPane.NO_OPTION:
                out.println("QUIT");
                System.exit(0);
                break;
            case JOptionPane.CLOSED_OPTION:
                out.println("QUIT");
                System.exit(0);
                break;
        }
    }
}