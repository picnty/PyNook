package com.project.pi.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackages = {"com.project.**",})
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PiAppBootstrap {
}
