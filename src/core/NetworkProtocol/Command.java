
package core.NetworkProtocol;

/** Base class for network commands.
 *
 * @author Jon McKee
 * @version 1.0
 */
public abstract class Command implements java.io.Serializable
{
    private final CommandType command;
    
    /** Initializes command type.
     * 
     * @param command {@link CommandType} of this command. 
     */
    public Command(CommandType command)
    {
        this.command = command;
    }
    
    /** Return the {@link CommandType} of this command.
     * 
     * @return {@link CommandType} of this command.
     */
    public CommandType getCommand()
    {
        return command;
    }
}
