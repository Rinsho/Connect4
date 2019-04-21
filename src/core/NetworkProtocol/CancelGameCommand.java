
package core.NetworkProtocol;

/** Command for canceling a game.
 *
 * @author Jon McKee
 * @version 1.0
 */
public class CancelGameCommand extends Command implements java.io.Serializable
{
    /**
     * Default constructor.
     */
    public CancelGameCommand()
    {
        super(CommandType.CANCEL_GAME);
    }
}
