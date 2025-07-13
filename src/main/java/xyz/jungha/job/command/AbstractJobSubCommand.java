package xyz.jungha.job.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractJobSubCommand implements SubCommand {

    protected static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    protected final JobService jobService;

    public AbstractJobSubCommand(JobService jobService) {
        this.jobService = jobService;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length != 3) {
            return false;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        Jobs job = Jobs.fromDisplayName(args[1]);
        if (job == null || job == Jobs.NONE) {
            sender.sendMessage(MINI_MESSAGE.deserialize("[<gold>직업<white>] <red>존재하지 않는 직업입니다."));
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(MINI_MESSAGE.deserialize("[<gold>직업<white>] <red>유효한 숫자를 입력해주세요."));
            return true;
        }

        if (!isValidAmount(amount, sender)) {
            return true;
        }

        applyChange(target, job, amount);
        sender.sendMessage(MINI_MESSAGE.deserialize(getSuccessMessage(target, job, amount)));
        return true;
    }

    protected abstract boolean isValidAmount(double amount, CommandSender sender);

    protected abstract void applyChange(OfflinePlayer player, Jobs job, double amount);

    protected abstract String getSuccessMessage(OfflinePlayer player, Jobs job, double amount);

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).collect(Collectors.toList());
        } else if (args.length == 2) {
            return Arrays.stream(Jobs.values())
                    .filter(job -> job != Jobs.NONE)
                    .map(Jobs::getDisplayName)
                    .collect(Collectors.toList());
        }
        return List.of();
    }
}