package xyz.jungha.job;

import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jungha.job.command.JobCommand;
import xyz.jungha.job.event.*;
import xyz.jungha.job.repository.JobRepository;
import xyz.jungha.job.service.JobService;

public class Job extends JavaPlugin {

    @Getter
    private static Job instance;

    @Getter
    private JobRepository jobRepo;

    @Getter
    private JobService jobService;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.jobRepo = new JobRepository(this);
        this.jobService = new JobService(this);

        getCommand("직업").setExecutor(new JobCommand(jobService));

        registerEvents(
                new PlayerJoinListener(jobService),
                new CookCompleteListener(jobService),
                new BlockBreakListener(jobService),
                new CropListener(jobService),
                new FishingListener(jobService)
        );
    }

    @Override
    public void onDisable() {
        jobRepo.saveConfig();
        saveConfig();
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}