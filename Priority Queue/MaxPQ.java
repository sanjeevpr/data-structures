import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * The MaxPQ uses binary heap data structure to implement the priority queue. The
 * insert operation inserts an item to the end of the queue, increment the size
 * of the queue by 1 and then swimUp operation is done with that item to restore
 * the heap invariant.
 *  - O(logn)
 * The remove the maximum operation removes the root
 * element at index 1 by exchanging the root element with the last element, then
 * removing the last element, decrement the size of the heap and sinkDown with
 * the element at the root to restore the heap invariant.
 *  - O(logn)
 */

@SuppressWarnings("unchecked")
public class MaxPQ<T> {
    // Queue
    private T[] queue;
    // Number of elements in the queue
    private int n;
    // Comparator
    private Comparator<T> comparator;

    public MaxPQ(int capacity) {
        queue = (T[]) new Object[capacity + 1];
        n = 0;
    }

    public MaxPQ() {
        this(1);
    }

    public MaxPQ(int capacity, Comparator<T> comparator) {
        this.comparator = comparator;
        queue = (T[]) new Object[capacity + 1];
        n = 0;
    }

    public MaxPQ(Comparator<T> comparator) {
        this(1, comparator);
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
    * Resize the array if we have reached its capacity
    */
    private void resize(int capacity) {
        T[] temp = (T[]) new Object[capacity];
        for (int i = 1; i < queue.length; i++) {
            temp[i] = queue[i];
        }
        queue = temp;
    }

    /*
    * Returns the largest element which at the root of the queue
    */
    public T max() {
        if(isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return queue[1];
    }
    /*
    * Inserts the specified item to the queue in order
    */
    public void insert(T item) {
        // Resize the array if we have reached its capacity
        if (n == queue.length - 1) {
            resize(2 * queue.length);
        }
        // Insert the element at the end and increment the size of the queue.
        queue[++n] = item;
        // swim up with the element restore heap invariant
        swim(n);
    }

    /*
    * Deletes the largest item from the queue
    */
    public T delMax() {
        if(isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        // Store the item to be returned
        T item = queue[1];
        // Swap the element at the root and the end, and decrement the size of the queue.
        swap(1, n--);
        // Sink down with the element restore heap invariant
        sink(1);
        // Helps GC
        queue[n+1] = null;
        // Resize the array if we have reached its capacity
        if (n > 0 && (n == (queue.length - 1)/4)) {
            resize(2 * queue.length / 2);
        }
        return item;
    }

    /*
    * Swims up with the element until the heap invariant is satisfied.
    */
    private void swim(int i) {
        // Continue while we have not reached the root at index 1 and 
        // the parent of i, i/2, is less than the child i. 
        while (i > 1 && less(i/2, i)) {
            // Swap the parent and the child.
            swap(i/2, i);
            // Set the child's index as the parent's index
            i = i/2;
        }
    }

    /*
    * Sinks down with the element until the heap invariant is satisfied.
    */
    private void sink(int i) {
        while (2*i <= n) {
            // Store the left child
            int child = 2*i;
            // Select the larger of the two children 2*i and 2*i+1 for swap with the parent i  
            if (child < n && less(child, child+1)) {
                child++;
            }
            // If parent is larger than the child, stop.
            if (!less(i, child)) {
                break;
            }
            // Swap the parent and the child. 
            swap(i, child);
            // Set the parent's index as the child's index
            i = child;     
        }
    }

    /*
    * Return true if the item at the ith position is less than the item at the jth
    * position
    */
    private boolean less(int i, int j) {
        if(comparator == null) {
            return ((Comparable<T>) queue[i]).compareTo(queue[j]) < 0;
        } else {
            return comparator.compare(queue[i], queue[j]) < 0;
        }
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