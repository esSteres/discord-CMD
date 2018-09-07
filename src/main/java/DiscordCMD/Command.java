package DiscordCMD;

import net.dv8tion.jda.core.entities.Role;

import java.util.*;

public abstract class Command {
    private String name;
    private String usage;
    private HashSet<PermissionLevel> allowedUsers;

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

    void execute (Scanner args, MessageEvent message) {
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
            message.getMessage().getChannel().sendMessage(reply).queue();
        }
    }

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

    public void addPermission (PermissionLevel newPermission) {
        this.allowedUsers.add(newPermission);
    }

    public void addAllPermissions (PermissionLevel... newPermissions) {
        this.allowedUsers.addAll(Arrays.asList(newPermissions));
    }

    public void addAllPermissions (Collection<PermissionLevel> newPermissions) {
        this.allowedUsers.addAll(newPermissions);
    }

    public void removePermission (PermissionLevel oldPermission) {
        this.allowedUsers.remove(oldPermission);
    }

    public void removeAllPermissions (PermissionLevel... oldPermissions) {
        this.allowedUsers.removeAll(Arrays.asList(oldPermissions));
    }

    public void removeAllPermissions (Collection<PermissionLevel> oldPermissions) {
        this.allowedUsers.removeAll(oldPermissions);
    }
}