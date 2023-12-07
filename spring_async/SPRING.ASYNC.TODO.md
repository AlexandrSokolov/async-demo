https://spring.io/guides/gs/async-method/
https://www.baeldung.com/spring-async

- canceling async method
- [Exception Handling](https://www.baeldung.com/spring-async)

For Spring Async you can:
- [configure executor](https://www.baeldung.com/spring-async)
- [set thread pool size]()
- [define task namespace](https://www.baeldung.com/spring-async)
[Make the Application Executable](https://spring.io/guides/gs/async-method/)
```java
  @Bean
  public Executor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2);
    executor.setMaxPoolSize(2);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix("GithubLookup-");
    executor.initialize();
    return executor;
  }
```

[Spring Async Event](https://www.baeldung.com/spring-events)