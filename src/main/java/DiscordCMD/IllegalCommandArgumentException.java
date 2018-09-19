package DiscordCMD;

/**
 * New exception for user-input related errors. Throwing this from inside any command execution will result in the
 * included message being displayed to the user as an error message.
 */
public class IllegalCommandArgumentException extends Exception {
    public IllegalCommandArgumentException(String message) {
        super(message);
    }
}
