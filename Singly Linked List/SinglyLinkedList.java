import java.util.Iterator;
import java.util.NoSuchElementException;

public class SinglyLinkedList<T> implements Iterable<T> {
    // The head of the List
    private int size = 0;
    private Node<T> head = null;

    // Internal class representing a Node
    private static class Node<T> {
        // Contains the data
        private T data;
        // Points to the next Node
        private Node<T> next;

        public Node(T data) {
            this.data = data;
        }
    }

    // Returns the size of List
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
        // If the List is empty
        if (isEmpty()) {
            // Add at the head
            addFirst(data);
        } else {
            // Add at the tail
            addLast(data);
        }
        return true;
    }

    // Adds a new node at specified index
    public void add(int index, T data) {
        // If index passed is within the limit
        checkRangeForAdd(index);

        // If the index if 0,
        if (index == 0) {
            // insert at the head
            addFirst(data);
        } else if (index == size) {
            addLast(data);
        } else {
            // Counter to track the current Node position.
            int counter = 0;
            // current Node points to the head of the list
            Node<T> current = head;
            // previous Node is the previous Node of the current Node.
            Node<T> previous = null;

            // Loop through the list
            while (current != null) {
                // If the Node is to be added at the current position
                if (counter == index) {
                    // Removes the current node in the list
                    Node<T> node = new Node<>(data);
                    node.next = current;
                    previous.next = node;
                    break;
                } else {
                    // Store the previous Node
                    previous = current;
                    // Move to the next node
                    current = current.next;
                    // Increment the counter
                    counter++;
                }
            }
        }
    }

    // Adds a new Node to the start of the list.
    public void addFirst(T data) {
        // Create a new Node.
        Node<T> newNode = new Node<T>(data);
        // The new Node's next Node becomes the current head Node
        newNode.next = head;
        // The newNode becomes the new head Node
        head = newNode;
        size++;
    }

    // Adds a new Node to the end of the list.
    public void addLast(T data) {
        // We traverse through the list to find the tail node whose
        Node<T> tail = head;
        while (tail.next != null) {
            tail = tail.next;
        }
        // The tail node's next now points to the new Node
        // And, the new Node becomes the tail node in the list.
        tail.next = new Node<T>(data);
        size++;
    }

    // Returns, but doesn't remove the data from the head of the list
    public T peek() {
        return ((head == null) ? null : head.data);
    }

    // Returns and removes the head Node of the list.
    public T remove() {
        return removeFirst();
    }

    // Remove a new node at specified index
    public void remove(int index) {
        checkRangeForRemove(index);

        // If the index is 0, then remove the head Node.
        if (index == 0) {
            removeFirst();
            return;
        }

        // Counter to track the current Node position.
        int counter = 0;
        // current Node points to the head of the list
        Node<T> current = head;
        // previous Node is the previous Node of the current Node.
        Node<T> previous = null;

        // Iterate through the loop
        while (current != null) {
            // If the index of the Node is to be removed is found
            if (counter == index) {
                // Store the Node next to the Node to be removed
                Node<T> next = current.next;
                // Set the previous Node's next as the Node to be removed next Node
                previous.next = next;
                // Helps in GC
                current.next = null;
                current.data = null;
                size--;
                return;
            } else {
                // Store the previous Node
                previous = current;
                // Move to the next node
                current = current.next;
                // Increment the counter
                counter++;
            }
        }
    }

    // Removes the first occurance of the specified element from the list
    public boolean remove(Object o) {
        // If the data passed is null
        if (o == null) {
            Node<T> current = head;
            Node<T> previous = null;
            // If the element is found at the head Node
            if (current != null && current.data == null) {
                // Set the new head as head's next Node
                head = current.next;
                // Deallocate the memory
                current.data = null;
                current.next = null;
                size--;
                return true;
            }
            // If the element is not found at the head Node,
            // then, search for the element.
            while (current.next != null) {
                if (current.data == null) {
                    unlink(current, previous);
                    return true;
                }
                previous = current;
                current = current.next;
            }
        } else {
            // Same as above, but for data passed is non-null.
            Node<T> current = head;
            Node<T> previous = null;
            if (current != null && current.data == null) {
                head = current.next;
                current.data = null;
                current.next = null;
                size--;
                return true;
            }
            while (current.next != null) {
                if (current.data.equals(o)) {
                    unlink(current, previous);
                    return true;
                }
                previous = current;
                current = current.next;
            }
        }
        return false;
    }

    // Returns and removes the head Node of the list.
    private T removeFirst() {
        // Store the current head
        Node<T> first = head;
        if (first == null) {
            throw new NoSuchElementException();
        }
        // Store the head's data to be returned
        T data = first.data;
        // Store the next Node to the head
        Node<T> next = first.next;
        // Set the head to null
        first.data = null;
        first.next = null;
        // Now set the next Node as the new head and decrement the size of the list.
        head = next;
        size--;
        return data;
    }

    // Removes a Node between two Nodes
    private void unlink(Node<T> current, Node<T> previous) {
        previous.next = current.next;
        current.data = null;
        current.next = null;
        size--;
    }

    // Removes all of the elements from this list.
    public void clear() {
        // Get the reference to the head Node
        Node<T> current = head;
        // Loop through the list until the tail
        while (current.next != null) {
            // Store the current Node's next node
            Node<T> next = current.next;
            // Set the current Node to null;
            current.next = null;
            current.data = null;
            // The next Node becomes the current Node
            current = next;
        }
        // Set head to null and size to 0
        head = null;
        size = 0;
    }

    // Returns the index of first occurance of the the specified element
    // If found, return the index
    // If not found, return -1
    public int indexOf(Object o) {
        int index = 0;
        // If the specified element is null
        if (o == null) {
            Node<T> current = head;
            while (current != null) {
                // Get the first occurance of the Node with null data
                if (current.data == null) {
                    return index;
                }
                index++;
                current = current.next;
            }
        } else {
            Node<T> current = head;
            while (current != null) {
                // Use the equals operator to compare the data
                if (current.data.equals(o)) {
                    return index;
                }
                index++;
                current = current.next;
            }
        }
        return -1;
    }

    // Returns if the list contains the specified element.
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    // Returns the string representation of the list
    // iterator has been used here, which is slower.
    // I would recommend using while loop
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            T data = it.next();
            sb.append(data);
            if (it.hasNext()) {
                sb.append(", ");
            }
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