/**
 * Document Name: PBCommand.java
 * Description: Class to define the /PB command (Permission Broadcast)
 * Author: Slayer
 * Creation Date: 1/14/25
 * Last Modified: 1/14/25
 */
package op.permissionbroadcast.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import op.permissionbroadcast.PermissionBroadcast;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PBCommand extends Command {
    private final PermissionBroadcast plugin;
    private final String RELOAD_PERMISSION = "pb.reload";
    private final String HELP_PERMISSION = "pb.help";

    public PBCommand(PermissionBroadcast plugin) {
        super("pb");
        this.plugin = plugin;
        this.description = "PermissionBroadcast main command";
        this.usageMessage = "/pb <reload|help>";
    }

    /**
     * On command execution check for permission then execute a PermissionBroadcast command
     *
     * @param sender       the one who sent the command
     * @param commandLabel first word sent in the command
     * @param args         all words sent after the initial command word
     * @return True: command handled correctly, False: command handled incorrectly
     */
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            if (sender.hasPermission(HELP_PERMISSION)) {
                return showHelp(sender);
            } else {
                plugin.notifySender(sender, "You don't have permission to use this command.");
                return true;
            }
        } else if (args[0].equalsIgnoreCase("reload")) {
            if (sender.hasPermission(RELOAD_PERMISSION)) {
                return reloadConfig(sender);
            } else {
                plugin.notifySender(sender, "You don't have permission to use this command.");
                return true;
            }
        }
        return false;
    }

    /**
     * reloads the config
     *
     * @param sender the one who sent the command
     * @return True: command handled correctly, False: command handled incorrectly
     */
    private boolean reloadConfig(CommandSender sender) {
        plugin.reloadConfiguration();
        plugin.notifySender(sender, "Config reloaded!");
        return true;
    }

    /**
     * Sends a message in game with all command options for PermissionBroadcast
     *
     * @param sender the one who sent the command
     * @return True: command handled correctly, False: command handled incorrectly
     */
    private boolean showHelp(CommandSender sender) {
        sender.sendMessage(Component.text("PermissionBroadcast Help:").color(NamedTextColor.GOLD));
        sender.sendMessage(Component.text("/pb reload").color(NamedTextColor.YELLOW)
                .append(Component.text(" - Reload the plugin configuration").color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("/pb help").color(NamedTextColor.YELLOW)
                .append(Component.text(" - Show this help message").color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("/BCA <message>").color(NamedTextColor.YELLOW)
                .append(Component.text(" - Broadcast a message to all players").color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("/BCP <permission> <message>").color(NamedTextColor.YELLOW)
                .append(Component.text(" - Broadcast a message to players with a specific permission").color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("/BCALink <link> <message>").color(NamedTextColor.YELLOW)
                .append(Component.text(" - Broadcast a message to all players with a link embedded").color(NamedTextColor.WHITE)));
        sender.sendMessage(Component.text("/BCPLink <permission> <link> <message>").color(NamedTextColor.YELLOW)
                .append(Component.text(" - Broadcast a message to players with a specific permission with a link embedded").color(NamedTextColor.WHITE)));
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
        if (args.length == 1 && sender.hasPermission(HELP_PERMISSION) && sender.hasPermission(RELOAD_PERMISSION)) {
            return List.of("reload", "help");
        } else if (args.length == 1 && sender.hasPermission(RELOAD_PERMISSION)) {
            return List.of("reload");
        } else if (args.length == 1 && sender.hasPermission(HELP_PERMISSION)) {
            return List.of("help");
        }
        return new ArrayList<>();
    }
}

