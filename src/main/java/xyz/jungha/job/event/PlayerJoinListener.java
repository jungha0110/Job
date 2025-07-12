package xyz.jungha.job.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

import java.util.Arrays;

public class PlayerJoinListener implements Listener {

    private final JobService jobService;

    public PlayerJoinListener(JobService jobService) {
        this.jobService = jobService;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            Arrays.stream(Jobs.values())
                    .filter(job -> job != Jobs.NONE)
                    .forEach(job -> jobService.setJobLevel(player, job, 1));
        }
    }
}
