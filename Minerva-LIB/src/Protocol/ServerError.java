package Protocol;

/**
 * Errores del servidor. Medianamente son iguales a los de hundir la flota. Como
 * no hemos definido completamente el juego, por ahora solo manejamos el error
 * de crear mundo.
 *
 * @author fj
 */
public enum ServerError {
    NO_ERROR,
    CREATE_SESSION,
    BAD_AUTH;
}
