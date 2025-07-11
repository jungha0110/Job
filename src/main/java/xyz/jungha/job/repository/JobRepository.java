package xyz.jungha.job.repository;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.jungha.job.Job;
import xyz.jungha.job.enums.Jobs;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class JobRepository {

    private final Job plugin;
    private File file;
    private FileConfiguration config;

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

    public boolean contains(OfflinePlayer player) {
        return config.contains(player.getUniqueId().toString());
    }

    public int getJobLevel(OfflinePlayer player, Jobs job) {
        return config.getInt(getLevelPath(player, job), 0);
    }

    public void setJobLevel(OfflinePlayer player, Jobs job, int level) {
        config.set(getLevelPath(player, job), level);
    }

    public void addJobLevel(OfflinePlayer player, Jobs job, int amount) {
        int current = getJobLevel(player, job);
        setJobLevel(player, job, current + amount);
    }

    public void subtractJobLevel(OfflinePlayer player, Jobs job, int amount) {
        int current = getJobLevel(player, job);
        setJobLevel(player, job, Math.max(0, current - amount));
    }

    public int getJobExp(OfflinePlayer player, Jobs job) {
        return config.getInt(getExpPath(player, job), 0);
    }

    public void setJobExp(OfflinePlayer player, Jobs job, int exp) {
        config.set(getExpPath(player, job), exp);
    }

    public void addJobExp(OfflinePlayer player, Jobs job, int amount) {
        int currentExp = getJobExp(player, job);
        int currentLevel = getJobLevel(player, job);
        int newExp = currentExp + amount;

        while (newExp >= Jobs.getExpRequiredForLevel(currentLevel + 1)) {
            newExp -= Jobs.getExpRequiredForLevel(currentLevel + 1);
            currentLevel++;
            setJobLevel(player, job, currentLevel);
        }
        setJobExp(player, job, newExp);
    }

    public void subtractJobExp(OfflinePlayer player, Jobs job, int amount) {
        int currentExp = getJobExp(player, job);
        int currentLevel = getJobLevel(player, job);
        int newExp = currentExp - amount;

        while (newExp < 0 && currentLevel > 0) {
            currentLevel--;
            newExp += Jobs.getExpRequiredForLevel(currentLevel + 1);
            setJobLevel(player, job, currentLevel);
        }
        setJobExp(player, job, Math.max(0, newExp));
    }
}