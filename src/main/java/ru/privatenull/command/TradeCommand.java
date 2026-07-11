package ru.privatenull.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import ru.privatenull.service.TradeManager;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public final class TradeCommand implements CommandExecutor, TabCompleter {
    private final TradeManager manager;

    public TradeCommand(TradeManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(manager.message("player-only", ""));
            return true;
        }
        if (!player.hasPermission("pntrades.use")) {
            player.sendMessage(manager.message("no-permission", ""));
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(manager.message("usage", ""));
            return true;
        }

        String action = args[0].toLowerCase(Locale.ROOT);
        if ((action.equals("accept") || action.equals("deny")) && args.length >= 2) {
            Player requester = Bukkit.getPlayerExact(args[1]);
            if (requester == null) {
                player.sendMessage(manager.message("player-not-online", ""));
                return true;
            }
            if (action.equals("accept")) {
                manager.acceptRequest(player, requester);
            } else {
                manager.denyRequest(player, requester);
            }
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage(manager.message("player-not-online", ""));
            return true;
        }
        manager.requestTrade(player, target);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(args[0].toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("accept") || args[0].equalsIgnoreCase("deny"))) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName)
                    .filter(name -> name.toLowerCase(Locale.ROOT).startsWith(args[1].toLowerCase(Locale.ROOT)))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
