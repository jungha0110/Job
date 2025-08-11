package xyz.jungha.job.event;

import net.momirealms.customfishing.api.BukkitCustomFishingPlugin;
import net.momirealms.customfishing.api.event.FishingResultEvent;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

import java.util.HashMap;
import java.util.Map;

public class FishingListener implements Listener {

    private final JobService jobService;
    private final Map<String, Double> fisherExpMap = new HashMap<>();

    public FishingListener(JobService jobService) {
        this.jobService = jobService;
        ConfigurationSection farmerExpSection = jobService.getConfig().getConfigurationSection("fisher-exp");
        if (farmerExpSection != null) {
            for (String key : farmerExpSection.getKeys(false)) {
                fisherExpMap.put(key, farmerExpSection.getDouble(key));
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (jobService.getJobLevel(event.getPlayer(), Jobs.CHEF) >= 10) {
            event.getHook().setWaitTime((int) (event.getHook().getWaitTime() * 0.5));
        }
    }

    @EventHandler
    public void onFishingResult(FishingResultEvent event) {
        if (event.getResult() != FishingResultEvent.Result.SUCCESS) return;
        String lootId = event.getLoot().id();
        double exp = fisherExpMap.getOrDefault("common", 0.0);
        if (lootId.contains("silver_star")) {
            exp = fisherExpMap.getOrDefault("silver_star", 0.0);
        } else if (lootId.contains("golden_star")) {
            exp = fisherExpMap.getOrDefault("golden_star", 0.0);
        }
        if (exp > 0) {
            jobService.addJobExp(event.getPlayer(), Jobs.FISHER, exp);
            handleFisherRewards(event);
        }
    }

    private void handleFisherRewards(FishingResultEvent event) {
        int level = jobService.getJobLevel(event.getPlayer(), Jobs.FISHER);
        ItemStack item = BukkitCustomFishingPlugin.getInstance()
                .getItemManager()
                .buildInternal(event.getContext(), event.getLoot().id());

        int extraAmount = getFisherRewardAmount(level);
        if (extraAmount > 0) {
            dropExtraItem(event.getPlayer().getLocation(), item, extraAmount);
        }

        if (level >= 50 && chance(0.03)) {
            dropExtraItem(event.getPlayer().getLocation(), item, 1); // 보물 아이템으로 수정
        }
    }

    private int getFisherRewardAmount(int level) {
        if (level == 100 && chance(0.3)) return 3;
        if (level >= 70 && chance(0.2)) return 2;
        if (level >= 50 && chance(0.2)) return 2;
        if (level >= 30 && chance(0.2)) return 1;
        return 0;
    }

    private void dropExtraItem(Location location, ItemStack item, int amount) {
        ItemStack drop = item.clone();
        drop.setAmount(amount);
        location.getWorld().dropItemNaturally(location, drop);
    }

    private boolean chance(double probability) {
        return Math.random() < probability;
    }
}
