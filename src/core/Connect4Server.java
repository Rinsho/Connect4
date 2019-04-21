
package core;

import java.net.*;
import java.io.*;
import java.util.Queue;
import java.util.LinkedList;
import core.NetworkProtocol.*;

/** Connect4 network server.
 *
 * @author Jon McKee
 * @version 1.0
 */
public class Connect4Server
{
    class Session implements Runnable
    {
        private final Socket player1;
        private final Socket player2;
        private final Connect4 game;
        private ObjectInputStream inputPlayer1;
        private ObjectInputStream inputPlayer2;
        private ObjectOutputStream outputPlayer1;
        private ObjectOutputStream outputPlayer2;
        
        /** Creates a game session.
         * 
         * @param player1 First player's socket.
         * @param player2 Second player's socket.
         */
        public Session(Socket player1, Socket player2)
        {
            this.player1 = player1;
            this.player2 = player2;
            this.game = new Connect4();
        }
        
        /** Handles stream operations.
         * 
         * @param input Input stream for operations.
         * @param output Primary output stream (active).
         * @param output2 Secondary output stream (non-active).
         */
        private void handleStream(int playerID)
        {
            ObjectInputStream input;
            ObjectOutputStream playerOutput;
            ObjectOutputStream opponentOutput;
            try
            {
                if (playerID == Connect4Constants.PLAYER1)
                {
                    input = inputPlayer1;
                    playerOutput = outputPlayer1;
                    opponentOutput = outputPlayer2;
                }
                else
                {
                    input = inputPlayer2;
                    playerOutput = outputPlayer2;
                    opponentOutput = outputPlayer1;
                }
                while (true)
                {
                    Command clientCommand = (Command) input.readObject();
                    if (clientCommand.getCommand() == CommandType.ADD_PIECE)
                    {
                        try
                        {
                            AddPieceCommand command = 
                                    (AddPieceCommand) clientCommand;
                            int column = command.getColumn();
                            int row = 
                                game.addPiece(playerID, column);
                            Reply reply = 
                                new BoardUpdateReply(playerID, row, column);
                            playerOutput.writeObject(reply);
                            opponentOutput.writeObject(reply);
                            GameStatus result = game.checkStatus();
                            //Game finished.
                            if (result != GameStatus.NONE)
                            {
                                playerOutput.writeObject(new GameStatusReply(result));
                                opponentOutput.writeObject(new GameStatusReply(result));
                                break;
                            }
                            opponentOutput.writeObject(new GameStatusReply(result));

                        }
                        catch (InvalidColumnException ex)
                        {
                            playerOutput.writeObject(
                                    new GameStatusReply(GameStatus.INVALID_MOVE));
                        }
                    }
                    else
                    {
                        opponentOutput.writeObject(
                                new GameStatusReply(GameStatus.CANCELLED));
                        break;
                    }
                }
            }
            catch (EOFException ex)
            {
                System.out.println("Session closed.");
            }
            catch (Exception ex)
            {
                System.err.println(ex);
            }
        }
        
        @Override
        public void run()
        {
            try
            {
                inputPlayer1 = 
                        new ObjectInputStream(player1.getInputStream());
                outputPlayer1 = 
                        new ObjectOutputStream(player1.getOutputStream());
                inputPlayer2 =
                        new ObjectInputStream(player2.getInputStream());
                outputPlayer2 =
                        new ObjectOutputStream(player2.getOutputStream());
                
                //Send messages to start game.
                outputPlayer2.writeObject(
                    new GameStartReply(Connect4Constants.PLAYER2));
                outputPlayer1.writeObject(
                    new GameStartReply(Connect4Constants.PLAYER1));
                outputPlayer1.writeObject(
                    new GameStatusReply(GameStatus.NONE));
                
                //Spin up listeners.
                //Player2
                Thread player2Stream = new Thread(() -> 
                    handleStream(Connect4Constants.PLAYER2)
                );
                player2Stream.start();
                //Player1
                handleStream(Connect4Constants.PLAYER1);
                player2Stream.join();
            }
            catch (Exception ex)
            {
                System.err.println(ex);
            }
            finally
            {
                try
                {
                    inputPlayer1.close();
                    inputPlayer2.close();
                    outputPlayer1.close();
                    outputPlayer2.close();
                    player1.close();
                    player2.close();
                }
                catch (IOException ex)
                {
                    System.err.println(ex);
                }
            }
        }
    }
    
    /** Initializes the Connect4 server.
     * 
     * @param port Port to run the server on.
     */
    public Connect4Server(int port)
    {
        Queue<Socket> playerQueue = new LinkedList<>();
        try
        {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true)
            {
                Socket socket = serverSocket.accept();
                playerQueue.add(socket);
                System.out.println("Client connected.");
                if (playerQueue.size() >= 2)
                {
                    Socket firstPlayer = playerQueue.poll();
                    Socket secondPlayer = playerQueue.poll();
                    Session task1 = 
                        new Session(firstPlayer, secondPlayer);
                    new Thread(task1).start();                           
                    System.out.println("Game started.");
                }
            }
        }
        catch (IOException ex)
        {
            System.err.println(ex);
        }
    }
    
    public static void main(String[] args)
    {
        Connect4Server server = new Connect4Server(8000);
    }
}
