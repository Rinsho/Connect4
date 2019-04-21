
package core;

import java.util.Random;

/** A naive computer AI to play Connect4
 *
 * @author Jon McKee
 * @version 1.3
 */
public class Connect4ComputerPlayer implements IConnect4Client
{
    private final Random rng;
    private final GameStartCallback startGame;
    private final PiecePlacedCallback placePiece;
    private final GameStatusCallback gameStatus;
    private final Connect4 game;
    
    /**
     * Initializes the computer client.
     * 
     * @param gameStart Callback for when game starts.
     * @param piecePlaced Callback for when a piece is placed.
     * @param gameState Callback for current game state.
     */
    public Connect4ComputerPlayer(
        GameStartCallback gameStart,
        PiecePlacedCallback piecePlaced,
        GameStatusCallback gameState)
    {
        startGame = gameStart;
        placePiece = piecePlaced;
        gameStatus = gameState;
        rng = new Random();
        game = new Connect4();
    }
    
    /** Chooses a column for the computer's turn.
     * 
     * @return Column chosen.
     */
    private int chooseColumn()
    {
        int column = rng.nextInt(Connect4Constants.COLUMN_COUNT);
        while (!game.isValidColumn(column))
        {
            if (column < Connect4Constants.COLUMN_COUNT)
                column++;
            else
                column = 0;
        }
        return column;
    }
    
    /** 
     * Handles the computer's turn.
     */
    private void takeTurn()
    {
        int column = chooseColumn();
        try
        {
            int row = game.addPiece(Connect4Constants.PLAYER2, column);
            placePiece.piecePlaced(Connect4Constants.PLAYER2, row, column);
            gameStatus.update(game.checkStatus());
        }
        catch (InvalidColumnException ex)
        {
            gameStatus.update(GameStatus.PLAYER1_WIN);
            System.err.println(ex);
        }
    }
    
    @Override
    public void requestGame()
    {
        startGame.gameStart(Connect4Constants.PLAYER1);
        gameStatus.update(GameStatus.NONE);
    }
    
    @Override
    public void cancelGame()
    {
        gameStatus.update(GameStatus.CANCELLED);
    }
    
    @Override
    public void addPiece(int column)
    {
        try
        {
            int row = game.addPiece(Connect4Constants.PLAYER1, column);
            placePiece.piecePlaced(Connect4Constants.PLAYER1, row, column);
            GameStatus result = game.checkStatus();
            if (result == GameStatus.NONE)
                takeTurn();
            else
                gameStatus.update(result);    
        }
        catch (InvalidColumnException ex)
        {
            gameStatus.update(GameStatus.INVALID_MOVE);
        }         
    }
}
