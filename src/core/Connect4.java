package core;

/**
 * Represents a game of Connect4.
 * 
 * @author Jon McKee
 * @version 1.3
 */
public class Connect4
{
    private static final int WIN_CONDITION_COUNT = 4;   
    private final int[] columnState;
    private final int[][] boardState;
    
    /**
     * Creates a new Connect4 game.
     */
    public Connect4()
    {
        columnState = new int[Connect4Constants.COLUMN_COUNT];
        boardState = 
            new int[Connect4Constants.ROW_COUNT][Connect4Constants.COLUMN_COUNT];
        clear();
    }
    
    /**
     * Check for a full board.
     * 
     * @return Whether the board is full (true) or not (false).
     */
    private boolean isBoardFull()
    {
        for (int i = 0; i < Connect4Constants.COLUMN_COUNT; i++)
            if (columnState[i] < Connect4Constants.ROW_COUNT)
                return false;
        return true;
    }
    
    /** Check if a column is valid.
     * A column is valid if:
     * 1) It isn't full.
     * 2) Is between 0 and COLUMN_COUNT - 1
     * 
     * @param column Column to check.
     * @return Whether the column is valid.
     */
    public boolean isValidColumn(int column)
    {
        return column >= 0 && 
               column < Connect4Constants.COLUMN_COUNT &&
               columnState[column] < Connect4Constants.ROW_COUNT;
    }
    
    /**
     * Adds a piece to the board for a given player.
     * 
     * @param player Player setting the piece.
     * @param column Column to set the piece in (0 to ROW_COUNT - 1)
     * @throws InvalidColumnException Column value is invalid.
     * @return Row piece was added to.
     */
    public int addPiece(int player, int column)
            throws InvalidColumnException
    {    
        if (!isValidColumn(column))
            throw new InvalidColumnException(
                    "Column #" + column + " is full.",
                    column);
        int row = columnState[column];
        boardState[row][column] = player;
        columnState[column]++;
        return row;
    }
    
    /**
     * Checks the board for a winner.
     * 
     * @return Integer representing winner or zero if none.
     */
    private int checkWinner()
    {
        //check rows     
        for (int row = 0; row < Connect4Constants.ROW_COUNT; row++)
        {
            int counter = 1;
            int currentID = boardState[row][0];
            for (int col = 1; col < Connect4Constants.COLUMN_COUNT; col++)
            {
                int comparisonID = boardState[row][col];
                if (comparisonID != 0 && currentID == comparisonID)
                    counter++;
                else
                {
                    counter = 1;
                    currentID = comparisonID;
                }
                if (counter == WIN_CONDITION_COUNT)
                    return currentID;
            }
        }
        
        //check columns
        for (int col = 0; col < Connect4Constants.COLUMN_COUNT; col++)
        {
            int counter = 1;
            int currentID = boardState[0][col];
            for (int row = 1; row < Connect4Constants.ROW_COUNT; row++)
            {
                int comparisonID = boardState[row][col];
                if (comparisonID != 0 && currentID == comparisonID)
                    counter++;
                else
                {
                    counter = 1;
                    currentID = comparisonID;
                }
                if (counter == WIN_CONDITION_COUNT)
                    return currentID;
            }
        }
        
        //check upwards diagonals
        for (int row = 2; row >= 0; row--)
        {
            int counter = 1;
            int currentID = boardState[row][0];
            for (int tempRow = row + 1, col = 1; 
                 tempRow < Connect4Constants.ROW_COUNT; 
                 tempRow++, col++)
            {
                int comparisonID = boardState[tempRow][col];
                if (comparisonID != 0 && currentID == comparisonID)
                    counter++;
                else
                {
                    counter = 1;
                    currentID = comparisonID;
                }
                if (counter == WIN_CONDITION_COUNT)
                    return currentID;
            }
        }
        for (int col = 3; col > 0; col--)
        {
            int counter = 1;
            int currentID = boardState[0][col];
            for (int tempCol = col + 1, row = 1; 
                 tempCol < Connect4Constants.COLUMN_COUNT; 
                 tempCol++, row++)
            {
                int comparisonID = boardState[row][tempCol];
                if (comparisonID != 0 && currentID == comparisonID)
                    counter++;
                else
                {
                    counter = 1;
                    currentID = comparisonID;
                }
                if (counter == WIN_CONDITION_COUNT)
                    return currentID;
            }
        }
        
        //check downwards diagonals
        for (int col = 3; col > 0; col--)
        {
            int counter = 1;
            int currentID = boardState[5][col];
            for (int tempCol = col + 1, row = 4; 
                 tempCol < Connect4Constants.COLUMN_COUNT; 
                 tempCol++, row--)
            {
                int comparisonID = boardState[row][tempCol];
                if (comparisonID != 0 && currentID == comparisonID)
                    counter++;
                else
                {
                    counter = 1;
                    currentID = comparisonID;
                }
                if (counter == WIN_CONDITION_COUNT)
                    return currentID;
            }
        }
        for (int row = 3; row < Connect4Constants.ROW_COUNT; row++)
        {
            int counter = 1;
            int currentID = boardState[row][0];
            for (int tempRow = row - 1, col = 1; tempRow >= 0; tempRow--, col++)
            {
                int comparisonID = boardState[tempRow][col];
                if (comparisonID != 0 && currentID == comparisonID)
                    counter++;
                else
                {
                    counter = 1;
                    currentID = comparisonID;
                }
                if (counter == WIN_CONDITION_COUNT)
                    return currentID;
            }
        }
        
        return 0;
    }
    
    /** Checks the current board and returns any defined result.
     * 
     * @return The {@link GameStatus} of the current board.
     */
    public GameStatus checkStatus()
    {
        GameStatus result = GameStatus.NONE;
        int winner = checkWinner();
        if (winner == Connect4Constants.PLAYER1)
            result = GameStatus.PLAYER1_WIN;
        else if (winner == Connect4Constants.PLAYER2)
            result = GameStatus.PLAYER2_WIN;
        else if (isBoardFull())
            result = GameStatus.TIE;
        return result;
    }
    
    /**
     * Clears the game board.
     */
    public final void clear()
    {
        for (int row = 0; row < Connect4Constants.ROW_COUNT; row++)
            for (int col = 0; col < Connect4Constants.COLUMN_COUNT; col++)
                boardState[row][col] = 0;
        for (int i = 0; i < Connect4Constants.COLUMN_COUNT; i++)
            columnState[i] = 0;
    }
    
    /**
     * Returns the current board state.
     * 
     * @return The board state.
     */
    public int[][] getBoard()
    {
        return boardState;
    }
}