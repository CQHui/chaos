package com.qihui.chaos.demo.concurrency;


import org.junit.Test;

/**
 * Created by Elliott Chen on 2023/4/20 15:22
 */
public class WaitNotifyDemo {

    private int threadsNum;
    private int count = 1;


    private void print(int num) {
        while (true) {
            synchronized (this) {
                while (count % threadsNum != num) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                System.out.println(Thread.currentThread().getName() + ":" + count);
                count ++;
                this.notifyAll();
            }
        }
    }


    @Test
    public void test() throws InterruptedException {
        threadsNum = 3;
        Thread a = new Thread(() -> print(1), "a");
        Thread b = new Thread(() -> print(2), "b");
        Thread c = new Thread(() -> print(0), "c");



        a.start();
        b.start();
        c.start();

        Thread.sleep(10L);
    }
}
