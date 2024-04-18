package threadpool.org;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPoolExample {

    public static void main(String[] args) {
        //  fixed thread pool with 5 threads
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        //  20 tasks to the thread pool
        for (int i = 1; i <= 25; i++) {
            final int taskNumber = i;
            executorService.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Task " + taskNumber + " is running on " + threadName);
                try {
                    // Simulate some work by sleeping for a short duration
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // Shutdown the thread pool
        executorService.shutdown();
    }
}
