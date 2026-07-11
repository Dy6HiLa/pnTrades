package ru.privatenull.update;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import ru.privatenull.PnTradesPlugin;

public final class UpdateChecker {
    private static final long CHECK_DELAY_TICKS = 100L;
    private static final long CHECK_PERIOD_MINUTES = 360L;

    private final PnTradesPlugin plugin;
    private final GitHubUpdateClient client = new GitHubUpdateClient();
    private final UpdateNotifier notifier;
    private BukkitTask task;
    private volatile String latestVersion;
    private volatile String downloadUrl = GitHubUpdateClient.DEFAULT_DOWNLOAD_URL;
    private volatile boolean updateAvailable;

    public UpdateChecker(PnTradesPlugin plugin) {
        this.plugin = plugin;
        this.notifier = new UpdateNotifier(plugin);
    }

    public void start() {
        cancel();
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::check,
                CHECK_DELAY_TICKS, CHECK_PERIOD_MINUTES * 60L * 20L);
    }

    public void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public void notifyAdminOnJoin(Player player) {
        if (updateAvailable && player.hasPermission("pntrades.admin")) {
            notifier.send(player, latestVersion, downloadUrl);
        }
    }

    private void check() {
        try {
            UpdateInfo info = client.fetchLatest();
            if (info.version() == null || info.version().isBlank()) {
                plugin.getLogger().warning("Проверка обновлений: GitHub не вернул версию.");
                return;
            }
            if (VersionComparator.compare(info.version(), plugin.getDescription().getVersion()) <= 0) {
                updateAvailable = false;
                latestVersion = info.version();
                return;
            }
            boolean firstNotice = !updateAvailable || !info.version().equalsIgnoreCase(latestVersion);
            updateAvailable = true;
            latestVersion = info.version();
            downloadUrl = info.downloadUrl() == null || info.downloadUrl().isBlank()
                    ? GitHubUpdateClient.DEFAULT_DOWNLOAD_URL : info.downloadUrl();
            if (firstNotice) {
                plugin.getLogger().warning(System.lineSeparator() + notifier.consoleMessage(latestVersion, downloadUrl));
                Bukkit.getScheduler().runTask(plugin,
                        () -> Bukkit.getOnlinePlayers().forEach(this::notifyAdminOnJoin));
            }
        } catch (Exception exception) {
            plugin.getLogger().warning("Ошибка проверки обновлений: " + exception.getMessage());
        }
    }
}
