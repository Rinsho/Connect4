
package core;

import core.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;
import java.net.ConnectException;

/** Test class for Connect4Client.
 *
 * @author Jon McKee
 * @version 1.0
 */
@FixMethodOrder(MethodSorters.DEFAULT)
public class Connect4ClientTest
{
    private Connect4Client client;
    private Connect4ServerStub stub;
    private int playerID;
    private int row;
    private int column;
    private GameStatus status;
    private int defaultPort = 8000;
    
    void gameStart(int player)
    {
        playerID = player;
    }
    
    void gameStatusUpdate(GameStatus status)
    {
        this.status = status;
    }
    
    void piecePlacedUpdate(int player, int row, int column)
    {
        playerID = player;
        this.row = row;
        this.column = column;
    }
    
    @Before
    public void setUp()
    {
        client = new Connect4Client(
                this::gameStart,
                this::piecePlacedUpdate,
                this::gameStatusUpdate);
        playerID = 0;
        row = -1;
        column = -1;
        status = GameStatus.NONE;
    }
    
    @After
    public void tearDown()
    {   
        if (stub != null)
        {
            stub.close();
            stub = null;
        }
        client = null;
        playerID = 0;
        row = -1;
        column = -1;
        status = GameStatus.NONE;
    }
    
    @Test
    public void testRequestGame_ServerStarted()
    {
        int port = 8006;
        client.setPort(port);
        stub = new Connect4ServerStub(port);
        //stub.waitForReady();
        client.requestGame();  
        //client.waitForCallback();
        assertEquals(Connect4ServerStub.PLAYER_NUMBER, playerID);
    }

    @Test
    public void testCancelGame_NoServer()
    {
        client.cancelGame();
        assertEquals(GameStatus.NONE, status);
    }
    
    @Test
    public void testCancelGame_PreRequest()
    {
        int port = 8001;
        client.setPort(port);
        stub = new Connect4ServerStub(port);
        client.cancelGame();
        assertEquals(GameStatus.NONE, status);
    }
    
    @Test
    public void testCancelGame_PostRequest()
    {
        int port = 8002;
        client.setPort(port);
        stub = new Connect4ServerStub(port);
        //stub.waitForReady();
        client.requestGame();
        //client.waitForCallback();
        client.cancelGame();
        //client.waitForCallback();
        assertEquals(GameStatus.CANCELLED, status);
    }

    @Test
    public void testAddPiece_NoServer()
    {
        //Set to impossible status
        status = GameStatus.TIE;
        client.addPiece(0);
        assertEquals(GameStatus.TIE, status);
    }
    
    @Test
    public void testAddPiece_PreRequest()
    {
        int port = 8003;
        client.setPort(port);
        stub = new Connect4ServerStub(port);
        status = GameStatus.TIE; //set to impossible status
        client.addPiece(0);
        assertEquals(GameStatus.TIE, status);
    }
    
    @Test
    public void testAddPiece_PostRequest()
    {
        int port = 8004;
        client.setPort(port);
        stub = new Connect4ServerStub(port);
        int col = 0;
        client.requestGame();
        client.addPiece(col);
        assertEquals(Connect4ServerStub.PLAYER_NUMBER, playerID);
        assertEquals(Connect4ServerStub.ROW_RETURN, row);
        assertEquals(col, column);
        assertEquals(GameStatus.NONE, status);
    }
}
