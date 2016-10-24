package Protocol;

/**
 *
 * @author fj
 */
public enum InterlocutorType {
    CLIENT,
    SERVER;

    static public InterlocutorType fromOrdinal ( int ordinal ) {
        return InterlocutorType.values()[ ordinal ];
    }

}

