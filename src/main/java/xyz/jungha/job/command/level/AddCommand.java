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
    protected boolean isValidAmount(double amount, CommandSender sender) {
        if (amount <= 0) {
            sender.sendMessage(MINI_MESSAGE.deserialize(":red_ex: <#f9cccc>증가량은 <red>1 이상<#f9cccc>이어야 합니다."));
            return false;
        }
        return true;
    }

    @Override
    protected void applyChange(OfflinePlayer player, Jobs job, double amount) {
        jobService.addJobLevel(player, job, (int) amount);
    }

    @Override
    protected String getSuccessMessage(OfflinePlayer player, Jobs job, double amount) {
        return String.format(":green: <#d5f9cc>%s님의 <green>%s 직업 레벨<#d5f9cc>을 <green>+%d<#d5f9cc> 만큼 증가시켰습니다.", player.getName(), job.getDisplayName(), (int) amount);
    }
}
