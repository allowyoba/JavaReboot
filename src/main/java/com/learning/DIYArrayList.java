package com.learning;

import java.util.*;

public class DIYArrayList<T> implements List<T> {
    private Object[] elementData;
    private int size = 0;
    private int iterIndex = -1;

    private static final Object[] EMPTY_LIST = {};

    public DIYArrayList() { this.elementData = EMPTY_LIST; }

    public DIYArrayList(int capacity) {
        if (capacity < 0) { throw new IllegalArgumentException(); }
        if (capacity == 0) { this.elementData = EMPTY_LIST; }
        if (capacity > 0) {
            this.elementData = new Object[capacity];
            size = capacity;
        }
    }

    private boolean validIndex(int index) {
        Objects.checkIndex(index, size);
        return true;
    }

    public void sort() {
        Arrays.sort(elementData, 0, size);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        for (Object e : elementData) {
            if (e.equals(o)) return true;
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Iterator is not defined");
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elementData, size);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < size)
            // Make a new array of a's runtime type, but my contents:
            return (T1[]) Arrays.copyOf(elementData, size, a.getClass());
        System.arraycopy(elementData, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    @Override
    public boolean add(T t) {
        if (size == elementData.length)
            elementData = increase();
        elementData[size] = t;
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (size == 0) return false;
        int index = indexOf(o);
        if (index == -1) return false;
        remove(index);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("containsAll is not defined");
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        Object[] a = c.toArray();
        int increaseSize = a.length;
        if (increaseSize == 0) return false;
        if (increaseSize > elementData.length - size) elementData = increase(increaseSize);
        System.arraycopy(a, 0, elementData, size, increaseSize);
        size += increaseSize;
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        if (validIndex(index)) {
            Object[] a = c.toArray();
            int increaseSize = a.length;
            if (increaseSize == 0) return false;
            if (increaseSize > elementData.length - size) elementData = increase(increaseSize);
            int movedCount = size - index;
            if (movedCount > 0) {
                System.arraycopy(this.elementData, index, this.elementData, index + increaseSize, movedCount);
            }
            System.arraycopy(a, 0, this.elementData, index, increaseSize);
            size += increaseSize;
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("removeAll is not defined");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("retainAll is not defined");
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
    }

    @Override
    public T get(int index) {
        if (validIndex(index)) { return (T) elementData[index]; };
        return null;
    }

    @Override
    public T set(int index, T element) {
        if (validIndex(index)) {
            T old_element = (T) elementData[index];
            elementData[index] = element;
            return old_element;
        }
        return null;
    }

    private Object[] increase() {
        return increase(1);
    }

    private Object[] increase(int size) {
        if (elementData.length > 0) {
            return elementData = Arrays.copyOf(elementData, elementData.length + size);
        }
        else {
            return elementData = new Object[size];
        }
    }

    @Override
    public void add(int index, T element) {
        if (size == elementData.length)
            elementData = increase();
        System.arraycopy(elementData, index, elementData, index + 1,size - index);
        elementData[index] = element;
        size++;
    }

    @Override
    public T remove(int index) {
        Object deletedElement = null;
        if (validIndex(index) && size > 0) {
            deletedElement = elementData[index];
            System.arraycopy(elementData, index + 1, elementData, index, size - 1 - index);
        }
        size--;
        elementData[size] = null;
        return (T) deletedElement;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elementData[i])) return i;
        }
        return -1;
    }

    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if (o.equals(elementData[i])) return i;
        }
        return -1;
    }

    private class DIYListIterator implements ListIterator<T> {
        int cursor;
        int lastReturned = -1;

        DIYListIterator(int index) {
            super();
            cursor = index;
        }

        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public T next() {
            if (cursor >= size)
                throw new NoSuchElementException();
            if (cursor >= elementData.length)
                throw new ConcurrentModificationException();
            lastReturned = cursor;
            cursor++;
            return (T) elementData[lastReturned];
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public T previous() {
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] elementData = DIYArrayList.this.elementData;
            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (T) elementData[lastReturned = i];
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            if (lastReturned < 0)
                throw new IllegalStateException();
            try {
                DIYArrayList.this.remove(lastReturned);
                cursor = lastReturned;
                lastReturned = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void set(T e) {
            if (lastReturned < 0)
                throw new IllegalStateException();

            try {
                DIYArrayList.this.set(lastReturned, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void add(T e) {
            try {
                int i = cursor;
                DIYArrayList.this.add(i, e);
                cursor = i + 1;
                lastReturned = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DIYListIterator(0);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return new DIYListIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("subList is not defined");
    }
}
