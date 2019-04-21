
package core;

import java.net.*;
import java.io.*;
//import core.*;
import core.NetworkProtocol.*;

/** Connect4Server stub for client testing.
 *
 * @author Jon McKee
 * @version 1.0
 */
public class Connect4ServerStub
{  
    public static final int PLAYER_NUMBER = Connect4Constants.PLAYER1;
    public static final int ROW_RETURN = 0;
    private boolean testActive;
    private final Thread main;
    private boolean blocking;
    private ServerSocket server = null;
    Socket client = null;
    ObjectInputStream input = null;
    ObjectOutputStream output = null;
    
    public void waitForReady()
    {
        blocking = true;
        while (blocking) {}
    }
    
    public Connect4ServerStub(int port)
    {
        testActive = true;
        main = new Thread(() -> {
            
            try
            {
                server = new ServerSocket(port);
                client = server.accept();
                input = new ObjectInputStream(client.getInputStream());
                output = new ObjectOutputStream(client.getOutputStream());

                output.writeObject(new GameStartReply(PLAYER_NUMBER));

                while(testActive)
                {
                    blocking = false;
                    Command cmd = (Command) input.readObject();
                    switch (cmd.getCommand())
                    {
                        case ADD_PIECE:
                            AddPieceCommand piece = (AddPieceCommand) cmd;
                            output.writeObject(
                                new BoardUpdateReply(
                                        PLAYER_NUMBER,
                                        ROW_RETURN,
                                        piece.getColumn()));
                            output.writeObject(
                                    new GameStatusReply(GameStatus.NONE));
                            break;
                        default:
                            output.writeObject(
                                    new GameStatusReply(GameStatus.CANCELLED));
                            break;
                    }
                }
            }
            catch (Exception ex)
            {
                System.out.println("Thrown from stub.");
                System.out.println(ex);
            }
            finally
            {
                try
                {
                    if (input != null)
                        input.close();
                    if (output != null)
                        output.close();
                    if (client != null)
                        client.close();
                    server.close();
                }
                catch (IOException ex)
                {
                    System.out.println(ex);
                }
            }
        });
        main.start();
    }
    
    public void close()
    {
        testActive = false;
        main.interrupt();
    }
    
    public Thread getThread()
    {
        return main;
    }
}
