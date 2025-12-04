package org.shiyi.moocworknodify.service

import org.shiyi.moocworknodify.model.HomeworkReminder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * 待发送邮件队列服务
 * 用于存储因网络原因发送失败的邮件提醒，等待下次一起发送
 *
 * @author ShiYi
 */
@Service
class PendingEmailService {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 待发送的作业提醒队列（线程安全）
     */
    private val pendingReminders: ConcurrentLinkedQueue<HomeworkReminder> = ConcurrentLinkedQueue()

    /**
     * 添加待发送的提醒到队列
     *
     * @param reminders 待发送的作业提醒列表
     */
    fun addPendingReminders(reminders: List<HomeworkReminder>) {
        if (reminders.isEmpty()) return

        reminders.forEach { reminder ->
            // 避免重复添加相同的提醒
            if (!containsReminder(reminder)) {
                pendingReminders.offer(reminder)
            }
        }
        logger.info("已将 {} 个提醒添加到待发送队列，当前队列大小: {}", reminders.size, pendingReminders.size)
    }

    /**
     * 获取并清空所有待发送的提醒
     *
     * @return 待发送的作业提醒列表
     */
    fun drainPendingReminders(): List<HomeworkReminder> {
        val result = mutableListOf<HomeworkReminder>()
        while (true) {
            val reminder = pendingReminders.poll() ?: break
            result.add(reminder)
        }
        if (result.isNotEmpty()) {
            logger.info("从待发送队列中取出 {} 个提醒", result.size)
        }
        return result
    }

    /**
     * 获取待发送队列的大小
     */
    fun getPendingCount(): Int = pendingReminders.size

    /**
     * 检查队列是否包含相同的提醒
     * 根据 homeworkId 判断是否重复
     */
    private fun containsReminder(reminder: HomeworkReminder): Boolean {
        return pendingReminders.any { it.homeworkId == reminder.homeworkId }
    }

    /**
     * 清空待发送队列
     */
    fun clearPending() {
        val count = pendingReminders.size
        pendingReminders.clear()
        if (count > 0) {
            logger.info("已清空待发送队列，共清除 {} 个提醒", count)
        }
    }
}

