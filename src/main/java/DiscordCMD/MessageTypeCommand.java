package DiscordCMD;

import java.util.Collection;
import java.util.Scanner;

public abstract class MessageTypeCommand extends Command {
    public MessageTypeCommand(String name, String template, String info, PermissionLevel... allowedUsers) {
        super (name, template, info, allowedUsers);
    }

    public MessageTypeCommand(String name, String template, String info, Collection<PermissionLevel> allowedUsers) {
        super (name, template, info, allowedUsers);
    }

    @Override
    protected String processMessage (Scanner args, MessageEvent message) {
        if (message.isPrivateMessage()) {
            return this.processDM(args, message);
        } else {
            return this.processServerMessage(args, message);
        }
    }

    protected String processDM (Scanner args, MessageEvent message) {
        return "You can't do that in DMs, use a server.";
    }

    protected String processServerMessage (Scanner args, MessageEvent message) {
        return "You can't do that in a server, use DMs.";
    }
}
