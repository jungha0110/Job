package xyz.jungha.job.command.exp;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import xyz.jungha.job.Job;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

public class SubtractExpCommand extends ExpSubCommand {

    public SubtractExpCommand(JobService jobService) {
        super(jobService);
    }

    @Override
    public String getName() {
        return "감소";
    }

    @Override
    public String getUsage() {
        return "[플레이어] [직업] [경험치]";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("job.exp.subtract");
    }

    @Override
    protected boolean isValidAmount(int amount, CommandSender sender) {
        if (amount <= 0) {
            sender.sendMessage(MINI_MESSAGE.deserialize("[<gold>직업<white>] <red>감소량은 1 이상이어야 합니다."));
            return false;
        }
        return true;
    }

    @Override
    protected void applyChange(OfflinePlayer player, Jobs job, int amount) {
        Job.getInstance().getJobRepo().subtractJobExp(player, job, amount);
    }

    @Override
    protected String getSuccessMessage(OfflinePlayer player, Jobs job, int amount) {
        return String.format("[<gold>직업<white>] <green>%s</green>님의 <yellow>%s</yellow> 직업 경험치를 <gold>-%d</gold> 만큼 감소시켰습니다.", player.getName(), job.getDisplayName(), amount);
    }
}
