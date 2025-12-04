package org.shiyi.moocworknodify.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Jackson配置
 *
 * @author ShiYi
 */
@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().apply {
            // 注册Kotlin模块
            registerKotlinModule()

            // 忽略未知属性
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

            // 格式化输出
            configure(SerializationFeature.INDENT_OUTPUT, true)

            // 不序列化null值
            configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false)
        }
    }
}


