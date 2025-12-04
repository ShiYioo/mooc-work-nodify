# 运行阶段 - 使用轻量级JRE镜像
FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="ShiYi"
LABEL description="MOOC作业提醒服务 - 自动检查中国大学MOOC平台作业截止时间并发送邮件提醒"
