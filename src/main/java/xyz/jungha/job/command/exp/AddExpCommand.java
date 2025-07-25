package xyz.jungha.job.command.exp;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

public class AddExpCommand extends ExpSubCommand {

    public AddExpCommand(JobService jobService) {
        super(jobService);
    }

    @Override
    public String getName() {
        return "추가";
    }

    @Override
    public String getUsage() {
        return "[플레이어] [직업] [경험치]";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("job.exp.add");
    }

    @Override
    protected boolean isValidAmount(double amount, CommandSender sender) {
        if (amount <= 0) {
            sender.sendMessage(MINI_MESSAGE.deserialize("[<gold>직업<white>] <red>증가량은 0 초과이어야 합니다."));
            return false;
        }
        return true;
    }

    @Override
    protected void applyChange(OfflinePlayer player, Jobs job, double amount) {
        jobService.addJobExp(player, job, amount);
    }

    @Override
    protected String getSuccessMessage(OfflinePlayer player, Jobs job, double amount) {
        return String.format("[<gold>직업<white>] <green>%s</green>님의 <yellow>%s</yellow> 직업 경험치를 <gold>+%d</gold> 만큼 증가시켰습니다.", player.getName(), job.getDisplayName(), amount);
    }
}