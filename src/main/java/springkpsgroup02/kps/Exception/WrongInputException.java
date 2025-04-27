package springkpsgroup02.kps.Exception;

public class WrongInputException extends RuntimeException{
    public WrongInputException(String message) {
        super(message);
    }
}
