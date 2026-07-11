package ru.privatenull.update;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import ru.privatenull.PnTradesPlugin;

final class UpdateNotifier {
    private final PnTradesPlugin plugin;

    UpdateNotifier(PnTradesPlugin plugin) {
        this.plugin = plugin;
    }

    String consoleMessage(String latestVersion, String downloadUrl) {
        return """
                ==================== pnTrades Обновление ====================
                Доступна новая версия pnTrades.
                Установлена: %s
                Новая:       %s
                Скачать:     %s
                Поддержка:   %s
                После замены JAR перезапустите сервер.
                =============================================================
                """.formatted(plugin.getDescription().getVersion(), latestVersion, downloadUrl, PnTradesPlugin.SUPPORT_DISCORD);
    }

    void send(Player player, String latestVersion, String downloadUrl) {
        player.sendMessage("§8§m                                                  ");
        player.sendMessage("§b§lpnTrades §8| §fВышло обновление");
        player.sendMessage("§b▸ §fУстановлена: §7" + plugin.getDescription().getVersion());
        player.sendMessage("§b▸ §fНовая версия: §d" + latestVersion);
        player.sendMessage("§b▸ §fЗамените JAR и перезапустите сервер.");
        player.spigot().sendMessage(link("§b▸ §fСкачать обновление: §d§n" + downloadUrl, downloadUrl));
        player.sendMessage("§8§m                                                  ");
        player.sendTitle("§b§lpnTrades", "§fВышло обновление §d" + latestVersion, 10, 80, 20);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.45f, 1.6f);
    }

    private TextComponent link(String label, String url) {
        TextComponent component = new TextComponent(label);
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new TextComponent[]{new TextComponent("§fНажмите, чтобы открыть ссылку")}));
        return component;
    }
}
