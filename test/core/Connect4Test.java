
package core;

import core.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/** Test class for Connect4.java
 *
 * @author Jon McKee
 * @version 1.0
 */
public class Connect4Test
{ 
    Connect4 board;
    
    @Before
    public void setUp()
    {
        board = new Connect4();
    }
    
    @After
    public void tearDown()
    {
        board = null;
    }

    @Test
    public void testIsValidColumn()
    {
        assertFalse(board.isValidColumn(-1));
        assertTrue(board.isValidColumn(0));
        assertTrue(board.isValidColumn(Connect4Constants.COLUMN_COUNT - 1));
        assertFalse(board.isValidColumn(Connect4Constants.COLUMN_COUNT));
        try
        {
            for (int i = 0; i < Connect4Constants.ROW_COUNT; i++)
                board.addPiece(Connect4Constants.PLAYER1, 0);
        }
        catch (InvalidColumnException ex)
        {
            System.out.println(ex);
        }
        assertFalse(board.isValidColumn(0));
    }

    @Test
    public void testAddPiece()
    {
        try
        {
            board.addPiece(Connect4Constants.PLAYER1, 0);
            int expected = 1;
            int actual = board.addPiece(Connect4Constants.PLAYER1, 0);
            assertEquals(actual, expected);
        }
        catch (InvalidColumnException ex)
        {
            System.out.println(ex);
        }
        
        try
        {
            //Purposefully cause exception
            for (int i = 0; i <= Connect4Constants.ROW_COUNT; i++)
                board.addPiece(Connect4Constants.PLAYER2, 1);
            fail();
        }
        catch (InvalidColumnException ex)
        {
            assertNotNull(ex);
        }
    }

    @Test
    public void testCheckStatus()
    {
        try
        {
            board.addPiece(Connect4Constants.PLAYER1, 0);
            GameStatus expected = GameStatus.NONE;
            GameStatus actual = board.checkStatus();
            assertEquals(expected, actual);
            
            for (int i = 1; i < 4; i++)
                board.addPiece(Connect4Constants.PLAYER1, i);
            expected = GameStatus.PLAYER1_WIN;
            actual = board.checkStatus();
            assertEquals(expected, actual);
            
            board = null;
            board = new Connect4();
            for (int i = 3; i < Connect4Constants.COLUMN_COUNT; i++)
                board.addPiece(Connect4Constants.PLAYER2, i);
            expected = GameStatus.PLAYER2_WIN;
            actual = board.checkStatus();
            assertEquals(expected, actual);
            
            board = null;
            board = new Connect4();
            for (int z = 0; z < Connect4Constants.ROW_COUNT; z++)
            {
                if (z % 3 != 0)
                {
                    for (int i = 0; i < Connect4Constants.COLUMN_COUNT; i++)
                    {
                        if (i % 2 == 0)
                            board.addPiece(Connect4Constants.PLAYER1, i);
                        else
                            board.addPiece(Connect4Constants.PLAYER2, i);
                    }
                }
                else {
                    for (int i = 0; i < Connect4Constants.COLUMN_COUNT; i++)
                    {
                        if (i % 2 == 0)
                            board.addPiece(Connect4Constants.PLAYER2, i);
                        else
                            board.addPiece(Connect4Constants.PLAYER1, i);
                    }
                }
            }
            expected = GameStatus.TIE;
            actual = board.checkStatus();
            assertEquals(expected, actual);
        }
        catch (InvalidColumnException ex)
        {
            System.out.println(ex);
        }
    }

    @Test
    public void testClear()
    {
        try
        {
            board.addPiece(Connect4Constants.PLAYER1, 1);
            board.addPiece(Connect4Constants.PLAYER2, 2);
        }
        catch (InvalidColumnException ex)
        {
            System.out.println(ex);
        }
        int[][] expResult = 
            new int[Connect4Constants.ROW_COUNT][Connect4Constants.COLUMN_COUNT];
        for (int i = 0; i < Connect4Constants.ROW_COUNT; i++)
            for (int j = 0; j < Connect4Constants.COLUMN_COUNT; j++)
                expResult[i][j] = 0;
        board.clear();
        assertArrayEquals(expResult, board.getBoard());
    }

    @Test
    public void testGetBoard()
    {
        try
        {
            board.addPiece(Connect4Constants.PLAYER1, 1);
            board.addPiece(Connect4Constants.PLAYER2, 2);
        }
        catch (InvalidColumnException ex)
        {
            System.out.println(ex);
        }
        int[][] expResult = 
            new int[Connect4Constants.ROW_COUNT][Connect4Constants.COLUMN_COUNT];
        for (int i = 0; i < Connect4Constants.ROW_COUNT; i++)
            for (int j = 0; j < Connect4Constants.COLUMN_COUNT; j++)
            {
                if (i == 0)
                {
                    if (j == 1)
                        expResult[i][j] = Connect4Constants.PLAYER1;
                    else if (j == 2)
                        expResult[i][j] = Connect4Constants.PLAYER2;
                }
                else
                    expResult[i][j] = 0;
            }
        int[][] result = board.getBoard();
        assertArrayEquals(expResult, result);
    }
    
}
