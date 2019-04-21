
package core.NetworkProtocol;

/** Reply for starting a game.
 *
 * @author Jon McKee
 * @version 1.0
 */
public class GameStartReply extends Reply implements java.io.Serializable
{
    private int playerID;
    
    /**
     * Default constructor.
     */
    public GameStartReply()
    {
        super(ReplyType.GAME_START);
    }
    
    /** Initializes a GameStartReply object.
     * 
     * @param playerID Player ID for the current game.
     */
    public GameStartReply(int playerID)
    {
        super(ReplyType.GAME_START);
        this.playerID = playerID;
    }
    
    /** Returns the player ID.
     * 
     * @return Player ID.
     */
    public int getPlayer()
    {
        return playerID;
    }
}
