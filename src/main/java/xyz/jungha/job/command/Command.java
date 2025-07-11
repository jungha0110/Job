package xyz.jungha.job.command;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public abstract class Command implements TabExecutor {

    protected final Map<String, SubCommand> subCommands;
    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    protected final boolean isPlayerCommand;

    public Command(boolean isPlayerCommand, SubCommand... subCommandList) {
        this.isPlayerCommand = isPlayerCommand;
        this.subCommands = Arrays.stream(subCommandList)
                .collect(Collectors.toUnmodifiableMap(SubCommand::getName, subCommand -> subCommand));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (isPlayerCommand && !(sender instanceof Player)) {
            sender.sendMessage(MINI_MESSAGE.deserialize("[<gold>EconSuite<white>] <red>오직 플레이어만 사용할 수 있습니다."));
            return true;
        }

        if (args.length == 0) {
            showHelp(sender, label);
            return true;
        }

        SubCommand subCommand = subCommands.get(args[0]);
        if (subCommand == null) {
            showHelp(sender, label);
            return true;
        }

        if (!subCommand.hasPermission(sender)) {
            sender.sendMessage(MINI_MESSAGE.deserialize("[<gold>EconSuite<white>] <red>당신은 권한이 없습니다."));
            return true;
        }

        return subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
    }

    private void showHelp(CommandSender sender, String label) {
        sender.sendMessage(MINI_MESSAGE.deserialize("[<gold>EconSuite<white>]"));
        subCommands.values().stream()
                .filter(sub -> sub.hasPermission(sender))
                .forEach(sub -> sender.sendMessage("- /%s %s %s".formatted(label, sub.getName(), sub.getUsage())));
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0],
                    subCommands.keySet().stream()
                            .filter(name -> subCommands.get(name).hasPermission(sender))
                            .toList(),
                    new ArrayList<>());
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand != null && subCommand.hasPermission(sender)) {
            return subCommand.onTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
        }
        return Collections.emptyList();
    }
}
