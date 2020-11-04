package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfiguration {
    @Bean
    public TaskScheduler poolScheduler(){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        // JVM 에서 이용가능한 코어 개수의 2배 만큼 스레드 할당한다.
        // Intel 하이퍼스레딩 기술 지원 가능. 물리적 코어 하나당 스레드 2개 할당해 성능을 높인다.
        // 따라서 물리적 코어개수의 두배인 "논리적 코어의 개수" 만큼 Pool 크기를 지정한다.
        threadPoolTaskScheduler.setPoolSize(Runtime.getRuntime().availableProcessors()*2);
        threadPoolTaskScheduler.setThreadNamePrefix("Scheduler-threadPool");
        return threadPoolTaskScheduler;
    }
}
