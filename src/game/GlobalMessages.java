package game;
/**
 * The GlobalMessages class contains global variables used across the Tic Tac Toe game application.
 * It maintains the player's name, game statistics, and the current state of the game.
 */
public class GlobalMessages {
    /**
     * The name of the current player.
     */
    public static String name = new String("");

    /**
     * The number of games won by the computer.
     */
    public static int wins_bot = 0;

    /**
     * The number of games won by the player.
     */
    public static int wins_player = 0;

    /**
     * The number of games that ended in a draw.
     */
    public static int draws = 0;

    /**
     * Represents the current state of the game.
     */
    public static int game_state = -1;
}