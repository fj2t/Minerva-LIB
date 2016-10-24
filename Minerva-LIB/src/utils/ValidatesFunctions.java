package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author fj
 */
public abstract class ValidatesFunctions {

    public static boolean isValidUserChars( String user ) {
        String numberChars = "12345678980"; // No puecde empezar por número
        String validChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"; // tienen que tener un conjunto formado sólo por estos caractéres.
        if( validChars.substring( 0, 10 ).contains( user.substring( 0, 1 ) ) ) return false;
        for( int idx = 0; idx < user.length() ; idx++ ) {
            if ( !validChars.contains( user.substring( idx, idx + 1 ) ) )
                return false;
        }
        return true;
    }

    public static boolean isUserOk( String user ) {
        return ( user.length() > 0 ) && ( user.length() > 7 && user.length() <= 15 ) && isValidUserChars( user );
    }
    
    public static boolean isNickOk( String user ) {
        return ( user.length() > 0 ) && ( user.length() > 7 && user.length() <= 15 ) && isValidUserChars( user );
    }
    
    public static boolean isEmailValid( String email ) {
        String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile( expression,Pattern.CASE_INSENSITIVE );
        Matcher matcher = pattern.matcher( email );
        return matcher.matches();
    }
    
    public static boolean isPassword( char[] pass ) {
        String password = new String( pass );
        return ( password.length() > 0 ) && ( password.length() >= 8 && password.length() <= 20 );
    }
    
    public static boolean isPasswordsOk( char[] pass1, char[] pass2 ) {
        String password1 = new String( pass1 );
        String password2 = new String( pass2 );
        return isPassword( pass1 )&& isPassword( pass2 ) && password1.equals( password2 );
    }    
}
