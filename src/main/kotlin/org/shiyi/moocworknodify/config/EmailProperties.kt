package org.shiyi.moocworknodify.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * 邮件配置属性
 *
 * @author ShiYi
 */
@Component
@ConfigurationProperties(prefix = "notification.email")
data class EmailProperties(
    /**
     * 是否启用邮件通知
     */
    var enabled: Boolean = true,

    /**
     * 收件人邮箱列表
     */
    var recipients: List<String> = emptyList(),

    /**
     * 邮件主题前缀
     */
    var subjectPrefix: String = "[MOOC作业提醒]"
)

