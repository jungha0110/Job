package xyz.jungha.job.service;

import org.bukkit.OfflinePlayer;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.repository.JobRepository;

public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
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

    public int getJobExp(OfflinePlayer player, Jobs job) {
        return jobRepository.getJobExp(player, job);
    }

    public void setJobExp(OfflinePlayer player, Jobs job, int exp) {
        jobRepository.setJobExp(player, job, exp);
    }

    public void addJobExp(OfflinePlayer player, Jobs job, int amount) {
        jobRepository.addJobExp(player, job, amount);
    }

    public void subtractJobExp(OfflinePlayer player, Jobs job, int amount) {
        jobRepository.subtractJobExp(player, job, amount);
    }

    public boolean hasJobData(OfflinePlayer player) {
        return jobRepository.contains(player);
    }

    public void saveJobData() {
        jobRepository.saveConfig();
    }
}
