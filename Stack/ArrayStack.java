import java.util.EmptyStackException;

public class ArrayStack<T> {
    private Object[] array;
    private int size;
    private int capacity;

    // Initial capacity is set to 16
    public ArrayStack() {
        capacity = 16;
        array = new Object[capacity];
    }

    // Returns the size of stack
    public int size() {
        return size;
    }

    // Returns true if the stack is empty, else, false
    public boolean isEmpty() {
        return size == 0;
    }

    // Pushes an element on the stack
    public void push(T elem) {
        if (size == capacity) {
            resize();
        }
        array[size++] = elem;
    }

    // Resizes the array if the array size reaches the array capacity
    // capacity is doubled
    private void resize() {
        capacity *= 2;
        Object[] temp = new Object[capacity];
        for (int i = 0; i < array.length; i++) {
            temp[i] = array[i];
        }
        array = temp;
        temp = null;
    }

    // Pops the element off the stack
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        T elem = (T) array[size];
        array[--size] = null;
        return elem;
    }

    // Returns, but doesn't removes an element from the top of the stack 
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return (T) array[size - 1];
    }

    // Returns the String representation of the stack
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(array[i]);
            if(i != size - 1) {
                sb.append(", ");        
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
