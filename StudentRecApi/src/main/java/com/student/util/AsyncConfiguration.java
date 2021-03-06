package com.student.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import java.util.concurrent.Executor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {
	 @Bean (name = "taskExecutor")
	    public Executor taskExecutor() {
	        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(2);
	        executor.setMaxPoolSize(2);
	        executor.setQueueCapacity(100);
	        executor.setThreadNamePrefix("CarThread-");
	        executor.initialize();
	        return executor;
	    }
}
