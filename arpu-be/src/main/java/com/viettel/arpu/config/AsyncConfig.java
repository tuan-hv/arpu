/*
 * Copyright (C) 2020 Viettel Digital Services. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.arpu.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;


@Configuration
@EnableAsync
@Slf4j
@Getter
@Setter
public class AsyncConfig {


    @Value("${executor.corePoolSize}")
    private String poolSize = "10";
    @Value("${executor.maxPoolSize}")
    private String setMaxPoolSize = "10";

    @Bean("threadPoolTaskExecutor")
    public TaskExecutor getAsyncExecutor() {
        log.info("corePoolSize : " + poolSize + "\nMaxPoolSize : " + setMaxPoolSize);
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Integer.parseInt(poolSize));
        executor.setMaxPoolSize(Integer.parseInt(setMaxPoolSize));
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("Async-");
        return executor;
    }
}
