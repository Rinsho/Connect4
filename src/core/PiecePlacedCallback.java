
package core;

/** Callback for when a piece is placed on the board.
 *
 * @author Jon McKee
 * @version 1.0
 */
@FunctionalInterface
public interface PiecePlacedCallback
{
    /** Handles a piece being placed on the board.
     * 
     * @param player Player that placed the piece.
     * @param row Row the piece is placed in.
     * @param column Column the piece is placed in.
     */
    void piecePlaced(int player, int row, int column);
}
