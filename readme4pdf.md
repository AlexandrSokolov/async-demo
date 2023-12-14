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