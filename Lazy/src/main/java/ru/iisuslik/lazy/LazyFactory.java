package ru.iisuslik.lazy;

import java.util.function.Supplier;

/**
 * Class from where you can get 2 realization of Lazy interface - simple and multithreaded safe
 */
public class LazyFactory {
    /**
     * @param function
     * @param <T>
     * @return
     */
    public static <T> Lazy<T> createLazySafe(Supplier<T> function){
        return new Lazy<T>() {
            private Supplier<T> func = function;
            private T result;
            {
                if (func == null)
                    System.out.print("FUCK YOU");//TODO
            }
            @Override
            public synchronized T get() {
                if(func == null)
                    return null;
                if(result == null) {
                    result = func.get();
                    if(result == null)
                        func = null;
                }
                return result;
            }
        };
    }

    /**
     * @param function
     * @param <T>
     * @return
     */
    public static <T> Lazy<T> createLazy(Supplier<T> function) {
        return new Lazy<T>() {
            private Supplier<T> func = function;
            private T result;
            {
                if (func == null)
                    System.out.print("FUCK YOU");//TODO
            }
            @Override
            public T get() {
                if(func == null)
                    return null;
                if(result == null) {
                    result = func.get();
                    if(result == null)
                        func = null;
                }
                return result;
            }
        };
    }
}
