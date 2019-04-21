
package core.NetworkProtocol;

/** Reply to update board status.
 *
 * @author Jon McKee
 * @version 1.0
 */
public class BoardUpdateReply extends Reply
{
    private int row;
    private int column;
    private int player;
    
    /**
     * Default constructor.
     */
    public BoardUpdateReply()
    {
        super(ReplyType.BOARD_UPDATE);
    }
    
    /** Initializes reply object.
     * 
     * @param player Player placing the piece.
     * @param row Row the piece is placed in.
     * @param column Column the piece is placed in.
     */
    public BoardUpdateReply(int player, int row, int column)
    {
        super(ReplyType.BOARD_UPDATE);
        this.row = row;
        this.column = column;
        this.player = player;
    }
    
    /** Returns the row to place piece in.
     * 
     * @return Row to place piece in.
     */
    public int getRow()
    {
        return row;
    }
    
    /** Returns the column to place piece in.
     * 
     * @return Column to place piece in.
     */
    public int getColumn()
    {
        return column;
    }
    
    /** Returns the player placing the piece.
     * 
     * @return Player placing the piece.
     */
    public int getPlayer()
    {
        return player;
    }
}
