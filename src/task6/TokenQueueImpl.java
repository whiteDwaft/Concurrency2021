package task6;

import javax.swing.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class TokenQueueImpl implements TokenQueue {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private String str = "";

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public void push(String str) throws InterruptedException {
        lock.lock();
        try {
            while (!this.str.isEmpty() && !Thread.currentThread().getName().equals("stop") ){
                condition.await();
            }

            this.str = str;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String poll() throws InterruptedException {
        lock.lock();
        try{
            while (this.str.isEmpty() && !Thread.currentThread().getName().equals("stop")){
                condition.await();
            }
            String taken = this.str;
            this.str = "";
            condition.signal();
            return taken;
        } finally {
            lock.unlock();
        }
    }
}
