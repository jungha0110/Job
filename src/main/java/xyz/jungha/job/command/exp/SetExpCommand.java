package xyz.jungha.job.command.exp;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

public class SetExpCommand extends ExpSubCommand {

    public SetExpCommand(JobService jobService) {
        super(jobService);
    }

    @Override
    public String getName() {
        return "설정";
    }

    @Override
    public String getUsage() {
        return "[플레이어] [직업] [경험치]";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("job.exp.set");
    }

    @Override
    protected boolean isValidAmount(int amount, CommandSender sender) {
        if (amount < 0) {
            sender.sendMessage(MINI_MESSAGE.deserialize("[<gold>직업<white>] <red>경험치는 0 이상이어야 합니다."));
            return false;
        }
        return true;
    }

    @Override
    protected void applyChange(OfflinePlayer player, Jobs job, int amount) {
        jobService.setJobExp(player, job, amount);
    }

    @Override
    protected String getSuccessMessage(OfflinePlayer player, Jobs job, int amount) {
        return String.format("[<gold>직업<white>] <green>%s</green>님의 <yellow>%s</yellow> 직업 경험치를 <gold>%d</gold>로 설정했습니다.", player.getName(), job.getDisplayName(), amount);
    }
}