import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Objects;
import java.nio.file.Path;

/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions for all user interface related
 * functionality such as printing menus and displaying the game board.
 */
public class FoxHoundUI {

    /** Number of main menu entries. */
    private static final int MENU_ENTRIES = 5   ;
    /** Main menu display string. */
    private static final String MAIN_MENU =
        "\n1. Move\n2. AI Move\n3. Save\n4. Load\n5. Exit\n\nEnter 1 - 5:";

    /** Menu entry to select a move action. */
    public static final int MENU_MOVE = 1;
    /** Menu entry to play against AI. */
    public static final int MENU_AI = 2;
    /** Menu entry to save the game */
    public static final int MENU_SAVE = 3;
    /** Menu entry to save the game */
    public static final int MENU_LOAD = 4;
    /** Menu entry to terminate the program. */
    public static final int MENU_EXIT = 5;
    /** ASCII code of the beginning column coordinate (A) */
    public static final int ASC_A = 65;

    public FoxHoundUI() {}
    /**
     * Create a 2D array that stores the state of each grid,
     * @param players The positions of fox and hounds
     * @param dimension The dimension of the board
     * @return a 2D array representing
     */
    public static String[][] initialiseBoard(String[] players, int dimension) {
        String[][] board = new String[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = ".";
            }
        }
        for (int i = 0; i < players.length; i++) {
            int[] intPosition = parsePosition(players[i]);
            if (i == players.length - 1) {
                // Fox position
                board[intPosition[0]][intPosition[1]] = "F";
            }
            else {
                // Hound position
                board[intPosition[0]][intPosition[1]] = "H";
            }
        }
        return board;
    }

    /**
     * Display the board in console
     * @param players The positions of the players
     * @param dimension Dimension of the board
     */
    public static void displayBoard(String[] players, int dimension) {
        /* Print the board
           1. Print row coordinates: ABCD...
           2. Print board & Player positions.
         */
        FoxHoundUtils.checkAll(dimension, players);
        String[][] board = initialiseBoard(players, dimension);
        // Initialise row coordinates
        StringBuilder rowCoordinates = new StringBuilder();
        StringBuilder spaces = new StringBuilder("  "); // The leading spaces in first row
        if (dimension >= 10) {
            // Extra one space needed
            spaces.append(" ");
        }
        rowCoordinates.append(spaces);
        for (int i = 0; i < dimension; i++) {
            rowCoordinates.append((char) (ASC_A + i));
        }
        rowCoordinates.append(spaces);
        System.out.println(rowCoordinates);
        System.out.println();

        // Print the board & Player position
        for (int i = 0; i < dimension; i++) {
            StringBuilder row = new StringBuilder();
            StringBuilder prettyDigit = new StringBuilder(); // Add zeros to number while necessary
            if (dimension >= 10 && i < 9) {
                // Add zeros to the vertical coordinates
                prettyDigit.append(0);
            }
            prettyDigit.append(i + 1);
            row.append(prettyDigit).append(" ");
            for (int j = 0; j < dimension; j++) {
                // Print the state of each row
                row.append(board[i][j]);
            }
            row.append(" ").append(prettyDigit);
            System.out.println(row);
        }
        System.out.println();
        System.out.println(rowCoordinates);
    }

    /**
     * The pretty version of initialise board
     */
    public static String[][] initialiseBoardPretty(String[] players, int dimension) {
        String[][] board = new String[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = "   |";
            }
        }
        for (int i = 0; i < players.length; i++) {
            int[] intPosition = parsePosition(players[i]);
            if (i == players.length - 1) {
                // Fox position
                board[intPosition[0]][intPosition[1]] = " F |";
            }
            else {
                // Hound position
                board[intPosition[0]][intPosition[1]] = " H |";
            }
        }
        return board;
    }

    /**
     * Display board in a fancier way
     */
    public static void displayBoardFancy(String[] players, int dimension) {
        /* Print the board
           1. Print row coordinates: ABCD...
           2. Initialise the separation line.
           3. Print board & Player positions.
         */
        String[][] board = initialiseBoardPretty(players, dimension);
        // Initialise row coordinates
        StringBuilder rowCoordinates = new StringBuilder();
        StringBuilder spaces = new StringBuilder("    "); // Leading spaces in first row
        if (dimension >= 10) {
            spaces.append(" ");
        }
        rowCoordinates.append(spaces);
        for (int i = 0; i < dimension; i++) {
            rowCoordinates.append((char) (ASC_A + i)).append("   ");
        }
        System.out.println(rowCoordinates);
        // Initialise the separation line
        StringBuilder separation = new StringBuilder("  "); // leading 2 spaces

        if (dimension >= 10) {
            separation.append(" ");
        }

        separation.append("|");

        for (int i = 0; i < dimension; i++) {
            separation.append("===|");
        }

        // Print the board & Player position
        for (int i = 0; i < dimension; i++) {
            System.out.println(separation);
            StringBuilder row = new StringBuilder();
            StringBuilder prettyDigit = new StringBuilder(); // Add zeros to number while necessary
            if (dimension >= 10 && i < 9) {
                // Add zeros to the vertical coordinates
                prettyDigit.append(0);
            }
            prettyDigit.append(i+1);
            row.append(prettyDigit).append(" |");
            for (int j = 0; j < dimension; j++) {
                // Print the state of each row
                row.append(board[i][j]);
            }
            row.append(" ").append(prettyDigit);
            System.out.println(row);
        }
        System.out.println(separation);
        System.out.println(rowCoordinates);
    }

    /**
     * Divide the input position (String) to an array with the first indicating
     * its row coordinate and second indicating its column coordinate.
     * Eg. Input A8, Output {7, 0}.
     * Explanation:
     * 0 is the row the character is currently in,
     * 7 is the column the character is currently in.
     * We subtract 65 (ASC_A) to each character to obtain its column position
     * in the initialization of the board.
     * Valid values are in range: [0, dimension - 1], for the purpose of index
     * representations with arrays
     * @param position A string indicating character's position
     * @return
     */
    public static int[] parsePosition(String position) {
        try {
            int column = position.charAt(0) - ASC_A; // Coordinates in "ABCD..."
            int row = Integer.parseInt(position.substring(1)) - 1;
            return new int[] {row, column};
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("ERROR: Please enter valid coordinate pair separated by space.");
        }
    }

    /**
     * Update the position of a figure
     * @param origin The original position of the character
     * @param destination The new position of the character
     * @param players The array of positions of characters
     */
    public static void updatePosition(String origin, String destination, String[] players) {
        for (int i = 0; i < players.length; i++) {
            if (Arrays.equals(FoxHoundUI.parsePosition(players[i]),
                              FoxHoundUI.parsePosition(origin))) {
                players[i] = destination;
                break;
            }
        }
    }

    /**
     * Print the main menu and query the user for an entry selection.
     * 
     * @param figureToMove the figure type that has the next move
     * @param stdin a Scanner object to read user input from
     * @return a number representing the menu entry selected by the user
     * @throws IllegalArgumentException if the given figure type is invalid
     * @throws NullPointerException if the given Scanner is null
     */
    public static int mainMenuQuery(char figureToMove, Scanner stdin) {
        Objects.requireNonNull(stdin, "Given Scanner must not be null");
        if (figureToMove != FoxHoundUtils.FOX_FIELD 
         && figureToMove != FoxHoundUtils.HOUND_FIELD) {
            throw new IllegalArgumentException("Given figure field invalid: " + figureToMove);
        }

        String nextFigure = 
            figureToMove == FoxHoundUtils.FOX_FIELD ? "Fox" : "Hounds";

        int input = -1;
        while (input == -1) {
            System.out.println(nextFigure + " to move");
            System.out.println(MAIN_MENU);

            boolean validInput = false;
            if (stdin.hasNextInt()) {
                input = stdin.nextInt();
                validInput = input > 0 && input <= MENU_ENTRIES;
            }

            if (!validInput) {
                System.out.println("Please enter valid number.");
                input = -1; // reset input variable
            }

            stdin.nextLine(); // throw away the rest of the line
        }

        return input;
    }

    /**
     * Prompt the user to enter the move he/she want to make.
     * Input consists of origin position and destination position
     * First check if the move is valid. If valid then update the position.
     * If not valid print an error message and ask the user to enter again.
     * @param dim Dimension of the board
     * @param stdIn The Scanner that accepts the input
     * @return If the move is valid, return the origin and destination.
     */
    public static String[] positionQuery(int dim, Scanner stdIn) {
        // Prompt input from console
        checkDim(dim);
        while (true) {
            System.out.println("Provide origin and destination coordinates.");
            System.out.printf("Enter two positions between A1-%c%d:", (char) (65 + dim - 1), dim);
            System.out.println();
            String[] input = stdIn.nextLine().split(" ");
            try {
                if (FoxHoundUtils.isValidPosition(dim, input[0]) &&
                        FoxHoundUtils.isValidPosition(dim, input[1]) &&
                        input.length == 2) {
                    return input;
                } else {
                    System.err.println("ERROR: Please enter valid coordinate pair separated by space.");
                    System.out.println();
                }
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                System.err.println("ERROR: Please enter two coordinate pairs");
                System.out.println();
            }
        }
    }

    /**
     * Prompt the user to enter a file path for purposes of saving and loading a game.
     * @param stdIn The Scanner that accepts the input
     * @return The file path entered by the user
     */
    public static Path fileQuery(Scanner stdIn) {
        while (true) {
            System.out.println("Enter file path:");
            String fileName = stdIn.nextLine();
            if (!fileName.endsWith(".txt")) {
                System.err.println("ERROR: not .txt file.");
                System.out.println();
            }
            else {
                return Paths.get(fileName);
            }
        }
    }

    public static void checkDim(int dim) {
        if (dim < 4 || dim > 26) {
            throw new IllegalArgumentException();
        }
    }
}







