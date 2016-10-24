package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author fj
 */
public abstract class MD5 {

    public static byte[] sum( String string ) throws NoSuchAlgorithmException {
        byte[] digest = null;
        
        MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );
        messageDigest.reset();
        digest = messageDigest.digest( string.getBytes() );
        return digest;
    }
    
    public static String strigSum( String string ) throws NoSuchAlgorithmException {
        return new String( sum( string ) );
    }
    
    public static String ASCIIString( String string ) throws NoSuchAlgorithmException {
        byte[] digest = sum( string );
        String ASCIISum = "";
        for ( int idx = 0; idx < digest.length; idx++ ) {
            ASCIISum += ( char ) ( 0x00FF & digest[ idx ] );
        }
        return ASCIISum;
    }
    
}
