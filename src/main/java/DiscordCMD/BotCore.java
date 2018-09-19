package DiscordCMD;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.*;

/**
 * Core class for command management. Methods can be overridden as ListenerAdapter, save for onMessageReceived and
 * onMessageUpdate, which should instead be altMessageReceived and altMessageUpdate, which are invoked if no command
 * is detected.
 */
public class BotCore extends ListenerAdapter {
    private String prefix = "!";
    private JDA self;

    private Map<String, Command> commands;

    /**
     * Creates a new BotCore connected to the account associated with the given token.
     *
     * @param self a JDA object with to which this listener will be attached
     */
    public BotCore(JDA self) {
        this.self = self;

        this.commands = new LinkedHashMap<>();

        this.registerCommand(new Command ("help","[optional: command name]",
                "Lists all commands. Use %prefix%help [command name] for more detailed usage information.",
                PermissionLevel.EVERYONE) {
            @Override
            protected String processMessage (Scanner args, MessageEvent message) throws IllegalCommandArgumentException {
                if (args.hasNext()) {
                    String command = args.next();
                    if (command.length() > prefix.length() && command.substring(0, prefix.length()).equals(prefix)) {
                        command = command.substring(prefix.length());
                    }
                    if (commands.containsKey(command)) {
                        return commands.get(command).getUsage(prefix);
                    } else  {
                        return "No command with that name!";
                    }
                } else {
                    StringBuilder out = new StringBuilder("Here is a list of commands - use " + prefix +
                            this.getName() + " [command name] for usage.");
                    for (Map.Entry<String, Command> entry : commands.entrySet()) {
                        if (entry.getValue().authenticate(message)) {
                            out.append("\n - ").append(entry.getKey());
                        }
                    }
                    return out.toString();
                }
            }
        });
    }

    @Override
    public void onMessageReceived (MessageReceivedEvent event) {
        this.processMessage(new MessageEvent(event));
    }

    @Override
    public void onMessageUpdate (MessageUpdateEvent event) {
        this.processMessage(new MessageEvent(event));
    }

    private void processMessage (MessageEvent event) {
        if (event.getAuthor().isBot()) return;
        // We don't want to respond to other bot accounts, including us

        String content = event.getMessage().getContentRaw();

        //detect commands
        if (event.getMessage().getMentionedUsers().contains(self.getSelfUser())) {
            content = content.substring(content.indexOf(" "));
        } else if (content.length() >= prefix.length() && content.substring(0, prefix.length()).equals(prefix)) {
            content = content.substring(prefix.length());
        } else {
            return;
        }

        // split up the message and process it
        Scanner args = new Scanner(content);
        String command = args.next().toLowerCase();
        if (this.commands.containsKey(command)) {
            this.commands.get(command).execute(args, event);
        }
        else {
            event.getMessage().getChannel().sendMessage("Command not recognized - use " + prefix +
                    "help for a list of commands.").queue();
        }
    }

    /**
     * Adds the given command to the set of commands this BotCore can execute
     * @param newCommand the Command to add
     */
    public void registerCommand (Command newCommand) {
        this.commands.put(newCommand.getName(), newCommand);
    }

    /**
     * Removes a command from the set this BotCore can execute by name
     * @param name the name of the Command to remove
     */
    public void removeCommand (String name) {
        this.commands.remove(name);
    }

    /**
     * Removes a command from the set this BotCore can execute
     * @param command the Command to remove
     */
    public void removeCommand (Command command) {
        this.commands.remove(command);
    }

    /**
     * Returns the named command, if it is in the set of commands this bot has
     * @param command the name of the command to return
     * @return the command with the associated name
     * @throws IllegalArgumentException if there si no such command with the given name
     */
    public Command getCommand(String command) throws IllegalArgumentException {
        if (this.commands.containsKey(command)) {
            return this.commands.get(command);
        } else {
            throw new IllegalArgumentException("No such command");
        }
    }

    /**
     * Sets the prefix that the bot will use to detect a command invocation
     * @param prefix the new prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * returns the prefix being used to detect commands
     * @return the current prefix
     */
    public String getPrefix() {
        return this.prefix;
    }
}
