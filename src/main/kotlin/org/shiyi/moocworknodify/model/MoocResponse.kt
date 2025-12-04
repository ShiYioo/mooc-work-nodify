package org.shiyi.moocworknodify.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * MOOC API响应数据模型
 *
 * @author ShiYi
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class MoocResponse(
    val code: Int = 0,
    val result: MocTermResult? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MocTermResult(
    val mocTermDto: MocTermDto? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MocTermDto(
    val id: Long = 0,
    val courseId: Long = 0,
    val courseName: String? = null,
    val startTime: Long = 0,
    val endTime: Long = 0,
    val chapters: List<Chapter> = emptyList()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Chapter(
    val id: Long = 0,
    val name: String? = null,
    val homeworks: List<Homework> = emptyList()
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Homework(
    val id: Long = 0,
    val name: String? = null,
    val termId: Long = 0,
    val chapterId: Long = 0,
    val releaseTime: Long = 0,
    val test: Test? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Test(
    val id: Long = 0,
    val name: String? = null,
    val deadline: Long = 0,
    val type: Int = 0,
    val totalScore: Double? = null,
    val userScore: Double? = null,
    val evaluateStart: Long? = null,
    val evaluateEnd: Long? = null
)

