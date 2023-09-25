package tg.kindhands_bot.kindhands.exceptions;

public class NullPointerExceptionAndSendMessage extends RuntimeExceptionAndSendMessage{
    public NullPointerExceptionAndSendMessage(String message, String sendMessage) {
        super(message, sendMessage);
    }
}
