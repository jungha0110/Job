package xyz.jungha.job.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;
import xyz.jungha.job.command.exp.*;
import xyz.jungha.job.service.JobService;

import java.util.*;
import java.util.stream.Collectors;

public class ExpCommand implements SubCommand {

    private final Map<String, SubCommand> expSubCommands;
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public ExpCommand(JobService jobService) {
        this.expSubCommands = List.of(
                new AddExpCommand(jobService),
                new SetExpCommand(jobService),
                new SubtractExpCommand(jobService),
                new GetExpCommand(jobService)
        ).stream().collect(Collectors.toUnmodifiableMap(SubCommand::getName, subCommand -> subCommand));
    }

    @Override
    public String getName() {
        return "경험치";
    }

    @Override
    public String getUsage() {
        return "[추가|설정|감소|확인] ...";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return true; // 권한은 각 서브 커맨드에서 처리
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            showHelp(sender, "경험치", expSubCommands);
            return true;
        }
        SubCommand subCommand = expSubCommands.get(args[0]);
        if (subCommand == null) {
            showHelp(sender, "경험치", expSubCommands);
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
                    expSubCommands.keySet().stream()
                            .filter(name -> expSubCommands.get(name).hasPermission(sender))
                            .toList(),
                    new ArrayList<>());
        }
        SubCommand subCommand = expSubCommands.get(args[0].toLowerCase());
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
