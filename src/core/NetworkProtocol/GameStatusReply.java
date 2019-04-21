
package core.NetworkProtocol;

import core.GameStatus;

/** Reply for when a game is finished.
 *
 * @author Jon McKee
 * @version 1.0
 */
public class GameStatusReply extends Reply implements java.io.Serializable
{
    private GameStatus result;
    
    /**
     * Default constructor.
     */
    public GameStatusReply()
    {
        super(ReplyType.GAME_STATUS);
    }
    
    /** Initializes a GameStatusReply object.
     * 
     * @param result Status of the game.
     */
    public GameStatusReply(GameStatus result)
    {
        super(ReplyType.GAME_STATUS);
        this.result = result;
    }
    
    /** Returns the current game status.
     * 
     * @return Current game status.
     */
    public GameStatus getStatus()
    {
        return result;
    }
}
