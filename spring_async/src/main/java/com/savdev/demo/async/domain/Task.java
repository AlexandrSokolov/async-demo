package com.savdev.demo.async.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Task {

  private static final Logger logger = LogManager.getLogger(Task.class);

  public String heavyTask(int input) {
    try {
      TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3, 6));
      logger.debug(() -> "heavyTask(" + input + ")");
      return "Finished task for: '" + input + "' input";
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }


}
