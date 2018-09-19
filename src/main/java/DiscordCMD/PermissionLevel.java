package DiscordCMD;

import net.dv8tion.jda.core.entities.Role;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a set of roles which have the same level of permissions. Any user with at least one role which is a member
 * of a PermissionLevel is considered to be authorized for that level. A PermissionLevel dynamically contains all the
 * roles of its supersets.
 *
 * For a command which should be usable by everyone, use {@code PermissionLevel.EVERYONE}.
 */
public class PermissionLevel {

    public static final PermissionLevel EVERYONE = new PermissionLevel() {
        public boolean contains (Collection<Role> roles) {
            return true;
        }

        public void addRole (String roleID) {}

        public void removeRole (String roleID) {}
    };

    private Set<String> roleIDs;

    private
    Set<PermissionLevel> superSets;

    public PermissionLevel () {
        roleIDs = new HashSet<>();
        superSets = new HashSet<>();
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
        for (PermissionLevel sup: superSets) {
            if (sup.contains(roles)) {
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

    public void addSubSet (PermissionLevel other) {
        other.addSuperSet(this);
    }

    public void addSuperSet (PermissionLevel other) {
        this.superSets.add(other);
    }

    public void removeSubSet (PermissionLevel other) {
        other.removeSuperSet(this);
    }

    public void removeSuperSet (PermissionLevel other) {
        this.superSets.remove(other);
    }
}
