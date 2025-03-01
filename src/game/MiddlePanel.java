package game;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * The MiddlePanel class represents the central section of the Tic Tac Toe game application.
 * It manages the game board, handles user interactions, updates game statistics, and controls
 * the game flow between the player and the computer opponent.
 */
public class MiddlePanel extends JPanel {
    /**
     * The label displaying informational messages to the user.
     */
    private JLabel infoLabel;

    /**
     * The 2D array representing the game board.
     * <p>
     * Each cell can have the following values:
     * <ul>
     *   <li><b>0</b>: Empty cell.</li>
     *   <li><b>1</b>: Player's move.</li>
     *   <li><b>2</b>: Computer's move.</li>
     * </ul>
     */
    private int[][] board;

    /**
     * The size of each cell in the game board.
     */
    private int cellSize;

    /**
     * The panel where the game board is drawn and interacted with.
     */
    private JPanel boardPanel;

    /**
     * The panel displaying game statistics such as wins and draws.
     */
    private JPanel statPanel;

    /**
     * The label displaying the number of times the player has won.
     */
    private JLabel player1WinsLabel;

    private int player1Wins = 0;

    /**
     * The label displaying the number of times the computer has won.
     */
    private JLabel player2WinsLabel;

    private int player2Wins = 0;

    /**
     * The label displaying the number of draws.
     */
    private JLabel drawsLabel;

    private int draws = 0;

    /**
     * Constructs a new MiddlePanel instance and initializes its components.
     */
    public MiddlePanel() {
        setLayout(new BorderLayout());

        this.board = new int[3][3];
        this.infoLabel = new JLabel("Enter your name...", SwingConstants.CENTER);
        add(infoLabel, BorderLayout.NORTH);

        initStatPanel();
        initBoardPanel();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardPanel, statPanel);
        splitPane.setResizeWeight(0.6);
        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Initializes the panel that displays game statistics.
     * <p>
     * This includes player wins, computer wins, and the number of draws.
     */
    private void initStatPanel() {
        this.statPanel = new JPanel();
        statPanel.setLayout(new GridLayout(4, 1));

        TitledBorder border = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Score");
        border.setTitleJustification(TitledBorder.LEFT);
        statPanel.setBorder(border);

        player1WinsLabel = new JLabel("Player 1 wins: 0", SwingConstants.LEFT);
        player2WinsLabel = new JLabel("Player 2 wins: 0", SwingConstants.LEFT);
        drawsLabel = new JLabel("Draws: 0", SwingConstants.LEFT);

        statPanel.add(player1WinsLabel);
        statPanel.add(player2WinsLabel);
        statPanel.add(drawsLabel);
    }

    /**
     * Initializes the panel where the game board is drawn and interacted with.
     * <p>
     * This panel handles mouse clicks to register player moves and manages the
     * computer's moves using a SwingWorker to perform actions in the background.
     */
    private void initBoardPanel() {
        this.boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                cellSize = getWidth() / 3;

                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        int x = col * cellSize;
                        int y = row * cellSize;
                        g2d.setColor(Color.WHITE);
                        g2d.fillRect(x, y, cellSize, cellSize);
                        g2d.setColor(Color.BLACK);
                        g2d.drawRect(x, y, cellSize, cellSize);

                        if (board[row][col] == 1) {
                            g2d.setColor(Color.GREEN);
                            g2d.drawLine(x, y, x + cellSize, y + cellSize);
                            g2d.drawLine(x + cellSize, y, x, y + cellSize);
                        } else if (board[row][col] == 2) {
                            g2d.setColor(Color.RED);
                            g2d.drawOval(x, y, cellSize, cellSize);
                        }
                    }
                }
            }
        };

        boardPanel.setPreferredSize(new Dimension(200, 200));
    }

    /**
     * Resets the game board to its initial empty state.
     * <p>
     * Clears all marks on the board, resets the count of valid cells, and repaints the board.
     */
    public void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }
        boardPanel.repaint();
        infoLabel.setText("Game reset. Enter your move.");
    }

    /**
     * Sets the informational label text.
     *
     * @param text The message to display in the informational label.
     */
    public void setInfoLabel(String text) {
        infoLabel.setText(text);
    }

    /**
     * Returns the size of a cell in the board.
     *
     * @return the size of a cell
     */
    public int getCellSize() {
        return cellSize;
    }

    /**
     * Returns the JPanel representing the board.
     *
     * @return the board JPanel
     */
    public JPanel getBoardPanel() {
        return boardPanel;
    }

    /**
     * Updates the board at the specified row and column with the given value.
     * Repaints the board panel after updating.
     *
     * @param row the row to update
     * @param col the column to update
     * @param value the value to set at the specified row and column
     */
    public void updateBoard(int row, int col, int value) {
        board[row][col] = value;
        boardPanel.repaint();
    }

    /**
     * Updates the statistics based on the winner.
     * Increments the win count for the respective player or the draw count.
     * Updates the labels displaying the statistics.
     *
     * @param winner the winner of the game (1 for player 1, 2 for player 2, 0 for draw)
     */
    public void updateStats(int winner) {
        if (winner == 1) {
            player1Wins++;
        } else if (winner == 2) {
            player2Wins++;
        } else {
            draws++;
        }

        player1WinsLabel.setText("Player 1 wins: " + player1Wins);
        player2WinsLabel.setText("Player 2 wins: " + player2Wins);
        drawsLabel.setText("Draws: " + draws);
    }

    /**
     * Prints the current state of the game board and the number of valid cells remaining
     * to the console for debugging purposes.
     */
    private void printBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}