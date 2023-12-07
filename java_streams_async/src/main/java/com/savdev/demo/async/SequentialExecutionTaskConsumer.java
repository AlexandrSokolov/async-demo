package com.savdev.demo.async;

import java.util.List;
import java.util.stream.IntStream;

public class SequentialExecutionTaskConsumer {

  public List<String> fireAndWait() {
    var task = new Task();
    return IntStream.range(0, 10)
      //.parallel() we need sequential execution!
      .boxed()
      .map(task::heavyTask)
      .toList();
  }
}
