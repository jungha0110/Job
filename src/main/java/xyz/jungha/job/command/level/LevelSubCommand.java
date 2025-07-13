package xyz.jungha.job.command.level;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import xyz.jungha.job.command.AbstractJobSubCommand;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

public abstract class LevelSubCommand extends AbstractJobSubCommand {

    public LevelSubCommand(JobService jobService) {
        super(jobService);
    }

    @Override
    protected abstract boolean isValidAmount(double amount, CommandSender sender);

    @Override
    protected abstract void applyChange(OfflinePlayer player, Jobs job, double amount);

    @Override
    protected abstract String getSuccessMessage(OfflinePlayer player, Jobs job, double amount);
}
