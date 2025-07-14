package xyz.jungha.job.event;

import net.momirealms.customfishing.api.event.FishingResultEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

import java.util.HashMap;
import java.util.Map;

public class FishingResultListener implements Listener {

    private final JobService jobService;
    private final Map<String, Double> fisherExpMap = new HashMap<>();

    public FishingResultListener(JobService jobService) {
        this.jobService = jobService;
        ConfigurationSection farmerExpSection = jobService.getConfig().getConfigurationSection("farmer-exp");
        if (farmerExpSection != null) {
            for (String key : farmerExpSection.getKeys(false)) {
                fisherExpMap.put(key, farmerExpSection.getDouble(key));
            }
        }
    }

    @EventHandler
    public void onFishingResult(FishingResultEvent event) {
        if (event.getResult() != FishingResultEvent.Result.SUCCESS) return;
        Double exp = fisherExpMap.getOrDefault(event.getLoot().id(), 0.0);
        if (exp > 0) {
            jobService.addJobExp(event.getPlayer(), Jobs.FISHER, exp);
        }
    }
}
