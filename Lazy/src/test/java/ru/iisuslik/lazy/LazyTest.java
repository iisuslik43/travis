package ru.iisuslik.lazy;

import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.*;

public class LazyTest {
    @Test
    public void createLazy() {
        Lazy<Integer> lazy = LazyFactory.createLazy(() -> 2 + 2);
        assertEquals(4, (int)lazy.get());
        assertEquals(4, (int)lazy.get());
    }

    private Supplier<Integer> strangeSupplier = new Supplier<Integer>() {
        boolean getHappend = false;
        @Override
        public Integer get() {
            if(getHappend)
                return null;
            getHappend = true;
            return 43;
        }
    };

    @Test
    public void getHappensOnlyOneTime() {
        Lazy<Integer> strange = LazyFactory.createLazy(strangeSupplier);
        assertEquals(43, (int)strange.get());
        assertEquals(43, (int)strange.get());
    }

    @Test
    public void nullReturnValue() {
        Lazy<Integer> returnsNull = LazyFactory.createLazy(() -> null);
        assertEquals(null, returnsNull.get());
        assertEquals(null, returnsNull.get());
    }
}