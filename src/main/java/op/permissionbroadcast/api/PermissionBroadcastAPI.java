/**
 * Document Name: PermissionBroadcastAPI.java
 * Description: Interface for PermissionBroadcastAPI
 * Author: Slayer
 * Creation Date: 1/15/25
 * Last Modified: 1/15/25
 */
package op.permissionbroadcast.api;

public interface PermissionBroadcastAPI {
    /**
     * Broadcasts a message to all players with default permission
     * @param message the message to broadcast
     */
    void broadcastToAll(String message);

    /**
     * Broadcasts a message to all players with a permission node
     * @param permission the permission node
     * @param message the message to broadcast
     */
    void broadcastToPermission(String permission, String message);

    /**
     * Broadcasts a message with an embedded link to all players
     * @param message the message to broadcast
     * @param link the link to be embedded
     */
    void broadcastToAllLink(String message, String link);

    /**
     * Broadcasts a message with an embedded link to all players with a permission node
     * @param permission the permission node
     * @param message the message to broadcast
     * @param link the link to be embedded
     */
    void broadcastToPermissionLink(String permission, String message, String link);
}
