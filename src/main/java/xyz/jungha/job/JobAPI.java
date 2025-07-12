package xyz.jungha.job;

import org.bukkit.OfflinePlayer;
import xyz.jungha.job.enums.Jobs;
import xyz.jungha.job.service.JobService;

public final class JobAPI {

    private static JobService jobService;

    private static JobService jobService() {
        if (jobService == null) jobService = Job.getInstance().getJobService();
        return jobService;
    }

    /**
     * 플레이어의 직업 레벨을 가져옵니다.
     * @param player 플레이어
     * @param job 직업
     * @return 레벨
     */
    public static int getJobLevel(OfflinePlayer player, Jobs job) {
        if (!jobService().hasJobData(player)) return 0;
        return jobService().getJobLevel(player, job);
    }

    /**
     * 플레이어의 직업 레벨을 설정합니다.
     * @param player 플레이어
     * @param job 직업
     * @param level 레벨
     */
    public static void setJobLevel(OfflinePlayer player, Jobs job, int level) {
        if (!jobService().hasJobData(player)) return;
        jobService().setJobLevel(player, job, level);
    }

    /**
     * 플레이어의 직업 레벨을 추가합니다.
     * @param player 플레이어
     * @param job 직업
     * @param amount 추가량
     */
    public static void addJobLevel(OfflinePlayer player, Jobs job, int amount) {
        if (!jobService().hasJobData(player)) return;
        jobService().addJobLevel(player, job, amount);
    }

    /**
     * 플레이어의 직업 레벨 감소합니다.
     * @param player 플레이어
     * @param job 직업
     * @param amount 감소량
     */
    public static void subtractJobLevel(OfflinePlayer player, Jobs job, int amount) {
        if (!jobService().hasJobData(player)) return;
        jobService().subtractJobLevel(player, job, amount);
    }

    /**
     * 플레이어의 직업 경험치를 가져옵니다.
     * @param player 플레이어
     * @param job 직업
     * @return 경험치
     */
    public static int getJobExp(OfflinePlayer player, Jobs job) {
        if (!jobService().hasJobData(player)) return 0;
        return jobService().getJobExp(player, job);
    }

    /**
     * 플레이어의 직업 최대 경험치를 가져옵니다.
     * @param player 플레이어
     * @param job 직업
     * @return 최대 경험치
     */
    public static int getJobMaxExp(OfflinePlayer player, Jobs job) {
        if (!jobService().hasJobData(player)) return 0;
        return Jobs.getExpRequiredForLevel(jobService.getJobLevel(player, job) + 1);
    }

    /**
     * 플레이어의 직업 경험치를 설정합니다.
     * @param player 플레이어
     * @param job 직업
     * @param exp 경험치
     */
    public static void setJobExp(OfflinePlayer player, Jobs job, int exp) {
        if (!jobService().hasJobData(player)) return;
        jobService().setJobExp(player, job, exp);
    }

    /**
     * 플레이어의 직업 경험치를 추가합니다.
     * @param player 플레이어
     * @param job 직업
     * @param amount 추가량
     */
    public static void addJobExp(OfflinePlayer player, Jobs job, int amount) {
        if (!jobService().hasJobData(player)) return;
        jobService().addJobExp(player, job, amount);
    }

    /**
     * 플레이어의 직업 경험치를 감소합니다.
     * @param player 플레이어
     * @param job 직업
     * @param amount 감소량
     */
    public static void subtractJobExp(OfflinePlayer player, Jobs job, int amount) {
        if (!jobService().hasJobData(player)) return;
        jobService().subtractJobExp(player, job, amount);
    }
}
