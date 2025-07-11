package xyz.jungha.job.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import xyz.jungha.job.command.level.*;
import xyz.jungha.job.service.JobService;

import java.util.*;
import java.util.stream.Collectors;

public class LevelCommand implements SubCommand {

    private final Map<String, SubCommand> levelSubCommands;
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public LevelCommand(JobService jobService) {
        this.levelSubCommands = List.of(
                new AddCommand(jobService),
                new SetCommand(jobService),
                new SubtractCommand(jobService),
                new GetCommand(jobService)
        ).stream().collect(Collectors.toUnmodifiableMap(SubCommand::getName, subCommand -> subCommand));
    }

    @Override
    public String getName() {
        return "레벨";
    }

    @Override
    public String getUsage() {
        return "[추가|설정|감소|확인] ...";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return true;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            showHelp(sender, "레벨", levelSubCommands);
            return true;
        }
        SubCommand subCommand = levelSubCommands.get(args[0]);
        if (subCommand == null) {
            showHelp(sender, "레벨", levelSubCommands);
            return true;
        }
        if (!subCommand.hasPermission(sender)) {
            sender.sendMessage(MINI_MESSAGE.deserialize("[<gold>직업<white>] <red>당신은 권한이 없습니다."));
            return true;
        }
        return subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0],
                    levelSubCommands.keySet().stream()
                            .filter(name -> levelSubCommands.get(name).hasPermission(sender))
                            .toList(),
                    new ArrayList<>());
        }
        SubCommand subCommand = levelSubCommands.get(args[0].toLowerCase());
        if (subCommand != null && subCommand.hasPermission(sender)) {
            return subCommand.onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        return Collections.emptyList();
    }

    private void showHelp(CommandSender sender, String parentCommand, Map<String, SubCommand> commands) {
        sender.sendMessage(MINI_MESSAGE.deserialize("[<gold>직업<white>] " + parentCommand + " 도움말"));
        commands.values().stream()
                .filter(sub -> sub.hasPermission(sender))
                .forEach(sub -> sender.sendMessage("- /직업 " + parentCommand + " %s %s".formatted(sub.getName(), sub.getUsage())));
    }
}
