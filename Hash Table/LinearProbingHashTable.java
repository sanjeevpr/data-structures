import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class LinearProbingHashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 3;
    private static final double DEFAUT_LOAD_FACTOR = 0.75;

    private static final int LINEAR_CONSTANT = 13;

    private double loadFactor;
    private int capacity, threshold;

    private int usedBuckets, keyCount;

    private K[] keys;
    private V[] values;

    private final K TOMBSTONE = (K) new Object();

    public LinearProbingHashTable() {
        this(DEFAULT_CAPACITY, DEFAUT_LOAD_FACTOR);
    }

    public LinearProbingHashTable(int capacity) {
        this(capacity, DEFAUT_LOAD_FACTOR);
    }

    public LinearProbingHashTable(int capacity, double loadFactor) {
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

    private int probe(int x) {
        return LINEAR_CONSTANT * x;
    }

    private void adjustCapacity() {
        while (gcd(LINEAR_CONSTANT, capacity) != 1) {
            capacity++;
        }
    }

    private void increaseCapacity() {
        capacity = (2 * capacity) + 1;
    }

    private int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    private int normalizeIndex(int keyHash) {
        return (keyHash & 0x7FFFFFFF) % capacity;
    }

    private void resize() {
        increaseCapacity();
        adjustCapacity();

        threshold = (int) (this.capacity * this.loadFactor);

        K[] oldKeys = (K[]) new Object[this.capacity];
        V[] oldValues = (V[]) new Object[this.capacity];

        K[] oldKeysTemp = keys;
        keys = oldKeys;
        oldKeys = oldKeysTemp;

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

    public int size() {
        return keyCount;
    }

    public boolean isEmpty() {
        return keyCount == 0;
    }

    public int getCapacity() {
        return capacity;
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) {
            keys[i] = null;
            values[i] = null;
        }
        usedBuckets = keyCount = 0;
    }

    public V put(K key, V value) {
        return insert(key, value);
    }

    public V add(K key, V value) {
        return insert(key, value);
    }

    public V insert(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key");
        }

        if (usedBuckets >= threshold) {
            resize();
        }

        final int offset = normalizeIndex(key.hashCode());

        for (int i = offset, j = -1, x = 0;; i = normalizeIndex(offset + probe(x++))) {
            if (keys[i] == TOMBSTONE) {
                if (j == -1) {
                    j = i;
                }
            } else if (keys[i] != null) {
                if (keys[i].equals(key)) {
                    V oldValue = values[i];
                    if (j == -1) {
                        values[i] = value;
                    } else {
                        keys[i] = TOMBSTONE;
                        values[i] = null;

                        keys[j] = key;
                        values[j] = value;
                    }
                    return oldValue;
                }
            } else {
                if (j == -1) {
                    keys[i] = key;
                    values[i] = value;
                    usedBuckets++;
                } else {
                    keys[j] = key;
                    values[j] = value;
                }
                keyCount++;
                return null;
            }
        }
    }

    public boolean containsKey(K key) {
        return hasKey(key);
    }

    public boolean hasKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key");
        }

        final int offset = normalizeIndex(key.hashCode());

        for (int i = offset, j = -1, x = 1;; normalizeIndex(offset + probe(x))) {
            if (keys[i] == TOMBSTONE) {
                if (j == -1) {
                    j = i;
                }
            } else if (keys[i].equals(key)) {
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

    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key");
        }

        final int offset = normalizeIndex(key.hashCode());

        for (int i = offset, j = -1, x = 1;; normalizeIndex(offset + probe(x))) {
            if (keys[i] == TOMBSTONE) {
                if (j == -1) {
                    j = i;
                }
            } else if (keys[i].equals(key)) {
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

    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Illegal key");
        }

        final int offset = normalizeIndex(key.hashCode());

        for (int i = offset, x = 1;; normalizeIndex(offset + probe(x))) {
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

    public List<K> keys() {
        List<K> keyList = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            if (keys[i] != null && keys[i] != TOMBSTONE)
                keyList.add(keys[i]);
        }
        return keyList;
    }

    public List<V> values() {
        List<V> valueList = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            if (keys[i] != null && keys[i] != TOMBSTONE)
                valueList.add(values[i]);
        }
        return valueList;
    }

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
