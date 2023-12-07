package com.savdev.demo.async;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@ComponentScan("com.savdev.demo.async")
@EnableAsync
public class SpringAsyncAppConfiguration {
}
