package synthesizer;

import java.util.Iterator;

public interface BoundedQueue<T> extends Iterable<T> {
    //Return size of the buffer.
    int capacity();

    //Return number of items currently in the buffer.
    int fillCount();

    //Add item x to the end.
    void enqueue(T x);
    
    //Delete and return the item from the front.
    T dequeue();

    //Return (but not delete) item from the front.
    T peek();

    //If the BoundedQueue is empty?
    default boolean isEmpty() {
        return fillCount() == 0;
    }
    
    //If the BoundedQueue is full?
    default boolean isFull() {
        return fillCount() == capacity();
    }

    Iterator<T> iterator();

}
