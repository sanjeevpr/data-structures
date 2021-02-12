import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class Entry<K, V> {
    int hashCode;
    K key;
    V value;

    Entry(K key, V value) {
        this.key = key;
        this.value = value;
        this.hashCode = key.hashCode();
    }

    public boolean equals(Entry<K, V> other) {
        if (other.hashCode != this.hashCode) {
            return false;
        }
        return key.equals(other.key);
    }

    public String toString() {
        return this.key + " : " + this.value;
    }
}

@SuppressWarnings("unchecked")
public class HashTableSeparateChaining<K, V> {
    private static final int DEFAUT_CAPACITY = 3;
    private static final double DEFAUT_LOAD_FACTOR = 0.75;

    private double maxLoadFactor;
    private int capacity, threshold, size = 0;
    private LinkedList<Entry<K, V>>[] table;

    public HashTableSeparateChaining() {
        this(DEFAUT_CAPACITY, DEFAUT_LOAD_FACTOR);
    }

    public HashTableSeparateChaining(int capacity) {
        this(capacity, DEFAUT_LOAD_FACTOR);
    }

    public HashTableSeparateChaining(int capacity, double maxLoadFactor) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Illegal capacity");
        }

        if (maxLoadFactor <= 0) {
            throw new IllegalArgumentException("Illegal maxLoadFactor");
        }

        this.maxLoadFactor = maxLoadFactor;
        this.capacity = Math.max(DEFAUT_CAPACITY, capacity);
        threshold = (int) (this.capacity * this.maxLoadFactor);
        table = new LinkedList[this.capacity];
    }

    public int size() {
        return size;
    }

    public boolean isEMpty() {
        return size == 0;
    }

    private int normalizeIndex(int keyHash) {
        return (keyHash & 0x7FFFFFFF) % capacity;
    }

    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    public boolean hasKey(K key) {
        int bucketIndex = normalizeIndex(key.hashCode());
        return bucketSeekEntry(bucketIndex, key) != null;
    }

    public V add(K key, V value) {
        return insert(key, value);
    }

    public V insert(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key");
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        int bucketIndex = normalizeIndex(newEntry.hashCode);
        return bucketInsertKey(bucketIndex, newEntry);
    }

    public V remove(K key) {
        if (key == null) {
            return null;
        }

        int bucketIndex = normalizeIndex(key.hashCode());
        return bucketRemoveKey(bucketIndex, key);
    }

    private V bucketInsertKey(int bucketIndex, Entry<K, V> newEntry) {
        LinkedList<Entry<K, V>> bucket = table[bucketIndex];
        if (bucket == null) {
            table[bucketIndex] = bucket = new LinkedList<>();
        }

        Entry<K, V> existantEntry = bucketSeekEntry(bucketIndex, newEntry.key);

        if (existantEntry == null) {
            bucket.add(existantEntry);
            if (++size > threshold) {
                resize();
            }
            return null;
        } else {
            V oldValue = existantEntry.value;
            existantEntry.value = newEntry.value;
            return oldValue;
        }
    }

    private Entry<K, V> bucketSeekEntry(int bucketIndex, K key) {
        if (key == null) {
            return null;
        }

        LinkedList<Entry<K, V>> bucket = table[bucketIndex];
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry;
            }
        }
        return null;
    }

    private V bucketRemoveKey(int bucketIndex, K key) {
        Entry<K, V> entry = bucketSeekEntry(bucketIndex, key);

        if (entry != null) {
            LinkedList<Entry<K, V>> bucket = table[bucketIndex];
            bucket.remove(entry);
            size--;
            return entry.value;
        }
        return null;
    }

    private void resize() {
        capacity *= 2;
        threshold = (int) (capacity * maxLoadFactor);

        LinkedList<Entry<K, V>>[] newTable = new LinkedList[capacity];

        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                for (Entry<K, V> entry : table[i]) {
                    int bucketIndex = normalizeIndex(entry.hashCode);
                    LinkedList<Entry<K, V>> bucket = newTable[bucketIndex];
                    if (bucket == null) {
                        newTable[bucketIndex] = bucket = new LinkedList<>();
                    }
                    bucket.add(entry);
                }

                table[i].clear();
                table[i] = null;
            }
        }

        table = newTable;
    }

    public List<K> keys() {
        List<K> keys = new ArrayList<>(size);
        for (LinkedList<Entry<K, V>> bucket : table) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    keys.add(entry.key);
                }
            }
        }
        return keys;
    }

    public List<V> values() {
        List<V> values = new ArrayList<>(size);
        for (LinkedList<Entry<K, V>> bucket : table) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    values.add(entry.value);
                }
            }
        }
        return values;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (LinkedList<Entry<K, V>> bucket : table) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    sb.append(entry.key + " : " + entry.value + ", ");
                }
            }
        }
        sb.append("}");
        return sb.toString();
    }
}