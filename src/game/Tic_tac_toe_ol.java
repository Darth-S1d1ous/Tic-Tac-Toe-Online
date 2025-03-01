package game;
/**
 * The Tic_tac_toe class serves as the entry point for the Tic Tac Toe game application.
 * It initializes the main window of the game and starts the game.
 */
public class Tic_tac_toe_ol {
    
    /**
     * The main window of the Tic Tac Toe game.
     */
    MainWindow mainWindow;
    Client player;
    /**
     * Constructs a new Tic_tac_toe instance and initializes the main window.
     */
    public Tic_tac_toe_ol() {
        mainWindow = new MainWindow();
        player = new Client(mainWindow);
    }

    /**
     * The main method which starts the Tic Tac Toe game.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Tic_tac_toe_ol game = new Tic_tac_toe_ol();
        game.start();
    }

    /**
     * Starts the Tic Tac Toe game by creating a new instance of the main window.
     */
    private void start() {
        player.start();
    }
}