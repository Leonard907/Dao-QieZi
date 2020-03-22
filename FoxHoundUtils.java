import java.util.Arrays;

/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions to check the state of the game
 * board and validate board coordinates and figure positions.
 */
public class FoxHoundUtils {

    // ATTENTION: Changing the following given constants can 
    // negatively affect the outcome of the auto grading!

    /** Default dimension of the game board in case none is specified. */
    public static final int DEFAULT_DIM = 8;
    /** Minimum possible dimension of the game board. */
    public static final int MIN_DIM = 4;
    /** Maximum possible dimension of the game board. */
    public static final int MAX_DIM = 26;

    /** Symbol to represent a hound figure. */
    public static final char HOUND_FIELD = 'H';
    /** Symbol to represent the fox figure. */
    public static final char FOX_FIELD = 'F';

    // HINT Write your own constants here to improve code readability ...
    /** Dimension of the board. Set by user. */
    private static int DIM;
    /**
     * An imaginary value that is used to distinguish differences between places of
     * board that are above or below the fox in terms of column position.
     */
    public static final int IRR = 100;

    public FoxHoundUtils() {}

    /**
     * Set the dimension according to keyboard input.
     * @param dim The dimension user enters.
     */
    public static void setDim(int dim) {
        DIM = dim;
    }

    /**
     * Initialise the starting positions of all players with a given dimension
     * @param dimension The dimension of the board
     * @return Array of strings that stores all positions of players.
     */
    public static String[] initialisePositions(int dimension) {
        // Check the dimension is within range
        if (dimension >= MIN_DIM && dimension <= MAX_DIM)
            return insert(initialiseFoxPosition(dimension), initialiseHoundPositions(dimension));
        else
            throw new IllegalArgumentException("Not in range [4-26]");
    }

    /**
     * insert an element into the bottom of an array.
     * @param element The element you want to add
     * @param list The original list
     * @return Array of String that is the result of insertion
     * {@link #}
     */
    public static String[] insert(String element, String[] list) {
        // Insert an element to a list
        String[] concat = new String[list.length + 1];
        for (int i = 0; i < list.length; i++) {
            concat[i] = list[i];
        }
        concat[list.length] = element;
        return concat;
    }

    /**
     * Initialise the position of the fox.
     * @param dimension Dimension of the board.
     * @return String indicating the position of the fox
     */
    private static String initialiseFoxPosition(int dimension) {
        // Initialise positions of fox
        if (dimension % 2 == 0) {
            // Last row starts with a black square
            if ((dimension / 2) % 2 == 0) {
                // Central position is black
                return Character.toString((char) (FoxHoundUI.ASC_A + dimension / 2)) + Integer.toString(dimension);
            }
            else {
                // Central position is white, fox is on the left
                return Character.toString((char) (FoxHoundUI.ASC_A + dimension / 2 - 1)) + Integer.toString(dimension);
            }
        }
        else {
            // Last row starts with a white square
            if ((dimension / 2) % 2 == 0) {
                // Central position is white, fox is on the right
                return Character.toString((char) (FoxHoundUI.ASC_A + dimension / 2 + 1)) + Integer.toString(dimension);
            }
            else {
                // Central position is black
                return Character.toString((char) (FoxHoundUI.ASC_A + dimension / 2)) + Integer.toString(dimension);
            }
        }
    }

    /**
     * initialise hound positions for all hounds
     * @param dimension Dimension of the board.
     * @return Array of String indicating the positions of the hounds
     */
    private static String[] initialiseHoundPositions(int dimension) {
        // Initialise positions of hounds
        String[] houndPositions = new String[dimension / 2];
        for (int i = 0; i < dimension; i++) {
            if (i % 2 == 1) {
                // black square, place a hound
                houndPositions[i / 2] = Character.toString((char) (FoxHoundUI.ASC_A + i)) + "1";
            }
        }
        return houndPositions;
    }

    /**
     * Convert an integer representation of figure's representation to a string.
     * Example:
     * {0,1} refers to "A2".
     * The first item corresponds to the "Letter Coordinate": 0-26 -> A-Z
     * The second item corresponds to the "Integer Coordinate": 0-(dimension-1) -> 1-dimension
     * @param coordinates The integer coordinate of a figure's position
     * @return The corresponding string representation of the position.
     */
    public static String toPosition(int[] coordinates) {;
        StringBuilder position = new StringBuilder();
        position.append((char) (FoxHoundUI.ASC_A + coordinates[1]));
        position.append(coordinates[0] + 1);
        return position.toString();
    }

