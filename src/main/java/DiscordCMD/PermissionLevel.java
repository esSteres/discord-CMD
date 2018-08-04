package DiscordCMD;

import net.dv8tion.jda.core.entities.Role;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public class PermissionLevel {

    public static final PermissionLevel EVERYONE = new PermissionLevel() {
        public boolean contains (Collection<Role> roles) {
            return true;
        }

        public void addRole (String roleID) {}

        public void removeRole (String roleID) {}
    };

    private HashSet<String> roleIDs;

    public PermissionLevel () {
        roleIDs = new HashSet<>();
    }

    public PermissionLevel (Collection<String> roles) {
        this();
        roleIDs.addAll(roles);
    }

    public PermissionLevel (String... roles) {
        this(Arrays.asList(roles));
    }

    public PermissionLevel (Role... roles) {
        this();
        for (Role role : roles) {
            roleIDs.add(role.getId());
        }
    }

    public boolean contains (Collection<Role> roles) {
        for (Role role : roles) {
            if (roleIDs.contains(role.getId())) {
                return true;
            }
        }
        return false;
    }

    public void addRole (String roleID) {
        this.roleIDs.add(roleID);
    }

    public void addRole (Role role) {
        this.addRole(role.getId());
    }

    public void removeRole (String roleID) {
        this.roleIDs.remove(roleID);
    }

    public void removeRole (Role role) {
        this.removeRole(role.getId());
    }
}
