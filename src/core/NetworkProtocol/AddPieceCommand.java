
package core.NetworkProtocol;

/** Command for adding a piece to the board.
 *
 * @author Jon McKee
 * @version 1.0
 */
public class AddPieceCommand extends Command implements java.io.Serializable
{
    private int column;
    
    /**
     * Default constructor.
     */
    public AddPieceCommand()
    {
        super(CommandType.ADD_PIECE);
    }
    
    /** Initializes the command.
     * 
     * @param column Column to place piece in.
     */
    public AddPieceCommand(int column)
    {
        super(CommandType.ADD_PIECE);
        this.column = column;
    }
    
    /** Returns the column to add piece to.
     * 
     * @return Column to add piece to.
     */
    public int getColumn()
    {
        return column;
    }
}
