import java.util.EmptyStackException;
import java.util.LinkedList;

public class LinkedListStack<T> {
    // Uses LinkedList class from java.util package.
    // We can also impleement our very own LinkedList, but this way is much easier.
    // For reference on how to implement it, visit
    // https://github.com/sanjeevpr/data-structures/blob/main/Singly%20Linked%20List/SinglyLinkedList.java
    private LinkedList<T> list = new LinkedList<>();

    public LinkedListStack() {
    }

    public LinkedListStack(T elem) {
        push(elem);
    }

    // Returns the size of stack
    public int size() {
        return list.size();
    }

    // Returns true if the stack is empty, else, false
    public boolean isEmpty() {
        return list.size() == 0;
    }

    // Pushes an element on the stack
    public void push(T elem) {
        list.addLast(elem);
    }

    // Pops the element off the stack
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return list.removeLast();
    }

    // Returns, but doesn't removes an element from the top of the stack
    public T peek() {
        return list.peekLast();
    }

    // Returns the String representation of the stack
    @Override
    public String toString() {
        return list.toString();
    }
}
