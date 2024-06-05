package com.demiphea.config;

import com.demiphea.service.inf.MessageQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * ScheduleConfig
 *
 * @author demiphea
 * @since 17.0.9
 */
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleConfig {
    private final MessageQueueService messageQueueService;

    @Scheduled(cron = "0 0 0 * * *")
    public void syncES() {
        messageQueueService.syncES();
    }
}
