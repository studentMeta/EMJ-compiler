package be.unamur.info.b314.compiler.exception;



/**
 * Exception class representing parsing errors.
 * @author James Ortiz
 */
public class ParsingException extends Exception {

    public ParsingException(String message, Exception e) {
        super(message, e);
    }
    
    public ParsingException(String message){
        super(message);
    }

}
