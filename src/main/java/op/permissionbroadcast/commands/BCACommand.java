/**
 * Document Name: BCACommand.java
 * Description: Class to define the /BCA command (Broad Cast All)
 * Author: Slayer
 * Creation Date: 1/14/25
 * Last Modified: 1/14/25
 */
package op.permissionbroadcast.commands;

import op.permissionbroadcast.PermissionBroadcast;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class BCACommand extends Command {
    private final PermissionBroadcast plugin;
    private final String BCA_PERMISSION = "pb.BCA";

    public BCACommand(PermissionBroadcast plugin) {
        super("BCA");
        this.plugin = plugin;
        this.description = "Broadcast a message to all players";
        this.usageMessage = "/BCA <message>";
    }

    /**
     * On command execution check for permission then send the broadcast to all players
     *
     * @param sender       the one who sent the command
     * @param commandLabel first word sent in the command
     * @param args         all words sent after the initial command word
     * @return True: command handled correctly, False: command handled incorrectly
     */
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (args.length == 0) {
            return false;
        } else if (!sender.hasPermission(BCA_PERMISSION)) {
            plugin.notifySender(sender, "You don't have permission to use this command.");
            return true;
        }
        String message = String.join(" ", args);
        plugin.permissionBroadcast(plugin.getDefaultPermissionNode(), message); // Empty string for no permission node
        if (!(sender instanceof ConsoleCommandSender)) {
            plugin.notifySender(sender, "Message Sent!");
        }
        return true;
    }

    /**
     * adds options that a player can tab through instead of needing to know the command beforehand
     *
     * @param sender the one who sent the command
     * @param alias  alias used when player executes the command
     * @param args   the words sent after the initial command word
     * @return a list containing the tab autocomplete messages
     */
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
        if (args.length == 1 && sender.hasPermission(BCA_PERMISSION)) {
            return List.of("<message>");
        }
        return new ArrayList<>();
    }
}

