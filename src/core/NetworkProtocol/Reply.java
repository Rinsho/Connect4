
package core.NetworkProtocol;

/** Base class for Connect4 network server replies.
 *
 * @author Jon McKee
 * @version 1.0
 */
public abstract class Reply implements java.io.Serializable
{
    private final ReplyType reply;
    
    /** Initializes reply type.
     * 
     * @param reply {@link ReplyType} of this command. 
     */
    public Reply(ReplyType reply)
    {
        this.reply = reply;
    }
    
    /** Return the {@link ReplyType} of this reply.
     * 
     * @return {@link ReplyType} of this reply.
     */
    public ReplyType getReply()
    {
        return reply;
    }
}
