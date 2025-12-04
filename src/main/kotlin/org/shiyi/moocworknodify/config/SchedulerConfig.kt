package org.shiyi.moocworknodify.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.scheduling.config.ScheduledTaskRegistrar

/**
 * 定时任务配置
 * 配置定时任务的线程池，确保任务能够稳定执行
 *
 * @author ShiYi
 */
@Configuration
@EnableScheduling
class SchedulerConfig : SchedulingConfigurer {

    /**
     * 配置定时任务线程池
     *
     * @return 线程池任务调度器
     */
    @Bean
    fun taskScheduler(): ThreadPoolTaskScheduler {
        return ThreadPoolTaskScheduler().apply {
            poolSize = 5 // 线程池大小
            setThreadNamePrefix("mooc-scheduler-") // 线程名称前缀
            setWaitForTasksToCompleteOnShutdown(true) // 关闭时等待任务完成
            setAwaitTerminationSeconds(60) // 等待任务完成的超时时间
            initialize()
        }
    }

    /**
     * 配置定时任务注册器
     */
    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.setTaskScheduler(taskScheduler())
    }
}

