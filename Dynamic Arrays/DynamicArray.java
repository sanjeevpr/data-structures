import java.util.Iterator;

/*
*   This is an implementation of Dynamic Array like ArrayList which can resize itself.
*   Internally uses an static array to store the data.
*/
@SuppressWarnings("unchecked")
public class DynamicArray<T> implements Iterable<T> {
    //Array to store data
    private T arr[];

    //Total elements in the array
    private int len = 0;

    //Total capacity of the array
    private int capacity = 0;

    public DynamicArray() {
        this(16);
    }

    //Contructing a new array of the passed capacity
    public DynamicArray(int capacity) {
        if(capacity < 0) {
            throw new IllegalArgumentException("Illegal Capacity");
        }
        this.capacity = capacity;
        arr = (T[]) new Object[capacity];
    }

    //Returns the total number of elements in array
    public int size() {
        return len;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    //Returns the element at a particular index
    public T get(int index) {
        if(index >= len || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return arr[index];
    }

    //Adding an element to the array
    public void add(T elem) {
        //Check if the array has reached the total capacity
        if(len == capacity) {
            //Resize
            //Creates a temp array of double the capacity of the original array
            T temp[] = (T[]) new Object[2*capacity];
            //Copy the original array to the temp array
            for (int i = 0; i < capacity; i++) {
                temp[i] = arr[i];
            }
            //Set the new capacity of the array
            capacity *= 2;
            //Copy back to the original array
            //arr has extra nulls padded.
            arr = temp;
        }
        //Add the element to the array
        arr[len] = elem;
        //Increase the total number of element by 1 
        len++;
    }

    //Adding element to a specified index
    public void add(T elem, int index) {
        if(index < 0) {
            throw new IndexOutOfBoundsException();
        }
        //Calls add method if the array has reached the capacity.
        if(index == capacity) {
            add(elem);
        } else {
            arr[index] = elem;
            len++;
        }
    }

    //Returns the index of an element, if not found, return -1
    public int indexOf(Object obj) {
        for (int i = 0; i < len + 1; i++) {
            //If the object passed is null
            if(obj == null) {
                if(arr[i] == null) {
                    return i;
                }    
            } else {
                if(obj.equals(arr[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    //An iterator method to iterate over the array.
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>(){
            int index = 0;
			@Override
			public boolean hasNext() {
				return index < len;
			}

			@Override
			public T next() {
				return arr[index++];
			}
            
        };
    }

    //Returns the String representation of the array 
    @Override
    public String toString() {
        if(len == 0) {
            return "[]";
        } 
        StringBuilder sb = new StringBuilder(len);
        sb.append("[");
        for (int i = 0; i < len; i++) {
            sb.append(arr[i] + ", ");
        }
        return sb.append(arr[len] + "]").toString();
    }    
}