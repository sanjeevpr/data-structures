/**
 * The items inserted in the PQ are ordered in ascending order. The insert
 * operation is moves the larger entries to the right and the end of the queue
 * always contains the largest item. - O(n) The remove the maximum operation is
 * just like the pop in stack where we remove the largest item from the end of
 * the queue. - O(1)
 */

@SuppressWarnings("unchecked")
public class OrderedArrayMaxPQ<T extends Comparable<T>> {
    // Queue
    private T[] queue;
    // Number of elements in the queue
    private int n;

    public OrderedArrayMaxPQ(int size) {
        queue = (T[]) new Comparable[size];
        n = 0;
    }

    /*
     * Returns true if the queue is empty, else, false
     */
    public boolean isEmpty() {
        return n == 0;
    }

    /*
     * Returns the size of queue
     */
    public int size() {
        return n;
    }

    /*
     * Inserts the specified item to the queue in order
     */
    public void insert(T item) {
        int i = n - 1;
        while (i >= 0 && less(item, queue[i])) {
            // Moves the larger item to the right of the queue.
            queue[i + 1] = queue[i];
            i--;
        }
        queue[i + 1] = item;
        n++;
    }

    /*
     * Deletes the largest item from the queue
     */
    public T delMax() {
        return queue[n--];
    }

    /*
     * Return true if the item at the ith position is less than the item at the jth
     * position
     */
    private boolean less(T i, T j) {
        return i.compareTo(j) < 0;
    }
}