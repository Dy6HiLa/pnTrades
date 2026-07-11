package ru.privatenull.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;

public final class TradeHolder implements InventoryHolder {
    private final UUID sessionId;
    private final UUID viewerId;

    public TradeHolder(UUID sessionId, UUID viewerId) {
        this.sessionId = sessionId;
        this.viewerId = viewerId;
    }

    public UUID sessionId() {
        return sessionId;
    }

    public UUID viewerId() {
        return viewerId;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
