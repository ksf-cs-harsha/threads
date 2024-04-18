package Concurrent.org;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentHashMapExample {

    public static void main(String[] args) {
        // Create a ConcurrentHashMap instance
        ConcurrentHashMap<String, Integer> concurrentHashMap = new ConcurrentHashMap<>();

        // Create a fixed thread pool with 5 threads
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        // Submit 5 tasks to the thread pool
        for (int i = 0; i < 6; i++) {
            executorService.submit(() -> {
                for (int j = 1; j <= 10; j++) {
                    String key = "Key" + j;
                    int value = j;

                    // entry into the ConcurrentHashMap
                    concurrentHashMap.put(key, value);

                    //  entry added by the current thread
                    System.out.println(Thread.currentThread().getName() + " added: " + key + " -> " + value);
                }
            });
        }

        // Shutdown
        executorService.shutdown();

        // completion of task
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // size of the ConcurrentHashMap
        System.out.println("ConcurrentHashMap size: " + concurrentHashMap.size());
    }
}
