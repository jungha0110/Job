package xyz.jungha.job.event;

import com.github.teamhungry22.addcook.api.event.CookCompleteEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

public class CookCompleteListener implements Listener {

    private final JobService jobService;

    public CookCompleteListener(JobService jobService) {
        this.jobService = jobService;
    }

    @EventHandler
    public void onCookComplete(CookCompleteEvent event) {
        jobService.addJobExp(event.getPlayer(), Jobs.CHEF, jobService.getConfig().getDouble("chef-exp"));
    }
}
