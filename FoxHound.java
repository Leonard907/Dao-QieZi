import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class FoxHound extends Application {


    /** Index of Row in position string. */
    private static final int ROW = 0;
    /** Index of Column in position string. */
    private static final int COLUMN = 1;

    /** Board of all the cells. */
    public final Cell[][] board = new Cell[DIM][DIM];
    /** Current turn. */
    private char turn;
    /** Array of players' positions. */
    private String[] players;
    /** Dimension of the board. (Do not support other dimensions for now). */
    public static final int DIM = 8;
    /** Origin of the move (first click). */
    private Cell origin;
    /** Destination of the move (second click). */
    private Cell destination;
    /** Pane of the board. */
    private BorderPane pane = new BorderPane();
    /** The board grid. */
    private GridPane mainBoard = new GridPane();
    /** Status of the game. */
    private HBox status = new HBox(10);
    /** Restart the game. */
    private Button restart = new Button("我柜子动了，我不玩了");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        initialize();
        updateBoard();
        restart.setOnAction(e -> {
            status.getChildren().clear();
            initialize();
            updateBoard();
        });
        status.getChildren().add(restart);
        pane.setCenter(mainBoard);
        pane.setBottom(status);

        Scene scene = new Scene(pane, 800, 800);
        primaryStage.setTitle("白给秀");
        primaryStage.setScene(scene);
        primaryStage.show(); // display scene
    }

    public void initialize() {
        mainBoard.getChildren().clear();
        players = FoxHoundUtils.initialisePositions(DIM);
        turn = 'F';
        // Set the initial pane
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if ((i + j) % 2 == 1) board[i][j] = new Cell(Color.BLACK);
                else                  board[i][j] = new Cell(Color.WHITE);
                board[i][j].position = FoxHoundUtils.toPosition(new int[] {i, j});
            }
        }
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                mainBoard.add(board[i][j], j, i);
            }
        }
        status.getChildren().add(setText());
    }

    public void updateBoard() {
        for (int i = 0; i < players.length; i++) {
            int row = FoxHoundUI.parsePosition(players[i])[ROW];
            int column = FoxHoundUI.parsePosition(players[i])[COLUMN];
            if (i != players.length - 1) {
                board[row][column].setFigure('H');
            } else {
                board[row][column].setFigure('F');
            }
        }
    }

    public String gameOver() {
        if (FoxHoundUtils.isFoxWin(players[players.length - 1])) return "fox";
        else if (FoxHoundUtils.isHoundWin(players, DIM)) return "hound";
        else return "continue";
    }

    public Text setText() {
        Text message = new Text();
        if (turn == 'F') message.setText("今天是个上坟的好日子");
        else message.setText("剩个茄子，能刀就刀");
        return message;
    }

    /**
     * Class for each cell on the board.
     */
    class Cell extends Pane {
        public static final int START_X = 20;
        /** Figure in the cell. */
        public char figure;
        /** Color of the cell. */
        public Color color;
        /** position of the cell. */
        public String position;

        public Cell(Color color) {
            this.color = color;
            this.setBackground(new Background(
                    new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
            this.setPrefSize(2000,2000);
            this.setOnMouseClicked(e -> handleMouseClick());
        }

        /**
         * Set the cell color.
         * @param color Color given to the cell.
         */
        public void setColor(Color color) {
            this.setBackground(new Background(
                    new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        }

        /**
         * Set the name for the figure.
         * @param figure Figure of the cell.
         */
        public void setFigure(char figure) {
            getChildren().clear();
            this.figure = figure;
            switch (figure) {
                case 'F':
                    ImageView qzz = new ImageView("Qzz.jpg");
                    qzz.setFitHeight(100);
                    qzz.setFitWidth(100);
                    getChildren().add(qzz);
                    break;
                case 'H':
                    ImageView knife = new ImageView("knife.jpg");
                    knife.setFitHeight(100);
                    knife.setFitWidth(100);
                    getChildren().add(knife);
                    break;
            }
        }

        public void updateFigure() {
            getChildren().clear();
            switch (turn) {
                case 'F':
                    ImageView wdnmd = new ImageView("Wdnmd.jpeg");
                    wdnmd.setFitHeight(100);
                    wdnmd.setFitWidth(100);
                    getChildren().add(wdnmd);
                    break;
                case 'H':
                    ImageView knife = new ImageView("drawKnife.jpeg");
                    knife.setFitHeight(100);
                    knife.setFitWidth(100);
                    getChildren().add(knife);
                    break;
            }
        }

        public void handleMouseClick() {
            if (origin == null && FoxHoundUtils.contains(this.position, players)) {
                updateFigure();
                origin = this;
            } else {
                destination = this;
                if (FoxHoundUtils.isValidMove(DIM, players, turn, origin.position, destination.position)) {
                    FoxHoundUI.updatePosition(origin.position, destination.position, players);
                    origin.getChildren().clear();
                    destination.setFigure(turn);
                    turn = turn == 'F' ? 'H' : 'F';
                }
                updateBoard();
                status.getChildren().clear();
                switch (gameOver()) {
                    case "fox":
                        Text foxWin = new Text("残局大师：Qennys");
                        status.getChildren().add(foxWin);
                        break;
                    case "hound":
                        Text houndWin = new Text("GK! 捏妈了个憋的");
                        status.getChildren().add(houndWin);
                        break;
                    default:
                        status.getChildren().add(setText());
                }
                status.getChildren().add(restart);
                origin.setColor(origin.color);
                origin = null;
                destination = null;
            }

        }
    }

}