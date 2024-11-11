package dev.amir.vwap_calculator.producer.configuration;

import dev.amir.vwap_calculator.producer.service.TaskScheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Configuration
public class TaskSchedulerConfig {
    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(3);
    }

    @Bean
    public boolean scheduleTasks(ScheduledExecutorService scheduledExecutorService, TaskScheduler scheduledTasks) {
        ScheduledFuture<?> scheduledFuture1 = scheduledExecutorService.scheduleAtFixedRate(scheduledTasks::task1, 0, 1, TimeUnit.SECONDS);
        ScheduledFuture<?> scheduledFuture2 = scheduledExecutorService.scheduleAtFixedRate(scheduledTasks::task2, 0, 1, TimeUnit.SECONDS);
        ScheduledFuture<?> scheduledFuture3 = scheduledExecutorService.scheduleAtFixedRate(scheduledTasks::task3, 0, 1, TimeUnit.SECONDS);

        return scheduledFuture1 != null && scheduledFuture2 != null && scheduledFuture3 != null;
    }
}