package xyz.jungha.job.event;

import com.github.teamhungry22.addcook.api.event.CookCompleteEvent;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
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
        handleChefRewards(event);
    }

    private void handleChefRewards(CookCompleteEvent event) {
        int level = jobService.getJobLevel(event.getPlayer(), Jobs.CHEF);
        ItemStack item = event.getRecipe().getIcon();
        Location location = event.getLocation();

        int extraAmount = getChefRewardAmount(level);
        if (extraAmount > 0) {
            dropExtraItem(location, item, extraAmount);
        }

        if (level >= 10 && chance(0.1)) {
            dropExtraItem(location, item, 1);
        }
    }

    private int getChefRewardAmount(int level) {
        if (level >= 100 && chance(0.7)) return 2;
        if (level >= 70 && chance(0.5)) return 2;
        if (level >= 50 && chance(0.3)) return 1;
        if (level >= 30 && chance(0.2)) return 1;
        return 0;
    }

    private void dropExtraItem(Location location, ItemStack item, int amount) {
        item.setAmount(amount);
        location.getWorld().dropItemNaturally(location, item);
    }

    private boolean chance(double probability) {
        return Math.random() < probability;
    }
}
