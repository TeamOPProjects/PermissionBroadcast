/**
 * Document Name: PermissionBroadcastAPIImplementation.java
 * Description: Interface Implementation for PermissionBroadcastAPI
 * Author: Slayer
 * Creation Date: 1/15/25
 * Last Modified: 1/15/25
 */
package op.permissionbroadcast.api;

import op.permissionbroadcast.PermissionBroadcast;

public class PermissionBroadcastAPIImplementation implements PermissionBroadcastAPI {
    private final PermissionBroadcast plugin;


    public PermissionBroadcastAPIImplementation(PermissionBroadcast plugin) {
        this.plugin = plugin;
    }

    /**
     * Broadcasts a message to all players with default permission
     * @param message the message to broadcast
     */
    @Override
    public void broadcastToAll(String message) {
        plugin.permissionBroadcast(plugin.getDefaultPermissionNode(), message);
    }

    /**
     * Broadcasts a message to all players with a permission node
     * @param permission the permission node
     * @param message the message to broadcast
     */
    @Override
    public void broadcastToPermission(String permission, String message) {
        plugin.permissionBroadcast(permission, message);
    }

    /**
     * Broadcasts a message with an embedded link to all players
     * @param message the message to broadcast
     * @param link the link to be embedded
     */
    @Override
    public void broadcastToAllLink(String message, String link) {
        plugin.permissionBroadcast(plugin.getDefaultPermissionNode(), message, link);
    }

    /**
     * Broadcasts a message with an embedded link to all players with a permission node
     * @param permission the permission node
     * @param message the message to broadcast
     * @param link the link to be embedded
     */
    @Override
    public void broadcastToPermissionLink(String permission, String message, String link) {
        plugin.permissionBroadcast(permission, message, link);
    }
}
