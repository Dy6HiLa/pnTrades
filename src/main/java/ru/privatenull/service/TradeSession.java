package ru.privatenull.service;

import org.bukkit.inventory.Inventory;

import java.util.UUID;

public final class TradeSession {
    final UUID id = UUID.randomUUID();
    final UUID first;
    final UUID second;
    Inventory firstInventory;
    Inventory secondInventory;
    boolean firstConfirmed;
    boolean secondConfirmed;

    TradeSession(UUID first, UUID second) {
        this.first = first;
        this.second = second;
    }

    boolean contains(UUID playerId) {
        return first.equals(playerId) || second.equals(playerId);
    }

    UUID other(UUID playerId) {
        return first.equals(playerId) ? second : first;
    }

    boolean isFirst(UUID playerId) {
        return first.equals(playerId);
    }
}
