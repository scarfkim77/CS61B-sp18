package lab9;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size; //The sum of all items in a MyHashMap

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }

    public MyHashMap(int n) {
        buckets = new ArrayMap[n];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        int index = hash(key);
        if (!buckets[index].containsKey(key)) {
            return null;
        }
        return buckets[index].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        int index = hash(key);
        if (!buckets[index].containsKey(key)) {
            size++;
        }
        if (loadFactor() >= MAX_LF) {
            resize(2);
        }
        buckets[index].put(key, value);
    }

    private void resize(int factor) {
        MyHashMap<K, V> newHashMap = new MyHashMap<>(DEFAULT_SIZE * factor);
        for (K key: this.keySet()) {
            newHashMap.put(key, this.get(key));
        }
        this.buckets = newHashMap.buckets;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (ArrayMap<K, V> am: buckets) {
            keySet.addAll(am.keySet());
        }
        return keySet;
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        int index = hash(key);
        if (!buckets[index].containsKey(key)) {
            return null;
        }
        V removedV = buckets[index].get(key);
        buckets[index].remove(key);
        return removedV;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        int index = hash(key);
        if (!buckets[index].containsKey(key)) {
            return null;
        }
        if (buckets[index].get(key) != value) {
            return null;
        }
        return value;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
