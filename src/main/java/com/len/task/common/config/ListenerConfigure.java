package com.len.task.common.config;

import com.len.task.server.interceptor.SessionListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sujianfeng
 * @date 2019-08-17 13:11
 */
@Slf4j
@Configuration
public class ListenerConfigure {
    @Bean
    public ServletListenerRegistrationBean<SessionListener> serssionListenerBean() {
        ServletListenerRegistrationBean<SessionListener>
                sessionListener = new ServletListenerRegistrationBean<>(new SessionListener());
        return sessionListener;
    }
}
