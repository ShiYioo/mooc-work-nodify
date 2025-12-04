package org.shiyi.moocworknodify.service

import org.shiyi.moocworknodify.config.MoocProperties
import org.shiyi.moocworknodify.model.HomeworkReminder
import org.shiyi.moocworknodify.model.MoocResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * 作业提醒服务
 * 负责检查作业截止时间并生成提醒信息
 *
 * @author ShiYi
 */
@Service
class HomeworkReminderService(
    private val moocApiService: MoocApiService,
    private val moocProperties: MoocProperties
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * 检查所有配置的课程，获取需要提醒的作业
     *
     * @return 需要提醒的作业列表
     */
    fun checkHomeworkDeadlines(): List<HomeworkReminder> {
        if (moocProperties.termIds.isEmpty()) {
            logger.warn("未配置任何课程ID (mooc.term-ids)，跳过作业检查")
            return emptyList()
        }

        logger.info("开始检查作业截止时间，课程数量: {}", moocProperties.termIds.size)

        val allReminders = mutableListOf<HomeworkReminder>()

        moocProperties.termIds.forEach { termId ->
            val courseInfo = moocApiService.getCourseInfo(termId)
            if (courseInfo != null) {
                val reminders = extractHomeworkReminders(courseInfo)
                allReminders.addAll(reminders)
            }
        }

        logger.info("作业检查完成，共找到{}个需要提醒的作业", allReminders.size)

        return allReminders
    }

    /**
     * 从课程信息中提取需要提醒的作业
     *
     * @param moocResponse MOOC API响应数据
     * @return 需要提醒的作业列表
     */
    private fun extractHomeworkReminders(moocResponse: MoocResponse): List<HomeworkReminder> {
        val reminders = mutableListOf<HomeworkReminder>()
        val now = LocalDateTime.now()


        val mocTermDto = moocResponse.result?.mocTermDto
        if (mocTermDto == null) {
            logger.warn("mocTermDto为null，无法解析作业信息。result={}", moocResponse.result)
            return reminders
        }

        val courseName = mocTermDto.courseName ?: "未知课程"
        logger.info("课程名称: {}，章节数量: {}", courseName, mocTermDto.chapters.size)

        mocTermDto.chapters.forEach { chapter ->
            logger.info("检查章节: {}，作业数量: {}", chapter.name, chapter.homeworks.size)
            chapter.homeworks.forEach { homework ->
                val test = homework.test
                if (test == null) {
                    logger.info("作业 {} 没有test信息，跳过", homework.name)
                    return@forEach
                }

                val deadline = Instant.ofEpochMilli(test.deadline)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()

                val hoursUntilDeadline = ChronoUnit.HOURS.between(now, deadline)

                // 检查是否在提醒时间点
                val shouldRemind = moocProperties.reminderHours.any { reminderHour ->
                    hoursUntilDeadline in (reminderHour - 1)..(reminderHour + 1)
                }

                if (shouldRemind && hoursUntilDeadline >= 0) {
                    val isCompleted = test.userScore != null && test.userScore > 0

                    val reminder = HomeworkReminder(
                        homeworkId = homework.id,
                        homeworkName = homework.name ?: "未命名作业",
                        chapterName = chapter.name ?: "未知章节",
                        courseName = courseName,
                        deadline = deadline,
                        remainingHours = hoursUntilDeadline,
                        totalScore = test.totalScore,
                        userScore = test.userScore,
                        isCompleted = isCompleted
                    )

                    reminders.add(reminder)

                    logger.info(
                        "发现需要提醒的作业: {} - {} - {}，剩余{}小时，完成状态: {}",
                        courseName,
                        chapter.name,
                        homework.name,
                        hoursUntilDeadline,
                        if (isCompleted) "已完成" else "未完成"
                    )
                }
            }
        }

        return reminders
    }
}

