package Protocol;

/**
 *
 * @author fj
 */
public enum MessageType {
    LOGIN,
    LOGOUT,
    REGISTER,
    ACK;

    static public MessageType fromOrdinal ( int ordinal ) {
        return MessageType.values()[ ordinal ];
    }

}
