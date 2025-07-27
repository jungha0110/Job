package xyz.jungha.job.command.exp;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import xyz.jungha.job.command.SubCommand;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GetExpCommand implements SubCommand {

    private final JobService jobService;
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public GetExpCommand(JobService jobService) {
        this.jobService = jobService;
    }

    @Override
    public String getName() {
        return "확인";
    }

    @Override
    public String getUsage() {
        return "[플레이어]";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("job.exp.get");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length != 1) {
            return false;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);

        sender.sendMessage(MINI_MESSAGE.deserialize(":blue_ex: <#9edaf4>" + player.getName() + "님의 직업 경험치"));
        Arrays.stream(Jobs.values())
                .filter(job -> job != Jobs.NONE)
                .map(job -> String.format("ㄴ <#9edaf4>%s : <blue>%.2f <#9edaf4>/ <blue>%.2f <blue>(레벨 %d)", job.getDisplayName(), jobService.getJobExp(player, job), jobService.getJobMaxExp(player, job), jobService.getJobLevel(player, job)))
                .forEach(message -> sender.sendMessage(MINI_MESSAGE.deserialize(message)));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toList());
        }
        return List.of();
    }
}