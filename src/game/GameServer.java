package game;
import java.util.*;
import java.util.concurrent.Executors;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.*;

/**
 * The GameServer class represents a server for a two-player game.
 * It handles client connections, game state, and communication between clients.
 */
public class GameServer {
    private static final Logger logger = Logger.getLogger(GameServer.class.getName());

    private ServerSocket serverSocket;
    private Set<PrintWriter> writers = new HashSet<>();
    private Map<Integer, Handler> player2Handlers = new HashMap<>();
    
    private int[][] board = new int[3][3];
    private boolean isPlayer1Turn = true;
    private int clientCount = 0;

    /**
     * Constructs a GameServer with the specified ServerSocket.
     * 
     * @param serverSocket the ServerSocket to accept client connections
     */
    public GameServer(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * Starts the game server to accept client connections and handle game logic.
     */
    public void start() {
        var pool = Executors.newFixedThreadPool(100);
        int currentPlayer = 1;
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                if(clientCount < 2) {
                    if(player2Handlers.isEmpty() || player2Handlers.get(1) == null) {
                        currentPlayer = 1;
                    } else {
                        currentPlayer = 2;
                    }
                    Handler handler = new Handler(socket, currentPlayer);
                    pool.execute(handler);
                    player2Handlers.put(currentPlayer, handler);
                    clientCount++;
                    logger.info("Connected to client " + currentPlayer);
                } else {
                    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
                    output.println("BUSY");
                    socket.close();
                }
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error in server socket", e);
                e.printStackTrace();
            }
        }
    }

    /**
     * The Handler class handles communication with a single client.
     */
    public class Handler implements Runnable {
        private Socket socket;
        private Scanner input;
        private PrintWriter output;
        private int currentPlayer = 1;
        
        /**
         * Constructs a Handler with the specified socket and player number.
         * 
         * @param socket the socket for communication with the client
         * @param currentPlayer the player number (1 or 2)
         */
        public Handler(Socket socket, int currentPlayer) {
            this.socket = socket;
            this.currentPlayer = currentPlayer;
        }

        /**
         * Runs the handler to process client commands and update game state.
         */
        @Override
        public void run() {
            logger.info("Connected: " + socket);
            try {
                input = new Scanner(socket.getInputStream());
                output = new PrintWriter(socket.getOutputStream(), true);
                output.println("ID " + currentPlayer);

                synchronized(writers) {
                    writers.add(output);
                }
                while(input.hasNextLine()) {
                    var command = input.nextLine();
                    if(isPlayer1Turn && currentPlayer == 1 || !isPlayer1Turn && currentPlayer == 2) {
                        if(command.startsWith("MOVE")) {
                            int row = Integer.parseInt(command.split(" ")[1]);
                            int col = Integer.parseInt(command.split(" ")[2]);
                            logger.info("Received player " + currentPlayer + " move: " + row + " " + col);

                            if(board[row][col] == 0) {
                                board[row][col] = currentPlayer;
                                for(PrintWriter writer : writers) {
                                    writer.println("MOVE " + currentPlayer + " " + row + " " + col);
                                }
                                if(checkWin()) {
                                    for(PrintWriter writer : writers) {
                                        writer.println("WIN " + currentPlayer);
                                    }
                                    reset();
                                } else if (isBoardFull()) {
                                    for(PrintWriter writer : writers) {
                                        writer.println("DRAW");
                                    }
                                    reset();
                                } else {
                                    isPlayer1Turn = !isPlayer1Turn;
                                }
                            }
                        } else if (command.startsWith("QUIT")) {
                            for(PrintWriter writer : writers) {
                                writer.println("QUIT " + currentPlayer);
                            }
                            reset();
                            return;
                        }
                    } else {
                        if(command.startsWith("MOVE")) {
                            logger.warning("Player " + currentPlayer + " is not allowed to move now.");
                            output.println("WAIT");
                        } else if (command.startsWith("QUIT")) {
                            for(PrintWriter writer : writers) {
                                writer.println("QUIT " + currentPlayer);
                            }
                            reset();
                            return;
                        }
                    }
                }           
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error handling client", e);
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                    logger.info("Connection closed: " + socket);
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error closing socket", e);
                }
                if(output != null) {
                    synchronized(writers) {
                        writers.remove(output);
                    }
                }
                player2Handlers.remove(currentPlayer);
                clientCount--;
                reset();
            }
        }

        /**
         * Checks if the current player has won the game.
         * 
         * @return true if the current player has won, false otherwise
         */
        private boolean checkWin() {
            for (int i = 0; i < 3; i++) {
                if (board[i][0] != 0 && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                    return true;
                }
                if (board[0][i] != 0 && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                    return true;
                }
            }
            if (board[0][0] != 0 && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
                return true;
            }
            if (board[0][2] != 0 && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
                return true;
            }
            return false;
        }

        /**
         * Resets the game state for a new game.
         */
        private void reset() {
            resetBoard();
            isPlayer1Turn = true;
        }

        /**
         * Checks if the game board is full.
         * 
         * @return true if the board is full, false otherwise
         */
        private boolean isBoardFull() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        return false;
                    }
                }
            }
            return true;
        }

        /**
         * Resets the game board to its initial state.
         */
        private void resetBoard() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    board[i][j] = 0;
                }
            }
        }
    }
}