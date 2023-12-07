package com.savdev.demo.async;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TasksAccumulatorConsumerTest {

  @BeforeAll
  public static void init() {
    Configurator.setLevel("com.savdev.demo.async", Level.DEBUG);
  }

  @Test
  public void fireAndWaitAsync() {
    var fireAndWaitTaskConsumer = new TasksAccumulatorConsumer();
    var result = fireAndWaitTaskConsumer.fireAndWait();
    Assertions.assertEquals(10, result.size());
  }
}
