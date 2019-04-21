
package ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TextField;
import core.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;

/** Represents a Connect4 graphical user interface.
 *
 * @author Jon McKee
 * @version 1.0
 */
public class Connect4GUI extends Application
{
    /**
     * The type of game played when "New Game" is selected.
     */
    enum GameType
    {
        TWO_PLAYER,
        VS_COMPUTER
    }
    private static final char PLAYER1_PIECE = 'X';
    private static final char PLAYER2_PIECE = 'O';
    private final int STAGE_SIZE = 400;
    private GameType currentGameType;
    private TextField statusBar;
    private GridPane board;
    private int playerID;
    private IConnect4Client client;
    
    /** Callback for a game request.
     * 
     * @param playerID ID assigned to this player.
     */
    private void gameStart(int playerID)
    {
        this.playerID = playerID;
    }
    
    /** Callback for updating the board.
     * 
     * @param playerID Player that placed a piece.
     * @param row Row the piece is placed in.
     * @param column Column the piece is placed in.
     */
    private void updateBoard(int playerID, int row, int column)
    {
        char playerSymbol = playerID == Connect4Constants.PLAYER1 ?
                PLAYER1_PIECE :
                PLAYER2_PIECE;
        int invertedRow = Connect4Constants.ROW_COUNT - row - 1;
        Platform.runLater(() -> {
            for (Node node : board.getChildren())
            {
                if (GridPane.getColumnIndex(node) == column &&
                    GridPane.getRowIndex(node) == invertedRow)
                {
                    TextField targetCell = (TextField) node;
                    targetCell.setText(
                            "" + playerSymbol);
                    if (playerID == Connect4Constants.PLAYER1)
                        targetCell.setStyle(
                                "-fx-background-color:lightblue;");                      
                    else
                        targetCell.setStyle(
                                "-fx-background-color:tomato;");
                    break;
                }
            }
        });
    }
    
    /** Callback for current game status.
     * 
     * @param status Current {@link GameStatus}.
     */
    private void gameStatus(GameStatus status)
    {
        Platform.runLater(() -> {
            switch (status)
            {
                case NONE:
                    statusBar.setText("Your turn.");
                    board.setDisable(false);
                    break;
                case CANCELLED:
                    statusBar.setText("Game cancelled.");
                    board.setDisable(true);
                    break;
                case INVALID_MOVE:
                    statusBar.setText("Choose a valid column.");
                    board.setDisable(false);
                    break;
                case TIE:
                    statusBar.setText("Game tied!");
                    board.setDisable(true);
                    break;
                case PLAYER1_WIN:
                    if (playerID == Connect4Constants.PLAYER1)
                        statusBar.setText("You won!");
                    else
                        statusBar.setText("You lost.");
                    board.setDisable(true);
                    break;
                case PLAYER2_WIN:
                    if (playerID == Connect4Constants.PLAYER2)
                        statusBar.setText("You won!");
                    else
                        statusBar.setText("You lost.");
                    board.setDisable(true);
                    break;
            }
        });
    }
    
    /**
     * Creates the GUI status bar.
     */
    private void setupStatusBar()
    {
        statusBar = new TextField("Welcome to Connect4!");
        statusBar.setAlignment(Pos.CENTER);  
        statusBar.setStyle(
                "-fx-background-color:lightgrey;-fx-highlight-fill:lightgrey;");
        statusBar.setEditable(false);
        statusBar.setMouseTransparent(true);
        statusBar.setFocusTraversable(false);
    }
    
