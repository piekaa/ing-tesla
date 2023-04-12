package pl.piekoszek.ing.cache;

import org.springframework.stereotype.Component;

@Component
public abstract class Cache<T extends Resettable> {
    ThreadLocal<T[]> threadLocalCache = new ThreadLocal<>();
    ThreadLocal<Int> threadLocalIndex = new ThreadLocal<>();

    public T use() {
        prepareThread();
        var index = threadLocalIndex.get();
        var cache = threadLocalCache.get();
        index.value %= cache.length;
        var accountStatus = cache[index.value++];
        accountStatus.reset();
        threadLocalIndex.set(index);
        return accountStatus;
    }

    private void prepareThread() {
        if (threadLocalIndex.get() == null) {
            threadLocalIndex.set(new Int());
            threadLocalCache.set(createArray());
        }
    }

    protected abstract T[] createArray();
}

class Int {
    int value;
}

