import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/*
* Class representing data.
* It constitutes of key-value pair.
*/
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

    // Changing load factor
    private double maxLoadFactor;

    // Capacity is the number of buckets the LinkedList can hold
    private int capacity = 0;

    // Threshold tells us to resize when it reaches a certain value
    private int threshold = 0;

    // Total number of (k, v) pair in the hash table.
    private int size = 0;

    // Hash table represented by LinkedList
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

    /*
     * Returns true if the hash table is empty, otherwise, false
    */
    public boolean isEmpty() {
        return size == 0;
    }

    /*
     * Returns the size of the hash table
    */
    public int size() {
        return size;
    }

    /*
     * Returns the hash value within 1 to N-1
    */
    private int normalizeIndex(int keyHash) {
        // 0x7FFFFFFF strips off the negative sign
        return (keyHash & 0x7FFFFFFF) % capacity;
    }

    /*
    * Clears the hash table and sets the size to 0
    */
    public void clear() {
        Arrays.fill(table, null);
        size = 0;
    }

    /*
    * Returns true if the specified key is present in the hash table, otherwise
    * false
    */
    public boolean hasKey(K key) {
        int bucketIndex = normalizeIndex(key.hashCode());
        return bucketSeekEntry(bucketIndex, key) != null;
    }

    /*
    * Returns the old value and modifies the old value with the specfied value if
    * the key is already present Otherwise, returns null and adds the new key-value
    * if the key is not present.
    */
    public V add(K key, V value) {
        return insert(key, value);
    }

    /*
    * Returns the old value and modifies the old value with the specfied value if
    * the key is already present Otherwise, returns null and adds the new key-value
    * if the key is not present.
    */
    public V insert(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key");
        }

        Entry<K, V> newEntry = new Entry<>(key, value);
        int bucketIndex = normalizeIndex(newEntry.hashCode);
        return bucketInsertKey(bucketIndex, newEntry);
    }

    /*
    * Returns the value against specfied key and remove the ey-value pair from the
    * bucket if the key is found Otherwise, returns null if the key is null and key
    * is not present.
    */
    public V remove(K key) {
        if (key == null) {
            return null;
        }

        int bucketIndex = normalizeIndex(key.hashCode());
        return bucketRemoveKey(bucketIndex, key);
    }

    /*
    * Helper method to insert a key-value in the hash table
    */
    private V bucketInsertKey(int bucketIndex, Entry<K, V> newEntry) {
        LinkedList<Entry<K, V>> bucket = table[bucketIndex];
        // If the bucket is not present, create a new bucket at the specified index
        if (bucket == null) {
            table[bucketIndex] = bucket = new LinkedList<>();
        }

        // Checks if the new key is present in the specified bucket
        Entry<K, V> existantEntry = bucketSeekEntry(bucketIndex, newEntry.key);

        // If not present, then add the key-value pair to the bucket.
        if (existantEntry == null) {
            bucket.add(existantEntry);
            // Resize the table if we have reached the threshold
            if (++size > threshold) {
                resize();
            }
            return null;
        } else {
            // Modify the old value with the new value
            V oldValue = existantEntry.value;
            existantEntry.value = newEntry.value;
            return oldValue;
        }
    }

    /*
    * Helper method to get a key-value pair from the specified bucket in the hash table
    */
    private Entry<K, V> bucketSeekEntry(int bucketIndex, K key) {
        if (key == null) {
            return null;
        }

        // Get the bucket
        LinkedList<Entry<K, V>> bucket = table[bucketIndex];
        for (Entry<K, V> entry : bucket) {
            // Check if the key is present 
            if (entry.key.equals(key)) {
                return entry;
            }
        }
        return null;
    }

    /*
    * Helper method to remove a key-value pair from the specified bucket in the hash table
    */
    private V bucketRemoveKey(int bucketIndex, K key) {
        // Get the key-value pair from the bucket
        Entry<K, V> entry = bucketSeekEntry(bucketIndex, key);

        // If present, remove it
        if (entry != null) {
            LinkedList<Entry<K, V>> bucket = table[bucketIndex];
            bucket.remove(entry);
            size--;
            return entry.value;
        }
        return null;
    }

    /*
    * Helper method to resize the hash table with new capacity on reaching a threshold 
    */
    private void resize() {
        // Double the capacity
        capacity *= 2;
        // Recalculate the threshold
        threshold = (int) (capacity * maxLoadFactor);

        // Create a new table with the new capacity
        LinkedList<Entry<K, V>>[] newTable = new LinkedList[capacity];

        // Copy the values from the old table to the new table
        for (int i = 0; i < table.length; i++) {
            if (table[i] != null) {
                for (Entry<K, V> entry : table[i]) {
                    int bucketIndex = normalizeIndex(entry.hashCode);
                    // Get the bucket at the index
                    LinkedList<Entry<K, V>> bucket = newTable[bucketIndex];
                    // Create a new bucket if it is not there
                    if (bucket == null) {
                        newTable[bucketIndex] = bucket = new LinkedList<>();
                    }
                    bucket.add(entry);
                }

                // Helps GC
                table[i].clear();
                table[i] = null;
            }
        }

        table = newTable;
    }

    /* 
    * Returns the list of keys in the hash table
    */
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

    /* 
    * Returns the list of values in the hash table
    */
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

    /* 
    * Returns the String representation of the hash table
    */
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