    /** Creates the GUI menu bar.
     * 
     * @return {@link MenuBar} for the GUI.
     */
    private MenuBar createMenuBar()
    {
        //Create file menu.
        Menu fileMenu = new Menu("File");
        MenuItem newGame = new MenuItem("New Game");
        newGame.setOnAction(event -> {
            resetBoard();
            if (client != null)
                client.cancelGame();
            switch (currentGameType)
            {
                case VS_COMPUTER:
                    client = new Connect4ComputerPlayer(
                            this::gameStart,
                            this::updateBoard,
                            this::gameStatus);
                    break;
                case TWO_PLAYER:
                    client = new Connect4Client(
                            this::gameStart,
                            this::updateBoard,
                            this::gameStatus);
                    break;
            }
            client.requestGame();
        });
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(event -> Platform.exit());
        fileMenu.getItems().addAll(newGame, exit);
        
        //Create game menu.
        Menu gameMenu = new Menu("Options");
        CheckMenuItem versusComputer = new CheckMenuItem("Versus Computer");
        CheckMenuItem twoPlayer = new CheckMenuItem("Versus Player");
        versusComputer.setOnAction(event -> {
            CheckMenuItem item = (CheckMenuItem) event.getTarget();
            if (item.isSelected())
            {
                twoPlayer.setSelected(false);
                currentGameType = GameType.VS_COMPUTER;
            } else {
                twoPlayer.setSelected(true);
                currentGameType = GameType.TWO_PLAYER;
            }
        });              
        twoPlayer.setOnAction(event -> {
            CheckMenuItem item = (CheckMenuItem) event.getTarget();
            if (item.isSelected())
            {
                versusComputer.setSelected(false);
                currentGameType = GameType.TWO_PLAYER;
            } else {
                versusComputer.setSelected(true);
                currentGameType = GameType.VS_COMPUTER;
            }
        });
        versusComputer.setSelected(true);
        twoPlayer.setSelected(false);
        currentGameType = GameType.VS_COMPUTER;
        gameMenu.getItems().addAll(versusComputer, twoPlayer);
        
        //Create menu bar.
        MenuBar menu = new MenuBar();
        menu.getMenus().addAll(fileMenu, gameMenu);
        return menu;
    }
    
    /** Creates a Connect4 game board.
     * 
     * @return {@link GridPane} representing the board.
     */
    private GridPane createBoard()
    {
        GridPane tempBoard = new GridPane();
        int cellSize = STAGE_SIZE / 10;
        for (int i = 0; i < Connect4Constants.ROW_COUNT; i++)
            for (int j = 0; j < Connect4Constants.COLUMN_COUNT; j++)
            {
                TextField cell = new TextField();
                cell.setStyle(
                        "-fx-border-color:black;-fx-border-width:1;" +
                        "-fx-border-style:solid;");
                cell.setPrefSize(cellSize, cellSize);
                cell.setEditable(false);
                cell.setFocusTraversable(false);
                cell.setCursor(Cursor.HAND);
                cell.setOnMouseClicked(event -> {
                    TextField chosenCell = (TextField) event.getSource();
                    int columnIndex = GridPane.getColumnIndex(chosenCell);
                    client.addPiece(columnIndex);
                    board.setDisable(true);
                });
                tempBoard.add(cell, j, i);
            }
        tempBoard.setPrefSize(Connect4Constants.COLUMN_COUNT * cellSize,
                Connect4Constants.ROW_COUNT * cellSize);
        tempBoard.setAlignment(Pos.CENTER);
        return tempBoard;
    }
    
    /** Resets the board state.
     */
    private void resetBoard()
    {
        for (Node node : board.getChildren())
        {
            TextField cell = (TextField) node;
            cell.setStyle(
                    "-fx-border-color:black;-fx-border-width:1;" +
                    "-fx-border-style:solid;");
            cell.setText("");
        }
        board.setDisable(true);
    }
    
    /** Starts up the Connect4 GUI application
     * 
     * @param mainStage Stage to create the main GUI on.
     */
    @Override
    public void start(Stage mainStage)
    {
        setupStatusBar();
        board = createBoard();
        board.setDisable(true);
        MenuBar menu = createMenuBar();
        
        //Create main layout.
        BorderPane gui = new BorderPane();
        gui.setTop(menu);
        gui.setCenter(board);
        gui.setBottom(statusBar);
        
        //Scene and Stage setup
        Scene scene = new Scene(gui, STAGE_SIZE, STAGE_SIZE);
        mainStage.setTitle("Connect4");
        mainStage.setScene(scene);
        mainStage.show();
    }
}
