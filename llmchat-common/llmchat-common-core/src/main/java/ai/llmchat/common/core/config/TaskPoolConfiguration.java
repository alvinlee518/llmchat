package ai.llmchat.common.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class TaskPoolConfiguration implements AsyncConfigurer {
    public static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    @Value("${thread.pool.corePoolSize:}")
    private Optional<Integer> corePoolSize;
    @Value("${thread.pool.maxPoolSize:}")
    private Optional<Integer> maxPoolSize;
    @Value("${thread.pool.queueCapacity:}")
    private Optional<Integer> queueCapacity;
    @Value("${thread.pool.keepAliveSeconds:}")
    private Optional<Integer> keepAliveSeconds;
    @Value("${thread.pool.awaitTerminationSeconds:}")
    private Optional<Integer> awaitTerminationSeconds;

    @Override
    @Bean
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        // 核心线程大小 默认区 CPU 数量
        taskExecutor.setCorePoolSize(corePoolSize.orElse(CPU_NUM));
        // 最大线程大小 默认区 CPU * 2 数量
        taskExecutor.setMaxPoolSize(maxPoolSize.orElse(CPU_NUM * 2 + 10));
        // 队列最大容量
        taskExecutor.setQueueCapacity(queueCapacity.orElse(256));
        taskExecutor.setKeepAliveSeconds(keepAliveSeconds.orElse(60));
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        taskExecutor.setAwaitTerminationSeconds(awaitTerminationSeconds.orElse(60));
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setThreadNamePrefix("Task-");
        taskExecutor.initialize();
        return taskExecutor;
    }
}
