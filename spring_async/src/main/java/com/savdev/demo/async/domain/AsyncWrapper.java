package com.savdev.demo.async.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
public class AsyncWrapper {

  private static final Logger logger = LogManager.getLogger(AsyncWrapper.class);

  @Async
  public <R> CompletableFuture<R> runAsync(Supplier<R> supplier) {
    try {
      logger.debug(() -> "triggered: AsyncWrapper#runAsync");
      return CompletableFuture.completedFuture(supplier.get());
    } finally {
      logger.debug(() -> "completed: AsyncWrapper#runAsync");
    }
  }
}
