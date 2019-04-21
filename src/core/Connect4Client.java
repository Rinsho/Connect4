
package core;

import java.net.*;
import java.io.*;
import core.NetworkProtocol.*;

/** Connect4 network client.
 *
 * @author Jon McKee
 * @version 1.0
 */
public class Connect4Client implements IConnect4Client
{
    private final String host = "localhost";
    private int port = 8000;
    private final GameStartCallback gameStart;
    private final PiecePlacedCallback piecePlaced;
    private final GameStatusCallback gameState;
    private Socket socket;
    private ObjectOutputStream output;
    private boolean waiting;
    
    /** Creates a Connect4 client instance.
     * 
     * @param gameStart Callback for when a game is requested.
     * @param piecePlaced Callback for when a piece is placed.
     * @param gameState Callback for current game status.
     */
    public Connect4Client(
            GameStartCallback gameStart,
            PiecePlacedCallback piecePlaced,
            GameStatusCallback gameState)
    {
        this.gameStart = gameStart;
        this.piecePlaced = piecePlaced;
        this.gameState = gameState;    
    }

    public void setPort(int port)
    {
        this.port = port;
    }
    
    public void waitForCallback()
    {
        waiting = true;
        while (waiting) {}
    }
    
    @Override
    public void requestGame()
    {
        if (socket != null && !socket.isClosed())
            return;
        try
        {
            socket = new Socket(host, port);
            output = new ObjectOutputStream(socket.getOutputStream());
            
            new Thread(() -> {   
                try
                {
                    ObjectInputStream input = 
                            new ObjectInputStream(socket.getInputStream());
                    threadLoop: while (true)
                    {
                        Reply reply = (Reply) input.readObject();
                        switch (reply.getReply())
                        {
                            case GAME_START:
                                GameStartReply startReply = (GameStartReply) reply;
                                gameStart.gameStart(startReply.getPlayer());
                                waiting = false;
                                break;
                            case GAME_STATUS:
                                GameStatusReply statusReply = (GameStatusReply) reply;
                                GameStatus status = statusReply.getStatus();
                                gameState.update(statusReply.getStatus());
                                waiting = false;
                                if (status == GameStatus.CANCELLED ||
                                    status == GameStatus.TIE ||
                                    status == GameStatus.PLAYER1_WIN ||
                                    status == GameStatus.PLAYER2_WIN)
                                {
                                    break threadLoop;
                                }
                                break;
                            case BOARD_UPDATE:
                                BoardUpdateReply boardReply = (BoardUpdateReply) reply;
                                piecePlaced.piecePlaced(
                                        boardReply.getPlayer(), 
                                        boardReply.getRow(),
                                        boardReply.getColumn());
                                waiting = false;
                                break;
                        }
                    }   
                }  
                catch (Exception ex)
                {
                    System.out.println("Thrown from client.");
                    ex.printStackTrace();
                    System.err.println(ex);
                }
                finally
                {
                    try
                    {
                        output.close();
                        socket.close();
                    }
                    catch (IOException ex)
                    {
                        System.err.println(ex);
                    }
                }
            }).start();
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
    }

    @Override
    public void cancelGame()
    {
        try
        {
            if (socket == null || socket.isClosed())
                return;
            output.writeObject(new CancelGameCommand());
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
    }

    @Override
    public void addPiece(int column)
    {
        try
        {
            if (socket == null || socket.isClosed())
                return;
            output.writeObject(new AddPieceCommand(column));
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
    }  
}
