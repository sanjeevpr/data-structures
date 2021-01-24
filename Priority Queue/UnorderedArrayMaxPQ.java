/**
 * UnorderedArrayMaxPQ is one of the simplest implementation of the Priority
 * Queues The items inserted in the PQ are not ordered. The insert operation is
 * just like the push operation in the stack where we insert the item at the
 * end. - O(1) The remove the maximum operation is where we loop through the
 * queue and find the largest and exchange it with the item at the end and
 * remove the end item. - O(n)
 */

@SuppressWarnings("unchecked")
public class UnorderedArrayMaxPQ<T extends Comparable<T>> {
    // Queue
    private T[] queue;
    // Number of elements in the queue
    private int n;

    public UnorderedArrayMaxPQ(int size) {
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
     * Inserts the specified item to the end of the queue
     */
    public void insert(T item) {
        queue[n++] = item;
    }

    /*
     * Deletes the largest item from the queue
     */
    public T delMax() {
        int max = 0;
        for (int i = 1; i < n; i++) {
            if (less(max, i)) {
                max = i;
            }
        }
        swap(max, n - 1);
        return queue[--n];
    }

    /*
     * Return true if the item at the ith position is less than the item at the jth
     * position
     */
    private boolean less(int i, int j) {
        return queue[i].compareTo(queue[j]) < 0;
    }

    /*
     * Swaps the item at the ith position with the item at the jth position
     */
    private void swap(int i, int j) {
        T temp = queue[i];
        queue[i] = queue[j];
        queue[j] = temp;
    }
}