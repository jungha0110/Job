package xyz.jungha.job.service;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import xyz.jungha.job.Job;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.repository.JobRepository;

public class JobService {

    private final JobRepository jobRepository;
    private final Job plugin;

    public JobService(Job plugin) {
        this.jobRepository = plugin.getJobRepo();
        this.plugin = plugin;
    }

    public int getJobLevel(OfflinePlayer player, Jobs job) {
        return jobRepository.getJobLevel(player, job);
    }

    public void setJobLevel(OfflinePlayer player, Jobs job, int level) {
        jobRepository.setJobLevel(player, job, level);
    }

    public void addJobLevel(OfflinePlayer player, Jobs job, int amount) {
        jobRepository.addJobLevel(player, job, amount);
    }

    public void subtractJobLevel(OfflinePlayer player, Jobs job, int amount) {
        jobRepository.subtractJobLevel(player, job, amount);
    }

    public double getJobExp(OfflinePlayer player, Jobs job) {
        return jobRepository.getJobExp(player, job);
    }

    public double getJobMaxExp(OfflinePlayer player, Jobs job) {
        return jobRepository.getJobMaxExp(player, job);
    }

    public void setJobExp(OfflinePlayer player, Jobs job, double exp) {
        jobRepository.setJobExp(player, job, exp);
    }

    public void addJobExp(OfflinePlayer player, Jobs job, double amount) {
        jobRepository.addJobExp(player, job, amount);
    }

    public void subtractJobExp(OfflinePlayer player, Jobs job, double amount) {
        jobRepository.subtractJobExp(player, job, amount);
    }

    public boolean hasJobData(OfflinePlayer player) {
        return jobRepository.contains(player);
    }

    public void reloadJobData() {
        jobRepository.loadConfig();
    }

    public FileConfiguration getConfig() {
        return plugin.getConfig();
    }

    public void loadConfig() {
        plugin.reloadConfig();
    }
}

