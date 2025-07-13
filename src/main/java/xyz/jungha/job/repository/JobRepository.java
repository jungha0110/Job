package xyz.jungha.job.repository;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import xyz.jungha.job.Job;
import xyz.jungha.job.enums.Jobs;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class JobRepository {

    private final Job plugin;
    private File file;
    private FileConfiguration config;
    private final Map<UUID, BossBar> playerBossBars = new HashMap<>();
    private final Map<UUID, BukkitTask> removalTasks = new HashMap<>();

    public JobRepository(Job plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        file = new File(plugin.getDataFolder(), "job.yml");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to load or create job.yml", e);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to save job.yml", e);
        }
    }

    private String getLevelPath(OfflinePlayer player, Jobs job) {
        return player.getUniqueId() + ".level." + job.getKey();
    }

    private String getExpPath(OfflinePlayer player, Jobs job) {
        return player.getUniqueId() + ".exp." + job.getKey();
    }

    private void showExpBar(Player player, Jobs job, double amount) {
        int level = getJobLevel(player, job);
        double exp = getJobExp(player, job);
        double maxExp = getJobMaxExp(player, job);
        double progress = (maxExp == 0) ? 1.0 : Math.min(1.0, exp / maxExp);

        String title = String.format("§8[§f%s §7Lv.§e%d§8] §7Exp: §a%.2f§7/§a%.2f §8(+%.2f)",
                job.getDisplayName(), level, exp, maxExp, amount);

        UUID playerUUID = player.getUniqueId();

        BukkitTask existingTask = removalTasks.remove(playerUUID);
        if (existingTask != null) {
            existingTask.cancel();
        }

        BossBar bar = playerBossBars.computeIfAbsent(playerUUID, uuid -> {
            BossBar newBar = Bukkit.createBossBar(title, BarColor.GREEN, BarStyle.SOLID);
            newBar.addPlayer(player);
            return newBar;
        });

        bar.setTitle(title);
        bar.setProgress(progress);
        if (!bar.getPlayers().contains(player)) {
            bar.addPlayer(player);
        }

        BukkitTask removalTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            BossBar b = playerBossBars.remove(playerUUID);
            if (b != null) {
                b.removeAll();
            }
            removalTasks.remove(playerUUID);
        }, 100L);

        removalTasks.put(playerUUID, removalTask);
    }

    public boolean contains(OfflinePlayer player) {
        return config.contains(player.getUniqueId().toString());
    }

    public int getJobLevel(OfflinePlayer player, Jobs job) {
        return config.getInt(getLevelPath(player, job), 0);
    }

    public void setJobLevel(OfflinePlayer player, Jobs job, int level) {
        config.set(getLevelPath(player, job), level);
        saveConfig();
    }

    public void addJobLevel(OfflinePlayer player, Jobs job, int amount) {
        int current = getJobLevel(player, job);
        setJobLevel(player, job, current + amount);
    }

    public void subtractJobLevel(OfflinePlayer player, Jobs job, int amount) {
        int current = getJobLevel(player, job);
        setJobLevel(player, job, Math.max(0, current - amount));
    }

    public double getJobExp(OfflinePlayer player, Jobs job) {
        return config.getDouble(getExpPath(player, job), 0);
    }

    public double getJobMaxExp(OfflinePlayer player, Jobs job) {
        return Jobs.getExpRequiredForLevel(getJobLevel(player, job) + 1);
    }

    public void setJobExp(OfflinePlayer player, Jobs job, double exp) {
        config.set(getExpPath(player, job), exp);
        saveConfig();
    }

    public void addJobExp(OfflinePlayer player, Jobs job, double amount) {
        double currentExp = getJobExp(player, job);
        int currentLevel = getJobLevel(player, job);
        double newExp = currentExp + amount;

        while (newExp >= Jobs.getExpRequiredForLevel(currentLevel + 1)) {
            newExp -= Jobs.getExpRequiredForLevel(currentLevel + 1);
            currentLevel++;
            setJobLevel(player, job, currentLevel);
        }
        if (player.isOnline()) {
            showExpBar((Player) player, job, amount);
        }
        setJobExp(player, job, newExp);
    }

    public void subtractJobExp(OfflinePlayer player, Jobs job, double amount) {
        double currentExp = getJobExp(player, job);
        int currentLevel = getJobLevel(player, job);
        double newExp = currentExp - amount;

        while (newExp < 0 && currentLevel > 0) {
            currentLevel--;
            newExp += Jobs.getExpRequiredForLevel(currentLevel + 1);
            setJobLevel(player, job, currentLevel);
        }
        setJobExp(player, job, Math.max(0, newExp));
    }
}
