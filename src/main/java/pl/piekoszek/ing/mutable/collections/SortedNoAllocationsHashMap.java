package pl.piekoszek.ing.mutable.collections;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SortedNoAllocationsHashMap<T extends MapEntry> implements Iterable<T> {
    private final MapEntry[] buckets;
    private final MapEntry[] forSort;
    private int size;
    int collisions;
    int deepCollisions;

    public SortedNoAllocationsHashMap(int buckets) {
        this.buckets = new MapEntry[buckets];
        this.forSort = new MapEntry[buckets];
    }

    public T getOrDefault(String key, Supplier<T> defaultValueSupplier) {
        var value = get(key);
        if (value != null) {
            return value;
        }
        return defaultValueSupplier.get();
    }

    public T get(String key) {
        var bucket = bucket(key);
        var item = buckets[bucket];

        while (item != null) {
            if (item.key().equals(key)) {
                return (T) item;
            }
            item = item.next;
        }
        return null;
    }


    public void put(T entry) {
        var bucket = bucket(entry.key());

        var firstItemInBucket = buckets[bucket];
        if (firstItemInBucket == null) {
            buckets[bucket] = entry;
            size++;
            return;
        }
        var item = firstItemInBucket;

        if (item == entry) {
            return;
        }

        if (item.key().equals(entry.key())) {
            entry.next = item.next;
            item.next = null;
            buckets[bucket] = entry;
            collisions++;
            return;
        }

        while (item.next != null) {
            if (entry.key().equals(item.next.key())) {
                if (entry == item.next) {
                    return;
                }

                entry.next = item.next.next;
                item.next.next = null;
                item.next = entry;
                deepCollisions++;
                return;
            }
            item = item.next;
        }
        item.next = entry;
        size++;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    private int bucket(String key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    public MapEntry[] sorted() {
        fillForSort();
        Arrays.sort(forSort, 0, size);
        return forSort;
    }

    public int size() {
        return size;
    }

    private void fillForSort() {
        int index = 0;
        for (MapEntry bucket : buckets) {
            var item = bucket;
            while (item != null) {
                forSort[index++] = item;
                item = item.next;
            }
        }
    }

    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            clearBucket(i);
        }
        size = 0;
    }

    private void clearBucket(int bucket) {
        var item = buckets[bucket];
        buckets[bucket] = null;

        while (item != null) {
            var next = item.next;
            item.next = null;
            item = next;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new MapIterator();
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<T> spliterator() {
        return Iterable.super.spliterator();
    }

    private class MapIterator implements Iterator<T> {

        private int position = 0;
        private MapEntry[] sorted;

        MapIterator() {
            sorted = sorted();
        }

        @Override
        public boolean hasNext() {
            return position < size;
        }

        @Override
        public T next() {
            return (T) sorted[position++];
        }
    }
}
