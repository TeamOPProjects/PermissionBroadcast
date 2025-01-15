/**
 * Document Name: BCPLinkCommand.java
 * Description: Class to define the /BCPLink command (Broad Cast Permission Link)
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


public class BCPLinkCommand extends Command {
    private final PermissionBroadcast plugin;
    private final String BCPLINK_PERMISSION = "pb.BCPLink";

    public BCPLinkCommand(PermissionBroadcast plugin) {
        super("BCPLink");
        this.plugin = plugin;
        this.description = "Broadcast a message with an embedded link to players with a specific permission";
        this.usageMessage = "/BCPLink <permission> <link> <message>";
    }


    /**
     * On command execution check for permission then send the broadcast to all players with the permission node
     *
     * @param sender       the one who sent the command
     * @param commandLabel first word sent in the command
     * @param args         all words sent after the initial command word
     * @return True: command handled correctly, False: command handled incorrectly
     */
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (args.length < 3) {
            return false;
        } else if (!sender.hasPermission(BCPLINK_PERMISSION)) {
            plugin.notifySender(sender, "You don't have permission to use this command.");
            return true;
        }
        String permission = args[0];
        String link = args[1];
        String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
        plugin.permissionBroadcast(permission, message, link);
        if (!(sender instanceof ConsoleCommandSender)) {
            plugin.notifySender(sender, "Broadcast Sent!");
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
        if (args.length == 1 && sender.hasPermission(BCPLINK_PERMISSION)) {
            return List.of("<permission>");
        } else if (args.length == 2 && sender.hasPermission(BCPLINK_PERMISSION)) {
            return List.of("<link>");
        } else if (args.length == 3 && sender.hasPermission(BCPLINK_PERMISSION)) {
            return List.of("<message>");
        }
        return new ArrayList<>();
    }
}

