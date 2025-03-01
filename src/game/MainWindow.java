package game;
import javax.swing.*;
import java.awt.*;

/**
 * The MainWindow class represents the main window of the Tic Tac Toe game application.
 * It initializes and manages the main components of the game, including the menu,
 * game board, and status panel.
 */
public class MainWindow {
    /**
     * The width of the main window.
     */
    private int w;

    /**
     * The height of the main window.
     */
    private int h;

    private String name;

    /**
     * The JFrame that constitutes the main window.
     */
    private JFrame frame;

    /**
     * The menu panel containing the application's menus.
     */
    private MenuPanel menuPanel;

    /**
     * The middle panel representing the game board.
     */
    private MiddlePanel middlePanel;

    /**
     * The bottom panel containing user input and status information.
     */
    private BottomPanel bottomPanel;

    /**
     * Constructs a new MainWindow instance and initializes the main window.
     */
    public MainWindow() {
        this.w = 950;
        this.h = 700;
        this.name = null;
        frame = new JFrame();
        menuPanel = new MenuPanel();
        middlePanel = new MiddlePanel();
        bottomPanel = new BottomPanel(this, middlePanel);
        
        init();
    }

    /**
     * Initializes the main window by setting up its properties and adding
     * the main components.
     */
    private void init() {
        frame.setTitle("Tic Tac Toe - Player: ");
        frame.setSize(w, h);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLayout(new BorderLayout());
        frame.add(menuPanel, BorderLayout.NORTH);
        frame.add(middlePanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Sets the title of the main window.
     *
     * @param title The new title for the main window.
     */
    public void setName(String name) {
        this.name = name;
        frame.setTitle("Tic Tac Toe - Player: " + name);
    }

    /**
     * Shows the end game dialog with the specified command.
     *
     * @param cmd The command indicating the end game state.
     * @return The user's response to the dialog.
     */
    public int showEndGameDialog(String cmd) {
        return showEndGameDialog(cmd, false);
    }

    /**
     * Shows the end game dialog with the specified command and player state.
     *
     * @param cmd The command indicating the end game state.
     * @param isCurrentPlayer Whether the current player is the one who won.
     * @return The user's response to the dialog.
     */
    public int showEndGameDialog(String cmd, boolean isCurrentPlayer) {
        String title = "Game Over";
        if(cmd.equals("WIN")) {
            if(isCurrentPlayer) {
                String msg = "Congratulations. You win! Do you want to play again?";
                return JOptionPane.showConfirmDialog(frame, msg, title, JOptionPane.YES_NO_OPTION);
            } else {
                String msg = "You lose. Do you want to play again?";
                return JOptionPane.showConfirmDialog(frame, msg, title, JOptionPane.YES_NO_OPTION);
            }
        } else if (cmd.equals("DRAW")){
            String msg = "It's a draw! Do you want to play again?";
            return JOptionPane.showConfirmDialog(frame, msg, title, JOptionPane.YES_NO_OPTION);
        } else if (cmd.equals("QUIT")) {
            String msg = "Game Ends. One of the players left.";
            Object[] options = {"Okay"};
            JOptionPane.showOptionDialog(
                this.frame,
                msg,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                (Icon) null,
                options,
                options[0]
            );
            return JOptionPane.CLOSED_OPTION;
        }
        return JOptionPane.CLOSED_OPTION;
    }

    /**
     * Gets the name of the player.
     *
     * @return The name of the player.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the middle panel representing the game board.
     *
     * @return The middle panel.
     */
    public MiddlePanel getMiddlePanel() {
        return middlePanel;
    }

    /**
     * Gets the menu panel containing the application's menus.
     *
     * @return The menu panel.
     */
    public MenuPanel getMenuPanel() {
        return menuPanel;
    }
}