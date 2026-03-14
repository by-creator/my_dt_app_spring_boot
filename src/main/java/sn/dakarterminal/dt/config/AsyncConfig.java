package sn.dakarterminal.dt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.concurrent.Executor;

/**
 * Configures the async executor to propagate both the Spring Security context
 * and the HTTP request attributes (needed for IP capture in AuditService) from
 * the calling thread into async worker threads.
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("async-audit-");
        executor.setTaskDecorator(runnable -> {
            // Capture in the calling (web) thread
            RequestAttributes requestAttributes;
            try {
                requestAttributes = RequestContextHolder.currentRequestAttributes();
            } catch (IllegalStateException e) {
                requestAttributes = null;
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final RequestAttributes capturedAttrs = requestAttributes;

            return () -> {
                try {
                    if (capturedAttrs != null) {
                        RequestContextHolder.setRequestAttributes(capturedAttrs, true);
                    }
                    if (authentication != null) {
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                    runnable.run();
                } finally {
                    RequestContextHolder.resetRequestAttributes();
                    SecurityContextHolder.clearContext();
                }
            };
        });
        executor.initialize();
        return executor;
    }
}
