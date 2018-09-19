package DiscordCMD;

import java.util.Collection;
import java.util.Scanner;

/**
 * A subtype of command offering separate overridable methods for when a command is invoked in a private channel
 * or in a server.
 */
public abstract class MessageTypeCommand extends Command {
    public MessageTypeCommand(String name, String template, String info, PermissionLevel... allowedUsers) {
        super (name, template, info, allowedUsers);
    }

    public MessageTypeCommand(String name, String template, String info, Collection<PermissionLevel> allowedUsers) {
        super (name, template, info, allowedUsers);
    }

    @Override
    protected String processMessage (Scanner args, MessageEvent message) throws IllegalCommandArgumentException {
        if (message.isPrivateMessage()) {
            return this.processDM(args, message);
        } else {
            return this.processServerMessage(args, message);
        }
    }

    /**
     * Called when the command is invoked from a direct message - override to create your command.
     * @param args a Scanner containing the arguments to the command
     * @param message the message that caused the invocation
     * @return the string to display to the user
     * @throws IllegalCommandArgumentException if the user input was invalid - the message will be displayed as an error
     * message to the user
     */
    protected String processDM (Scanner args, MessageEvent message) throws IllegalCommandArgumentException {
        return "You can't do that in DMs, use a server.";
    }

    /**
     * Called when the command is invoked from a server - override to create your command.
     * @param args a Scanner containing the arguments to the command
     * @param message the message that caused the invocation
     * @return the string to display to the user
     * @throws IllegalCommandArgumentException if the user input was invalid - the message will be displayed as an error
     * message to the user
     */
    protected String processServerMessage (Scanner args, MessageEvent message) throws IllegalCommandArgumentException {
        return "You can't do that in a server, use DMs.";
    }
}
