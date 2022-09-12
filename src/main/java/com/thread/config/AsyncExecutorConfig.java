package com.thread.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@Configuration
public class AsyncExecutorConfig {

    @Value("${ThreadPool.corePoolSize}")
    private Integer corePoolSize;
    @Value("${ThreadPool.maxPoolSize}")
    private Integer maxPoolSize;
    @Value("${ThreadPool.queueCapacity}")
    private Integer queueCapacity;
    @Value("${ThreadPool.namePrefix}")    
    private String namePrefix;    
    @Value("${ThreadPool.keepAliveSeconds}")
    private Integer keepAliveSeconds;    
	
    @Bean(name = "asyncTaskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(namePrefix);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }	
	
}
