package com.savdev.demo.async;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TasksAccumulatorConsumer {

  public List<String> fireAndWait() {
    var task = new Task();
    return IntStream.range(0, 10)
      .parallel()
      .boxed()
      .map(task::heavyTask)
      .toList();
  }
}
