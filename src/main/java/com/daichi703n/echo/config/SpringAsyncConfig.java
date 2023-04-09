package com.daichi703n.echo.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class SpringAsyncConfig {
  
  @Bean(name = "asyncExecutor")
  public Executor getAsyncExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setCorePoolSize(4);
    threadPoolTaskExecutor.setQueueCapacity(4);
    threadPoolTaskExecutor.setMaxPoolSize(40);
    threadPoolTaskExecutor.initialize();
    return threadPoolTaskExecutor;
  }
}
