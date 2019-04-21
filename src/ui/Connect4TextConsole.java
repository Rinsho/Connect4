package ui;

import core.*;
import java.util.Scanner;
import java.util.InputMismatchException;
import javafx.application.Application;

/**
 * Text console for displaying the Connect4 game.
 * 
 * @author Jon McKee
 * @version 1.0
 */
public class Connect4TextConsole
{
    private static final char PLAYER1_PIECE = 'X';
    private static final char PLAYER2_PIECE = 'O';
    private final IConnect4Client client;
    private int playerID;
    private final int[][] board;
    
    /**
     * Creates a new text console to play Connect4.
     * 
     * @param versusComputer Whether this is versus AI or another player.
     */
    public Connect4TextConsole(boolean versusComputer)
    {
        board = new int[Connect4Constants.ROW_COUNT][Connect4Constants.COLUMN_COUNT];
        if (versusComputer)
            client = new Connect4ComputerPlayer(
                    this::gameStart,
                    this::boardUpdate,
                    this::gameStatus);
        else
            client = new Connect4Client(
                    this::gameStart,
                    this::boardUpdate,
                    this::gameStatus);
    }
    
    /**
     * Manages a turn.
     */
    private void takeTurn()
    {
        Scanner input = new Scanner(System.in);
        displayBoard();
        System.out.println("Your turn. Choose a column number from 1-7.");  
        int column = 0;
        try
        {
            column = input.nextInt();
            client.addPiece(--column);
        }
        catch (InputMismatchException ex)
        {
            System.out.println("That is not a valid column.");
            takeTurn();
        }       
    }
    
    /** 
     * Starts the game.
     */
    public void playGame()
    {
        client.requestGame();
    }
    
    /**
     * Displays the Connect4 game board.
     */
    public void displayBoard()
    {
        for (int row = Connect4Constants.ROW_COUNT; row > 0; row--)
        {
            for (int col = 0; col < Connect4Constants.COLUMN_COUNT; col++)
            {
                char piece = ' ';
                if (board[row - 1][col] == Connect4Constants.PLAYER1) 
                    piece = PLAYER1_PIECE;
                else if (board[row - 1][col] == Connect4Constants.PLAYER2)
                    piece = PLAYER2_PIECE;
                System.out.print("|" + piece);
            }
            System.out.println('|');
        }
    }
    
    /**
     * Clears the board and associated game data.
     */
    public final void clearBoard()
    {
        for (int row = 0; row < Connect4Constants.ROW_COUNT; row++)
            for (int col = 0; col < Connect4Constants.COLUMN_COUNT; col++)
                board[row][col] = 0;
    }
    
    /** Callback for a game request.
     * 
     * @param playerID ID assigned to this player.
     */
    public void gameStart(int playerID)
    {
        this.playerID = playerID;
    }
    
    /** Callback for updating the board.
     * 
     * @param player Player that placed a piece.
     * @param row Row the piece is placed in.
     * @param column Column the piece is placed in.
     */
    public void boardUpdate(int player, int row, int column)
    {
        board[row][column] = player;
    }
    
    /** Callback for current game status.
     * 
     * @param status Current {@link GameStatus}.
     */
    public void gameStatus(GameStatus status)
    {
        switch (status)
        {
            case NONE:
                takeTurn();
                break;
            case CANCELLED:
                System.out.println("Game cancelled.");
                break;
            case INVALID_MOVE:
                takeTurn();
                break;
            case TIE: 
                System.out.println("Game tied.");
                displayBoard();
                break;
            case PLAYER1_WIN:
                if (playerID == Connect4Constants.PLAYER1)
                    System.out.println("You won!");
                else
                    System.out.println("You lost.");
                displayBoard();
                break;
            case PLAYER2_WIN:
                if (playerID == Connect4Constants.PLAYER2)
                    System.out.println("You won!");
                else
                    System.out.println("You lost.");
                displayBoard();
                break;
        }
    }
    
    /**
     * Manages the progress of the Connect4 game.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Use the GUI (Y/N)? ");
        String choice = " ";
        while (!choice.equalsIgnoreCase("Y") &&
               !choice.equalsIgnoreCase("N"))
        {
            choice = input.nextLine();
        }
        if (choice.equalsIgnoreCase("Y"))
            Application.launch(Connect4GUI.class);
        else
        {
            System.out.println("Begin Game. Enter 'P' if you want to play against" +
                    " another player; enter 'C' to play against a computer.");
            choice = " ";
            while (!choice.equalsIgnoreCase("C") &&
                   !choice.equalsIgnoreCase("P"))
            {
                choice = input.nextLine();
            }
            Connect4TextConsole console =
                    new Connect4TextConsole(choice.equalsIgnoreCase("C"));
            console.playGame();
        }
    }   
}