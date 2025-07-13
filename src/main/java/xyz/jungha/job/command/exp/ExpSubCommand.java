package xyz.jungha.job.command.exp;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import xyz.jungha.job.command.AbstractJobSubCommand;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

public abstract class ExpSubCommand extends AbstractJobSubCommand {

    public ExpSubCommand(JobService jobService) {
        super(jobService);
    }

    @Override
    protected abstract boolean isValidAmount(double amount, CommandSender sender);

    @Override
    protected abstract void applyChange(OfflinePlayer player, Jobs job, double amount);

    @Override
    protected abstract String getSuccessMessage(OfflinePlayer player, Jobs job, double amount);
}