    /**
     * Determining whether a move is valid.
     * @param dim Dimesnion of the board.
     * @param players Positions of the players.
     * @param figure The figure to move next.
     * @param origin The start position of the move.
     * @param destination The destination of the move.
     * @throws IllegalArgumentException When the input coordinates is invalid,
     * throws this error which means the coordinates cannot be read.
     * @return A boolean indicating whether the move is valid.
     */
    public static boolean isValidMove(int dim, String[] players, char figure,
                                      String origin, String destination) {
        checkAll(dim, players);
        if (origin == null || destination == null) {
            throw new NullPointerException();
        }
        if (isValidInput(dim, players, figure, origin, destination)) {
            // Test whether destination is occupied
            if (!contains(destination, players)) {
                if (figure == 'H') {
                    // Check hound
                    boolean isHound = false;
                    for (int i = 0; i < players.length - 1; i++) {
                        if (Arrays.equals(FoxHoundUI.parsePosition(players[i]),
                                          FoxHoundUI.parsePosition(origin))) {
                            isHound = true;
                            break;
                        }
                    }
                    if (isHound) {
                        // Check move is in range
                        int[] originCoordinates = FoxHoundUI.parsePosition(origin);
                        int[] destinationCoordinates = FoxHoundUI.parsePosition(destination);
                        return originCoordinates[0] - destinationCoordinates[0] == -1 &&
                                Math.abs(originCoordinates[1] - destinationCoordinates[1]) == 1;
                    } else {
                        return false;
                    }
                }
                else {
                    // Check fox
                    if (Arrays.equals(FoxHoundUI.parsePosition(players[players.length - 1]),
                                      FoxHoundUI.parsePosition(origin))) {
                        // Check move in range
                        int[] originCoordinates = FoxHoundUI.parsePosition(origin);
                        int[] destinationCoordinates = FoxHoundUI.parsePosition(destination);
                        return Math.abs(originCoordinates[0] - destinationCoordinates[0]) == 1 &&
                                Math.abs(originCoordinates[1] - destinationCoordinates[1]) == 1;
                    } else {
                        return false;
                    }
                }
            }
            else {
                return false;
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public static void checkAll(int dim, String[] players) {
        FoxHoundIO.checkNull(players);
        FoxHoundIO.checkContents(players, dim);
        FoxHoundUI.checkDim(dim);
    }

    /**
     * Whether the input is valid (No syntax errors)
     * @param dim Dimesnion of the board.
     * @param players Positions of the players.
     * @param figure The figure to move next.
     * @param origin The start position of the move.
     * @param destination The destination of the move.
     * @return Whether the input is valid or not.
     */
    private static boolean isValidInput(int dim, String[] players, char figure,
                                        String origin, String destination) {
        /* Determine whether the input is valid or not from:
           1.players not null
           2.figure not 'F', 'H'
           3.origin, destination valid & in range
        */
        return isValidPosition(dim, origin) && isValidPosition(dim, destination) &&
                (figure == 'F' || figure == 'H');
    }

    /**
     * Whether the position is valid (In range, not out of bounds)
     * @param dim Dimension of the board
     * @param position Position of the player
     * @return Whether this position is valid or not.
     */
    public static boolean isValidPosition(int dim, String position) {
        // Check input is in correct format
        try {
            int[] coordinates = FoxHoundUI.parsePosition(position);
            // Check validity of coordinates
            return coordinates[0] < dim && coordinates[1] < dim
                    && coordinates[0] >= 0 && coordinates[1] >= 0;
        }
        catch (IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Whether an array contains an element or not.
     * @param element The element to be tested
     * @param array Array to be teseted
     * @return Whether the element is in the array or not.
     */
    public static boolean contains(String element, String[] array) {
        for (String target: array) {
            if (element.equals(target)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determine if the fox wins or not.
     * @param foxPosition The position of the fox.
     * @return Whether the fox wins or not.
     */
    public static boolean isFoxWin(String foxPosition) {
        // Determine whether the fox reaches the other side
        if (foxPosition == null) {
            throw new NullPointerException();
        } else if (!isValidPosition(MAX_DIM, foxPosition)) {
            throw new IllegalArgumentException();
        }
        return FoxHoundUI.parsePosition(foxPosition)[0] == 0;
    }

    /**
     * Determine whether the hounds win or not.
     * @param players Array of positions of players
     * @param dim Dimension of the board
     * @return Whether the houdns win or not.
     */
    public static boolean isHoundWin(String[] players, int dim) {
        FoxHoundUI.checkDim(dim);
        FoxHoundIO.checkNull(players);
        FoxHoundIO.checkContents(players, dim);
        // Get fox's position and its possible moves
        if (dim < MIN_DIM || dim > MAX_DIM) {
            throw new IllegalArgumentException();
        }
        if (players.length != dim / 2 + 1) {
            throw new IllegalArgumentException();
        }
        int[] foxPosition = FoxHoundUI.parsePosition(players[players.length - 1]);
        int[][] possibleMoves = {{foxPosition[0] - 1, foxPosition[1] - 1},
                                 {foxPosition[0] - 1, foxPosition[1] + 1},
                                 {foxPosition[0] + 1, foxPosition[1] - 1},
                                 {foxPosition[0] + 1, foxPosition[1] + 1}};
        boolean[] blockedByHounds = new boolean[4];
        /* Check whether the move is possible
           -> Move is invalid
           -> Blocked by hounds
        */
        for (int i = 0; i < possibleMoves.length; i++) {
            if (isValidPosition(dim, toPosition(possibleMoves[i]))) {
                for (int j = 0; j < players.length - 1; j++) {
                    if (Arrays.equals(possibleMoves[i], FoxHoundUI.parsePosition(players[j]))) {
                        blockedByHounds[i] = true;
                    }
                }
            }
            else {
                blockedByHounds[i] = true;
            }
        }
        for (boolean blocked: blockedByHounds) {
            if (!blocked) {
                return false;
            }
        }
        return true;
    }
}
