package tg.kindhands_bot.kindhands.exceptions;

public abstract class RuntimeExceptionAndSendMessage extends RuntimeException{

    protected String sendMessage;

    public RuntimeExceptionAndSendMessage(String message, String sendMessage) {
        super(message);
        this.sendMessage = sendMessage;
    }

    public String getSendMessage() {
        return sendMessage;
    }
}
