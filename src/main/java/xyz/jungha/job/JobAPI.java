package xyz.jungha.job;

import org.bukkit.OfflinePlayer;
import xyz.jungha.job.enums.Jobs;

public final class JobAPI {

    private static Job plugin;

    private static Job plugin() {
        if (plugin == null) plugin = Job.getInstance();
        return plugin;
    }

    /**
     * 플레이어의 직업 레벨을 가져옵니다.
     * @param player 플레이어
     * @param job 직업
     * @return 레벨
     */
    public static int getJobLevel(OfflinePlayer player, Jobs job) {
        if (!plugin().getJobService().hasJobData(player)) return 0;
        return plugin().getJobService().getJobLevel(player, job);
    }

    /**
     * 플레이어의 직업 레벨을 설정합니다.
     * @param player 플레이어
     * @param job 직업
     * @param level 레벨
     */
    public static void setJobLevel(OfflinePlayer player, Jobs job, int level) {
        if (!plugin().getJobService().hasJobData(player)) return;
        plugin().getJobService().setJobLevel(player, job, level);
    }

    /**
     * 플레이어의 직업 레벨을 추가합니다.
     * @param player 플레이어
     * @param job 직업
     * @param amount 추가량
     */
    public static void addJobLevel(OfflinePlayer player, Jobs job, int amount) {
        if (!plugin().getJobService().hasJobData(player)) return;
        plugin().getJobService().addJobLevel(player, job, amount);
    }

    /**
     * 플레이어의 직업 레벨 감소합니다.
     * @param player 플레이어
     * @param job 직업
     * @param amount 감소량
     */
    public static void subtractJobLevel(OfflinePlayer player, Jobs job, int amount) {
        if (!plugin().getJobService().hasJobData(player)) return;
        plugin().getJobService().subtractJobLevel(player, job, amount);
    }

    /**
     * 플레이어의 직업 경험치를 가져옵니다.
     * @param player 플레이어
     * @param job 직업
     * @return 경험치
     */
    public static double getJobExp(OfflinePlayer player, Jobs job) {
        if (!plugin().getJobService().hasJobData(player)) return 0;
        return plugin().getJobService().getJobExp(player, job);
    }

    /**
     * 플레이어의 직업 최대 경험치를 가져옵니다.
     * @param player 플레이어
     * @param job 직업
     * @return 최대 경험치
     */
    public static double getJobMaxExp(OfflinePlayer player, Jobs job) {
        if (!plugin().getJobService().hasJobData(player)) return 0;
        return plugin().getJobService().getJobMaxExp(player, job);
    }

    /**
     * 플레이어의 직업 경험치를 설정합니다.
     * @param player 플레이어
     * @param job 직업
     * @param exp 경험치
     */
    public static void setJobExp(OfflinePlayer player, Jobs job, double exp) {
        if (!plugin().getJobService().hasJobData(player)) return;
        plugin().getJobService().setJobExp(player, job, exp);
    }

    /**
     * 플레이어의 직업 경험치를 추가합니다.
     * @param player 플레이어
     * @param job 직업
     * @param amount 추가량
     */
    public static void addJobExp(OfflinePlayer player, Jobs job, double amount) {
        if (!plugin().getJobService().hasJobData(player)) return;
        plugin().getJobService().addJobExp(player, job, amount);
    }

    /**
     * 플레이어의 직업 경험치를 감소합니다.
     * @param player 플레이어
     * @param job 직업
     * @param amount 감소량
     */
    public static void subtractJobExp(OfflinePlayer player, Jobs job, double amount) {
        if (!plugin().getJobService().hasJobData(player)) return;
        plugin().getJobService().subtractJobExp(player, job, amount);
    }
}
