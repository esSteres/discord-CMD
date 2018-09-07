package DiscordCMD;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.MessageUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.*;

public class BotCore extends ListenerAdapter {
    private String prefix = "!";
    private JDA api;

    private LinkedHashMap<String, Command> commands;

    public BotCore(String token) throws IllegalArgumentException {
        try {
            api = new JDABuilder(AccountType.BOT).setToken(token).buildAsync();
        } catch (javax.security.auth.login.LoginException e) {
            throw new IllegalArgumentException("Invalid token provided. It may be expired or the account deleted.");
        }

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
        if (this.processMessage(new MessageEvent(event))) {
            this.altMessageRecieved(event);
        }
    }

    public void altMessageRecieved (MessageReceivedEvent event) {}

    @Override
    public void onMessageUpdate (MessageUpdateEvent event) {
        if (this.processMessage(new MessageEvent(event))) {
            this.altMessageUpdate(event);
        }
    }

    public void altMessageUpdate (MessageUpdateEvent event) {}

    private boolean processMessage (MessageEvent event) {
        if (event.getAuthor().isBot()) return true;
        // We don't want to respond to other bot accounts, including us

        String content = event.getMessage().getContentRaw();

        //detect commands
        if (event.getMessage().getMentionedUsers().contains(api.getSelfUser())) {
            content = content.substring(content.indexOf(" "));
        } else if (content.length() >= prefix.length() && content.substring(0, prefix.length()).equals(prefix)) {
            content = content.substring(prefix.length());
        } else {
            return true;
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

        return false;
    }

    public void registerCommand (Command newCommand) {
        this.commands.put(newCommand.getName(), newCommand);
    }

    public void removeCommand (String name) {
        this.commands.remove(name);
    }

    public void removeCommand (Command command) {
        this.commands.remove(command);
    }

    public Command getCommand(String command) {
        return this.commands.get(command);
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void start() {
        api.addEventListener(this);
    }
}
