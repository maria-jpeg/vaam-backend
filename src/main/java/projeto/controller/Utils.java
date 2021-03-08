package projeto.controller;

import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import projeto.core.User;

//import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utils {

    public static DateTime parseDate( String dateString )
    {
        if( dateString == null )
            return null;

        DateTime dt = null;
        String[] formatesMinus = { "dd-MM-yyyy HH:mm:ss.SSSSSSS", "yyyy-MM-dd HH:mm:ss.SSSSSSS", "dd-MM-yyyy" };
        String[] formatesSlash = { "dd/MM/yyyy HH:mm:ss.SSSSSSS", "yyyy/MM/dd HH:mm:ss.SSSSSSS" };

        boolean hasMinus = dateString.indexOf( '-' ) != -1;
        boolean hasSlash = dateString.indexOf( '/' ) != -1;

        if( !hasMinus && !hasSlash )
            throw new IllegalArgumentException( "Formato de data inválido: '" + dateString + "'" );

        String [] formats = hasMinus && !hasSlash ? formatesMinus : formatesSlash;
        for( String format : formats )
        {
            try {
                dt = DateTime.parse( dateString,
                        DateTimeFormat.forPattern( format ) );
                // Valid date
                return dt;
            } catch (Exception ignore) {}
        }

        try {
            dt = new DateTime( dateString );
            // Valid date
            return dt;
        } catch (Exception ignore) {}


        throw new IllegalArgumentException( "Formato de data inválido: '" + dateString + "'" );


        //DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        //DateTime dt = formatter.parseDateTime( dateString );
        //DateTimeFormatter formatter = ISODateTimeFormat.dateTime();

    }

    public static String hashPassword(String password) {
        char[] encoded = null;
        try {
            ByteBuffer passwdBuffer =
                    Charset.defaultCharset().encode(CharBuffer.wrap(password));
            byte[] passwdBytes = passwdBuffer.array();
            MessageDigest mdEnc = MessageDigest.getInstance("SHA-256");
            mdEnc.update(passwdBytes, 0, password.toCharArray().length);
            encoded = new BigInteger(1, mdEnc.digest()).toString(16).toCharArray();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new String(encoded);
    }


    public static boolean isEmailValid(String email)
    {
        if( email == null )
            return false;

        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(regExpn,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        return matcher.matches();
    }


    /*
    public static void printMatrix( Object[][] matrix )
    {
        StringBuilder toReturn = new StringBuilder("  ");
        for (int i = 0; i < matrix.length; i++) {
            toReturn.append( i ).append(' ');
        }

        toReturn.append('\n');
        for (int i = 0; i < matrix.length; i++) {
            toReturn.append( i ).append(' ');
            for (int j = 0; j < matrix.length; j++) {
                toReturn.append(matrix[i][j].toString() ).append(' ');
            }

            toReturn.append('\n');
        }

        System.out.println( toReturn.toString() );
    }
    */

}
