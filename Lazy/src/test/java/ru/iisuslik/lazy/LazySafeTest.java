package ru.iisuslik.lazy;

import org.junit.Test;

import java.util.ArrayList;
import java.util.function.Supplier;

import static org.junit.Assert.*;

public class LazySafeTest {


    private Supplier<Integer> strangeSupplier = new Supplier<Integer>() {
        boolean getHappend = false;

        @Override
        public Integer get() {
            if (getHappend)
                return null;
            getHappend = true;
            return 43;
        }
    };

    private Supplier<Integer> countSupplier = new SupplierWithCount();


    @Test
    public void nullReturnValue() {
        Lazy<Integer> returnsNull = LazyFactory.createLazySafe(() -> null);
        assertEquals(null, returnsNull.get());
        assertEquals(null, returnsNull.get());
    }

    /**
     * With 1 thread LazySafe works not worse than LazySimple
     */
    @Test
    public void strangeTestFromSimple() {
        Lazy<Integer> strange = LazyFactory.createLazySafe(strangeSupplier);
        assertEquals(43, (int) strange.get());
        assertEquals(43, (int) strange.get());
    }

    @Test
    public void twoThreads() throws Exception {
        Lazy<Integer> counter = LazyFactory.createLazySafe(countSupplier);
        Runnable forThreads = () -> {
            counter.get();
            counter.get();
            counter.get();
        };
        Thread a = new Thread(forThreads);
        Thread b = new Thread(forThreads);
        a.start();
        b.start();
        a.join();
        b.join();
        assertEquals(2, (int) countSupplier.get());
    }

    @Test
    public void manyThreadsAndTasks() throws Exception {
        for (int j = 0; j < 10; j++) {
            ArrayList<Lazy<Integer>> tasks = new ArrayList<>(100);
            for (int i = 0; i < tasks.size(); i++) {
                tasks.set(i, LazyFactory.createLazySafe(new SupplierWithCount()));
            }
            Runnable forThreads = () -> {
                for (Lazy<Integer> task : tasks) {
                    task.get();
                }
            };
            Thread[] threads = new Thread[8];
            for (int i = 0; i < threads.length; i++) {
                threads[i] = new Thread(forThreads);
            }
            for (Thread thread : threads) {
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
            for (Lazy<Integer> task : tasks) {
                assertEquals(2, (int) task.get());
            }
        }
    }

    private class SupplierWithCount implements Supplier<Integer> {

        private int count = 0;

        @Override
        public Integer get() {
            count++;
            return count;
        }
    }

}