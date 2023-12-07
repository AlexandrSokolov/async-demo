package com.savdev.demo.async.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncTask extends Task{

  private static final Logger logger = LogManager.getLogger(AsyncTask.class);

  @Async
  public CompletableFuture<String> asyncHeavyTask(int input) {
    try {
      logger.debug(() -> "triggered: AsyncTask#asyncHeavyTask(" + input + ")");
      return CompletableFuture.completedFuture(super.heavyTask(input));
    } finally {
      logger.debug(() -> "completed: AsyncTask#asyncHeavyTask(" + input + ")");
    }
  }
}
