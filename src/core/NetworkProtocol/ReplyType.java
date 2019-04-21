
package core.NetworkProtocol;

/** The types of replies supported by the Connect4 protocol.
 *
 * @author Jon McKee
 * @version 1.0
 */
public enum ReplyType implements java.io.Serializable
{
    GAME_START,
    GAME_STATUS,
    BOARD_UPDATE
}
