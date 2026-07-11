package ru.privatenull;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import ru.privatenull.command.TradeCommand;
import ru.privatenull.config.GuiConfig;
import ru.privatenull.gui.TradeListener;
import ru.privatenull.lifecycle.PluginBanner;
import ru.privatenull.service.TradeManager;
import ru.privatenull.update.UpdateChecker;

import java.util.Objects;

public final class PnTradesPlugin extends JavaPlugin {
    public static final String SUPPORT_DISCORD = "https://discord.gg/rRbzq6cnc6";
    private TradeManager tradeManager;
    private UpdateChecker updateChecker;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        tradeManager = new TradeManager(this, new GuiConfig(this));
        TradeCommand command = new TradeCommand(tradeManager);
        PluginCommand trade = Objects.requireNonNull(getCommand("trade"), "trade command is missing");
        trade.setExecutor(command);
        trade.setTabCompleter(command);
        updateChecker = new UpdateChecker(this);
        updateChecker.start();
        getServer().getPluginManager().registerEvents(new TradeListener(tradeManager, updateChecker), this);
        PluginBanner.enabled(this, SUPPORT_DISCORD);
    }

    @Override
    public void onDisable() {
        if (updateChecker != null) {
            updateChecker.cancel();
        }
        if (tradeManager != null) {
            tradeManager.closeAll();
        }
        PluginBanner.disabled(this, SUPPORT_DISCORD);
    }

    public String message(String path, String playerName) {
        String value = getConfig().getString("messages." + path, "");
        return value.replace("{player}", playerName).replace('&', '§');
    }
}
