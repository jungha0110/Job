package xyz.jungha.job.command.level;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

public class AddCommand extends LevelSubCommand {

    public AddCommand(JobService jobService) {
        super(jobService);
    }

    @Override
    public String getName() {
        return "추가";
    }

    @Override
    public String getUsage() {
        return "[플레이어] [직업] [레벨]";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("job.level.add");
    }

    @Override
    protected boolean isValidAmount(int amount, CommandSender sender) {
        if (amount <= 0) {
            sender.sendMessage(MINI_MESSAGE.deserialize("[<gold>직업<white>] <red>증가량은 1 이상이어야 합니다."));
            return false;
        }
        return true;
    }

    @Override
    protected void applyChange(OfflinePlayer player, Jobs job, int amount) {
        jobService.addJobLevel(player, job, amount);
    }

    @Override
    protected String getSuccessMessage(OfflinePlayer player, Jobs job, int amount) {
        return String.format("[<gold>직업<white>] <green>%s</green>님의 <yellow>%s</yellow> 직업 레벨을 <gold>+%d</gold> 만큼 증가시켰습니다.", player.getName(), job.getDisplayName(), amount);
    }
}
