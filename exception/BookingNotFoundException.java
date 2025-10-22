package exception;

public class BookingNotFoundException extends Exception{
    public BookingNotFoundException(String message){
        super(message);
    }
}
