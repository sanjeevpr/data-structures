public class ArrayQueue<T> {
    // Stores the elements of the queue
    private Object[] array;
    // Points to the front of the queue
    private int front;
    // Points to the end of the queue
    private int rear;
    // The capacity of the queue
    private int capacity;
    // The size of the queue
    private int size;

    ArrayQueue(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.front = 0;
        this.rear = -1;
        array = new Object[this.capacity];
    }

    // Returns the size of queue
    public int size() {
        return size;
    }

    // Returns true if the queue is empty, else, false
    public boolean isEmpty() {
        return size == 0;
    }

    // Returns true if the queue is full, else, false
    private boolean isFull() {
        return size == capacity;
    }

    // Adds an element to the end of the queue
    public void enqueue(T item) {
        if (isFull()) {
            throw new RuntimeException("Queue is full");
        }

        rear = (rear + 1) % capacity;
        array[rear] = item;
        size++;
    }

    // Removes an element from the front of the queue
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }

        T item = (T) array[front];
        front = (front + 1) % capacity;
        size--;
        return item;
    }

    // Returns, but doesn't removes an element from the front of the queue
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        T item = (T) array[front];
        return item;
    }
}
