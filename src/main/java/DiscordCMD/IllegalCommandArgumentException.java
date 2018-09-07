package DiscordCMD;

public class IllegalCommandArgumentException extends Exception {
    IllegalCommandArgumentException(String message) {
        super(message);
    }
}
