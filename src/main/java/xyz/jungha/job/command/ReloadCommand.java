package xyz.jungha.job.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import xyz.jungha.job.service.JobService;

import java.util.List;

public class ReloadCommand implements SubCommand {

    private final JobService jobService;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ReloadCommand(JobService jobService) {
        this.jobService = jobService;
    }

    @Override
    public String getName() {
        return "리로드";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("job.reload");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        jobService.reloadJobData();
        jobService.loadConfig();
        sender.sendMessage(miniMessage.deserialize(":green_ex: <#d5f9cc>콘피그를 <green>리로드<#d5f9cc>했습니다."));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
