package org.shiyi.moocworknodify.model

import java.time.LocalDateTime

/**
 * 作业提醒信息
 *
 * @author ShiYi
 */
data class HomeworkReminder(
    val homeworkId: Long,
    val homeworkName: String,
    val chapterName: String,
    val courseName: String,
    val deadline: LocalDateTime,
    val remainingHours: Long,
    val totalScore: Double?,
    val userScore: Double?,
    val isCompleted: Boolean
) {
    /**
     * 获取格式化的剩余时间描述
     */
    fun getFormattedRemainingTime(): String {
        return when {
            remainingHours >= 24 -> "${remainingHours / 24}天${remainingHours % 24}小时"
            remainingHours > 0 -> "${remainingHours}小时"
            else -> "已截止"
        }
    }

    /**
     * 获取完成状态描述
     */
    fun getCompletionStatus(): String {
        return if (isCompleted) {
            "已完成 (得分: ${userScore ?: "未知"}/${totalScore ?: "未知"})"
        } else {
            "未完成"
        }
    }
}

