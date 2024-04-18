package methods.org;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerPattern {
    private static final int CAPACITY = 10;
    private final Queue<Integer> queue = new LinkedList<>();

    public void produce() throws InterruptedException {
        synchronized (this) {
            while (queue.size() == CAPACITY) {
                System.out.println("Queue is full. Producer is waiting...");
                wait();
            }

            int value = (int) (Math.random() * 10);
            System.out.println("Produced: " + value);
            queue.add(value);

            // Notify consumer that data is available
            notify();
        }
    }

    public void consume() throws InterruptedException {
        synchronized (this) {
            while (queue.isEmpty()) {
                System.out.println("Queue is empty. Consumer is waiting...");
                wait();
            }

            int value = queue.poll();
            System.out.println("Consumed: " + value);

            // Notify producer that space is available
            notify();
        }
    }

    public static void main(String[] args) {
        ProducerConsumerPattern pc = new ProducerConsumerPattern();

        Thread producerThread = new Thread(() -> {
            try {
                while (true) {
                    pc.produce();
                    Thread.sleep(1000); // Producer sleeps for 1 second
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                while (true) {
                    pc.consume();
                    Thread.sleep(15); // Consumer sleeps for 1.5 seconds
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producerThread.start();
        consumerThread.start();
    }
}
