package game;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;

/**
 * The BottomPanel class represents the bottom section of the Tic Tac Toe game application.
 * It handles user input for the player's name, displays status messages, and shows the current time.
 * Additionally, it manages the interaction between the main window and the game board.
 */
public class BottomPanel extends JPanel {
    /**
     * The text field where the player enters their name.
     */
    private JTextField nameField;

    /**
     * The button to submit the player's name.
     */
    private JButton submitButton;

    /**
     * The label displaying messages to the user.
     */
    private JLabel messageLabel;

    /**
     * The label displaying the current time.
     */
    private JLabel timeLabel;

    /**
     * The timer that updates the current time every second.
     */
    private Timer timer;

    /**
     * Reference to the main window of the application.
     */
    private MainWindow mainWindow;

    /**
     * Reference to the middle panel (game board) of the application.
     */
    private MiddlePanel middlePanel;

    /**
     * Constructs a new BottomPanel instance with references to the main window.
     *
     * @param mainWindow The main window of the Tic Tac Toe game.
     */
    public BottomPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        nameField = new JTextField(25);
        submitButton = new JButton("Submit");
        messageLabel = new JLabel("Enter your name: ");
        timeLabel = new JLabel("Current Time: ");

        init();
    }

    /**
     * Constructs a new BottomPanel instance with references to the main window and the middle panel.
     *
     * @param mainWindow   The main window of the Tic Tac Toe game.
     * @param middlePanel  The middle panel (game board) of the Tic Tac Toe game.
     */
    public BottomPanel(MainWindow mainWindow, MiddlePanel middlePanel) {
        this.mainWindow = mainWindow;
        this.middlePanel = middlePanel;
        nameField = new JTextField(25);
        submitButton = new JButton("Submit");
        messageLabel = new JLabel("Enter your name: ");
        timeLabel = new JLabel("Current Time: ");

        init();
    }

    /**
     * Initializes the BottomPanel by setting up the layout, adding components,
     * and attaching event listeners.
     */
    private void init() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(messageLabel);
        topPanel.add(nameField);
        topPanel.add(submitButton);

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                if (!name.isEmpty()) {
                    nameField.setEditable(false);
                    submitButton.setEnabled(false);
                    mainWindow.setName(name);
                    if (middlePanel != null) {
                        middlePanel.setInfoLabel("Welcome " + name);
                    }
                } else {
                    messageLabel.setText("Name cannot be empty. Please enter your name.");
                }
            }
        });

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(timeLabel);
        add(centerPanel, BorderLayout.CENTER);

        startTimer();
    }

    /**
     * Starts the timer that updates the current time label every second.
     */
    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                timeLabel.setText("Current Time: " + formatter.format(date));
            }
        });
        timer.start();
    }
}