import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoublyLinkedList<T> implements Iterable<T> {
    // Two Nodes to represent head and the tail of the list
    private Node<T> head = null;
    private Node<T> tail = null;
    // Size of the list
    private int size = 0;

    // Internal class representing a Node
    private static class Node<T> {
        // Contains the data
        private T data;

        // Points to the previous Node
        private Node<T> previous;
        // Points to the next Node
        private Node<T> next;

        public Node(T data, Node<T> previous, Node<T> next) {
            this.data = data;
            this.previous = previous;
            this.next = next;
        }
    }

    // Returns the size of the list
    public int size() {
        return size;
    }

    // Returns true if the list is empty, else false
    public boolean isEmpty() {
        return size() == 0;
    }

    // Checks index range for add
    private void checkRangeForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(outOfBoundMsg(index));
        }
    }

    // Checks index range for remove
    private void checkRangeForRemove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(outOfBoundMsg(index));
        }
    }

    private String outOfBoundMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }

    // Returns true if a new Node with the specified data is added to the list.
    // The new Node always added to the end of the list.
    public boolean add(T data) {
        addLast(data);
        return true;
    }

    // Adds a new Node at an specified index
    public void add(int index, T data) {
        checkRangeForAdd(index);

        if (index == 0) {
            // Adds at the start
            addFirst(data);
            return;
        } else if (index == size()) {
            // Adds at the end
            addLast(data);
            return;
        } else {
            // Adds anywhere in the middle
            addBefore(index, data);
            return;
        }
    }

    // Adds a new Node to the start of the list.
    public void addFirst(T data) {
        // Copy the current head Node
        Node<T> first = head;
        // Create a new Node with the current head as its next Node and previous Node as
        // null.
        Node<T> newNode = new Node<T>(data, null, first);
        head = newNode;
        // If the head Node was null, meaning the list was empty,
        // then set the tail as the new Node
        // Else, set the old head Node's previous Node as the new Node.
        if (first == null) {
            tail = newNode;
        } else {
            first.previous = newNode;
        }
        size++;
    }

    // Adds a new Node to the end of the list.
    public void addLast(T data) {
        // Copy the current tail Node
        Node<T> last = tail;
        // Create a new Node with the current tail as its previous Node and next Node as
        // null.
        Node<T> newNode = new Node<T>(data, last, null);
        // Set the new tail as the new Node
        tail = newNode;
        // If the tail Node was null, meaning the list was empty,
        // then set the head as the new Node
        // Else, set the old tail Node's next Node as the new Node.
        if (last == null) {
            head = newNode;
        } else {
            last.next = newNode;
        }
        size++;
    }

    // Adds a new Node anywhere at the specified index.
    private void addBefore(int index, T data) {
        // Traverse through the list to reach the index after which new Node needs to be
        // inserted
        Node<T> first = head;
        for (int i = 0; i < index - 1; i++) {
            first = first.next;
        }
        // Create a new Node with the current Node as its previous Node and next Node as
        // the current Node's next Node.
        Node<T> newNode = new Node<>(data, first, first.next);
        Node<T> next = first.next;
        // Set the current Node's next Node's previous Node to the new Node
        // And, set the current Node's next Node as the new Node.
        next.previous = newNode;
        next = newNode;
        size++;
    }

    // Returns, but doesn't remove the data from the head of the list
    public T peekFirst() {
        return ((head == null) ? null : head.data);
    }

    // Returns, but doesn't remove the data from the tail of the list
    public T peekLast() {
        return ((tail == null) ? null : tail.data);
    }

    // Removes the first occurance of the specified element from the list
    public boolean remove(Object o) {
        // If the data passed is null
        if (o == null) {
            Node<T> first = head;
            while (first != null) {
                if (first.data == null) {
                    unlink(first);
                    return true;
                }
                first = first.next;
            }
        } else {
            Node<T> first = head;
            while (first != null) {
                if (first.data.equals(o)) {
                    unlink(first);
                    return true;
                }
                first = first.next;
            }
        }
        return false;
    }

    // Removes a Node at specified index
    public T remove(int index) {
        checkRangeForRemove(index);

        // If the index is 0, then remove the head Node.
        if (index == 0) {
            return removeFirst();
        }
        // If the index is the tail Node's index, then remove the tail Node
        if (index == size - 1) {
            return removeLast();
        }

        // Else, remove a Node anywhere in the middle.
        int i;
        Node<T> x;
        // If the index is less than half the size of the list,
        // then just search the Node in first half of the list.
        // Complexity: O(nlogn)
        if (index < size / 2) {
            for (i = 0, x = head; i != index; i++) {
                x = x.next;
            }
        } else {
            // Searches in the other half of the list
            for (i = size - 1, x = tail; i != index; i--) {
                x = x.previous;
            }
        }
        return unlink(x);
    }

    // Returns and removes the head Node of the list.
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        // Store the current head Node
        Node<T> first = head;
        // Store the data to be returned
        T data = first.data;
        // Get the head's next Node
        Node<T> next = first.next;
        // Set the head's next Node's previous Node to null
        next.previous = null;
        // Set the new head Node as the old head's next Node
        head = next;
        // Helps in GC
        first.next = null;
        first.data = null;

        // If the list is empty, then also set the tail to null
        if (isEmpty()) {
            tail = null;
        }
        size--;
        return data;
    }

    // Returns and removes the tail Node of the list.
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        // Store the current tail Node
        Node<T> last = tail;
        // Store the data to be returned
        T data = last.data;
        // Get the tail's previous Node
        Node<T> prev = last.previous;
        // Set tail's previous Node's next Node to null
        prev.next = null;
        // Set the new tail Node as the old tail's previous Node
        tail = prev;
        // Helps in GC
        last.previous = null;
        last.data = null;

        // If the list is empty, then also set the head to null
        if (isEmpty()) {
            head = null;
        }
        size--;
        return data;
    }

    // Removes the specified Node from the list
    private T unlink(Node<T> node) {
        // Store the data to be returned
        T data = node.data;
        // Get the previous and the next Node of the Node to be removed
        Node<T> prev = node.previous;
        Node<T> next = node.next;

        if (prev == null) {
            // This means that the Node to be removed is the head Node.
            head = next;
        } else {
            // Else, it is any other Node
            // Set the previous Node's next to the next Node of the Node to be removed
            prev.next = next;
            node.next = null;
        }

        if (next == null) {
            // This means that the Node to be removed is the tail Node.
            tail = prev;
        } else {
            // Else, it is any other Node
            // Set the next Node's previous to the previous Node of the Node to be removed
            next.previous = prev;
            node.previous = null;
        }
        node.data = null;
        size--;
        return data;
    }

    // Removes all of the elements from this list.
    public void clear() {
        // Traverse the list and set each Node as null
        Node<T> node = head;
        while (node != null) {
            Node<T> next = node.next;
            node.previous = node.next = null;
            node.data = null;
            node = next;
        }
        head = tail = node = null;
        size = 0;
    }

    // Returns the index of first occurance of the the specified element
    // If found, return the index
    // If not found, return -1
    public int indexOf(Object o) {
        int counter = 0;
        if (o == null) {
            Node<T> first = head;
            while (first != null) {
                if (first.data == null) {
                    return counter;
                }
                first = first.next;
                counter++;
            }
        } else {
            Node<T> first = head;
            while (first != null) {
                if (first.data.equals(o)) {
                    return counter;
                }
                first = first.next;
                counter++;
            }
        }
        return -1;
    }

    // Returns if the list contains the specified element.
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // Returns the string representation of the list
    // iterator can also be used here, but is slower.
    // I would recommend using while loop
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        Node<T> first = head;

        while (first != null) {
            sb.append(first.data);
            if (first.next != null) {
                sb.append(", ");
            }
            first = first.next;
        }
        sb.append("]");
        return sb.toString();
    }

    // Returns an instance of iterator
    @Override
    public Iterator<T> iterator() {

        return new Iterator<T>() {
            Node<T> first = head;

            @Override
            public boolean hasNext() {
                return first != null;
            }

            @Override
            public T next() {
                T data = first.data;
                first = first.next;
                return data;
            }

        };
    }
}
