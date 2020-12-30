import java.util.Iterator;
import java.util.NoSuchElementException;

public class DoublyLinkedList<T> implements Iterable<T> {

    private Node<T> head = null;
    private Node<T> tail = null;
    private int size = 0;

    private static class Node<T> {
        private Node<T> previous;
        private Node<T> next;
        private T data;

        public Node(T data, Node<T> previous, Node<T> next) {
            this.data = data;
            this.previous = previous;
            this.next = next;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    private boolean checkPositionIndex(int index) {
        return index >= 0 && index < size;
    }

    public void clear() {
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

    public boolean add(T data) {
        addLast(data);
        return true;
    }

    public void addLast(T data) {
        Node<T> last = tail;
        Node<T> newNode = new Node<T>(data, last, null);
        tail = newNode;
        if (last == null) {
            head = newNode;
        } else {
            last.next = newNode;
        }
        size++;
    }

    public void addFirst(T data) {
        Node<T> first = head;
        Node<T> newNode = new Node<T>(data, null, first);
        head = newNode;
        if (first == null) {
            tail = newNode;
        } else {
            first.previous = newNode;
        }
        size++;
    }

    public void add(int index, T data) {
        if (!checkPositionIndex(index)) {
            throw new IndexOutOfBoundsException("Illegal index");
        }

        if (index == 0) {
            addFirst(data);
            return;
        } else if (index == size()) {
            addLast(data);
            return;
        } else {
            addBefore(index, data);
            return;
        }
    }

    private void addBefore(int index, T data) {
        Node<T> first = head;
        for (int i = 0; i < index - 1; i++) {
            first = first.next;
        }
        Node<T> newNode = new Node<>(data, first, first.next);
        first.next.previous = newNode;
        first.next = newNode;
        size++;
    }

    public T peekFirst() {
        return ((head == null) ? null : head.data);
    }

    public T peekLast() {
        return ((tail == null) ? null : tail.data);
    }

    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<T> first = head;
        T data = first.data;
        Node<T> next = first.next;
        next.previous = null;
        head = next;
        first.next = null;
        first.data = null;

        if (isEmpty()) {
            tail = null;
        }
        size--;
        return data;
    }

    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<T> last = tail;
        T data = last.data;
        Node<T> prev = last.previous;
        prev.next = null;
        tail = prev;
        last.previous = null;
        last.data = null;

        if (isEmpty()) {
            head = null;
        }
        size--;
        return data;
    }

    public boolean remove(Object o) {
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

    public T remove(int index) {
        if (!checkPositionIndex(index)) {
            throw new IndexOutOfBoundsException("Illegal index");
        }

        if(index == 0) {
            return removeFirst();
        }
        if(index == size - 1) {
            return removeLast();
        }

        int i;
        Node<T> x;
        if (index < size / 2) {
            for (i = 0, x = head; i != index; i++) {
                x = x.next;
            }
        } else {
            for (i = size - 1, x = tail; i != index; i--) {
                x = x.previous;
            }
        }
        return unlink(x);
    }

    private T unlink(Node<T> node) {
        T data = node.data;
        Node<T> prev = node.previous;
        Node<T> next = node.next;
        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
            node.next = null;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.previous = prev;
            node.previous = null;
        }
        node.data = null;
        size--;
        return data;
    }

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

    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        Node<T> first = head;
        
        while(first != null) {
            sb.append(first.data);
            if(first.next != null) {
                sb.append(", ");
            }
            first = first.next;
        }
        sb.append("]");
        return sb.toString();
    }

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
