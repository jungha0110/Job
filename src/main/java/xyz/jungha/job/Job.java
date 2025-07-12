package xyz.jungha.job;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.jungha.job.command.JobCommand;
import xyz.jungha.job.event.PlayerJoinListener;
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
        this.jobRepo = new JobRepository(this);
        this.jobService = new JobService(this.jobRepo);

        getCommand("직업").setExecutor(new JobCommand(jobService));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(jobService), this);
    }

    @Override
    public void onDisable() {
        jobRepo.saveConfig();
    }
}