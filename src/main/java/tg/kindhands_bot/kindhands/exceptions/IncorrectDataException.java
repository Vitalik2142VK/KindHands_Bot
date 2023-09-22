package tg.kindhands_bot.kindhands.exceptions;

public class IncorrectDataException extends RuntimeException{
    public IncorrectDataException() {
    }

    public IncorrectDataException(String message) {
        super(message);
    }
}
