import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class DoubleHashing<K extends SecondaryHash, V> {
    private static final int DEFAULT_CAPACITY = 3;
    private static final double DEFAUT_LOAD_FACTOR = 0.75;

    private int hash;

    // Changing load factor
    private double loadFactor;

    // Capacity is the number of key-value pairs the hash table can hold
    private int capacity;

    // Threshold tells us to resize when it reaches a certain value
    private int threshold;

    // The number of unique key-value pair in the hash table
    private int keyCount;

    // The number of buckets used by TOMBSTONE and the key-value pairs
    private int usedBuckets;

    // Array of keys of type K
    private K[] keys;
    // Array of values of type V
    private V[] values;

    // Represents a deleted key-value pair
    private final K TOMBSTONE = (K) new Object();

    public DoubleHashing() {
        this(DEFAULT_CAPACITY, DEFAUT_LOAD_FACTOR);
    }

    public DoubleHashing(int capacity) {
        this(capacity, DEFAUT_LOAD_FACTOR);
    }

    public DoubleHashing(int capacity, double loadFactor) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Illegal capacity");
        }

        if (loadFactor <= 0) {
            throw new IllegalArgumentException("Illegal maxLoadFactor");
        }

        this.loadFactor = loadFactor;
        this.capacity = Math.max(DEFAULT_CAPACITY, capacity);
        adjustCapacity();
        threshold = (int) (this.capacity * this.loadFactor);

        keys = (K[]) new Object[this.capacity];
        values = (V[]) new Object[this.capacity];
    }

    /*
     * Helper method to setup probing
     */
    private void setupProbing(K key) {
        hash = normalizeIndex(key.hashCode2());
        if (hash == 0) {
            hash = 1;
        }
    }

    /*
     * Helper method which returns the next index to be probed
     */
    private int probe(int x) {
        // Quadratic probing function (x^2+x)/2
        return x * hash;
    }

    /*
     * Helper method which returns the next power of 2
     */
    private int nextPowerOfTwo(int n) {
        return Integer.highestOneBit(n) << 1;
    }

    /*
     * Helper method which adjust the capacity to be power of 2
     */
    private void adjustCapacity() {
        while (!(new BigInteger(String.valueOf(capacity)).isProbablePrime(20))) {
            capacity++;
        }
    }

    /*
     * Helper method which increases the capacity to be the next power of 2
     */
    private void increaseCapacity() {
        capacity = nextPowerOfTwo(capacity);
    }

    /*
     * Returns the hash value within 1 to N-1
     */
    private int normalizeIndex(int keyHash) {
        return (keyHash & 0x7FFFFFFF) % capacity;
    }

    /*
     * Helper method to resize the hash table with new capacity on reaching a
     * threshold
     */
    private void resize() {
        increaseCapacity();
        adjustCapacity();

        threshold = (int) (this.capacity * this.loadFactor);

        K[] oldKeys = (K[]) new Object[this.capacity];
        V[] oldValues = (V[]) new Object[this.capacity];

        // Swap the tables
        K[] oldKeysTemp = keys;
        keys = oldKeys;
        oldKeys = oldKeysTemp;

        // Swap the tables
        V[] oldValuesTemp = values;
        values = oldValues;
        oldValues = oldValuesTemp;

        keyCount = usedBuckets = 0;

        for (int i = 0; i < oldKeys.length; i++) {
            if (oldKeys[i] != null && oldKeys[i] != TOMBSTONE) {
                insert(oldKeys[i], oldValues[i]);
            }
            oldKeys[i] = null;
            oldValues[i] = null;
        }
    }

    /*
     * Returns true if the hash table is empty, otherwise, false
     */
    public boolean isEmpty() {
        return keyCount == 0;
    }

    /*
     * Returns the size of the hash table
     */
    public int size() {
        return keyCount;
    }

    /*
     * Returns the capacity of the hash table
     */
    public int getCapacity() {
        return capacity;
    }

    /*
     * Clears the hash table
     */
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            keys[i] = null;
            values[i] = null;
        }
        usedBuckets = keyCount = 0;
    }

    /*
     * Returns the old value and modifies the old value with the specfied value if
     * the key is already present Otherwise, returns null and adds the new key-value
     * if the key is not present.
     */
    public V put(K key, V value) {
        return insert(key, value);
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

        if (usedBuckets >= threshold) {
            resize();
        }

        setupProbing(key);

        final int offset = normalizeIndex(key.hashCode());

        for (int i = offset, j = -1, x = 0;; i = normalizeIndex(offset + probe(x++))) {
            // If we have reached a deleted node
            if (keys[i] == TOMBSTONE) {
                // If we encountered first deleted Node
                if (j == -1) {
                    // Save the position in j
                    j = i;
                }
            } else if (keys[i] != null) {
                // If key is found
                if (keys[i].equals(key)) {
                    // Store of returning
                    V oldValue = values[i];
                    // If we have not encountered any deleted nodes,
                    // Set the value at position i
                    if (j == -1) {
                        values[i] = value;
                    } else {
                        // Set the tombstone at the i
                        keys[i] = TOMBSTONE;
                        values[i] = null;

                        // Store the new key-value in the first encounter tombstone position
                        keys[j] = key;
                        values[j] = value;
                    }
                    return oldValue;
                }
            } else {
                // If key is not found and we have not encountered any deleted nodes,
                if (j == -1) {
                    // Store the new key-value at the ith index
                    keys[i] = key;
                    values[i] = value;
                    usedBuckets++;
                } else {
                    // Store the new key-value in the first encounter tombstone position
                    keys[j] = key;
                    values[j] = value;
                }
                keyCount++;
                return null;
            }
        }
    }

    /*
     * Returns true if the hash table is contains the specified key, otherwise,
     * false
     */
    public boolean containsKey(K key) {
        return hasKey(key);
    }

    /*
     * Returns true if the hash table is contains the specified key, otherwise,
     * false
     */
    public boolean hasKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key");
        }

        setupProbing(key);

        final int offset = normalizeIndex(key.hashCode());

        for (int i = offset, j = -1, x = 1;; normalizeIndex(offset + probe(x++))) {
            if (keys[i] == TOMBSTONE) {
                if (j == -1) {
                    j = i;
                }
            } else if (keys[i].equals(key)) {
                // Lazy deletion/relocation for faster look-ups
                if (j != -1) {
                    keys[j] = keys[i];
                    values[j] = values[i];

                    keys[i] = TOMBSTONE;
                    values[i] = null;
                }
                return true;
            } else {
                return false;
            }
        }
    }

    /*
     * Returns the value against the specfied key if the key is found. Otherwise,
     * returns null.
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key");
        }

        setupProbing(key);

        final int offset = normalizeIndex(key.hashCode());

        for (int i = offset, j = -1, x = 1;; normalizeIndex(offset + probe(x++))) {
            if (keys[i] == TOMBSTONE) {
                if (j == -1) {
                    j = i;
                }
            } else if (keys[i].equals(key)) {
                // Lazy deletion/relocation for faster look-ups
                if (j != -1) {
                    keys[j] = keys[i];
                    values[j] = values[i];

                    keys[i] = TOMBSTONE;
                    values[i] = null;
                    return values[j];
                } else {
                    return values[i];
                }
            } else {
                return null;
            }
        }
    }

    /*
     * Returns the value against the specfied key if the key is found. Otherwise,
     * returns null.
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key");
        }

        setupProbing(key);

        final int offset = normalizeIndex(key.hashCode());

        for (int i = offset, x = 1;; normalizeIndex(offset + probe(x++))) {
            if (keys[i] == TOMBSTONE) {
                continue;
            }

            if (keys[i] == null) {
                return null;
            }

            if (keys[i].equals(key)) {
                V oldValue = values[i];
                keys[i] = TOMBSTONE;
                values[i] = null;
                keyCount--;
                return oldValue;
            }
        }
    }

    /*
     * Returns the list of keys in the hash table
     */
    public List<K> keys() {
        List<K> keyList = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            if (keys[i] != null && keys[i] != TOMBSTONE)
                keyList.add(keys[i]);
        }
        return keyList;
    }

    /*
     * Returns the list of values in the hash table
     */
    public List<V> values() {
        List<V> valueList = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            if (keys[i] != null && keys[i] != TOMBSTONE)
                valueList.add(values[i]);
        }
        return valueList;
    }

    /*
     * Returns the String representation of the hash table
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (int i = 0; i < capacity; i++) {
            if (keys[i] != null && keys[i] != TOMBSTONE) {
                sb.append(keys[i] + " : " + values[i] + ", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }

}
