package com.jcoroutine.test;

import co.paralleluniverse.fibers.Stack;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.Strand;
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import sun.misc.Unsafe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;

/**
 * @author: guiliehua
 * @description:
 * @date:2018-09-17
 */
public class Quasar {
    @Test
    public void test() throws SuspendExecution {

        Strand.park();
        Thread.currentThread().suspend();
    }

    @Test
    public void test2() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        for (int i=0; i<10; i++){
            executor.submit(new Task(i));
        }

        Thread.sleep(9999999999L);
    }


    class Task implements Runnable{
        private int name;

        public Task(int name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(name + " starts");
            if (name%2==0){
                LockSupport.parkNanos(9999999999L);
            }
            System.out.println(name + " ends");
        }
    }
}
