package xyz.jungha.job.command.level;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

public class SetCommand extends LevelSubCommand {

    public SetCommand(JobService jobService) {
        super(jobService);
    }

    @Override
    public String getName() {
        return "설정";
    }

    @Override
    public String getUsage() {
        return "[플레이어] [직업] [레벨]";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("job.level.set");
    }

    @Override
    protected boolean isValidAmount(double amount, CommandSender sender) {
        if (amount < 0) {
            sender.sendMessage(MINI_MESSAGE.deserialize("[<gold>직업<white>] <red>레벨은 0 이상이어야 합니다."));
            return false;
        }
        return true;
    }

    @Override
    protected void applyChange(OfflinePlayer player, Jobs job, double amount) {
        jobService.setJobLevel(player, job, (int) amount);
    }

    @Override
    protected String getSuccessMessage(OfflinePlayer player, Jobs job, double amount) {
        return String.format("[<gold>직업<white>] <green>%s</green>님의 <yellow>%s</yellow> 직업 레벨을 <gold>%d</gold>로 설정했습니다.", player.getName(), job.getDisplayName(), (int) amount);
    }
}
