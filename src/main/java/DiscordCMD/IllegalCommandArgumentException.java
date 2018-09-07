package DiscordCMD;

public class IllegalCommandArgumentException extends Exception {
    public IllegalCommandArgumentException(String message) {
        super(message);
    }
}
