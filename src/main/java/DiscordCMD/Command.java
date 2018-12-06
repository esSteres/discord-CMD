package DiscordCMD;

import net.dv8tion.jda.core.entities.Role;

import java.util.*;

/**
 * Class to contain code for commands. Extend to create new commands
 */
public abstract class Command {
    private String name;
    private String usage;
    private Set<PermissionLevel> allowedUsers;

    /**
     * Creates a new Command.
     * @param name the name of the command - what users must type to invoke it, e.g. {@code roll}
     * @param template the template for command usage - what parameters does it have, and in what order? For example:
     *                 {@code [# of dice] d [# of sides] +/- [modifiers]}
     * @param info information about what the command does - this and template are used in the built-in {@code help}
     *            command
     * @param allowedUsers a collection of the PermissionLevels allowed to use this command
     */
    public Command (String name, String template, String info, Collection<PermissionLevel> allowedUsers) {
        if (template.contains(",")) {
            template += "\n(Note that arguments are separated by commas)";
        }
        this.usage = "Usage: %prefix%" + name + " " + template + "\n" + info;
        this.name = name;
        this.allowedUsers = new HashSet<>(allowedUsers);
    }

    public Command (String name, String template, String info, PermissionLevel... allowedUsers) {
        this(name, template, info, Arrays.asList(allowedUsers));
    }

    void execute (Scanner args, MessageEvent message, String prefix) {
        args.useDelimiter("\\s*,\\s*");
        args.skip("\\s*");

        String reply;

        if (this.authenticate(message)) {
            try {
                reply = this.processMessage(args, message);
            } catch (IllegalCommandArgumentException e) {
                reply = e.getMessage();
            }
        } else {
            reply = "You don't have permission to do that.";
        }

        if (reply != null) {
            reply.replaceAll("%prefix%", prefix);
            message.getMessage().getChannel().sendMessage(reply).queue();
        }
    }

    /**
     * The relevant method to override when creating a new command.
     * @param args a scanner of strings containing the arguments to the command
     * @param message the message object that caused execution
     * @return the string to be displayed to the user
     * @throws IllegalCommandArgumentException if some error occurs due to user input. The exception will be handled by
     * the command object itself and the exception message will be displayed to the user.
     */
    abstract protected String processMessage (Scanner args, MessageEvent message) throws IllegalCommandArgumentException;

    protected String getUsage(String prefix) {
        return this.usage.replaceAll("%prefix%", prefix);
    }

    String getName() {
        return this.name;
    }

    boolean authenticate (MessageEvent message) {
        List<Role> roles;

        if (message.isPrivateMessage()) {
            roles = Collections.EMPTY_LIST;
        } else {
            roles = message.getMember().getRoles();
        }

        for (PermissionLevel permission : this.allowedUsers) {
            if (permission.contains(roles)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a permissionlevel
     * @param newPermission the new PermissionLevel object that will be able to execute this command
     */
    public void addPermission (PermissionLevel newPermission) {
        this.allowedUsers.add(newPermission);
    }

    /**
     * Adds all given PermissionLevels
     * @param newPermissions all new PermissionLevels that will be able to use this command
     */
    public void addAllPermissions (PermissionLevel... newPermissions) {
        this.allowedUsers.addAll(Arrays.asList(newPermissions));
    }

    /**
     * Adds all given PermissionLevels
     * @param newPermissions all new PermissionLevels that will be able to use this command
     */
    public void addAllPermissions (Collection<PermissionLevel> newPermissions) {
        this.allowedUsers.addAll(newPermissions);
    }

    /**
     * Removes a PermissionLevel
     * @param oldPermission the PermissionLevel that will no longer be able to use this command
     */
    public void removePermission (PermissionLevel oldPermission) {
        this.allowedUsers.remove(oldPermission);
    }

    /**
     * Removes all given PermissionLevels
     * @param oldPermissions all PermissionLevels that will no longer be able to use this command
     */
    public void removeAllPermissions (PermissionLevel... oldPermissions) {
        this.allowedUsers.removeAll(Arrays.asList(oldPermissions));
    }

    /**
     * Removes all given PermissionLevels
     * @param oldPermissions all PermissionLevels that will no longer be able to use this command
     */
    public void removeAllPermissions (Collection<PermissionLevel> oldPermissions) {
        this.allowedUsers.removeAll(oldPermissions);
    }
}