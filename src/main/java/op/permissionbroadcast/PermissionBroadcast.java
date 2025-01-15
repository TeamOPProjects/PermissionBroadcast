/**
 * Document Name: PermissionBroadcast.java
 * Description: Main class for the PermissionBroadcast Plugin
 * Author: Slayer
 * Creation Date: 1/14/25
 * Last Modified: 1/15/25
 */
package op.permissionbroadcast;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import op.permissionbroadcast.api.PermissionBroadcastAPIImplementation;
import op.permissionbroadcast.api.PermissionBroadcastAPI;
import op.permissionbroadcast.commands.*;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


import java.util.ArrayList;
import java.util.List;


public class PermissionBroadcast extends JavaPlugin {
    private List<String> messages;
    private int interval;
    private boolean random;
    private int currentIndex = 0;
    private BukkitTask broadcastTask;
    private boolean autoBroadcast;
    private String ABPermission;
    private String prefix;
    private String defaultPermissionNode;
    private PermissionBroadcastAPI api;
    private static PermissionBroadcast instance;


    // @TODO Add an api

    /**
     * Starts plugin at server startup
     */
    @Override
    public void onEnable() {
        // Loads config
        saveDefaultConfig();
        loadConfig();

        // Sets up API Implementation
        instance = this;
        this.api = new PermissionBroadcastAPIImplementation(this);
        getServer().getServicesManager().register(
                PermissionBroadcastAPI.class,
                this.api,
                this,
                ServicePriority.Normal
        );

        // Starts autoBroadcasting
        if (autoBroadcast) {
            startAutoBroadcasting();
        }

        // Registers commands
        getServer().getCommandMap().register("PB", new PBCommand(this));
        getServer().getCommandMap().register("BCA", new BCACommand(this));
        getServer().getCommandMap().register("BCP", new BCPCommand(this));
        getServer().getCommandMap().register("BCALink", new BCALinkCommand(this));
        getServer().getCommandMap().register("BCPLink", new BCPLinkCommand(this));

        //Displays message in console on plugin enable
        getLogger().info("Successfully enabled.");
    }

    /**
     * Disables plugin at server shutdown
     */
    @Override
    public void onDisable() {
        getLogger().info("Successfully disabled.");
    }


    /**
     * Loads config file
     */
    private void loadConfig() {
        reloadConfig();
        FileConfiguration config = getConfig();

        // Get the Messages section
        ConfigurationSection messagesSection = config.getConfigurationSection("AutoBroadcast.Messages");
        messages = new ArrayList<>();

        if (messagesSection != null) {
            // Iterate through each message group
            for (String key : messagesSection.getKeys(false)) {
                String link = messagesSection.getString(key + ".link", "");
                List<String> messageGroup = messagesSection.getStringList(key + ".text");
                // Join the lines with \n for multi-line support
                messages.add(link + "|" + String.join("\n", messageGroup));
            }
        }

        interval = config.getInt("AutoBroadcast.interval", 60) * 20;
        random = config.getBoolean("AutoBroadcast.random", false);
        ABPermission = config.getString("AutoBroadcast.permission", "group.default");
        autoBroadcast = config.getBoolean("AutoBroadcast.enabled", true);
        prefix = config.getString("Prefix", "&7[&fAutobroadcast]");
        defaultPermissionNode = config.getString("Broadcast.defaultPermission", "group.default");

        if (broadcastTask != null) {
            broadcastTask.cancel();
        }
    }


    /**
     * Starts broadcasting in chat
     */
    private void startAutoBroadcasting() {
        broadcastTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (messages.isEmpty()) return;
                String message;
                if (random) {
                    message = messages.get((int) (Math.random() * messages.size()));
                } else {
                    message = messages.get(currentIndex);
                    currentIndex = (currentIndex + 1) % messages.size();
                }

                String[] parts = message.split("\\|", 2);
                String link = parts[0];
                String text = parts.length > 1 ? parts[1] : message;

                broadcastToOnlinePlayers(ABPermission, text, link);
            }
        }.runTaskTimer(this, interval, interval);
    }

    /**
     * overloaded method to broadcast to online players who have a certain permission with no link provided
     *
     * @param permission the permission node a player needs to receive the broadcast message
     * @param message    the message being broadcasted
     */
    private void broadcastToOnlinePlayers(String permission, String message) {
        broadcastToOnlinePlayers(permission, message, "");
    }

    /**
     * method to broadcast to online players who have a certain permission
     *
     * @param permission the permission node a player needs to receive the broadcast message
     * @param message    the message being broadcasted
     * @param link       the link to embed in chat so players can click on it to open a website
     */
    private void broadcastToOnlinePlayers(String permission, String message, String link) {
        getServer().getOnlinePlayers().stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(player -> player.sendMessage(colorize(message, link)));
    }

    /**
     * method to colorize the message being sent in chat using AdventureAPI and Legacy color codes
     *
     * @param message message to colorize
     * @param link    the link to embed in the message
     * @return a component that can be used to send messages in chat using "player.sendMessage" in PaperAPI
     */
    private Component colorize(String message, String link) {
        // First convert legacy and hex colors
        Component colored = LegacyComponentSerializer.builder()
                .character('&')
                .hexColors()
                .build()
                .deserialize(message);

        // Then handle click events if link is provided
        if (link != null && !link.isEmpty()) {
            return colored.clickEvent(ClickEvent.openUrl(link));
        }
        return colored;
    }

    /**
     * public method so the command methods can reload the config
     */
    public void reloadConfiguration() {
        loadConfig();
        if (autoBroadcast) {
            startAutoBroadcasting();
        }
    }

    /**
     * gets the prefix defined in the config
     *
     * @return the colorized prefix as a component
     */
    public Component getPrefix() {
        return colorize(prefix + " &f", "");
    }

    /**
     * Overloaded message to broadcast a message sent from either command methods or the API
     *
     * @param permission the permission node a player needs to receive the broadcast message
     * @param message    the message being broadcasted
     */
    public void permissionBroadcast(String permission, String message) {
        broadcastToOnlinePlayers(permission, message);
    }

    /**
     * method to broadcast a message sent from either command methods or the API
     *
     * @param permission the permission node a players needs to receive the broadcast message
     * @param message    the message being broadcasted
     * @param link       the link to embed in the message
     */
    public void permissionBroadcast(String permission, String message, String link) {
        broadcastToOnlinePlayers(permission, message, link);
    }

    /**
     * Method to notify the command sender of command execution status
     *
     * @param sender  the one who sent the command
     * @param message the message to be sent to the sender
     */
    public void notifySender(CommandSender sender, String message) {
        sender.sendMessage(Component.text()
                .append(this.getPrefix())
                .append(Component.text(message))
                .build());
    }

    /**
     * method to get the default permission node (may vary depending on the permission plugin)
     * Used to send messages to all players in the server
     *
     * @return the default permission node defined in the config
     */
    public String getDefaultPermissionNode() {
        return this.defaultPermissionNode;
    }

    /**
     * Gets the PermissionBroadcast API
     * @return PermissionBroadcastAPI
     */
    public static PermissionBroadcastAPI getAPI() {
        return instance.api;
    }
}
