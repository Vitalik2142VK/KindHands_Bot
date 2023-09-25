package tg.kindhands_bot.kindhands.exceptions;

public class IncorrectDataExceptionAndSendMessage extends RuntimeExceptionAndSendMessage{

    public IncorrectDataExceptionAndSendMessage(String message, String sendMessage) {
        super(message, sendMessage);
    }
}
