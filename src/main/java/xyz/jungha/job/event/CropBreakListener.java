package xyz.jungha.job.event;

import net.momirealms.customcrops.api.core.mechanic.crop.CropConfig;
import net.momirealms.customcrops.api.core.mechanic.crop.CropStageConfig;
import net.momirealms.customcrops.api.event.CropBreakEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

import java.util.HashMap;
import java.util.Map;

public class CropBreakListener implements Listener {

    private final JobService jobService;
    private final Map<String, Double> farmerExpMap = new HashMap<>();

    public CropBreakListener(JobService jobService) {
        this.jobService = jobService;
        ConfigurationSection farmerExpSection = jobService.getConfig().getConfigurationSection("farmer-exp");
        if (farmerExpSection != null) {
            for (String key : farmerExpSection.getKeys(false)) {
                farmerExpMap.put(key, farmerExpSection.getDouble(key));
            }
        }
    }

    @EventHandler
    public void onCropBreak(CropBreakEvent event) {
        if (!(event.entityBreaker() instanceof Player player)) return;
        CropConfig cropConfig = event.cropConfig();
        if (cropConfig.stageByID(event.cropStageItemID()).point() < cropConfig.maxPoints()) return;
        Double exp = farmerExpMap.getOrDefault(cropConfig.id(), 0.0);
        if (exp > 0) {
            jobService.addJobExp(player, Jobs.FARMER, exp);
        }
    }
}
