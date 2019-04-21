
package core;

/** Interface that represents a Connect4 client.
 * 
 * @author Jon McKee
 * @version 1.0
 */
public interface IConnect4Client
{
    /**
     * Sends a request for a game.
     */
    void requestGame();
    
    /**
     * Cancels a game in progress.
     */
    void cancelGame();
    
    /** Adds a piece to the game board.
     * 
     * @param column Column to add piece in.
     */
    void addPiece(int column);
}
