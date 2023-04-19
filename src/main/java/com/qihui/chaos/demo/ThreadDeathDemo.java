package com.qihui.chaos.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Elliott Chen on 2023/4/19 14:47
 */
public class ThreadDeathDemo {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 2; i++) {

            executorService.execute(() -> {
                threads.add(Thread.currentThread());
                int num = 0;

                while (true) {
                    try {
                        if (num % 1_000_000_000 == 0) {
                            System.out.println(Thread.currentThread().getName() + ":" + num / 1_000_000_000);
                        }
                        num ++;
                    } catch (ThreadDeath ignored) {
                        System.out.println("thread is stopped");
                    }

                }
            });
        }

        while (threads.isEmpty()) {

        }

        Thread thread = threads.get(0);

        //does not work
        thread.interrupt();

        thread.stop();

        executorService.execute(() -> System.out.println("the third thread is executed..."));

        Thread.sleep(1000L);

    }
}
