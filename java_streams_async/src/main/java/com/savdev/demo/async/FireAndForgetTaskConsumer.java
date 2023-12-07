package com.savdev.demo.async;

import java.util.stream.IntStream;

public class FireAndForgetTaskConsumer {

  public void fireAndForget() {
    var task = new Task();
    IntStream.range(0, 10)
      .parallel()
      .forEach(task::heavyTask);
  }
}
