/**
 * Document Name: BCALinkCommand.java
 * Description: Class to define the /BCALink command (Broad Cast All Link)
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
import java.util.Arrays;
import java.util.List;


public class BCALinkCommand extends Command {
    private final PermissionBroadcast plugin;
    private final String BCALINK_PERMISSION = "pb.BCALink";

    public BCALinkCommand(PermissionBroadcast plugin) {
        super("BCALink");
        this.plugin = plugin;
        this.description = "Broadcast a message with an embedded link to all players";
        this.usageMessage = "/BCALink <link> <message>";
    }

    /**
     * On command execution check for permission then send the broadcast to all players
     * Includes an embedded link
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
        } else if (!sender.hasPermission(BCALINK_PERMISSION)) {
            plugin.notifySender(sender, "You don't have permission to use this command.");
            return true;
        }
        String link = args[0];
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        plugin.permissionBroadcast(plugin.getDefaultPermissionNode(), message, link); // Empty string for no permission node
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
        if (args.length == 1 && sender.hasPermission(BCALINK_PERMISSION)) {
            return List.of("<link>");
        } else if (args.length == 2 && sender.hasPermission(BCALINK_PERMISSION)) {
            return List.of("<message>");
        }
        return new ArrayList<>();
    }
}
