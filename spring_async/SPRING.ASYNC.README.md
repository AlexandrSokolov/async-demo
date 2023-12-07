
## Build as docker image and run as the container:

From within the root project folder:
```bash
$ ls -1
docker-compose.yaml
Dockerfile
pom.xml
scripts
src
```
For the first build run:
```bash
./scripts/firstStart.sh
```
To remove docker container, docker image, rebuild and start it again, run:
```bash
./scripts/clearAndStart.sh
```

Trigger async method:
```bash
curl -i -X GET -w "\n" -H 'Content-Type: application/json' http://localhost:8080/sav/rest/fire/and/wait/async
```

Trigger method via async wrapper:
```bash
curl -i -X GET -w "\n" -H 'Content-Type: application/json' http://localhost:8080/sav/rest/fire/and/wait/async/wrapper
```

## Issues:

* [Enable async methods in Spring](#enable-async-methods-in-spring)
* [Async methods in Spring](#async-methods-in-spring)
* [Return type of async methods](#return-type-of-async-methods)
* [Running not-async methods via async helper](#running-not-async-methods-via-async-helper)
* [TODO Canceling async task]

### Enable async methods in Spring

By default, `@EnableAsync` detects both Spring’s `@Async` annotation and the EJB 3.1 `javax.ejb.Asynchronous`.

1. Add `org.springframework.scheduling.annotation.EnableAsync` annotation on 
    [the configuration class](src/main/java/com/savdev/demo/async/SpringAsyncAppConfiguration.java):

    ```java
    @Configuration
    @ComponentScan("com.savdev.demo.async")
    @EnableAsync
    public class SpringAsyncAppConfiguration {
    }
    ```

2. You could also apply the annotation on the application starter:
    ```java
    @SpringBootApplication
    @EnableAsync
    public class SpringAsyncAppStarter {
    
      public static void main(String[] args) {
        SpringApplication.run(SpringAsyncAppStarter.class, args);
      }
    }
    ```

### Async methods in Spring

1. Java class that contains async methods must be Spring bean (`@Service` or `@Component`)
2. Spring’s `@Async` must be applied to public methods only, so that it can be proxied.
3. Self-invocation — calling the async method from within the same class — won’t work 
    because it bypasses the proxy and calls the underlying method directly:
    ```java
    @Service
    public class MyService {
        
        public worker() {
            asyncJob(); //gets invoked synchronously
        }
        
        @Async
        public void asyncJob() {
            ...
        }
    }
    ```
    Possible solutions:
    - [run via async wrapper](#running-not-async-methods-via-async-helper)
    - split classes:
    ```java
    @Service
    public class MyService {
    
        @Autowired
        MyAsyncService myAsyncService;
    
        public void worker() {
            myAsyncService.asyncJob();
        }
    }
    
    @Service
    public class MyAsyncService {
    
        @Async
        public void asyncJob() { // switched to public
            ...
        }
    
    }
    ```

### Return type of async methods

1. `CompletableFuture` (`CompletableFuture<T> implements Future<T>`)

* Available since Java 8
* Helps to collect results in the list and wait when all of them complete:
    ```java
    // Kick of multiple, asynchronous lookups
    CompletableFuture<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
    CompletableFuture<User> page2 = gitHubLookupService.findUser("CloudFoundry");
    CompletableFuture<User> page3 = gitHubLookupService.findUser("Spring-Projects");
  
    // Wait until they are all done
    CompletableFuture.allOf(page1,page2,page3).join();
    ```
    Or [wait for list of future, see `TasksRestService`](src/main/java/com/savdev/demo/async/rest/service/TasksRestService.java):
    ```java
    List<CompletableFuture<String>> listOfAsyncTasks = List.of();
    //wait untill all async tasks are completed
    CompletableFuture.allOf(
      listOfAsyncTasks.toArray(new CompletableFuture<?>[0]))
      .join();
    //extract result as list of String:
    List<String> result = listOfAsyncTasks.stream().map(r -> {
      try {
      return r.get();
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    })
    .toList();
    ```

2. `Future` via `AsyncResult`

    Suits only for Spring-based projects, and makes sense to use for projects that use older versions of Java (before Java 8):
    ```java
    import org.springframework.scheduling.annotation.AsyncResult;
    ...
    @Async
    public Future<String> asyncTask(int input) {
      return new AsyncResult<>("hello");
    }
    ```

3. `Future` via libraries
- Apache Commons Lang: `ConcurrentUtils.constantFuture(T myValue);`
- Guava: `Futures.immediateFuture(value)`

### Running not-async methods via async helper

1. All tasks that takes time to implement are not defined with async method. 
    They are plain java object, that returns result, [See `Task`](src/main/java/com/savdev/demo/async/domain/Task.java).
2. We create an async wrapper/utility class, [see `AsyncWrapper`](src/main/java/com/savdev/demo/async/domain/AsyncWrapper.java):
    ```java
    @Service
    public class AsyncWrapper {
    
      @Async
      public <R> CompletableFuture<R> runAsync(Supplier<R> supplier) {
        return CompletableFuture.completedFuture(supplier.get());
      }
    }
    ```
3. Now it is responsibility of the client of the heavy task to decide, will it be run asynchronously or synchronously.
    In case a client wants to run it asynchronously, he wrapps the heavy task with our `AsyncWrapper` as:
    ```java
    @Service
    public class HeavyTaskConsumer {
    
      @Autowired
      private AsyncWrapper asyncWrapper;
    
      @Override
      public String fireAsyncViaWrapper() {
        var task = new Task();
        return asyncWrapper
          .runAsync(() -> task.heavyTask(100)) //returns CompletableFuture<String>
          .get(); //try/catch logic removed to make it more clear
      }
    }
    ```
    [See `TasksRestService#fireAsyncViaWrapper`](src/main/java/com/savdev/demo/async/rest/service/TasksRestService.java)