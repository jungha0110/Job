package xyz.jungha.job.event;

import com.github.teamhungry22.addcrop.core.compatibility.nexo.NexoUtils;
import net.momirealms.customcrops.api.core.mechanic.crop.CropConfig;
import net.momirealms.customcrops.api.event.CropBreakEvent;
import net.momirealms.customcrops.api.event.CropPlantEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import xyz.jungha.job.Job;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CropListener implements Listener {

    private final JobService jobService;
    private final Map<String, Double> farmerExpMap = new HashMap<>();

    public CropListener(JobService jobService) {
        this.jobService = jobService;
        ConfigurationSection farmerExpSection = jobService.getConfig().getConfigurationSection("farmer-exp");
        if (farmerExpSection != null) {
            for (String key : farmerExpSection.getKeys(false)) {
                farmerExpMap.put(key, farmerExpSection.getDouble(key));
            }
        }
    }

    @EventHandler
    public void onCropPlant(CropPlantEvent event) {
        CropConfig cropConfig = event.cropConfig();
        int max = cropConfig.maxPoints();
        int randomValue = 1;
        if (max > 2) {
            int min = 1;
            int maxRange = max - 2;
            randomValue = new Random().nextInt(maxRange) + min;
        }
        event.point(randomValue);
    }

    @EventHandler
    public void onCropBreak(CropBreakEvent event) {
        if (!(event.entityBreaker() instanceof Player player)) return;
        CropConfig cropConfig = event.cropConfig();
        if (cropConfig.stageByID(event.cropStageItemID()).point() < cropConfig.maxPoints()) return;
        Double exp = farmerExpMap.getOrDefault(cropConfig.id(), 0.0);
        if (exp > 0) {
            jobService.addJobExp(player, Jobs.FARMER, exp);
            handleFarmerRewards(event, player);
        }
    }

    private void handleFarmerRewards(CropBreakEvent event, Player player) {
        int level = jobService.getJobLevel(player, Jobs.FARMER);
        Location location = event.location();
        ItemStack item = NexoUtils.getNexoItem(event.cropConfig().id());

        int extraAmount = getFarmerRewardAmount(level);
        if (extraAmount > 0) {
            dropExtraItem(location, item, extraAmount);
        }

        if (level >= 50 && extraAmount != 0) {
            setMoveSpeed(player, 0.12000000178813934);
            Bukkit.getScheduler().runTaskLater(Job.getInstance(), () -> setMoveSpeed(player, 0.10000000149011612), 200L);
        }
    }

    private int getFarmerRewardAmount(int level) {
        if (level >= 100 && chance(0.2)) return 3;
        if (level >= 70 && chance(0.15)) return 3;
        if (level >= 50 && chance(0.15)) return 2;
        if (level >= 30 && chance(0.1)) return 1;
        return 0;
    }

    private void setMoveSpeed(Player player, double value) {
        if (player.getAttribute(Attribute.BLOCK_BREAK_SPEED) != null) {
            player.getAttribute(Attribute.BLOCK_BREAK_SPEED).setBaseValue(value);
        }
    }

    private void dropExtraItem(Location location, ItemStack item, int amount) {
        item.setAmount(amount);
        location.getWorld().dropItemNaturally(location, item);
    }

    private boolean chance(double probability) {
        return Math.random() < probability;
    }
}
