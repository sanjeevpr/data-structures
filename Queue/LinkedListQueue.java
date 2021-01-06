import java.util.LinkedList;

public class LinkedListQueue<T> {
    // Uses LinkedList class from java.util package.
    // We can also impleement our very own LinkedList, but this way is much easier.
    // For reference on how to implement it, visit
    // https://github.com/sanjeevpr/data-structures/blob/main/Singly%20Linked%20List/SinglyLinkedList.java 
    private LinkedList<T> list = new LinkedList<>();

    public LinkedListQueue() {}

    public LinkedListQueue(T item) {
        enqueue(item);
    }

    // Returns the size of queue
    public int size() {
        return list.size();
    }

    // Returns true if the queue is empty, else, false
    public boolean isEmpty() {
        return list.size() == 0;
    }

    // Adds an element to the end of the queue
    public void enqueue(T item) {
        list.addLast(item);
    }

    // Removes an element from the front of the queue
    public T dequeue() {
        return list.removeFirst();
    }

    // Returns, but doesn't removes an element from the front of the queue
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        return list.peekFirst();
    }

    // Returns the String representation of the queue
    @Override
    public String toString() {
        return list.toString();
    }
}
