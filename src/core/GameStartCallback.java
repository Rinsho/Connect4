
package core;

/** Callback for starting game information.
 *
 * @author Jon McKee
 * @version 1.0
 */
@FunctionalInterface
public interface GameStartCallback
{
    /** Callback for receiving playerID for this game session.
     * 
     * @param playerID PlayerID for this session.
     */
    void gameStart(int playerID);
}
