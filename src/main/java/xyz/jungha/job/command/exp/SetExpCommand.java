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
    protected boolean isValidAmount(double amount, CommandSender sender) {
        if (amount < 0) {
            sender.sendMessage(MINI_MESSAGE.deserialize(":red_ex: <#f9cccc>경험치는 <red>0 이상이어야 합니다."));
            return false;
        }
        return true;
    }

    @Override
    protected void applyChange(OfflinePlayer player, Jobs job, double amount) {
        jobService.setJobExp(player, job, amount);
    }

    @Override
    protected String getSuccessMessage(OfflinePlayer player, Jobs job, double amount) {
        return String.format(":green: <#d5f9cc>%s님의 <green>%s 직업 경험치<#d5f9cc>를 <green>%f<#d5f9cc>로 설정했습니다.", player.getName(), job.getDisplayName(), amount);
    }
}