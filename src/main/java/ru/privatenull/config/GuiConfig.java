package ru.privatenull.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.privatenull.PnTradesPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class GuiConfig {
    private final FileConfiguration config;

    public GuiConfig(PnTradesPlugin plugin) {
        plugin.saveResource("gui.yml", false);
        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "gui.yml"));
    }

    public String title(String playerName) {
        return color(config.getString("title", "&8Обмен: &f{player}").replace("{player}", playerName));
    }

    public int slot(String path, int fallback) {
        return config.getInt(path + ".slot", fallback);
    }

    public List<Integer> slots(String path, List<Integer> fallback) {
        List<Integer> values = config.getIntegerList(path + ".slots");
        return values.isEmpty() ? fallback : values;
    }

    public ItemStack item(String path) {
        Material material = Material.matchMaterial(config.getString(path + ".material", "BARRIER"));
        ItemStack item = new ItemStack(material == null ? Material.BARRIER : material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(config.getString(path + ".name", " ")));
        List<String> lore = new ArrayList<>();
        for (String line : config.getStringList(path + ".lore")) lore.add(color(line));
        if (!lore.isEmpty()) meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private String color(String value) {
        return value.replace('&', '§');
    }
}
