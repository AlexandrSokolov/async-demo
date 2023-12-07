package com.savdev.demo.async;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TaskTest {

  @BeforeAll
  public static void init() {
    Configurator.setLevel("com.savdev.demo.async", Level.DEBUG);
  }

  @Test
  public void testHeavyTask() {
    var task = new Task();
    task.heavyTask(1);
  }
}
