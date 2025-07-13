package xyz.jungha.job.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public enum Jobs {
    FARMER("농부"),
    MINER("광부"),
    FISHER("어부"),
    CHEF("요리사"),
    NONE("없음");

    private final String displayName;

    private static final Map<String, Jobs> BY_DISPLAY_NAME = Arrays.stream(values())
            .collect(Collectors.toUnmodifiableMap(Jobs::getDisplayName, Function.identity()));

    Jobs(String displayName) {
        this.displayName = displayName;
    }

    public String getKey() {
        return name().toLowerCase();
    }

    public static Jobs fromDisplayName(String name) {
        return BY_DISPLAY_NAME.get(name);
    }

    public static double getExpRequiredForLevel(int level) {
        if (level < 1) return 0;
        if (level < 20) {
            return 100 + (level * (level + 1));
        } else if (level < 50) {
            return 100 + 420 + (int)(level * (level + 1) * 1.5);
        } else {
            return 100 + 420 + 3825 + (int)(level * (level + 1) * 2.5);
        }
    }
}