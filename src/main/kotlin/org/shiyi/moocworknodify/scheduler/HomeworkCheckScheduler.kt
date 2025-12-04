package org.shiyi.moocworknodify.scheduler

import org.shiyi.moocworknodify.service.EmailNotificationService
import org.shiyi.moocworknodify.service.HomeworkReminderService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * 作业检查定时任务
 * 定期检查作业截止时间并发送提醒
 *
 * @author ShiYi
 */
@Component
class HomeworkCheckScheduler(
    private val homeworkReminderService: HomeworkReminderService,
    private val emailNotificationService: EmailNotificationService
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 每小时检查一次作业截止时间
     *
     * cron表达式: 每小时的第0分钟执行
     */
    @Scheduled(cron = "0 0 * * * ?")
    fun checkHomeworkDeadlines() {
        logger.info("=== 开始执行作业检查定时任务 ===")

        try {
            // 检查作业截止时间
            val reminders = homeworkReminderService.checkHomeworkDeadlines()

            // 发送邮件提醒
            if (reminders.isNotEmpty()) {
                emailNotificationService.sendHomeworkReminders(reminders)
            } else {
                logger.info("当前没有需要提醒的作业")
            }

            logger.info("=== 作业检查定时任务执行完成 ===")
        } catch (e: Exception) {
            logger.error("作业检查定时任务执行失败", e)
        }
    }

    /**
     * 应用启动后延迟1分钟执行首次检查
     * 用于验证配置是否正确
     */
    @Scheduled(initialDelay = 60000, fixedDelay = Long.MAX_VALUE)
    fun initialCheck() {
        logger.info("=== 执行应用启动后的首次作业检查 ===")
        checkHomeworkDeadlines()
    }
}

