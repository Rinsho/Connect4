
package core;

/** A callback for Connect4 game state information.
 *
 * @author Jon McKee
 * @version 1.0
 */
@FunctionalInterface
public interface GameStatusCallback
{
    /** Handles game over updates.
     * 
     * @param status Status with current game state.
     */
    void update(GameStatus status);
}
