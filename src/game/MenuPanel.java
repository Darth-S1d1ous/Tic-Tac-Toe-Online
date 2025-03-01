package game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * The MenuPanel class represents the menu section of the Tic Tac Toe game application.
 * It includes the control and help menus, with options to exit the game and view instructions.
 */
public class MenuPanel extends JPanel {
    /**
     * The menu bar that contains all the menus.
     */
    private JMenuBar menuBar;

    /**
     * The control menu containing control-related menu items.
     */
    private JMenu controlMenu;

    /**
     * The help menu containing help-related menu items.
     */
    private JMenu helpMenu;

    /**
     * The menu item to exit the application.
     */
    private JMenuItem exitButton;

    /**
     * The menu item to display game instructions.
     */
    private JMenuItem instructionButton;

    /**
     * Constructs a new MenuPanel instance and initializes the menu components.
     */
    public MenuPanel() {
        controlMenu = new JMenu("Control");
        helpMenu = new JMenu("Help");
        menuBar = new JMenuBar();
        exitButton = new JMenuItem("Exit");
        instructionButton = new JMenuItem("Instruction");
        init();
    }

    /**
     * Initializes the menu panel by setting up menus, menu items, and their action listeners.
     */
    private void init() {
        setLayout(new BorderLayout());

        controlMenu.add(exitButton);
        helpMenu.add(instructionButton);
        menuBar.add(controlMenu);
        menuBar.add(helpMenu);

        add(menuBar, BorderLayout.NORTH);

        instructionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showHelp();
            }
        });
    }

    /**
     * Displays the help dialog with game instructions.
     * Allows customization of the dialog's button text.
     */
    private void showHelp() {
        String helpMessage = "Some information about the game:\n\n"
                           + "Criteria for a valid move:\n"
                           + "- The move is not occupied by any mark.\n"
                           + "- The move is made in the player's turn.\n"
                           + "- The move is made within the 3 x 3 board.\n\n"
                           + "The game would continue and switch among the opposite player until it reaches either one of the following conditions:\n"
                           + "- Player 1 wins.\n"
                           + "- Player 2 wins.\n"
                           + "- Draw.\n"
                           + "- One of the players leaves the game.\n\n";
        Object[] options = {"Yes"};
        JOptionPane.showOptionDialog(
            this,
            helpMessage,
            "Help",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );
    }
    /**
     * Gets the exit button menu item.
     * @return
     */
    public JMenuItem getExitButton() {
        return exitButton;
    }
}