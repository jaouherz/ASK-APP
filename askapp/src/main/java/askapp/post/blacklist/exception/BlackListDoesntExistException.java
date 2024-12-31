package askapp.post.blacklist.exception;

public class BlackListDoesntExistException extends RuntimeException{
    public BlackListDoesntExistException(String message) {
        super(message);
    }
}
