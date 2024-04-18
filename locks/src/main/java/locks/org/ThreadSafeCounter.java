package locks.org;

import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeCounter {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void incrementAndPrint() {
        // Acquire the lock
        lock.lock();

        try {
            // Increment the count
            count++;

            // Print the incremented count and the current thread name
            System.out.println("Thread " + Thread.currentThread().getName() + " incremented count to: " + count);
        } finally {
            // Release the lock
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        // Create an instance of ThreadSafeCounter
        ThreadSafeCounter counter = new ThreadSafeCounter();

        // Create and start 5 threads
        for (int i = 1; i <= 5; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 5; j++) {
                    counter.incrementAndPrint();

                    // Sleep for a short duration to simulate some work
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.setName("Thread-" + i);
            thread.start();
        }
    }
}
