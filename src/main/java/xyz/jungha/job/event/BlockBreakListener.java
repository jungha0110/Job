package xyz.jungha.job.event;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
        Double exp = minerExpMap.get(blockType);
        if (exp != null) {
            jobService.addJobExp(event.getPlayer(), Jobs.MINER, exp);
        }
    }
}
