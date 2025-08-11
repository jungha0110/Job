package xyz.jungha.job.event;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import xyz.jungha.job.Job;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

import java.util.HashMap;
import java.util.Map;

public class BlockBreakListener implements Listener {

    private final JobService jobService;
    private final Map<String, Double> minerExpMap = new HashMap<>();

    public BlockBreakListener(JobService jobService) {
        this.jobService = jobService;
        ConfigurationSection minerExpSection = jobService.getConfig().getConfigurationSection("miner-exp");
        if (minerExpSection != null) {
            for (String key : minerExpSection.getKeys(false)) {
                minerExpMap.put(key.toUpperCase(), minerExpSection.getDouble(key));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        String blockType = event.getBlock().getType().name();
        Double exp = minerExpMap.getOrDefault(blockType, 0.0);
        if (exp > 0) {
            jobService.addJobExp(event.getPlayer(), Jobs.MINER, exp);
            handleMinerRewards(event);
        }
    }

    private void handleMinerRewards(BlockBreakEvent event) {
        int level = jobService.getJobLevel(event.getPlayer(), Jobs.MINER);

        if (level == 100 && chance(0.2)) {
            dropExtraItem(event, 2);
        } else if (level >= 70 && chance(0.15)) {
            dropExtraItem(event, 2);
        } else if (level >= 50) {
            if (chance(0.15)) dropExtraItem(event, 1);
            if (chance(0.05)) boostBlockBreakSpeed(event);
        } else if (level >= 30 && chance(0.1)) {
            dropExtraItem(event, 1);
        }

        double speed = level >= 10 ? 1.2 : 1.0;
        setBlockBreakSpeed(event, speed);
    }

    private boolean chance(double probability) {
        return Math.random() < probability;
    }

    private void dropExtraItem(BlockBreakEvent event, int extraAmount) {
        event.getBlock().getDrops().forEach(drop -> {
            ItemStack bonus = drop.clone();
            bonus.setAmount(drop.getAmount() + extraAmount);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), bonus);
        });
    }

    private void setBlockBreakSpeed(BlockBreakEvent event, double value) {
        if (event.getPlayer().getAttribute(Attribute.BLOCK_BREAK_SPEED) != null) {
            event.getPlayer().getAttribute(Attribute.BLOCK_BREAK_SPEED).setBaseValue(value);
        }
    }

    private void boostBlockBreakSpeed(BlockBreakEvent event) {
        setBlockBreakSpeed(event, 1.4);
        Bukkit.getScheduler().runTaskLater(Job.getInstance(), () -> setBlockBreakSpeed(event, 1.2), 400L);
    }
}
