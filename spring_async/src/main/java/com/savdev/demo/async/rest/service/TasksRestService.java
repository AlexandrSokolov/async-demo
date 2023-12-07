package com.savdev.demo.async.rest.service;

import com.savdev.demo.async.domain.AsyncTask;
import com.savdev.demo.async.domain.AsyncWrapper;
import com.savdev.demo.async.domain.Task;
import com.savdev.demo.async.rest.api.Result;
import com.savdev.demo.async.rest.api.TasksRestApi;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Service
public class TasksRestService implements TasksRestApi {

  private static final Logger logger = LogManager.getLogger(TasksRestService.class);

  @Autowired
  private AsyncTask asyncTask;

  @Autowired
  private AsyncWrapper asyncWrapper;

  @Override
  public Result fireAsync() {
    try {
      logger.debug(() -> "Triggered: TasksRestService#fireAsync");
      var results = IntStream.range(0, 10)
        .boxed()
        .map(asyncTask::asyncHeavyTask)
        .toList();
      logger.debug(() -> "Waiting for results: TasksRestService#fireAsync");
      CompletableFuture.allOf(
          results.toArray(new CompletableFuture<?>[0]))
        .join();
      return new Result(results.stream().map(this::resultExtractor).toList());
    } finally {
      logger.debug(() -> "Completed: TasksRestService#fireAsync");
    }
  }

  @Override
  public Result fireAsyncViaWrapper() {
    //the key difference, instead of invoking:
    //`.map(asyncTask::asyncHeavyTask)`
    //task gets invoked via `AsyncWrapper` as:
    //`.map(i -> asyncWrapper.runAsync(() -> task.heavyTask(i)))`
    var task = new Task();
    try {
      logger.debug(() -> "Triggered: TasksRestService#fireAsyncViaWrapper");
      var results = IntStream.range(0, 10)
        .boxed()
        .map(i -> asyncWrapper.runAsync(()
          -> task.heavyTask(i)))
        .toList();
      logger.debug(() -> "Waiting for results: TasksRestService#fireAsyncViaWrapper");
      CompletableFuture.allOf(
          results.toArray(new CompletableFuture<?>[0]))
        .join();
      return new Result(
        results.stream().map(this::resultExtractor).toList());
    } finally {
      logger.debug(() -> "Completed: TasksRestService#fireAsync");
    }
  }

  private String resultExtractor(CompletableFuture<String> result) {
    try {
      return result.get();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

}
